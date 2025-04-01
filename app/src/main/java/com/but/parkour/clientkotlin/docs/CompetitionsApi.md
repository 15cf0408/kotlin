# CompetitionsApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**addCompetition**](CompetitionsApi.md#addCompetition) | **POST** api/competitions | Store a competition, default status set to &#39;not_ready&#39; |
| [**deleteCompetition**](CompetitionsApi.md#deleteCompetition) | **DELETE** api/competitions/{id} | Delete the competition and everything attached to it |
| [**getAllCompetitions**](CompetitionsApi.md#getAllCompetitions) | **GET** api/competitions | List of every competitions you have access. |
| [**getCompetitionCourses**](CompetitionsApi.md#getCompetitionCourses) | **GET** api/competitions/{id}/courses | Get the courses of a given competition |
| [**getCompetitionDetails**](CompetitionsApi.md#getCompetitionDetails) | **GET** api/competitions/{id} | Get the details of a competition |
| [**getCompetitionInscriptions**](CompetitionsApi.md#getCompetitionInscriptions) | **GET** api/competitions/{id}/inscriptions | Get the competitors registered to the given competition |
| [**updateCompetition**](CompetitionsApi.md#updateCompetition) | **PUT** api/competitions/{id} | Update the competition |



Store a competition, default status set to &#39;not_ready&#39;

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitionsApi::class.java)
val competitionCreate : CompetitionCreate =  // CompetitionCreate | Competition's informations

val result : Competition = webService.addCompetition(competitionCreate)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **competitionCreate** | [**CompetitionCreate**](CompetitionCreate.md)| Competition&#39;s informations | |

### Return type

[**Competition**](Competition.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Delete the competition and everything attached to it

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitionsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competition ID in the database

webService.deleteCompetition(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Competition ID in the database | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


List of every competitions you have access.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitionsApi::class.java)

val result : kotlin.collections.List<Competition> = webService.getAllCompetitions()
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Competition&gt;**](Competition.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the courses of a given competition

Get the courses of a given competition

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitionsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competition ID in the database

val result : kotlin.collections.List<Course> = webService.getCompetitionCourses(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Competition ID in the database | |

### Return type

[**kotlin.collections.List&lt;Course&gt;**](Course.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the details of a competition

Get the details of a competition by his id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitionsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competition ID in the database

val result : Competition = webService.getCompetitionDetails(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Competition ID in the database | |

### Return type

[**Competition**](Competition.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get the competitors registered to the given competition

Get the competitors registered to the given competition

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitionsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competition ID in the database

val result : kotlin.collections.List<Competitor> = webService.getCompetitionInscriptions(id)
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int**| Competition ID in the database | |

### Return type

[**kotlin.collections.List&lt;Competitor&gt;**](Competitor.md)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update the competition

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(CompetitionsApi::class.java)
val id : kotlin.Int = 56 // kotlin.Int | Competition ID in the database
val competitionUpdate : CompetitionUpdate =  // CompetitionUpdate | Updating competitions informations

webService.updateCompetition(id, competitionUpdate)
```

### Parameters
| **id** | **kotlin.Int**| Competition ID in the database | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **competitionUpdate** | [**CompetitionUpdate**](CompetitionUpdate.md)| Updating competitions informations | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

