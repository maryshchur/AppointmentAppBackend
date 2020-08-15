# AppointmentApp
AppointmentApp is API for univercity or other educational structure to facilitate connection between students and teachers. This why system supports two roles: teacher and student. Every role is responsible for a different part of functionality. 
Teacher can set time range for personal lessons, set prize for certain amount of time and also approve or decline lessons request by email.
Student can book private lesson and cancel it by email. 

## Installation
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
