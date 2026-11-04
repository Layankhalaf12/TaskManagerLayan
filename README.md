# Task Manager with Role-Based Security

A Spring Boot MVC web app where users manage their own tasks and an admin manages everyone's. The focus is **Spring Security**: authentication, role-based access to whole areas of the site, and method-level checks that enforce who owns each task.

## Features

- **Login with BCrypt-hashed passwords.** In-memory users (`user1`, `user2`, `admin`) are defined in `SecurityConfig` and stored with a `BCryptPasswordEncoder`.
- **Role-based routes.** A `SecurityFilterChain` makes the home page public, opens `/user/**` to the USER and ADMIN roles, restricts `/admin/**` to ADMIN, and requires login everywhere else.
- **Role-aware redirect.** After login, `/dashboard` sends admins to the admin dashboard and users to their own.
- **Ownership enforced in the service layer.** `@PreAuthorize` rules on `TaskService` mean a user can only list or complete their own tasks. Only an admin can see all tasks or delete any task. This works even if someone calls a URL directly.
- **Custom access-denied page.**

## How it works

| Layer | Class | Responsibility |
|---|---|---|
| Security | `security/SecurityConfig` | Users, password encoder, URL rules, form login, method security |
| Controllers | `HomeController`, `UserController`, `AdminController` | Thin controllers that read the logged-in user from `Authentication` and call the service |
| Service | `service/TaskService` | Business logic plus `@PreAuthorize` ownership and role checks |
| Model | `model/Task` | Task id, title, description, completed flag and owner username |
| Views | `templates/**` (Thymeleaf) | Home, user dashboard, create-task form, admin dashboard, access denied |

Tasks are kept in memory, so they reset when the app restarts.

## Running

```bash
./mvnw spring-boot:run
```

Then open http://localhost:8080 and sign in as one of the demo users from `SecurityConfig`.

## Tech
Java 17 · Spring Boot · Spring Security · Spring MVC · Thymeleaf · Maven

## Reflection

This assignment made me see how Spring Security can be integrated in a real MVC application. I applied in-memory users with BCrypt, role based access SecurityFilterChain and method level security in service layer to enforce ownership. I also understood the importance of the layered architecture: controllers remain thin and the service is made up of the business logic and security checks. Developing the views and using it with various users demonstrated clearly how role and task ownership can be implemented. Generally, I am more secure in developing secure, role-conscious web apps using Spring Boot.
