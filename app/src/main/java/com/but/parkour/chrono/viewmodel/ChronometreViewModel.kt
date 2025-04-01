package com.but.parkour.courses.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CourseObstacle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChronometreViewModel : ViewModel() {
    private val _obstacles = MutableStateFlow<List<CourseObstacle>>(emptyList())
    val obstacles: StateFlow<List<CourseObstacle>> = _obstacles

    private val apiClient = ApiClient(
        bearerToken = "GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r"
    )
    private val courseApi = apiClient.createService(CoursesApi::class.java)

    fun fetchObstacles(parkourId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ChronometreViewModel", "Fetching obstacles for parkour $parkourId...")
                val response = courseApi.getCourseObstacles(parkourId)

                apiClient.fetchData(
                    response,
                    onSuccess = { data, _ ->
                        Log.d("ChronometreViewModel", "Obstacles received: $data")
                        _obstacles.value = data ?: emptyList()
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ChronometreViewModel", "Error: $errorMessage")
                        _obstacles.value = emptyList()
                    }
                )
            } catch (e: Exception) {
                Log.e("ChronometreViewModel", "Exception: ${e.message}", e)
                _obstacles.value = emptyList()
            }
        }
    }
}
