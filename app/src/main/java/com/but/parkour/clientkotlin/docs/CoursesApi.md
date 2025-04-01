# CoursesApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**addCourse**](CoursesApi.md#addCourse) | **POST** api/courses | Store a course as the last one of the competition, the position is automatically computed. |
| [**addCourseObstacle**](CoursesApi.md#addCourseObstacle) | **POST** api/courses/{id}/add_obstacle | Add a given obstacle to a course |
| [**deleteCourse**](CoursesApi.md#deleteCourse) | **DELETE** api/courses/{id} | Delete the course and everything attached to it |
| [**getAllCourses**](CoursesApi.md#getAllCourses) | **GET** api/courses | List of every courses you have access. |
| [**getCourseCompetitors**](CoursesApi.md#getCourseCompetitors) | **GET** api/courses/{id}/competitors | List of every competitors registered to this course. |
| [**getCourseDetails**](CoursesApi.md#getCourseDetails) | **GET** api/courses/{id} | Get the details of a course |
| [**getCourseObstacles**](CoursesApi.md#getCourseObstacles) | **GET** api/courses/{id}/obstacles | List of every obstacles of the course. |
| [**updateCourse**](CoursesApi.md#updateCourse) | **PUT** api/courses/{id} | Update the course |
| [**updateCourseObstaclePosition**](CoursesApi.md#updateCourseObstaclePosition) | **POST** api/courses/{id}/update_obstacle_position | Update the course |



Store a course as the last one of the competition, the position is automatically computed.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val courseCreate : CourseCreate =  // CourseCreate | Store a course as the last one of the competition, the position is automatically computed.

webService.addCourse(courseCreate)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseCreate** | [**CourseCreate**](CourseCreate.md)| Store a course as the last one of the competition, the position is automatically computed. | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Add a given obstacle to a course

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
val addCourseObstacleRequest : AddCourseObstacleRequest =  // AddCourseObstacleRequest | Obstacle ID to add

webService.addCourseObstacle(id, addCourseObstacleRequest)
```

### Parameters
| **id** | **kotlin.Int**| Course ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **addCourseObstacleRequest** | [**AddCourseObstacleRequest**](AddCourseObstacleRequest.md)| Obstacle ID to add | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


Delete the course and everything attached to it

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database

webService.deleteCourse(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Course ID in the database | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


List of every courses you have access.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)

val result : kotlin.collections.List<Course> = webService.getAllCourses()
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Course&gt;**](Course.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


List of every competitors registered to this course.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val UNKNOWN_PARAMETER_NAME :  =  //  | Course ID in the database

val result : kotlin.collections.List<Competitor> = webService.getCourseCompetitors(UNKNOWN_PARAMETER_NAME)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **UNKNOWN_PARAMETER_NAME** | [****](.md)| Course ID in the database | |

### Return type

[**kotlin.collections.List&lt;Competitor&gt;**](Competitor.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the details of a course

Get the details of a course by his id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database

val result : Course = webService.getCourseDetails(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Course ID in the database | |

### Return type

[**Course**](Course.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


List of every obstacles of the course.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val UNKNOWN_PARAMETER_NAME :  =  //  | Course ID in the database

val result : kotlin.collections.List<CourseObstacle> = webService.getCourseObstacles(UNKNOWN_PARAMETER_NAME)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **UNKNOWN_PARAMETER_NAME** | [****](.md)| Course ID in the database | |

### Return type

[**kotlin.collections.List&lt;CourseObstacle&gt;**](CourseObstacle.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update the course

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
val courseUpdate : CourseUpdate =  // CourseUpdate | Updating courses informations

webService.updateCourse(id, courseUpdate)
```

### Parameters
| **id** | **kotlin.Int**| Course ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseUpdate** | [**CourseUpdate**](CourseUpdate.md)| Updating courses informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


Update the course

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CoursesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
val courseObstacleUpdate : CourseObstacleUpdate =  // CourseObstacleUpdate | Updating courses informations

webService.updateCourseObstaclePosition(id, courseObstacleUpdate)
```

### Parameters
| **id** | **kotlin.Int**| Course ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseObstacleUpdate** | [**CourseObstacleUpdate**](CourseObstacleUpdate.md)| Updating courses informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

