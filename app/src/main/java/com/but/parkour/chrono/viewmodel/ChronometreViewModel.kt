// ChronometreViewModel.kt
package com.but.parkour.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CourseObstacle
import kotlinx.coroutines.launch

class ChronometreViewModel : ViewModel() {
    private val _obstacles = MutableLiveData<List<CourseObstacle>>()
    val obstacles: LiveData<List<CourseObstacle>> = _obstacles

    private val apiClient = ApiClient(
        bearerToken = "GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r"
    )

    private val courseApi = apiClient.createService(CoursesApi::class.java)

    fun fetchObstacles(parkourId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ChronometreViewModel", "Fetching obstacles for parkour $parkourId...")
                val call = courseApi.getCourseObstacles(parkourId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("ChronometreViewModel", "Obstacles received: $data")
                        _obstacles.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ChronometreViewModel", "Error: $errorMessage")
                        _obstacles.postValue(emptyList())
                    }
                )
            } catch (e: Exception) {
                Log.e("ChronometreViewModel", "Exception: ${e.message}", e)
                _obstacles.postValue(emptyList())
            }
        }
    }
}
