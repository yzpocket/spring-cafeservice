# Spring Boot Project!
커피 및 차, 그리고 디저트를 주문하고 받을 수 있는 앱 또는 웹 서비스

## 🖥️ 프로젝트 소개
`Spring Boot`를 활용한 웹 서비스를 연습하기 위한 프로젝트입니다.

## 🕰️ 개발 기간
* 23.09.10 ~ 23.09.12

### 🧑‍🤝‍🧑 맴버구성
- 이해찬
- 김인용
- 장지예

## ⚙️ 개발 환경
- **MainLanguage** : `Java - JDK 17`
- **IDE** : `IntelliJ IDEA Ultimate`
- **Framework** : `SpringBoot`
- **Database** : `MySQL`
- **SERVER** : `AWS EC2`
- **TEST** : `Spring InnerServer(Apache Tomcat) & POSTMAN API Request`

## 📌 주요 기능
### 

## ⚠️ 주의
#### 추적 예외
* DB 접속 정보 및 Naver Developers의 Client ID 및 Secret Key는 추적이 제외되어 있습니다.
* `application.properties`와 src/main/resources/ 경로에 생성해야 합니다.
```
# application.properties의 내용
spring.datasource.url=jdbc:mysql://localhost:3306/orderapp
spring.datasource.username={MySQL 계정}
spring.datasource.password={비밀번호}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

# API-KEY 포함
spring.profiles.include=API-KEY
```
```
# application-API-KEY.properties의 내용
naver.client.id={CLIENT_ID}
naver.client.secret={SECRET_KEY}
```
