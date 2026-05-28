# IMDb Clone

A movie database application inspired by IMDb, built with Spring Boot and React.

## Tech Stack

**Backend:** Spring Boot, Java  
**Frontend:** React, JavaScript  
**Database:** PostgreSQL

## Project Structure
```
ImdbClone/
├── backend/          # Spring Boot application
├── frontend/         # React application
└── README.md
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6+
- Docker and Docker Compose

### Local Postgres Setup

The backend includes a Docker Compose Postgres database with a persistent named volume. Spring creates/updates the schema from JPA entities and seeds initial users, media, ratings, reviews, genres, and people on the first empty startup through the `dev` profile.

`application.properties` enables `dev,jwt,postgres` by default, so the backend uses this Docker Postgres database without requiring every developer to set `SPRING_PROFILES_ACTIVE` manually.

Start Postgres:
```bash
cd backend
cp .env.example .env
docker compose up -d
```

Run the backend against Postgres:
```bash
cd backend
./mvnw spring-boot:run
```

The local defaults are:
- JDBC URL: `jdbc:postgresql://localhost:2345/imdb_clone_app`
- Database: `imdb_clone_app`
- User: `imdb`
- Password: `imdb`

You can override them with `POSTGRES_JDBC_URL`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`, and `POSTGRES_PORT`.

Local overrides belong in `backend/.env`, copied from `backend/.env.example`. Docker Compose reads `backend/.env` automatically. Spring uses the same defaults from `application-postgres.properties`; if you change the database credentials or port in `.env`, export matching values before running `./mvnw spring-boot:run`.

Reset to a fresh seeded database:
```bash
cd backend
docker compose down -v
docker compose up -d
./mvnw spring-boot:run
```

### Backend Setup
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

The backend server will start on `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

The React app will start on `http://localhost:3000`

## Development Workflow

### Important: Always Pull Before Starting Work

**Before you start working each day:**
```bash
git checkout master
git pull origin master
```

This ensures you have the latest changes from the team.

## Team Guidelines

- **Always pull from main before starting work**
- Write clear, descriptive commit messages
- Review code thoroughly
- Test your changes locally before pushing

## Features

- [ ] Movie search and browse
- [ ] Movie details page
- [ ] User ratings and reviews
- [ ] Watchlist functionality
- [ ] User authentication
- [ ] [Add your features here]
