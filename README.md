# TextKernel Integration Test Assignment

## Used Technologies and Libraries

+ Java 11
+ Spring Boot 2+ (MVC)
+ Spring 5 (WebFlux)
+ Mongo DB (Embedded Reactive)
+ Junit / Mockito
+ Lombok
+ Swagger
+ Maven 3+
+ Docker
+ Postman / Curl
+ Intellij IDEA

## Run the Application

Below command will run the provided .jar file from any command line on your machine with Java 11

`java -jar async-proxy-0.0.1.jar`
 

## Build and Run with Maven

To build and run the application on your local machine with maven 3.6+.

`mvn clean install`

`mvn spring-boot:run `

## Build and Run in Docker Container

To run the application in a docker container.

`docker build -t {image-name} .`

` docker run -d -p 8080:8080 -t  {image-name}`

## Swagger API Documentation ##

To check API details on a browser 

`http://localhost:8080/swagger-ui.html`

## Testing

A full integration test is implemented together with unit test for service classes.

For Integration test webTestClient is used. Below is the list of tests.

+ Should_Retrieve_In_Progress()
+ Should_Upload_A_File()


## CURL Commands

Generate Access Token.
 
Once the application runs it loads one test user into Database.

Authorization header in this command includes the credentials for the test user.
 
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
If CV process is failed

```json
{
    "processId": "4dc77ad4-83ae-48be-a6cc-c8a8850bee5c",
    "status": "INVALID"
}
```

If CV process is still underway

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
    "status" : "COMPLETE",
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

## Design Choices and Basic Algorithms - (Bronze & Silver)

The application is written in reactive programming using Spring WebFlux framework and embedded reactive MongoDB.
For security Basic Authentication and URL Based token (Json web token) authentications are implemented. Once the application
runs it loads a mock user into the database which will be used to perform basic authentication. After a successful GET call to /accesstoken a JSON Web Token with an expiration time will be generated. POST /submit call will take a multipart file (CV) and create a CVProcess Object and return it's ID to the client while in the background it will send CV to CV Parser Service. Once the application receive the response (xml) from remote CV Parser service it will convert into a JSON document and save it to the database. In case of error it will mark CV Process status as INVALID. Finally, GET /retrieve/{processId} will return a JSON object according to the status of cv process.


## Gold

To make it sure the application can be deployed in multi nodes in one data center Kubernetes could be a solution. In that case the application should be deployed and run in a Docker container.
Dockerfile that I provide is able to run the application in Docker container however in real life case there should be more docker containers for database. A possible scenario would be like;

+ Prepare a deployment-mongodb.yml file for mongodb 
+ That file defines Deployment and Service (image, port, protocol) to create Mongo database on Kubernetes cluster
+ Perform `kubectl apply -f deployment-mongodb.yml`  command to create a mongo db instance


With that we have a running Mongo database and since we don't use in memory mongodb our Spring application should be configured accordingly.

+ Remove embedded mongodb from dependencies
+ Update application.properties by adding mongo db host and port information
+ Create a deployment-spring-boot.yml file which will include information about docker containers of our application and mongo db
+ Build the docker image `docker build -t cv-proxy-app:1.0 .` then tag and push it to Google container registry
+ Perform `kubectl apply -f deployment-spring-boot.yml` to deploy the application on another pod

With that now we have two pods are deployed and another one would be for a gateaway to open our application for public calls.
After deploying all the pods now the application is ready to scale. The scaling can be done automatically by Kubernetes as well as manually.


                         
 



 
