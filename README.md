# 🆔 IDPR - Identity Protection Registration

A full-stack application for user registration, login, and identity management. The project includes a Java Spring Boot backend with AES encryption and a React frontend.

---

## 📦 Project Structure

```
/idpr-backend     --> Java Spring Boot API  
/idpr-frontend    --> React Frontend UI
```

---

## ✅ Prerequisites

- Java 17+ (Preferably Java 21)
- Maven
- MySQL Server & MySQL Workbench
- Node.js & npm
- IDEs: IntelliJ IDEA (Backend), VSCode (Frontend)

---

## ⚙️ Backend Setup (Spring Boot)

### 1. Open the Backend Project
- Navigate to the `/idpr-backend` folder.
- Open the project in **IntelliJ IDEA**.

### 2. Import as a Maven Project
- Right-click on `pom.xml`.
- Select **Add as Maven Project** (wait for Maven dependencies to load).

### 3. Configure Database Connection
- Open the file:
  ```
  idpr-backend/src/main/resources/application.properties
  ```
- Edit the database credentials:
  ```properties
  spring.datasource.username=<your-mysql-username>
  spring.datasource.password=<your-mysql-password>
  ```
  Example:
  ```properties
  spring.datasource.username=root
  spring.datasource.password=root
  ```

### 4. Run the Application
- Open the file:
  ```
  idpr-backend/src/main/java/com/idpr/IdprApplication.java
  ```
- Right-click `IdprApplication` class → **Run 'IdprApplication'**.

---

## 🐬 MySQL Setup

### 1. Install MySQL
- Install **MySQL Server** & **MySQL Workbench**.

### 2. Create Database
- Open MySQL Workbench.
- Run the following query:
  ```sql
  CREATE DATABASE idpr_db;
  ```

---

## 💻 Frontend Setup (React)

### 1. Open the Frontend Project
- Navigate to the `/idpr-frontend` folder.
- Open it in **VSCode** (or your preferred code editor).

### 2. Install Dependencies
- Open the terminal in VSCode.
- Run:
  ```bash
  npm install
  ```

> 🔹 **Note:** Run `npm install` again if the project is updated from Git or if `node_modules` is missing.

### 3. Start the React App
```bash
npm start
```
- The app will run on:
  ```
  http://localhost:3000
  ```

---

## 🚀 Project Overview

| Component | Technology |
|-----------|------------|
| **Backend**  | Java 21, Spring Boot, Spring Security, AES Encryption, JPA |
| **Frontend** | React, Axios, React Router, CSS (Dark Theme) |
| **Database** | MySQL 8+ |

### Clone the Repository
```sh
git clone https://github.com/your-repo/project.git
cd project

## 🚀 **Authentication APIs**
### 1️⃣ **User Registration**
- **Endpoint**: `POST /api/auth/register`
- **Description**: Registers a new user.
- **Request Body**:
  ```json
  {
    "username": "johndoe",
    "email": "john@example.com",
    "password": "securepassword"
  }
  ```
- **Response**:
  ```json
  { "message": "User registered successfully" }
  ```

---

### 2️⃣ **User Login**
- **Endpoint**: `POST /api/auth/login`
- **Description**: Authenticates a user and returns a JWT token.
- **Request Body**:
  ```json
  {
    "username": "johndoe",
    "password": "securepassword"
  }
  ```
- **Response**:
  ```json
  {
    "token": "jwt-token-here"
  }
  ```

---

### 3️⃣ **Get Current User**
- **Endpoint**: `GET /api/auth/me`
- **Description**: Returns the details of the currently logged-in user.
- **Headers**:
  ```json
  { "Authorization": "Bearer jwt-token-here" }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com"
  }
  ```

---

## 👤 **User Management APIs**
### 5️⃣ **Get User by ID**
- **Endpoint**: `GET /api/users/{userId}`
- **Description**: Retrieves user details by ID.

---

### 6️⃣ **Update User Profile**
- **Endpoint**: `PUT /api/users/{userId}`
- **Description**: Updates user details.
- **Request Body**:
  ```json
  {
    "username": "john_updated",
    "email": "john_updated@example.com"
  }
  ```

---

### 7️⃣ **Delete User**
- **Endpoint**: `DELETE /api/users/{userId}`
- **Description**: Deletes a user.

---

## 🛠️ **Swagger API Documentation**
- **Endpoint**: `http://localhost:8080/swagger-ui.html`
- **Description**: Swagger UI for testing APIs.
---
Swagger UI (if enabled):

---

## 🛠️ Technologies Used

- **Spring Boot**
- **Spring Security**
- **JWT Authentication**
- **AES-256 Encryption (GCM)**
- **MySQL**
- **React 18**
- **Axios**

## 🌟 Features

- Secure user registration and login with JWT authentication.
- AES-256 encryption for sensitive data.
- Role-based access control (RBAC).
- RESTful APIs with Swagger documentation.
- Responsive React frontend with dark theme.
- MySQL database integration.

## ✨ Made By

- **Hitesh Kadam**  
  [GitHub Profile](https://github.com/HiteshKadam)