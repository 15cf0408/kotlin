# PerformancesApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**addPerformance**](PerformancesApi.md#addPerformance) | **POST** api/performances | Store a performance |
| [**deletePerformance**](PerformancesApi.md#deletePerformance) | **DELETE** api/performances/{id} | Delete the performance and everything attached to it. |
| [**getAllPerformances**](PerformancesApi.md#getAllPerformances) | **GET** api/performances | List of every performance you have access. |
| [**getPerformanceDetails**](PerformancesApi.md#getPerformanceDetails) | **GET** api/performances/{id} | Get the details of a performance |
| [**updatePerformance**](PerformancesApi.md#updatePerformance) | **PUT** api/performances/{id} | Update the performance |



Store a performance

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformancesApi::class.java)
val performanceCreate : PerformanceCreate =  // PerformanceCreate | Storing performance informations

webService.addPerformance(performanceCreate)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **performanceCreate** | [**PerformanceCreate**](PerformanceCreate.md)| Storing performance informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


Delete the performance and everything attached to it.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformancesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Performance ID in the database

webService.deletePerformance(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Performance ID in the database | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


List of every performance you have access.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformancesApi::class.java)

val result : kotlin.collections.List<Performance> = webService.getAllPerformances()
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Performance&gt;**](Performance.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the details of a performance

Get the details of a performance by his id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformancesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Performance's ID in the database

val result : Performance = webService.getPerformanceDetails(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Performance&#39;s ID in the database | |

### Return type

[**Performance**](Performance.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update the performance

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PerformancesApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Performance ID in the database
val performanceUpdate : PerformanceUpdate =  // PerformanceUpdate | Updating performance informations

webService.updatePerformance(id, performanceUpdate)
```

### Parameters
| **id** | **kotlin.Int**| Performance ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **performanceUpdate** | [**PerformanceUpdate**](PerformanceUpdate.md)| Updating performance informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

