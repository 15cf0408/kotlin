# PerformanceObstaclesApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createPerformanceObstacle**](PerformanceObstaclesApi.md#createPerformanceObstacle) | **POST** api/performance_obstacles | Store a performance obstacle |
| [**getAllPerformanceObstacles**](PerformanceObstaclesApi.md#getAllPerformanceObstacles) | **GET** api/performance_obstacles | List of every performance by obstacle you have access. |
| [**getPerformanceObstacleDetails**](PerformanceObstaclesApi.md#getPerformanceObstacleDetails) | **GET** api/performance_obstacles/{id} | Get the details of a obstacle |
| [**updatePerformanceObstacle**](PerformanceObstaclesApi.md#updatePerformanceObstacle) | **PUT** api/performance_obstacles/{id} | Update the performance obstacle |



Store a performance obstacle

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformanceObstaclesApi::class.java)
val performanceObstacleCreate : PerformanceObstacleCreate =  // PerformanceObstacleCreate | Storing performance obstacles informations

webService.createPerformanceObstacle(performanceObstacleCreate)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **performanceObstacleCreate** | [**PerformanceObstacleCreate**](PerformanceObstacleCreate.md)| Storing performance obstacles informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


List of every performance by obstacle you have access.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformanceObstaclesApi::class.java)

val result : kotlin.collections.List<PerformanceObstacle> = webService.getAllPerformanceObstacles()
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;PerformanceObstacle&gt;**](PerformanceObstacle.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the details of a obstacle

Get the details of a performance obstacle by his id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformanceObstaclesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Performance obstacle ID in the database

val result : PerformanceObstacle = webService.getPerformanceObstacleDetails(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Performance obstacle ID in the database | |

### Return type

[**PerformanceObstacle**](PerformanceObstacle.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update the performance obstacle

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformanceObstaclesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Performance obstacle ID in the database
val performanceObstacleUpdate : PerformanceObstacleUpdate =  // PerformanceObstacleUpdate | Updating obstacles informations

webService.updatePerformanceObstacle(id, performanceObstacleUpdate)
```

### Parameters
| **id** | **kotlin.Int**| Performance obstacle ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **performanceObstacleUpdate** | [**PerformanceObstacleUpdate**](PerformanceObstacleUpdate.md)| Updating obstacles informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

