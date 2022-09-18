<h1 align="center">Spring Boot JWT Boilerplate</h1>
<p align="center">SpringBoot Boilerplate with JWT Refresh Token</p>

<p align="center">
    <img src="./.github/spring-boot-jwt-logo.png" width="120" />
</p>

## :wrench: How to use?

### :mag_right: Requirements:

* Docker and docker-compose.
* Java 11 or higher.

### :athletic_shoe: Steps:

1. Clone this repository to your machine and open the terminal already in the project directory. 
2. Use the command `docker-compose up -d`, to start the MySQL.
3. Use the command `./mvnw clean package` to generate the *.jar*.
4. Use the command `./mvnw spring-boot:run` to start the server.

Everything must be working properly.

### :gear: Default Config

By default, an admin user will be created when the users table is empty with this data:


```json
{
  "email": "admin@mail.com",
  "password": "admin123",
  "username": "Admin"
}
```

To change this default information you can add the values in your [properties file](https://github.com/SilvanoGPM/spring-boot-jwt-boilerplate/blob/main/src/main/resources/application.yml)(application.properties, application.yml), for example:

```yml
app:
  init:
    user:
      email: testemail@mail.com
      password: somepass
      username: testname
```

For better security, it is also recommended that you change the properties for JWT creation.


```yml
app:
  jwt:
    secret: YOUR_SECRET_KEY
    expirationMs: 3600000 # 1 hour
    refreshExpirationMs: 2592000000 # 30 days
```

## 🌐 Authentication Flow

<img width="1648" alt="JWT Authentication Flow" src="https://user-images.githubusercontent.com/59753526/190884844-764e5af1-1894-4573-b689-c2f3c11331a7.png">

## :rocket: Technologies

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Project Lombok](https://projectlombok.org/)
* [JUnit5](https://junit.org/junit5/)
* [H2](http://www.h2database.com/html/features.html)
* [MySQL](https://www.mysql.com/)
* [JJWT](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt)

## :heart: Thanks

- [JWT Implementation](https://github.com/bezkoder/spring-boot-refresh-token-jwt)
