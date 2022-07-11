## Cape texture get

### Endpoint
GET https://lobstershack.dev/osmium/v2/static/cape/get/texture/

### Headers
No required headers

### Parameters
uuid: The uuid of the player to get the data for. Formatted as specified in the formatting page\
token: The authentication token obtained from the login endpoint

### Returns
If the texture was found, returns the texture data raw with response code 200\
If the texture was not found, returns code 404\
If any other error occurred, returns an error code and message