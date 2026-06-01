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

`application.properties` defaults to the in-memory H2 `dev` profile for quick local starts. Use the `postgres` profile when you want imported TMDB data to remain after the backend stops.

Start Postgres:
```bash
cd backend
cp .env.example .env
docker compose up -d
```

Run the backend against Postgres:
```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,jwt,postgres
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
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,jwt,postgres
```

### Optional TMDB Import

The backend imports a larger movie and TV-series catalogue from TMDB on startup when
`TMDB_ENABLED=true`. For this student project it defaults to enabled, but the
import is skipped if no token is configured. Put credentials in `backend/.env`;
do not commit real TMDB tokens or API keys.

```bash
cd backend
cp .env.example .env
# Edit .env and set TMDB_API_TOKEN to your v4 Read Access Token.
./mvnw spring-boot:run
```

For a persistent imported catalogue, start Postgres and run with
`-Dspring-boot.run.profiles=dev,jwt,postgres`.

Default import settings add up to 500 new movies and 300 new TV series, using
several TMDB discovery sorts to get a mix of popular, highly rated, highly
voted, and newer titles. Imported media includes genres, poster URL, TMDB vote
average, trailer URL when available, and cast/creator/director credits.

Useful overrides:

- `TMDB_MAX_MOVIES=1000` changes the import cap for one startup run.
- `TMDB_MAX_TV_SERIES=600` changes the TV-series import cap for one startup run.
- `TMDB_PAGES=50` changes how many 20-item TMDB pages are read per sort.
- `TMDB_DISCOVERY_SORTS=popularity.desc,vote_average.desc` changes the discovery
  sort list for movies.
- `TMDB_TV_DISCOVERY_SORTS=popularity.desc,vote_average.desc` changes the
  discovery sort list for TV series.
- `TMDB_MINIMUM_VOTE_COUNT=200` filters out obscure titles when sorting by rating.
- `TMDB_MAX_CAST=10` changes how many cast members are stored per media item.

TMDB requires attribution when their API data or images are used. Keep the
approved TMDB logo and attribution notice visible in the application
footer/About/Credits area.

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
