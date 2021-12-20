# 스마일게이트 STOVE_DEV_CAMP 개인 과제

# 목차

- [개발 도구](#개발-도구)   
- [기능 설명](#기능-설명)
- [프로젝트 명세](#프로젝트-명세)
  - [배포 방법](#배포-방법)
  - [산출물](#산출물) 
    - [API 명세서](#API-명세서) 
    - [ERD](#DB-ERD) 
    - [동작 화면](#동작-화면) 
- [프로젝트 후기](#프로젝트-후기)


---

## ⚒ 개발 도구

- **Front-end**
  - Vue.js
  - Vuetify
- **Back-end**
  - Spring Boot
  - JPA + Quserydsl
  - MySql
---

## 🖥 기능 설명

- 회원 페이지
    - 로그인
    - 회원 가입
- 관리자 페이지
    - 회원 목록 조회
    - 회원 목록 삭제


- Password Encryption

**TODO**

- E-Mail 인증
- 비밀번호 찾기
- 캐시

---

## 🔧 프로젝트 명세

### ️ 배포 방법

### 환경

- 사용 버전	
  - JDK 11

### 추가 설정 및 파일

1. application.yml, appication_test.yml
   1. /backend/src/main/resources 으로 이동합니다.
   2. 아래와 같은 내용으로 resources 안에 application.yml와 application_test.yml를 생성합니다.

   ```yaml
   spring:
     datasource:
       hikari:
         platform: mysql
         driver-class-name: com.mysql.cj.jdbc.Driver
         jdbc-url: {{ your database url }}
         username: {{ your database username }}
         password: {{ your database password }}
     jpa:
       properties:
         hibernate:
           show_sql: true
           format_sql: true
           use_sql_comments: true
           hbm2ddl.auto: update
     mail:
       host: smtp.gmail.com
       port: 587
       username: {{ your email }}
       password: {{ your email password }}
       properties:
         mail:
           smtp:
             auth: true
             starttls:
               enable: true
   custom:
     constant:
       token:
         secret:
           key: {{ your secret key }}
       user:
         info:
           password:
             key: {{ your password key }}
   ```



---

## 🎞 산출물

### API 명세서

- 회원가입  ` [POST] /api/users/signup` 
- 로그인 `[POST] /api/users/signin`
- 회원탈퇴 `[DELETE] /api/users`
- 이메일 인증 `[POST] /api/users/email`
- jwt 검증 `[GET] /api/users/jwt`
- 내 프로필 조회 `[GET] /api/users/me`
- 내 프로필 수정 `[PATCH] /api/users`
- ACCESS TOKEN 재발급 `[GET] /api/users/reissue`
- 유저 리스트 조회 `[GET] /api/admin/users?page=1&size=10&userId=1&nickname=홍길&grade=admin`
- 유저 리스트 삭제 `[DELETE] /api/admin/users`

---

### DB ERD

### ![img](https://user-images.githubusercontent.com/56910798/146702793-1162c02a-23be-49be-bea4-3410ffe35d10.png)![img](https://user-images.githubusercontent.com/56910798/146702793-1162c02a-23be-49be-bea4-3410ffe35d10.png)![erd](https://user-images.githubusercontent.com/56910798/146702793-1162c02a-23be-49be-bea4-3410ffe35d10.png)



---



### 동작 화면

#### 홈

![image](https://user-images.githubusercontent.com/56910798/146709583-beb7a079-f4d6-4864-80ea-4cd9abcfcfef.png)

#### 로그인 페이지

![image](https://user-images.githubusercontent.com/56910798/146709382-a8c67aec-c7a7-4de7-9dec-16648376ae34.png)

#### 회원가입 페이지

![image](https://user-images.githubusercontent.com/56910798/146709440-d10d5b1e-7298-405c-8cf3-bf37eb75943f.png)

#### 관리자 페이지

![image](https://user-images.githubusercontent.com/56910798/146709520-4f370f63-ece3-4066-83f2-08adf3b35b85.png)

---



# 프로젝트 후기
