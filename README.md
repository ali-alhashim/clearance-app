# Clearance App

A full-stack clearance management system built using **Spring Boot**. This app streamlines the employee clearance process by allowing HR, IT, Admin, and other departments to manage and approve clearances digitally.

## 🚀 Features

- 🔐 Role-based access control ( ADMIN, USER)
- 👨‍💼 Manage employees, approvals, and clearance forms
- 🗂️ Track clearance status and comments
- 📨 Email notifications for new Clearance
- 📊 Dashboard for clearance overview
- 🧩 Modular code structure using DTOs, Repositories, Services, and Controllers

## 🛠️ Tech Stack

- **Backend:** Java 24, Spring Boot
- **Frontend:** Thymeleaf (HTML templating)
- **Database:** mongodb (configurable)
- **Build Tool:** Maven




## 📂 Project Structure

```bash
clearance-app/
├── src/main/java/com/clearance/app/
│   ├── controller/
│   ├── model/
│   ├── repository/
│   ├── service/
│   └── dto/
    └── config/

├── src/main/resources/
│   ├── templates/
│   ├── static/
│   └── application.properties 
├── pom.xml
└── README.md

## you have to create this file application.properties 
spring.application.name=clearance-app

# === MongoDB Configuration ===
spring.data.mongodb.uri=mongodb://localhost:27017/clearance_app
spring.data.mongodb.database=clearance_app



# === Thymeleaf Settings ===
spring.thymeleaf.cache=false

# === Server Port  ===
server.port=8080

# === Your Sever Domain
app.base-url=http://localhost:8080


# === Logging Level  ===
logging.level.org.springframework=INFO


# === Email ===
spring.mail.host= smtp server for your email
spring.mail.port=587
spring.mail.username=Your Email 
spring.mail.password=Your Emaill app password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true


# === Devtools ===
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
spring.resources.cache.cachecontrol.no-store=true

