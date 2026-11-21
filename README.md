# Helpdesk

A simple Spring Boot application for managing support tickets.

## Requirements

- Java 17
- A local MySQL server

## Running the project

### 1. Start MySQL

Start MySQL and make sure it is listening on port `3306`.

Create the application database and user if they do not already exist:

```sql
CREATE DATABASE IF NOT EXISTS helpdesk;
CREATE USER IF NOT EXISTS 'helpdesk'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON helpdesk.* TO 'helpdesk'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Start the application

Windows:

```bat
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

### 3. Open the application

<http://localhost:8080>

## Features

- `USER` can create tickets, view their own tickets, and add comments.
- `AGENT` can view all tickets, assign tickets to themselves, change status, and add comments.
- `ADMIN` can manage users, roles, and accounts.

Important pages:

- `/login` – log in
- `/tickets` – tickets
- `/tickets/new` – create a ticket
- `/admin/users` – manage users

## Test accounts

| Username | Password | Role |
|---|---|---|
| `admin` | `admin` | ADMIN |
| `agent` | `agent` | AGENT |
| `user` | `user` | USER |

Sign in at <http://localhost:8080/login>.

## Database

The application uses MySQL by default. The local development connection is:

| Setting | Default |
|---|---|
| Host | `localhost` |
| Port | `3306` |
| Database | `helpdesk` |
| Username | `helpdesk` |
| Password | `admin` |
