# AppointmentApp
AppointmentApp is an API for university or other educational structure to facilitate connection between students and teachers. This why system supports two roles: teacher and student. Every role is responsible for a different part of functionality. 
Teacher can set time range for personal lessons, set prize for certain amount of time and also approve or decline lessons request by email.
Student can book private lesson and cancel it by email. They both can sudsribe on each other to easily management.

## Installation

 1. You should create environmental variables that are defined in ``` application.properties ```
 2. You should create database ``` appointment_app ```
and set all enviromental variables.
```java
spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USER}
spring.datasource.password=${DATASOURCE_PASSWORD}
spring.mail.user=${EMAIL}
spring.mail.password=${EMAIL_PASSWORD}
```
Clone this repo to your local machine using https://github.com/maryshchur/AppointmentApp.git
```bash
mvn compile
```
It will just compile the code of application.

```bash
mvn package
```
It will pack the code up in a JAR file within the target directory.

To execute the JAR file run:
```bash
java -jar target\AppointmentApp-1.0-SNAPSHOT.jar
```
  3. If you did everything correctly, you should be able access swagger by this URL: http://localhost:8080/swagger-ui.html#/
