## Cape texture get

### Endpoint
GET https://lobstershack.dev/osmium/v2/direct/keep-alive

### Headers
No required headers

### Parameters
uuid: The uuid of the currently logged-in user\
token: The authentication token obtained from the login endpoint

### Returns
If the keep alive was successful returns 204 no content
If not successful returns the respective error code