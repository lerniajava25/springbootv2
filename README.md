# Helpdesk

A simple Spring Boot application for managing support tickets.

## Start

1. Start Docker Desktop and run `docker compose up -d db` in the project folder.
2. Run `.\mvnw.cmd spring-boot:run` in PowerShell.
3. Open <http://localhost:8080>.

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

The application uses MySQL by default. The Docker and local development connection is:

| Setting | Default |
|---|---|
| Host | `localhost` |
| Port | `3306` |
| Database | `helpdesk` |
| Username | `helpdesk` |
| Password | `admin` |
