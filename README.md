### 1주차 spring batch study

## 기본 프로젝트 구성
<img width="873" alt="스크린샷 2024-10-07 오후 10 27 56" src="https://github.com/user-attachments/assets/ae541cbf-074b-4939-af37-77d34edfd0bf">

- application.yaml 구성
``` 
# H2 DataBase용
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
```
## 배치 기동 시키기
- @EnableBatchProcessing main class 위에 추가
  - spring boot 3.0 이전 버전에서는 필수
  - spring boot 3.0 이후 버전에서는 기본 설정으로 추가되어 어노테이션 불필요

## spring-boot-start-web 추가
- implementation 'org.springframework.boot:spring-boot-starter-web' 의존성 추가
- spring boot는 내장 톰켓을 사용하기 때문에 위 의존성을 추가해야 서버 구동이 가능하다.

## 스프링 배치 스키마 구조
![image](https://github.com/user-attachments/assets/88366e3b-94ff-4dff-8eaf-c7e8b3ad5b3d)

### BATCH_JOB_INSTANCE Table
- 스키마중 가장 기본이 되는 배치 잡 인스턴스 테이블이다.
- 배치가 수행되면 Job이 생성이 되고, 해당 잡 인스턴스에 대해서 관련된 모든 정보를 가진 최상위 테이블이다.

### BATCH_JOB_EXECUTION_PARAMS Table
- JobParameter에 대한 정보를 저장하는 테이블이다.
- 여기에는 하나 이상의 key/value 쌍으로 Job에 전달되며, job이 실행될때 전달된 파라미터 정보를 저장하게 된다.
- 각 파라미터는 IDENTIFYING이 true로 설정되면, JobParameter 생성시 유니크한 값으로 사용된경우라는 의미가 된다.

### BATCH_JOB_EXECUTION Table
- JobExecution과 관련된 모든 정보를 저장한다.
- Job이 매번 실행될때, JobExecution이라는 새로운 객체가 있으며, 이 테이블에 새로운 row로 생성이 된다.

### BATCH_STEP_EXECUTION Table
- BATCH_STEP_EXECUTION Table 은 StepExecution과 관련된 모든 정보를 가진다.
- 이 테이블은 여러 면에서 BATCH_JOB_EXECUTION 테이블과 유사하며 생성된 각 JobExecution에 대한 단계당 항목이 항상 하나 이상이 있다.

### BATCH_JOB_EXECUTION_CONTEXT Table
- Job의 ExecutionContext 에 대한 모든 정보를 저장한다.
- 이것은 매 JobExecution마다 정확히 하나의 JobExecutionContext를 가진다. 여기에는 특정 작업 실행에 필요한 모든 작업 수준 데이터가 포함되어 있다
- 이 데이터는 일반적으로 실패 후 중단된 부분부터 시작될 수 있도록 실패후 검색해야하는 상태를 나타낸다.
 
### BATCH_STEP_EXECUTION_CONTEXT Table
- BATCH_STEP_EXECUTION_CONTEXT 테이블은 Step의 ExecutionContext 과 관련된 모든 정보를 가진다.
- StepExecution 마다 정확히 하나의 ExecutionContext 이 있다. 그리고 특정 step execution 에 대해서 저장될 필요가 있는 모든 데이터가 저장된다.
- 이 데이터는 일반적으로 JobInstance가 중단된 위치에서 시작 할 수 있도록 실패 후 검색해야 하는 상태를 나타낸다.
