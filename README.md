# Banking System With Spring Boot And Angular with Spring Security

## Introduction

@author: ANG KUO SHENG CLEMENT

This project is a simplified version that aims to allow users to perform online banking activity. It uses spring boot framework (Java) in the backend & angular in the front-end . Some of the functionalities implemented are
- Account Controller
    - Account creation
    - Retrieve Acccount Details
    - Perform Transactions to transfer funds between two accounts
    - Fetching of account transaction history
- User Controller
    - Use username & password for sign-in/login authentication
    - IAM policies - role, privileges and permissions tables      

## Prerequisites
- Java JDK 13
- Oracle 12cR2
- maven 3.x
- npm for Angular 9

## ER Diagram

- banking system ER diagram


![banking system ER diagram](/images/banking-systems-ER-diagram.png "banking-system-ER diagram")

### Steps To Setup Backend

**1. Clone the repository**
```bash
    https://github.com/kuosheng-ang/dbs-internetbanking-system.git![image](https://github.com/kuosheng-ang/dbs-internetbanking-system/assets/90085499/05a7b52c-73ed-4483-965b-5f003011a087)

```

**2. Move to root directory of backend**

**3. Build project**
```bash
    mvn clean install
``` 

**4. Run project** 
```bash
    java -jar target/backend-0.0.1-SNAPSHOT.jar
``` 
- Alternatively, you can run the app without packaging it using -
```bash
    mvn spring-boot:run
```
  #### Explore apis in backend

The app defines following APIs. 
 
```   
    POST /api/signup   
    POST /api/user
    GET /api/user
    PUT /api/user
    GET /api/account
    POST /api/transfer
    GET /api/transaction      
```

### Steps To Setup Frontend

**1. Move To Frontend Derectory**

**2. Install Package**
```bash 
    npm install
```

**3. Run Project**
```bash
    npm start
```

**4. Open url**
```bash
    http://localhost:4200/
```
# Application Screenshots

### Login



### Register



### Home



### Profile



### Transaction History



### Transfer




