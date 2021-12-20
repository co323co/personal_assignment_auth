# CafeIn Backend

![Generic badge](https://img.shields.io/badge/java-v11.0.11-green.svg) ![Generic badge](https://img.shields.io/badge/mysql-8.0.25-yellow.svg) ![Generic badge](https://img.shields.io/badge/springboot-v2.3.0-blue.svg) ![Generic badge](https://img.shields.io/badge/Gradle-v7.0.2-red.svg) ![Generic badge](https://img.shields.io/badge/querydsl-v4.3.1-blueviolet.svg) ![Generic badge](https://img.shields.io/badge/lombok-v1.18.12-orange.svg)

```
📦src
 ┣ 📂docs
 ┃ ┗ 📂asciidoc
 ┃ ┃ ┗ 📜api-doc.adoc
 ┣ 📂main
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂cafein
 ┃ ┃ ┃ ┃ ┣ 📂configuration
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AES128.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DBConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜QuerydslConfiguration.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ValidationCheck.java
 ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┣ 📂dao
 ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂email
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂selectprofile
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂signin
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂signup
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂updateprofile
 ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┣ 📂response
 ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┃ ┣ 📂serviceImpl
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserServiceImpl.java
 ┃ ┃ ┃ ┃ ┗ 📜Application.java
 ┗ 📂test
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂cafein
 ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┣ 📜ApiDocumentationTest.java
 ┃ ┃ ┃ ┃ ┗ 📜ApiDocumentUtils.java
 ┃ ┗ 📂resources
 ┃ ┃ ┗ 📂org
 ┃ ┃ ┃ ┗ 📂springframework
 ┃ ┃ ┃ ┃ ┗ 📂restdocs
 ┃ ┃ ┃ ┃ ┃ ┗ 📂templates
```

#### :star: You can change environment according to your setting

### Local Env

- Java 11
- MYSQL Workbench 8.0.25
- Intellij Ultimate 2021.2

<br>

## ✔ How to start project in local environment

### 1. Clone this repository

```
// 레포지토리 클론
$ git clone <https://github.com/co323co/personal_assignment_auth.git>

// 경로 변경
$ cd personal_assignment_auth/backend
```

### 2. Setup application.yml

```
# src/main/resources/application.yml
spring:
  datasource:
    hikari:
      driver-class-name: ${YOUR_DATABASE_DIRVER_CLASS_NAME}
      jdbc-url: ${YOUR_DATABASE_URL}
      username: ${YOUR_DATABASE_USERNAME}
      password: ${YOUR_DATABASE_PASSWORD}
  jpa:
    properties:
      hibernate:
        hbm2ddl.auto: update

  mail:
    host: ${YOUR_EMAIL_AUTHENTICATION_HOST}
    port: ${YOUR_EMAIL_AUTHENTICATION_PORT}
    username: ${YOUR_EMAIL}
    password: ${${YOUR_EMAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
custom:
  constant:
    access:
      token:
        secret:
          key: ${YOUR_ACCESS_TOKEN_SECRET_KEY}
    user:
      info:
        password:
          key: ${YOUR_USER_INFO_PASSWORD_KEY}
```

### 3. Start Project

> Before starting, you need to create a **Q table** through build (Querydsl setting)

> Right-click on the **src/main/java/com/cafein/Application.java** - [Run 'Application.main()']

## ✔ Tech Stack

| Usage              | Stack                     |
| ------------------ | ------------------------- |
| `Spring Boot`      | Backend Framework         |
| `Spring Data JPA`  | JPA Library(Module)       |
| `Querydsl`         | Domain Specific Language  |
| `MySQL`            | Database                  |
| `JWT`              | JSON Web Token            |
| `Spring Rest Docs` | Document RESTful Services |

## ✔ Project Structure

---

- `src/` 하위 폴더들은 다음과 같은 역할을 한다.
- `configuration/` : 프로젝트를 실행하기 위한 설정이 세팅되어 있다.
- `controller/` : 클라이언트의 요청을 받아, 처리한 후 응답 데이터를 넘겨주는 역할을 한다.
- `dao/` : DB에 실질적으로 접근하는 객체들이 행하는 기능들이 정의되어 있다.
- `dto/` : 계층간 데이터 교환을 위한 객체가 정의되어 있다.
- `entity/` : DB의 엔티티가 Spring Boot JPA에 맞게 세팅되어 있다.
- `response/` : 처리에 대한 응답 구조와 응답 리스트가 정의되어 있다.
- `service/` : 실질적으로 클라이언트의 요청을 처리하는 business logic이 정의되어 있다.
- `serviceImpl/` : service interface로부터 받은 business logic이 구현되어 있다.
- `docs/asciidoc/` : Document 생성 시 사용되는 .adoc가 정의되어있다.
- `main/resources/static/docs/` : Spring Rest Docs로 완성된 Document가 복사되는 곳이다.

---

- `test/controller` : Document 생성 시 필요한 테스트를 위한 logic이 정의되어있다.

## ✔ cf) cafein ERD , API Document

1. ERD Document


2. API Document

