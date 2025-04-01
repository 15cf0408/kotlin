# ObstaclesApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**addObstacle**](ObstaclesApi.md#addObstacle) | **POST** api/obstacles | Store a obstacle |
| [**deleteObstacle**](ObstaclesApi.md#deleteObstacle) | **DELETE** api/obstacles/{id} | Delete the obstacle and everything attached to it, if it was in a course, it&#39;s deleted from the course. |
| [**getAllObstacles**](ObstaclesApi.md#getAllObstacles) | **GET** api/obstacles | List of every obstacles you have access. |
| [**getObstacleDetails**](ObstaclesApi.md#getObstacleDetails) | **GET** api/obstacles/{id} | Get the details of an obstacle |
| [**updateObstacle**](ObstaclesApi.md#updateObstacle) | **PUT** api/obstacles/{id} | Update the obstacle |



Store a obstacle

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ObstaclesApi::class.java)
val obstacleCreate : ObstacleCreate =  // ObstacleCreate | Updating obstacles informations

webService.addObstacle(obstacleCreate)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **obstacleCreate** | [**ObstacleCreate**](ObstacleCreate.md)| Updating obstacles informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Delete the obstacle and everything attached to it, if it was in a course, it&#39;s deleted from the course.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ObstaclesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Obstacle ID in the database

webService.deleteObstacle(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Obstacle ID in the database | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


List of every obstacles you have access.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ObstaclesApi::class.java)

val result : kotlin.collections.List<Obstacle> = webService.getAllObstacles()
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Obstacle&gt;**](Obstacle.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the details of an obstacle

Get the details of an obstacle by his id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ObstaclesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Obstacle ID in the database

val result : Obstacle = webService.getObstacleDetails(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Obstacle ID in the database | |

### Return type

[**Obstacle**](Obstacle.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update the obstacle

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ObstaclesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Obstacle ID in the database
val obstacleUpdate : ObstacleUpdate =  // ObstacleUpdate | Updating obstacles informations

webService.updateObstacle(id, obstacleUpdate)
```

### Parameters
| **id** | **kotlin.Int**| Obstacle ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **obstacleUpdate** | [**ObstacleUpdate**](ObstacleUpdate.md)| Updating obstacles informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

