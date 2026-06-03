# 🌱 Hídrico Monitor | Water Stress Monitoring System
Developed by: Vitória, Kauan, Isabel e Ane
Institution: FIAP
Course: Information Systems Development

## 🇧🇷 Português

### Sobre o Projeto

O Hídrico Monitor é uma aplicação desenvolvida para monitoramento de estresse hídrico em propriedades agrícolas utilizando índices de vegetação obtidos por sensoriamento remoto.

O sistema permite acompanhar indicadores ambientais, identificar áreas críticas e gerar alertas para auxiliar produtores rurais na tomada de decisões relacionadas à irrigação e manejo agrícola.

### Funcionalidades

- Cadastro de propriedades rurais
- Cadastro de produtores
- Monitoramento de índices de vegetação
- Processamento de dados de satélite
- Geração de alertas de estresse hídrico
- Dashboard de monitoramento
- Histórico de leituras
- API REST com Spring Boot

### Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Data JPA
- Hibernate
- Maven
- H2 Database
- Lombok
- Thymeleaf
- Git & GitHub

### Estrutura do Projeto

```text
src
├── controller
├── dto
├── model
├── repository
├── service
└── resources
```

### Como Executar

```bash
git clone https://github.com/vislopes/hidrico-monitor-globalsolution.git

cd hidrico-monitor-globalsolution

mvn clean install

mvn spring-boot:run
```

Aplicação disponível em:

```text
http://localhost:8080
```

### Objetivo Acadêmico

Projeto desenvolvido para a disciplina Global Solution, com foco em sustentabilidade, agricultura inteligente e monitoramento ambiental.

---

## 🇺🇸 English

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
