package com.but.parkour.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.clientkotlin.models.CourseUpdate
import kotlinx.coroutines.launch

class ParkourViewModel : ViewModel() {
    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> get() = _courses

    private val apiClient = ApiClient(bearerToken = "GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r")
    private val competitionsApi = apiClient.createService(CompetitionsApi::class.java)
    private val coursesApi = apiClient.createService(CoursesApi::class.java)

    fun chargerCourses(eventId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Fetching courses...")
                val response = competitionsApi.getCompetitionCourses(eventId)
                apiClient.fetchData(response,
                    onSuccess = { data, _ ->
                        _courses.postValue(data ?: emptyList())
                        Log.d("ParkourViewModel", "Courses retrieved: ${data?.size ?: 0}")
                    },
                    onError = { error, _ ->
                        Log.e("ParkourViewModel", "Error fetching courses: $error")
                        _courses.postValue(emptyList())
                    }
                )
            } catch (exception: Exception) {
                Log.e("ParkourViewModel", "Exception fetching courses: ${exception.message}", exception)
                _courses.postValue(emptyList())
            }
        }
    }

    fun ajoutCourse(course: CourseCreate) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Adding new course...")
                val response = coursesApi.addCourse(course)
                apiClient.fetchData(response,
                    onSuccess = { data, _ ->
                        Log.d("ParkourViewModel", "Course added successfully: $data")
                    },
                    onError = { error, _ ->
                        Log.e("ParkourViewModel", "Error adding course: $error")
                    }
                )
            } catch (exception: Exception) {
                Log.e("ParkourViewModel", "Exception adding course: ${exception.message}", exception)
            }
        }
    }

    fun supprimerCourse(courseId: Int, eventId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Deleting course with ID: $courseId")
                val response = coursesApi.deleteCourse(courseId)
                apiClient.fetchData(response,
                    onSuccess = { _, _ ->
                        Log.d("ParkourViewModel", "Course deleted successfully")
                        chargerCourses(eventId)
                    },
                    onError = { error, _ ->
                        Log.e("ParkourViewModel", "Error deleting course: $error")
                    }
                )
            } catch (exception: Exception) {
                Log.e("ParkourViewModel", "Exception deleting course: ${exception.message}", exception)
            }
        }
    }

    fun updateCourse(courseId: Int, updatedCourse: CourseUpdate) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Updating course with ID: $courseId")
                val response = coursesApi.updateCourse(courseId, updatedCourse)
                apiClient.fetchData(response,
                    onSuccess = { _, _ ->
                        Log.d("ParkourViewModel", "Course updated successfully")
                    },
                    onError = { error, _ ->
                        Log.e("ParkourViewModel", "Error updating course: $error")
                    }
                )
            } catch (exception: Exception) {
                Log.e("ParkourViewModel", "Exception updating course: ${exception.message}", exception)
            }
        }
    }
}
