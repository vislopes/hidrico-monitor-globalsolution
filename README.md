# 🌱 Hydro Monitor | Water Stress Monitoring System
Developed by: Vitória, Kauan, Isabel e Ane
Institution: FIAP
Course: Information Systems Development

### About the Project

Hídrico Monitor is an application developed to monitor water stress in agricultural properties using vegetation indices obtained through remote sensing.

The system helps farmers monitor environmental indicators, identify critical areas, and generate alerts to support irrigation and crop management decisions.

### Features

- Rural property registration
- Producer registration
- Vegetation index monitoring
- Satellite data processing
- Water stress alert generation
- Monitoring dashboard
- Historical readings
- REST API with Spring Boot

### Technologies

- Java 21
- Spring Boot 3
- Spring Data JPA
- Hibernate
- Maven
- H2 Database
- Lombok
- Thymeleaf
- Git & GitHub

### Project Structure

```text
src
├── controller
├── dto
├── model
├── repository
├── service
└── resources
```

### Running the Application

```bash
git clone https://github.com/vislopes/hidrico-monitor-globalsolution.git

cd hidrico-monitor-globalsolution

mvn clean install

mvn spring-boot:run
```

Application available at:

```text
http://localhost:8080
```

### Academic Purpose

This project was developed for the Global Solution challenge, focusing on sustainability, smart agriculture, and environmental monitoring.
