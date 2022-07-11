## Cape texture get

### Endpoint
GET https://lobstershack.dev/osmium/v2/static/cape/get/data/

### Headers
No required headers

### Parameters
uuid: The uuid of the player to get the data for. Formatted as specified in the formatting page\
token: The authentication token obtained from the login endpoint

### Returns
If the data was found, returns the cape data raw with response code 200\
If the data was not found, returns code 404\
If any other error occurred, returns an error code and message