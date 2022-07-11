## Cape texture get

### Endpoint
POST https://lobstershack.dev/osmium/v2/direct/cape/upload

### Headers
No required headers

### Parameters
uuid: The uuid of the currently logged-in user\
token: The authentication token obtained from the login endpoint

### Payload
Requires a valid multipart upload with a text section called data that contains the capes json data, and a binary section with the name file0 that contains the capes png data

### Returns
If the upload was successful returns code 200
If there was any kind of error returns the respective status code with an error message