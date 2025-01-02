# ForumHub

A forum project featuring JWT authentication, developed with Spring Boot.

## 🚀 Features

- Create, read, update, delete, view details, and mark topics as solved
- Create and read courses
- Post answers to topics
- Register and log in users

## 🔨 Tools

- **Java**
- **Spring Boot**:
  - Data JPA
  - Web
  - Security
  - Validation
  - Dev Tools
  - Flyway Migration
  - MySQL Driver
- **MySQL**
- **Maven**
- **JSON Web Token (JWT)**
- **SpringDoc**

## 📁 Access to the Project

1. Clone this repository.
   ```bash
   git clone https://github.com/kamille-rabelo/forum-hub-api.git
   ```
2. Create MySQL databases: `forumhub_api` and `forumhub_api_test`.
3. Update the `application.properties` file with your MySQL username and password.
4. Run the `ApiApplication` class.
5. After running the migrations, manually add a user with ADMIN role to register other users.
6. Open http://localhost:8080/swagger-ui.html to view the API documentation.

## 🏷️ Database Modeling

### **User**

- `name` 
- `email`
- `password`
- `role` (Enum: ADMIN, INSTRUCTOR, STUDENT)

### **Course**

- `name`
- `categories`

### **Topic**

- `title`
- `message`
- `course` (FK to Course)
- `author` (FK to User)
- `creation_date`
- `solved`

### **Answer**

- `message`
- `author` (FK to User)
- `topic` (FK to Topic)
- `creation_date`
- `solution`

### **Courses_Categories**

- `course` (FK to Course)
- `category`

## 🔐 Security Configuration

The application uses role-based access control to secure endpoints. Below is a snippet of the security configuration:

```java
.authorizeHttpRequests(req -> {
    req.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
    req.requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN");
    req.requestMatchers(HttpMethod.POST, "/topics/{topicId}/answers/{answerId}/mark-solved", "/courses").hasRole("INSTRUCTOR");
    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
    req.anyRequest().authenticated();
});
```

### Explanation:
- **Public Endpoints**: `POST /auth/login` and API documentation paths are accessible without authentication.
- **Admin-Only Access**: `POST /auth/register` Restricted to users with ADMIN role to ensure only authorized personnel can register new accounts.
- **Instructor-Specific**: Actions like marking topics as solved or creating courses require the INSTRUCTOR role.
- **Default Policy**: All other requests require authentication.
