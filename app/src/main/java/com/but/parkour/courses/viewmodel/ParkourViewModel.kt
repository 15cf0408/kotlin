package com.but.parkour.courses.viewmodel

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
    private val _coursesList = MutableLiveData<List<Course>>()
    val coursesList: LiveData<List<Course>> = _coursesList

    private val apiClient = ApiClient(
        bearerToken = "GkZ7jDp6pyzKRos3GgnlUvX6wU7tR7UMrB9y1mQINGJzOiXPGSHqKoPgIVaqYh1r"
    )

    private val competitionApi = apiClient.createService(CompetitionsApi::class.java)
    private val coursesApi = apiClient.createService(CoursesApi::class.java)

    // Fetching the courses related to a specific competition
    fun fetchCourses(competitionId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Starting fetch for courses...")

                val courseCall = competitionApi.getCompetitionCourses(competitionId)

                apiClient.fetchData(
                    call = courseCall,
                    onSuccess = { response, _ ->
                        Log.d("ParkourViewModel", "Courses successfully fetched: $response")
                        _coursesList.postValue(response ?: emptyList())
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ParkourViewModel", "Error fetching courses: $errorMessage")
                        _coursesList.postValue(emptyList()) // Returning empty list in case of error
                    }
                )
            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Exception encountered while fetching courses: ${e.message}", e)
                _coursesList.postValue(emptyList())
            }
        }
    }

    // Adding a new course to the server
    fun addCourse(newCourse: CourseCreate) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Initiating course addition...")

                val addCourseCall = coursesApi.addCourse(newCourse)

                apiClient.fetchData(
                    call = addCourseCall,
                    onSuccess = { response, _ ->
                        Log.d("ParkourViewModel", "Course added successfully: $response")
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ParkourViewModel", "Error adding course: $errorMessage")
                    }
                )
            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Error encountered while adding course: ${e.message}", e)
            }
        }
    }

    // Removing a specific course from the competition
    fun removeCourse(courseId: Int, competitionId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Removing course with ID: $courseId...")

                val removeCourseCall = coursesApi.deleteCourse(courseId)

                apiClient.fetchData(
                    call = removeCourseCall,
                    onSuccess = { response, _ ->
                        Log.d("ParkourViewModel", "Course removed successfully: $response")
                        fetchCourses(competitionId) // Refresh the courses list after deletion
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ParkourViewModel", "Error removing course: $errorMessage")
                        Log.e("ParkourViewModel", "Failed to remove course with ID: $courseId")
                    }
                )
            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Error while trying to remove course: ${e.message}", e)
            }
        }
    }

    // Updating a course with new information
    fun updateCourse(courseId: Int, courseUpdate: CourseUpdate) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Updating course with ID: $courseId...")

                val updateCourseCall = coursesApi.updateCourse(courseId, courseUpdate)

                apiClient.fetchData(
                    call = updateCourseCall,
                    onSuccess = { response, _ ->
                        Log.d("ParkourViewModel", "Course updated successfully: $response")
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ParkourViewModel", "Error updating course: $errorMessage")
                    }
                )
            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Error during course update: ${e.message}", e)
            }
        }
    }
}
