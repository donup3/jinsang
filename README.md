### 실행 시
- application.yml
    - application.yml에 개인 정보(이메일관련)가 담겨 있어서 gitignore 했습니다.
    - 실행시 applicaion.yml을 추가해서 실행하세요.

<br>

```java
spring:
  datasource:
    #    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3307/test?useSSL=false&serverTimezone=Asia/Seoul
    username: root
    password: test

  jpa:
    hibernate:
      ddl-auto: update
      show_sql: true
      format_sql: true

mail:
    host: smtp.gmail.com
    port: 587
    username: 본인 이메일 주소  // 이메일 주소추가하기
    password: 본인 이메일 비번  // 이메일 비번 추가하기
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

```