package com.but.parkour.concurrents.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.AddCompetitorRequest
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.CompetitorCreate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
class CompetitorViewModel : ViewModel() {
    private val _competitors = MutableLiveData<List<Competitor>>()
    val competitors: LiveData<List<Competitor>> = _competitors

    private val _unregisteredCompetitors = MutableLiveData<List<Competitor>>()
    val unregisteredCompetitors: LiveData<List<Competitor>> = _unregisteredCompetitors

    private val _competitorsCourse = MutableLiveData<List<Competitor>>()
    val competitorsCourse: LiveData<List<Competitor>> = _competitorsCourse

    private val apiClient = ApiClient(
        bearerToken = "GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r"
    )


    val competitionApi = apiClient.createService(CompetitionsApi::class.java)
    val competitorApi = apiClient.createService(CompetitorsApi::class.java)

    fun fetchCompetitorsInscrit(competitionId: Int) {
        viewModelScope.launch {
            try {
                Log.d("CompetitorViewModel", "Fetching competitors...")

                val call = competitionApi.getCompetitionInscriptions(competitionId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitors received: $data")
                        _competitors.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                        _competitors.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _competitors.postValue(emptyList())
            }
        }
    }

    private suspend fun getCompetitionDetails(competitionId: Int): Competition = suspendCoroutine { continuation ->
        val call = competitionApi.getCompetitionDetails(competitionId)
        apiClient.fetchData(
            call,
            onSuccess = { data, _ ->
                continuation.resume(data ?: Competition())
            },
            onError = { errorMessage, _ ->
                continuation.resumeWithException(Exception(errorMessage))
            }
        )
    }

    fun fetchUnregisteredCompetitors(competitionId: Int) {
        viewModelScope.launch {
            try {
                val competition = getCompetitionDetails(competitionId)
                Log.d("CompetitorViewModel", "Competition fetched: $competition")

                apiClient.fetchData(
                    call = competitorApi.getAllCompetitors(),
                    onSuccess = { allCompetitors, _ ->
                        apiClient.fetchData(
                            call = competitionApi.getCompetitionInscriptions(competitionId),
                            onSuccess = { registeredCompetitors, _ ->
                                val unregisteredCompetitors = allCompetitors?.filter { competitor ->
                                    registeredCompetitors?.none { it.id == competitor.id } != false &&
                                            isCompetitorInAgeRange(competitor, competition) &&
                                            isCompetitorOfCorrectGender(competitor, competition)
                                } ?: emptyList()
                                _unregisteredCompetitors.postValue(unregisteredCompetitors)
                                Log.d("CompetitorViewModel", "Competitors unregistered fetch: $unregisteredCompetitors")
                            },
                            onError = { _, _ ->
                                _unregisteredCompetitors.postValue(emptyList())
                            }
                        )
                    },
                    onError = { _, _ ->
                        _unregisteredCompetitors.postValue(emptyList())
                    }
                )
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _unregisteredCompetitors.postValue(emptyList())
            }
        }
    }
    private fun isCompetitorOfCorrectGender(competitor: Competitor, competition: Competition): Boolean {
        return competition.gender == null || competition.gender.value == competitor.gender?.value
    }

    private fun isCompetitorInAgeRange(competitor: Competitor, competition: Competition): Boolean {
        val age = calculateAge(competitor.bornAt)
        return age in (competition.ageMin ?: 0)..(competition.ageMax ?: Int.MAX_VALUE)
    }

    private fun calculateAge(bornAt: LocalDate?): Int {
        if (bornAt == null) return 0
        return Period.between(bornAt, LocalDate.now()).years
    }


    fun registerCompetitor(competitionId: Int, competitorId: Int) {
        viewModelScope.launch {
            try {
                val competitorId = AddCompetitorRequest(
                    competitorId = competitorId
                )
                val call = competitionApi.addCompetitor(competitionId, competitorId)
                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitor registered: $data")
                        fetchCompetitorsInscrit(competitionId)
                        fetchUnregisteredCompetitors(competitionId)
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun fetchCompetitorsCourse(competitionId : Int){
        viewModelScope.launch {
            try {
                Log.d("CompetitorViewModel", "Fetching competitors for a course... id: $competitionId")

                val call = competitionApi.getCompetitionInscriptions(competitionId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitors received: $data")
                        _competitorsCourse.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                        _competitorsCourse.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _competitorsCourse.postValue(emptyList())
            }
        }
    }

    fun addCompetitor(competitor: CompetitorCreate){
        viewModelScope.launch {
            try{
                val call = competitorApi.addCompetitor(competitor)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitor registered: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                    }
                )
            }catch (e: Exception){
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun unregisterCompetitior(competitionId: Int, competitorId: Int){
        viewModelScope.launch {
            try {
                val call = competitionApi.removeCompetitorFromCompetition(competitionId, competitorId)
                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("CompetitorViewModel", "Competitor unregistered: $data")
                        fetchCompetitorsInscrit(competitionId)
                        fetchUnregisteredCompetitors(competitionId)
                    },
                    onError = { errorMessage, _ ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                    }
                )
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

}
