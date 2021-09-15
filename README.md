# CustomerFileDataProcess
Microservice which decrypts the data received and store in the file format specified and also updates and fetch the contents based on the request
On receiving the data for Post request, the data is decrypted after decoding the encoded key and based on the file type(csv or xml), the decrypted contents are written to the file.
For GET request, the data is encrypted and sent to the microservice requesting it and later it will be decrypted by the requesting microservice.
