package com.but.parkour.obstacles.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.apis.ObstaclesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.AddCourseObstacleRequest
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.clientkotlin.models.Obstacle
import com.but.parkour.clientkotlin.models.ObstacleCreate
import kotlinx.coroutines.launch

class ObstaclesViewModel : ViewModel() {
    private val _obstaclesCourse = MutableLiveData<List<CourseObstacle>>()
    val obstaclesCourse: LiveData<List<CourseObstacle>> = _obstaclesCourse

    private val _allObstacles = MutableLiveData<List<Obstacle>>()
    val allObstacles: LiveData<List<Obstacle>> = _allObstacles

    private val apiClient = ApiClient(
        bearerToken ="GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r"
    )

    private val courseApi = apiClient.createService(CoursesApi::class.java)
    private val obstacleApi = apiClient.createService(ObstaclesApi::class.java)

    fun fetchCoursesObstacles(parkourId : Int) {
        viewModelScope.launch {
            try {
                Log.d("ObstaclesViewModel", "Fetching courses...")

                val call = courseApi.getCourseObstacles(parkourId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ParkourViewModel", "courses received: $data")
                        _obstaclesCourse.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ParkourViewModel", "Error: $errorMessage")
                        _obstaclesCourse.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Exception: ${e.message}", e)
                _obstaclesCourse.postValue(emptyList())
            }
        }
    }

    fun fetchAllObstacles() {
        viewModelScope.launch {
            try {
                Log.d("ObstaclesViewModel", "Fetching all obstacles...")

                val call = obstacleApi.getAllObstacles()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ObstaclesViewModel", "obstacles received: $data")
                        _allObstacles.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ObstaclesViewModel", "Error: $errorMessage")
                        _allObstacles.postValue(emptyList())
                    }
                )


            } catch (e: Exception) {
                Log.e("ObstaclesViewModel", "Exception: ${e.message}", e)
                _allObstacles.postValue(emptyList())
            }
        }
    }

    fun addObstacleCourse(parkourId: Int, obstacleId: AddCourseObstacleRequest) {
        viewModelScope.launch {
            try {
                Log.d("ObstaclesViewModel", "Adding obstacle to course...")

                val call = courseApi.addCourseObstacle(parkourId, obstacleId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ObstaclesViewModel", "obstacle added: $data")
                        fetchCoursesObstacles(parkourId)
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ObstaclesViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("ObstaclesViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun addObstacle(obstacle: ObstacleCreate) {
        viewModelScope.launch {
            try {
                Log.d("ObstaclesViewModel", "Adding obstacle...")

                val call = obstacleApi.addObstacle(obstacle)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ObstaclesViewModel", "obstacle added: $data")
                        fetchAllObstacles()
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ObstaclesViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("ObstaclesViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun removeObstacle(obstacleId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ObstaclesViewModel", "Removing obstacle...")

                val call = obstacleApi.deleteObstacle(obstacleId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("ObstaclesViewModel", "obstacle removed: $data")
                        fetchAllObstacles()
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ObstaclesViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("ObstaclesViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}