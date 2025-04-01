
# Competition

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Int** |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **updatedAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **name** | **kotlin.String** |  |  [optional] |
| **ageMin** | **kotlin.Int** |  |  [optional] |
| **ageMax** | **kotlin.Int** |  |  [optional] |
| **gender** | [**inline**](#Gender) |  |  [optional] |
| **hasRetry** | **kotlin.Boolean** |  |  [optional] |
| **status** | [**inline**](#Status) |  |  [optional] |


<a id="Gender"></a>
## Enum: gender
| Name | Value |
| ---- | ----- |
| gender | H, F |


<a id="Status"></a>
## Enum: status
| Name | Value |
| ---- | ----- |
| status | not_ready, not_started, started, finished |



