package com.but.parkour.competition.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import kotlinx.coroutines.launch

class CompetitionViewModel : ViewModel() {
    private val _competitions = MutableLiveData<List<Competition>>()
    val competitions: LiveData<List<Competition>> = _competitions
    private val apiClient = ApiClient(
        bearerToken = "GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r"
    )

    val competitionApi = apiClient.createService(CompetitionsApi::class.java)

    init {
        fetchCompetitions()
    }


    private fun fetchCompetitions() {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Fetching competitions...")

                val call = competitionApi.getAllCompetitions()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitionViewModel", "Competitions received: $data")
                        _competitions.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitionViewModel", "Error: $errorMessage")
                        _competitions.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _competitions.postValue(emptyList())
            }
        }
    }

    fun addCompetition(competition: CompetitionCreate) {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Adding competition...")

                val call = competitionApi.addCompetition(competition)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitionViewModel", "Competition added: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitionViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun updateCompetition(competitionId: Int, competition: CompetitionUpdate) {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Updating competition...")
                Log.d("CompetitionViewModel", "Competition: $competition")
                Log.d("CompetitionViewModel", "CompetitionId: $competitionId")

                val call = competitionApi.updateCompetition(competitionId, competition)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitionViewModel", "Competition updated: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitionViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun removeCompetition(competitionId: Int) {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Removing competition...")

                val call = competitionApi.deleteCompetition(competitionId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("CompetitionViewModel", "Competition removed: $data")
                        fetchCompetitions()
                    },
                    onError = { errorMessage, _ ->
                        Log.e("CompetitionViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

}