# Search Project
키워드에 대한 카카오 검색 결과와 네이버 검색 결과 취합 시스템 <br/>
일단위 검색 이력 제공

## 개발 프레임워크
Spring boot 2.7.6

## Prerequisites
jdk-11.0.16.1 <br/>
Lombok plugin <br/>

## Dependencies
spring-boot-starter-data-jpa <br/>
spring-boot-starter-web <br/>
spring-boot-starter-webflux <br/>
spring-boot-starter-validation <br/>
com.h2database:h2 <br/>
lombok <br/>
spring-boot-starter-test <br/>
spring-boot-maven-plugin <br/>

## Package

## API
|Action|API|Parameter|
|------|------|------|
|Search|GET /search|{"keyword": "hi"}|
|Rank|GET /rank||

## 호출방법
1. search : query param인 keyword를 바꿔가며 호출 가능
curl --location 'http://localhost:9090/search?keyword=Thanks'
2. rank : 다른 기능없이 rank 호출 가능
curl --location 'http://localhost:9090/rank'

## 테이블 설계
|keyword_history(테이블명)|
|------|
|**historyId (pk)**|
|keyword|
|search_day|
|create_date|

|statistics(테이블명)|
|------|
|**keyword (pk)**|
|**statistics_minute(pk)**|
|search_day|
|search_count|
|create_date|

## 문제해결 전략
### 베이스 데이터
네이버가 5개로 데이터 리턴 개수가 고정되어 있어, 카카오를 먼저 10개를 조회하고
네이버의 리턴 개수에 따라 베이스 데이터를 10개를 맞추도록 카카오 데이터를 제거하는 형태로 개발
### 확장성을 이용한 팩토리 패턴 구현
webclient를 팩토리 패턴으로 구현하여 추가되는 api 기관이 있을 시 webclient를 추가해주는 형태로 설계
### 대용량 트래픽 처리
webclient를 병렬로 호출하고, 데이터 취합시 countDownlatch로 응답값을 받아 병렬처리로 서비스 속도 향상
통계를 호출 시 마다가 아닌, 10분마다 스케줄러 형태로 반영하여 시스템 부하를 줄임
### 동시성 이슈 방지
데이터 업데이트를 없이 insert 위주의 데이터 설계
### 같은 상호로 판단하는 기준 구현
상호명을 trim해서 같은 값일 때, 도로명주소와 기존 주소중에 한 값이 같으면 같은 상호로 구현<br/>
도로명 주소와 기존 주소는 카카오와 네이버가 규칙이 달라, 카카오에 맞춰 네이버값을 정규식으로 변환함 (ex 서울특별시 -> 서울) <br/>
주소값에서 층 호 들은 표기된 곳도 있고, 아닌 곳도 있어 번지까지 같으면 같은 주소로 취급 <br/>
### 단위테스트로 각 기능 검증
ApiClientTest.java로 ApiClient 기능 검증 <br/>
ContorllerTest.java로 Search 기능 검증 <br/>
StatisticsControllerTest.java로 통계 기능 검증
### Exception 처리
GlobalExceptionHandler를 구현하여 에러 처리를 분기함
실패시 errCode는 "E"로 시작하게 설계
Apiclient에서는 에러 발생 시 한개의 기관에서라도 데이터를 산출할 수 있도록 빈 값의 데이터를 받아오도록 함
## 빌드 및 실행 방법
gradle update 후 spring boot app 으로 기동.

