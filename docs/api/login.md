## Cape texture get

### Endpoint
GET https://lobstershack.dev/osmium/v2/direct/login

### Initial stage

#### Parameters
uuid: The uuid of the authenticating user. Formatted as specified in the formatting page\
stage: initial

#### Headers
No headers

#### Returns
Returns code 200 along with the skin data to be uploaded
If an error occurred, returns an error code along with an error message

### In between

The skin data received from the initial stage is uploaded to mojang servers. This must be completed before the confirmation stage

### Confirmation stage

#### Parameters
uuid: The uuid of the authenticating user. Formatted as specified in the formatting page\
stage: confirm

#### Headers
No headers

#### Returns
Returns code 200 if the login was successful, along with the authorization token

### Note
Although not strictly necessary, it is recommended to revert the users skin back to its original state