🏥 Healthcare Microservices Platform
This project is a production-style microservices application built with Java Spring Boot, following real-world backend architecture patterns such as
service isolation, API Gateway routing,authentication, gRPC communication, and event-driven messaging with Kafka.

Architecture Overview
---------------------
Services
Patient Service – Core domain service handling patient-related operations
Billing Service – gRPC-based billing operations
Notification Service – Kafka consumer for event-driven notifications
Auth Service – Authentication and authorization using JWT
PostgreSQL Databases – Dedicated databases per service

Communication
REST: External APIs
gRPC: Synchronous service-to-service communication
Kafka: Asynchronous event streaming
Protobuf: Message serialization

📁 Repository Structure
.
├── api-gateway/          # Central entry point for all client requests
├── auth-service/         # Authentication & JWT authorization
├── patient-service/      # Patient domain logic
├── billing-service/      # Billing logic (gRPC-based)
├── analytics-service/    # Kafka consumer for analytics/events
├── test/                 # JUnit tests
└── README.md

🧱 Technology Stack

Layer	        Technology
Language	Java 17
Framework	Spring Boot
API	        REST + gRPC
Messaging	Apache Kafka
Authentication	Spring Security + JWT
Database	PostgreSQL
Containers	Docker / Docker Compose
Testing	        JUnit
API Testing	Postman (REST & gRPC)

Notes
1. Each service is independently deployable
2. gRPC is used for low-latency internal communication
3. Kafka enables asynchronous, event-driven workflows
4. Protobuf ensures efficient and strongly typed messaging



