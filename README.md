# UserStorage

This is a CRUD REST-API project, which allows you to work with users. You can create user with:
1) Surname,
2) Name,
3) Middle name, 
4) Date of birth, 
5) Email, 
6) Phone number, 
7) Image. 

## Technology stack

- Java Core,
- Spring Boot,
- Spring Security,
- Spring Data,
- Hibernate,
- PostgreSQL,
- Swagger.

## Web

First you need to go to the registration endpoint. Then pass authorization on the authorization endpoint. After authorization, you will receive a token in response. Use this token to access other endpoints.
-/api3/auth/register
-/api3/auth/authenticate

You can see the endpoints on the swager page.
- /swagger-ui/index.html

You can import JSON and test REST-API from Postman.
- https://api.postman.com/collections/26285521-35048025-6b2a-4462-a9e9-645a07c32e98?access_key=PMAT-01H0MT8CFFA9F5ZNCWNGBCAYB7

## Set up before starting

- "application.properties" file.
