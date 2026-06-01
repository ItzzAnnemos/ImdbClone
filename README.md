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

### Local Docker Setup

The repository root includes a full-stack Docker Compose file for running
Postgres, the Spring Boot backend, and the production-built React frontend
together.

```bash
cp backend/.env.example backend/.env
# Edit backend/.env and set JWT_SECRET and TMDB_API_TOKEN.
docker compose --env-file backend/.env up --build
```

Services:

- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080`
- Postgres: `localhost:2345`

Stop the stack:

```bash
docker compose --env-file backend/.env down
```

Reset the persistent Docker database:

```bash
docker compose --env-file backend/.env down -v
docker compose --env-file backend/.env up --build
```

The local database defaults are:
- JDBC URL inside Docker: `jdbc:postgresql://postgres:5432/imdb_clone_app`
- JDBC URL from your host machine: `jdbc:postgresql://localhost:2345/imdb_clone_app`
- Database: `imdb_clone_app`
- User: `imdb`
- Password: `imdb`

You can override them in `backend/.env` with `POSTGRES_DB`, `POSTGRES_USER`,
`POSTGRES_PASSWORD`, and `POSTGRES_PORT`.

### Optional TMDB Import

The backend imports a larger movie and TV-series catalogue from TMDB on startup when
`TMDB_ENABLED=true`. For this student project it defaults to enabled, but the
import is skipped if no token is configured. Put credentials in `backend/.env`;
do not commit real TMDB tokens or API keys.

```bash
cp backend/.env.example backend/.env
# Edit .env and set TMDB_API_TOKEN to your v4 Read Access Token.
docker compose --env-file backend/.env up --build
```

The Docker setup stores imported data, users, ratings, reviews, and watchlists
in the persistent Postgres volume.

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

### Manual Backend Setup
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

The backend server will start on `http://localhost:8080`

### Manual Frontend Setup
```bash
cd frontend
npm install
npm run dev
```

The React app will start on `http://localhost:3000`

## Deployment

Deploy the app as three pieces: hosted Postgres, the Spring Boot backend, and
the Vite frontend.

### Backend Environment

Deploy the `backend/` directory. The included `backend/Dockerfile` can be used
by platforms that support Docker web services.

Set these environment variables on the backend host:

```env
SPRING_PROFILES_ACTIVE=jwt,postgres
PORT=8080
POSTGRES_JDBC_URL=jdbc:postgresql://your-db-host:5432/your-db-name
POSTGRES_USER=your-db-user
POSTGRES_PASSWORD=your-db-password
JWT_SECRET=use_a_long_random_secret
APP_CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
TMDB_API_TOKEN=your_new_v4_read_access_token
TMDB_ENABLED=true
```

After the first successful TMDB import, set `TMDB_ENABLED=false` if you do not
want the deployed backend to call TMDB on every restart.

### Frontend Environment

Deploy the `frontend/` directory. Use:

```bash
npm install
npm run build
```

Set this frontend environment variable to the deployed backend URL:

```env
VITE_API_BASE_URL=https://your-backend-domain.com
```

The backend CORS value must match your deployed frontend origin exactly. For
example, if the frontend is deployed at `https://imdb-clone.vercel.app`, set:

```env
APP_CORS_ALLOWED_ORIGINS=https://imdb-clone.vercel.app
```

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
