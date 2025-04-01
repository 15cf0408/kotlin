# CompetitorsApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**addCompetitor**](CompetitorsApi.md#addCompetitor) | **POST** api/competitors | Store a competitor |
| [**deleteCompetitor**](CompetitorsApi.md#deleteCompetitor) | **DELETE** api/competitors/{id} | Delete the competitor and everything attached to him |
| [**getAllCompetitors**](CompetitorsApi.md#getAllCompetitors) | **GET** api/competitors | List of every competitors you have access. |
| [**getCompetitorCoursePerformanceDetails**](CompetitorsApi.md#getCompetitorCoursePerformanceDetails) | **GET** api/competitors/{id}/{id_course}/details_performances | Retrieves details about an competitor&#39;s performance in a course |
| [**getCompetitorCourses**](CompetitorsApi.md#getCompetitorCourses) | **GET** api/competitors/{id}/courses | List of every courses a competitor is registered in. |
| [**getCompetitorDetails**](CompetitorsApi.md#getCompetitorDetails) | **GET** api/competitors/{id} | Get the details of a competitor |
| [**getCompetitorPerformances**](CompetitorsApi.md#getCompetitorPerformances) | **GET** api/competitors/{id}/performances | List of every performance for a given competitor. |
| [**updateCompetitor**](CompetitorsApi.md#updateCompetitor) | **PUT** api/competitors/{id} | Update the competitor |



Store a competitor

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)
val competitorCreate : CompetitorCreate =  // CompetitorCreate | Competitor's informations

val result : Competitor = webService.addCompetitor(competitorCreate)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **competitorCreate** | [**CompetitorCreate**](CompetitorCreate.md)| Competitor&#39;s informations | |

### Return type

[**Competitor**](Competitor.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Delete the competitor and everything attached to him

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competitor ID in the database

webService.deleteCompetitor(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Competitor ID in the database | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


List of every competitors you have access.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)

val result : kotlin.collections.List<Competitor> = webService.getAllCompetitors()
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Competitor&gt;**](Competitor.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Retrieves details about an competitor&#39;s performance in a course

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)
val UNKNOWN_PARAMETER_NAME :  =  //  | Competitor ID in the database
val UNKNOWN_PARAMETER_NAME2 :  =  //  | Course ID in the database

val result : kotlin.collections.List<PerformanceObstacle> = webService.getCompetitorCoursePerformanceDetails(UNKNOWN_PARAMETER_NAME, UNKNOWN_PARAMETER_NAME2)
```

### Parameters
| **UNKNOWN_PARAMETER_NAME** | [****](.md)| Competitor ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **UNKNOWN_PARAMETER_NAME2** | [****](.md)| Course ID in the database | |

### Return type

[**kotlin.collections.List&lt;PerformanceObstacle&gt;**](PerformanceObstacle.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


List of every courses a competitor is registered in.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)
val UNKNOWN_PARAMETER_NAME :  =  //  | Competitor ID in the database

val result : kotlin.collections.List<Course> = webService.getCompetitorCourses(UNKNOWN_PARAMETER_NAME)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **UNKNOWN_PARAMETER_NAME** | [****](.md)| Competitor ID in the database | |

### Return type

[**kotlin.collections.List&lt;Course&gt;**](Course.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the details of a competitor

Get the details of a competitor by his id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competitor ID in the database

val result : Competitor = webService.getCompetitorDetails(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Competitor ID in the database | |

### Return type

[**Competitor**](Competitor.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


List of every performance for a given competitor.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)
val UNKNOWN_PARAMETER_NAME :  =  //  | Competitor ID in the database

val result : kotlin.collections.List<Performance> = webService.getCompetitorPerformances(UNKNOWN_PARAMETER_NAME)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **UNKNOWN_PARAMETER_NAME** | [****](.md)| Competitor ID in the database | |

### Return type

[**kotlin.collections.List&lt;Performance&gt;**](Performance.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update the competitor

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitorsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competitor ID in the database
val competitorUpdate : CompetitorUpdate =  // CompetitorUpdate | Updating competitors informations

webService.updateCompetitor(id, competitorUpdate)
```

### Parameters
| **id** | **kotlin.Int**| Competitor ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **competitorUpdate** | [**CompetitorUpdate**](CompetitorUpdate.md)| Updating competitors informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

