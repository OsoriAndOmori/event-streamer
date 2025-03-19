# Event Streamer

Event Streamer는 Kafka 토픽에서 메시지를 소비하고, 설정된 조건에 따라 다양한 HTTP 엔드포인트로 메시지를 전달하는 Spring Boot 애플리케이션입니다.

## 기능

- Kafka 토픽 메시지 소비
- 설정 기반의 HTTP 엔드포인트 라우팅
- 조건부 메시지 전달
- 유연한 HTTP 헤더 설정

## 기술 스택

- Kotlin 1.9.22
- Spring Boot 3.2.3
- Spring Kafka
- Gradle (Kotlin DSL)
- Jackson Kotlin Module
- Docker & Docker Compose

## 시작하기

### 필수 조건

- Java 17 이상
- Gradle 7.5 이상
- Docker & Docker Compose (선택사항)

### Kafka 환경 설정

#### Docker Compose를 사용하는 경우

```bash
# Kafka, Zookeeper, Kafka Manager 실행
docker-compose up -d

# Kafka Manager 접속
# 브라우저에서 http://localhost:9000 접속
```

#### 수동으로 Kafka를 설치하는 경우

1. [Kafka 다운로드](https://kafka.apache.org/downloads)
2. Zookeeper 실행
3. Kafka 서버 실행

### 애플리케이션 빌드

```bash
./gradlew clean build
```

### 애플리케이션 실행

```bash
./gradlew bootRun
```

## 설정

### application.yml 설정

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: event-streamer-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer

http-endpoints:
  endpoints:
    - name: endpoint1
      url: http://localhost:8081/api/endpoint1
      method: POST
      headers:
        Content-Type: application/json
      condition: "${event.type == 'TYPE1'}"
    - name: endpoint2
      url: http://localhost:8082/api/endpoint2
      method: POST
      headers:
        Content-Type: application/json
      condition: "${event.type == 'TYPE2'}"
```

## HTTP 엔드포인트 설정

각 엔드포인트는 다음 속성을 가집니다:

- `name`: 엔드포인트 식별자
- `url`: HTTP 요청을 보낼 URL
- `method`: HTTP 메서드 (GET, POST, PUT, DELETE 등)
- `headers`: HTTP 헤더 설정
- `condition`: 메시지 전달 조건 (SpEL 표현식)

## Kotlin 특징

- 데이터 클래스를 사용한 설정 속성 관리
- 널 안정성을 통한 안전한 코드
- 함수형 프로그래밍 스타일의 이벤트 처리
- 코틀린 DSL을 사용한 Gradle 빌드 설정

## Docker Compose 구성

프로젝트는 다음 Docker 서비스들을 포함합니다:

- **Zookeeper** (포트: 2181)
  - Kafka 클러스터 관리를 위한 분산 코디네이터
  - Confluent Platform 7.5.1 버전 사용

- **Kafka** (포트: 9092)
  - 메시지 브로커
  - Confluent Platform 7.5.1 버전 사용
  - 단일 브로커 구성 (개발 환경용)

- **Kafka Manager** (포트: 9000)
  - Kafka 클러스터 모니터링 및 관리 UI
  - 웹 브라우저에서 http://localhost:9000 으로 접속 가능 