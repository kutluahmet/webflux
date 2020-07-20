# TextKernel Integration Test Assignment

## Used Technologies and Libraries

+ Java 11
+ Spring Boot 2+ (MVC)
+ Spring 5 (WebFlux)
+ Mongo DB (Embedded Reactive)
+ Junit 5
+ Lombok
+ Swagger
+ Maven 3+
+ Docker
+ Postman / Curl
+ Intellij IDEA

## Build and Run with Maven

To run the application on your local machine.

`mvn clean install`

`mvn spring-boot:run `

## Build and Run in Docker Container

To run the application in a docker container.

`docker build -t {image-name} .`

` docker run -d -p 8080:8080 -t  {image-name}`

## Swagger API Documentation ##

`http://localhost:8080/swagger-ui.html`

## CURL Commands

Generate Access Token

`curl --location --request GET http://localhost:8080/accesstoken --header Authorization:"Basic dGVzdHVzZXI6P1kqYmJMN0dtWExU" `

Submit CV to be Parsed

`curl --location --request POST localhost:8080/submit?access_token={generatedToken} --form uploaded_file=@"{pathToCVFile}"
`

Get CV or Status

`curl --location --request GET localhost:8080/retrieve/{processId}?access_token={generatedToken}`

## Sample Execution

#### 1. Access Token

Request to get an api access token for testuser

`curl --location --request GET http://localhost:8080/accesstoken --header Authorization:"Basic dGVzdHVzZXI6P1kqYmJMN0dtWExU" `

Failed Response
```json
{
    "code": 401,
    "message": "User not found"
}
```

Successful Response
```json
{
    "code": 201,
    "message": "Access token is generated",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1ZjE0N2Y1YmVjMzE4MjM1YjVjNmJkODIiLCJpYXQiOjE1OTUxNzg4ODEsImV4cCI6MTU5NTE3ODk4MX0.cOzZsFI6VgbT0xiofe8PZLhN_f3Rko4-wCXg_4ijzPc"
}
```

#### 2. Submit CV

Request to post a CV to be parsed 

`curl --location --request POST localhost:8080/submit?access_token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1ZjE0N2Y1YmVjMzE4MjM1YjVjNmJkODIiLCJpYXQiOjE1OTUxNzg4ODEsImV4cCI6MTU5NTE3ODk4MX0.cOzZsFI6VgbT0xiofe8PZLhN_f3Rko4-wCXg_4ijzPc --form uploaded_file=@"petra.pdf"
`

Failed Response
```json
{
    "code": 401,
    "message": "Token Validation is failed"
}
```

Successful Response
```json
{
    "processId": "4dc77ad4-83ae-48be-a6cc-c8a8850bee5c",
    "status": "IN_PROGRESS"
}
```

#### 3. Retrieve CV

Request to post a CV to be parsed 

`curl --location --request GET localhost:8080/retrieve/4dc77ad4-83ae-48be-a6cc-c8a8850bee5c?access_token=access_token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1ZjE0N2Y1YmVjMzE4MjM1YjVjNmJkODIiLCJpYXQiOjE1OTUxNzg4ODEsImV4cCI6MTU5NTE3ODk4MX0.cOzZsFI6VgbT0xiofe8PZLhN_f3Rko4-wCXg_4ijzPc`
                                                                                                         `
If CV process failed

```json
{
    "processId": "4dc77ad4-83ae-48be-a6cc-c8a8850bee5c",
    "status": "INVALID"
}
```

If CV process still underway

```json
{
    "processId": "4dc77ad4-83ae-48be-a6cc-c8a8850bee5c",
    "status": "IN_PROGRESS"
}
```

Successful Response
```json
{
    "processId": "4dc77ad4-83ae-48be-a6cc-c8a8850bee5c",
    "profile": {
        "address": {
            "streetNumberBase": "15-A",
            "city": "EDE",
            "streetName": "Hoofdstraat",
            "postalCode": "6717 AA"
        },
        "firstName": "Petra",
        "lastName": "van de Ven"
    }
}
```

## Design Choices and Basic Algorithms

The application is written in reactive programming using Spring WebFlux framework and embedded reactive MongoDB.
For security Basic Authentication and URL Based token (Json web token) authentications are implemented. Once the application
runs it loads a mock user into database which will be used to perform basic authentication. After a successful GET call to /accesstoken a JSON Web Token with an expiration time will be generated. POST /submit call will take a multipart file (CV) and create a Process Object and return it's ID to the client while in the background it will send CV to CV Parser Service. Once CV parser complete it's job it will save CV into Database as JSON documents as well as update the corresponding Process object by changing its status from IN PROGRESS to COMPLETE. Finally, GET /retrieve/{processId} will return a JSON object according to the status of process Id.


## Gold