# Restaurant Network

Application was built as a practice task for Arcadia. It is written in Java and based on Spring Boot + Data + Web + Hibernate and Thymeleaf for view rendering. PostgreSQL is used as a DBMS.

## Getting Started

Data generation script is located in resources folder and is named 'import.sql'. Due to the option
```
spring.jpa.hibernate.ddl-auto = create-drop
```
data is loaded in database each time application starts and is dropped from database after.
Configuration can be found in ‘application-dev.properties’ in resources folder.
Default PostgreSQL credentials:
```
login: restnetwork
password: restnetwork
localhost port 5432
```

## Screenshots
View of cook list
![screen 1](./screenshots/1.png)
View of shift roster
![screen 2](./screenshots/2.png)



