# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.3/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.3/reference/web/servlet.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/3.5.3/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/3.5.3/reference/data/nosql.html#data.nosql.mongodb)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.3/reference/web/spring-security.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.3/reference/using/devtools.html)
* [Validation](https://docs.spring.io/spring-boot/3.5.3/reference/io/validation.html)
* [Java Mail Sender](https://docs.spring.io/spring-boot/3.5.3/reference/io/email.html)

### Guides
you have to create /src/main/resources/application.properties

spring.application.name=clearance-app

# === MongoDB Configuration ===
spring.data.mongodb.uri=mongodb://localhost:27017/clearance_app
spring.data.mongodb.database=clearance_app



# === Thymeleaf Settings ===
spring.thymeleaf.cache=false

# === Server Port  ===
server.port=8080

# === Logging Level  ===
logging.level.org.springframework=INFO


# === Email ===
spring.mail.host=smtp server here
spring.mail.port=587
spring.mail.username=email here
spring.mail.password= app password here
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true