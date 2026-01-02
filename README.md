
# Healthcare Microservices Platform

This project is a production-style microservices application built with Java Spring Boot, following real-world backend architecture patterns such as service isolation, API Gateway routing,authentication, gRPC communication, and event-driven messaging with Kafka.


## Architecture Overview

Services

1. Patient Service – Core domain service handling patient-related operations

2. Billing Service – gRPC-based billing operations

3. Notification / Analytics Service – Kafka consumer for event-driven notifications

4. Auth Service – Authentication and authorization using JWT

5. PostgreSQL Databases – Dedicated DB per service

# Communication Patterns

• REST: External APIs

• gRPC: Synchronous service-to-service communication

• Kafka: Asynchronous event streaming

• Protobuf: Message serialization

# Technology Stack

| Layer          | Technology              |
| -------------- | ----------------------- |
| Language       | Java 17                 |
| Framework      | Spring Boot             |
| API            | REST + gRPC             |
| Messaging      | Apache Kafka            |
| Authentication | Spring Security + JWT   |
| Database       | PostgreSQL              |
| Containers     | Docker / Docker Compose |
| Testing        | JUnit                   |
| API Testing    | Postman (REST & gRPC)   |

# Getting Started

This project uses Docker containers for each microservice and database. Make sure you have Docker installed before starting.

All containers are connected via a custom Docker network called internal.

‣ Run This

docker network create internal

# Patient Service Database

Container: patient-service-db
Image: postgres:15
Ports: Host 5000 → Container 5432

docker run -d \
  --name patient-service-db \
  -e POSTGRES_USER=admin_user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=db \
  -p 5000:5432 \
  -v patient_db_data:/var/lib/postgresql/data \
  --network internal \
  postgres:15

# Patient Service

Container: patient-service
Image: patient-service:latest
Ports: Host 4000 → Container 4000

‣ Environment Variables:

-e SPRING_DATASOURCE_URL=jdbc:postgresql://patient-service-db:5432/db
-e SPRING_DATASOURCE_USERNAME=admin_user
-e SPRING_DATASOURCE_PASSWORD=password
-e SPRING_JPA_HIBERNATE_DDL_AUTO=update
-e SPRING_SQL_INIT_MODE=always
-e BILLING_SERVICE_ADDRESS=billing-service
-e BILLING_SERVICE_GRPC_PORT=9001
-e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092


‣ Build Docker image

docker build -t patient-service:latest ./patient-service

‣ Run container

docker run -d \
  --name patient-service \
  --network internal \
  -p 4000:4000 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://patient-service-db:5432/db \
  -e SPRING_DATASOURCE_USERNAME=admin_user \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
  -e SPRING_SQL_INIT_MODE=always \
  -e BILLING_SERVICE_ADDRESS=billing-service \
  -e BILLING_SERVICE_GRPC_PORT=9001 \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  patient-service:latest

# Billing Service

Container: billing-service
Image: billing-service:latest
Ports: Host 4001 → Container 4001 (REST)
Host 9001 → Container 9001 (gRPC)

‣ Build Docker image

docker build -t billing-service:latest ./billing-service

‣ Run container

docker run -d \
  --name billing-service \
  -p 4001:4001 -p 9001:9001 \
  --network internal \
  billing-service:latest

# Analytics / Notification Service

Container: analytics-service
Image: analytics-service:latest
Ports: Host 4002 → Container 4002

‣ Environment Variables:
-e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092

‣ Build Docker image

docker build -t analytics-service:latest ./analytics-service

‣ Run container

docker run -d \
  --name analytics-service \
  --network internal \
  -p 4002:4002 \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  analytics-service:latest

# Kafka Broker

Container: kafka
Image: bitnami/kafka:latest
Ports: Host 9092, 9094 → Container 9092, 9094

Environment Variables:

-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,EXTERNAL://localhost:9094
-e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
-e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093
-e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT
-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://:9094
-e KAFKA_CFG_NODE_ID=0
-e KAFKA_CFG_PROCESS_ROLES=controller,broker

# Auth Service

Database Container: auth-service-db

‣ Run container

docker run -d \
  --name auth-service-db \
  --network internal \
  -e POSTGRES_USER=admin_user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=db \
  -p 5001:5432 \
  -v auth_db_data:/var/lib/postgresql/data \
  postgres:15

Service Container: auth-service

‣ Environment Variables:

-e SPRING_DATASOURCE_URL=jdbc:postgresql://auth-service-db:5432/db
-e SPRING_DATASOURCE_USERNAME=admin_user
-e SPRING_DATASOURCE_PASSWORD=password
-e SPRING_JPA_HIBERNATE_DDL_AUTO=update
-e SPRING_SQL_INIT_MODE=always
-e JWT_SECRET=<your-secret-key>

‣ Build Docker image

docker build -t auth-service:latest ./auth-service

‣ Run container 

docker run -d \
  --name auth-service \
  --network internal \
  -p 4005:4005 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://auth-service-db:5432/db \
  -e SPRING_DATASOURCE_USERNAME=admin_user \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
  -e SPRING_SQL_INIT_MODE=always \
  -e JWT_SECRET=<your-secret-key> \
  auth-service:latest

# API Gateway

Container: api-gateway
Image: api-gateway:latest
Ports: Host 4004 → Container 4004

Environment Variables:

-e AUTH_SERVICE_URL=http://auth-service:4005

‣ Build and Run Image

docker build -t api-gateway:latest ./api-gateway

docker run -d \
  --name api-gateway \
  --network internal \
  -p 4004:4004 \
  -e AUTH_SERVICE_URL=http://auth-service:4005 \
  api-gateway:latest


# Key Notes

• Each service is independently deployable

• gRPC is used for low-latency internal service communication

• Kafka enables asynchronous, event-driven workflows

• Protobuf ensures efficient and strongly typed messaging
