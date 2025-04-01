package com.but.parkour.concurrents.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.CompetitorUpdate
import kotlinx.coroutines.launch

class GestionConcurrentViewModel: ViewModel() {
    private val _competitors = MutableLiveData<List<Competitor>>()
    val competitors: LiveData<List<Competitor>> = _competitors

    private val apiClient = ApiClient(
        bearerToken = "GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r"
    )

    private val competitorApi = apiClient.createService(CompetitorsApi::class.java)

    fun fetchAllCompetitors(){
        viewModelScope.launch {
            try {
                Log.d("GestionConcurrentViewModel", "Fetching all competitors...")

                val call = competitorApi.getAllCompetitors()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("GestionConcurrentViewModel", "Competitors received: $data")
                        _competitors.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("GestionConcurrentViewModel", "Error: $errorMessage")
                        _competitors.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("GestionConcurrentViewModel", "Exception: ${e.message}", e)
                _competitors.postValue(emptyList())
            }
        }
    }

    fun deleteCompetitor(competitorId: Int){
        viewModelScope.launch {
            try {
                Log.d("GestionConcurrentViewModel", "Deleting competitor...")

                val call = competitorApi.deleteCompetitor(competitorId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("GestionConcurrentViewModel", "Competitor deleted: $data")
                        fetchAllCompetitors()
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("GestionConcurrentViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("GestionConcurrentViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun updateCompetitor(competitorId: Int, competitor: CompetitorUpdate){
        viewModelScope.launch {
            try {
                Log.d("GestionConcurrentViewModel", "Updating competitor...")

                val call = competitorApi.updateCompetitor(competitorId, competitor)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("GestionConcurrentViewModel", "Competitor updated: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("GestionConcurrentViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("GestionConcurrentViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}