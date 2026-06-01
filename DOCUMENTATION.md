# IMDb Clone — Project Documentation

A full-stack movie & TV database application inspired by IMDb. The backend is a
Spring Boot REST API backed by PostgreSQL; the frontend is a React (Vite) single
page application. Users can browse movies and TV series, view details and cast,
search across titles and people, rate and review titles, keep a personal
watchlist, and receive personalized recommendations.

> **Course:** Тимски проект — ФИНКИ, Скопје
> **Stack:** Spring Boot (Java 17) · React 19 (Vite) · PostgreSQL · JWT auth

---

## 1. Table of contents

1. Tech stack
2. High-level architecture
3. Backend layering and packages
4. Domain model
5. Authentication & authorization
6. REST API reference
7. Feature subsystems (ratings & averages, search, recommendations, watchlist, reviews, preferences)
8. Seed data & demo accounts
9. Running the project locally
10. Configuration & profiles
11. Testing
12. Deployment notes
13. Changelog (this iteration)

---

## 2. Tech stack

| Layer        | Technology                                                        |
|--------------|-------------------------------------------------------------------|
| Backend      | Spring Boot, Spring Web, Spring Data JPA, Spring Security          |
| Auth         | JSON Web Tokens (stateless), BCrypt password hashing              |
| Database     | PostgreSQL (Docker Compose for local dev); H2 console config exists |
| Build        | Maven (`mvnw` wrapper), Java 17                                    |
| API docs     | springdoc / Swagger UI (`/swagger-ui`)                            |
| Frontend     | React 19, React Router 7, Vite 7                                  |
| Styling      | Tailwind CSS, CSS variables (light/dark theme), framer-motion     |
| HTTP client  | axios (with a JWT request interceptor)                            |
| Icons        | lucide-react                                                      |

---

## 3. High-level architecture

```
┌────────────────────────┐         HTTP / JSON          ┌──────────────────────────────┐
│   React SPA (port 3000) │ ───────────────────────────▶ │  Spring Boot API (port 8080)  │
│                         │   Authorization: Bearer JWT  │                               │
│  pages / components     │ ◀─────────────────────────── │  Controllers → App services   │
│  context (Auth, Watch.) │                              │  → Domain services → JPA repos │
│  lib/*Service.js (axios)│                              │                               │
└────────────────────────┘                              └───────────────┬───────────────┘
                                                                         │ JDBC
                                                                ┌────────▼────────┐
                                                                │   PostgreSQL    │
                                                                └─────────────────┘
```

The frontend never talks to the database directly — all data flows through the
REST API. Authentication is stateless: after login the client stores a JWT in
`localStorage` and attaches it to every request via an axios interceptor.

---

## 4. Backend layering and packages

The backend follows the FINKI two-tier service pattern (a **domain** layer that
works with entities, and an **application** layer that works with DTOs). This
keeps controllers thin and keeps entity objects from leaking out of the API.

```
mk.ukim.finki.imdbclone
├── model
│   ├── domain          // JPA entities (Media, Movie, TVSeries, Rating, ...)
│   ├── dto             // Create* and Display* records used by the API
│   ├── enumerations    // Role, PreferenceType
│   └── exceptions      // domain-specific exceptions
├── repository          // Spring Data JPA repositories
├── service
│   ├── domain          // business logic over entities (+ impl, + helper)
│   └── application     // DTO-facing orchestration (+ impl)
├── web
│   ├── controllers     // REST controllers
│   ├── filters         // JwtFilter
│   └── helpers         // ControllerAuthorizationHelper
├── config              // SecurityConfig, JwtSecurityWebConfig, DataInitializer
├── security            // CustomUsernamePasswordAuthenticationProvider
├── helpers / util      // JwtHelper, search utilities
└── constants           // JwtConstants
```

**Request flow:** `Controller` → `*ApplicationService` (maps DTO ⇄ entity) →
`*Service` (domain logic) → `*Repository` (persistence).

---

## 5. Domain model

All watchable content extends an abstract `Media` superclass using JPA
`JOINED` inheritance, so `Movie` and `TVSeries` share a common table for ratings,
reviews, genres and cast while keeping their own type-specific columns.

### Entities

- **Media (abstract)** — `id`, `title`, `description`, `releaseYear`, `posterUrl`,
  `averageRating`, plus relationships to `genres`, `ratings`, `reviews`, and
  `castAndCrew`. Auditing fields `createdAt` / `updatedAt`.
  - **Movie** — adds `duration` (minutes).
  - **TVSeries** — adds `numberOfSeasons` and `status` (e.g. `Ended`, `Returning Series`).
- **Genre** — `id`, `name`. Many-to-many with `Media`.
- **Person** — `id`, `firstName`, `lastName`, `biography`, `birthDate`, `profilePictureUrl`.
- **MediaPerson** — join entity linking a `Person` to a `Media` with a `Role`
  (`MAIN_ACTOR`, `ACTOR`, `DIRECTOR`, `WRITER`, `PRODUCER`, `CREW`, `CREATOR`) and an
  optional `characterName`. Unique on `(media, person, role)`.
- **User** — `id`, `username`, `password` (BCrypt), `firstName`, `lastName`, `email`,
  plus `ratings`, `reviews`, a `watchlist` (many-to-many with `Media`), and `preferences`.
- **Rating** — a user's score (1–10) for a media item. Unique on `(user, media)`,
  so a user has at most one rating per title (re-rating updates it).
- **Review** — a user's free-text review for a media item.
- **UserPreference** — a learned weight for a `PreferenceType` (`GENRE`, `DIRECTOR`,
  `ACTOR`) and value (e.g. genre name), built up from a user's ratings and watchlist.

### Relationships (simplified)

```
User 1───* Rating *───1 Media        Media *───* Genre
User 1───* Review *───1 Media        Media 1───* MediaPerson *───1 Person
User *───* Media (watchlist)         Media ◀── Movie | TVSeries  (JOINED inheritance)
User 1───* UserPreference
```

---

## 6. Authentication & authorization

- **Login** (`POST /api/user/login`) verifies credentials via
  `CustomUsernamePasswordAuthenticationProvider` and returns a `LoginResponseDto`
  containing a signed **JWT** and a `DisplayUserDto` (`id`, `username`, `firstName`,
  `lastName`).
- The client stores the token and sends `Authorization: Bearer <token>` on every
  request. `JwtFilter` validates the token and populates the security context.
- Sessions are **stateless** (`SessionCreationPolicy.STATELESS`).
- `ControllerAuthorizationHelper` enforces that a user can only act on their own
  resources (watchlist, ratings, reviews, recommendations) by comparing the
  authenticated principal against the `username` / `userId` in the request.

### Public vs. protected endpoints

Active security is defined in `JwtSecurityWebConfig` (profile `jwt`). The
following are **public** (no token required) so the catalogue is browsable by
anonymous visitors:

```
/api/user/register, /api/user/login
/api/movies, /api/movies/recent, /api/movies/top-rated,
  /api/movies/{id}, /api/movies/{id}/similar,
  /api/movies/genre/**, /api/movies/year/**, /api/movies/year-range, /api/movies/director
/api/tv-series, /api/tv-series/recent, /api/tv-series/top-rated,
  /api/tv-series/{id}, /api/tv-series/{id}/similar, /api/tv-series/status
/api/search
/api/ratings/media/**, /api/reviews/media/**
/swagger-ui/**, /v3/api-docs/**
```

Everything else requires a valid JWT — including writing ratings/reviews,
watchlist operations, personalized recommendations, and all create/edit/delete
endpoints. `/api/genres` and `/api/persons` collection reads currently require
authentication.

> A second `SecurityConfig` exists under profile `!jwt` that denies all requests;
> it is **not** active in the default configuration because the `jwt` profile is
> enabled.

---

## 7. REST API reference

Base URL (local): `http://localhost:8080`. All payloads are JSON. 🔒 = requires JWT.

### Users & auth — `/api/user`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/register` | Create a new account (`CreateUserDto`) |
| POST | `/login` | Authenticate, returns `{ token, user }` |
| GET  | `/logout` | Invalidate session |
| 🔒 POST | `/{username}/watchlist/{mediaId}` | Add a title to the watchlist |
| 🔒 DELETE | `/{username}/watchlist/{mediaId}` | Remove a title from the watchlist |
| 🔒 GET | `/{username}/watchlist` | List the user's watchlist (cards) |
| 🔒 GET | `/{username}/watchlist/{mediaId}` | Is a title in the watchlist? (boolean) |
| 🔒 GET | `/{userId}/recommendations` | Personalized recommendations (cards) |

### Movies — `/api/movies`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | All movies |
| GET | `/{id}` | Movie detail (`DisplayMovieDto` incl. genres + cast) |
| GET | `/top-rated` | Highest-rated movies |
| GET | `/recent` | Most recently added movies |
| GET | `/director?director=` | Movies by a director's name |
| GET | `/year/{year}` | Movies from a year |
| GET | `/year-range?startYear=&endYear=` | Movies in a year range |
| GET | `/genre/{genreName}` | Movies in a genre |
| GET | `/{id}/similar` | Similar movies (cards) |
| 🔒 POST | `/add` | Create movie (`CreateMovieDto`) |
| 🔒 PUT | `/edit/{id}` | Update movie |
| 🔒 DELETE | `/delete/{id}` | Delete movie |

### TV series — `/api/tv-series`
Mirrors movies, with `GET /status?status=` instead of the director/year filters,
plus `GET /`, `/{id}`, `/top-rated`, `/recent`, `/{id}/similar`, and 🔒
`/add`, `/edit/{id}`, `/delete/{id}`.

### Search — `/api/search`
| Method | Path | Description |
|--------|------|-------------|
| GET | `?query=` | Unified search over titles, people and release year |

Returns `SearchResultDto { results: SearchItemDto[], interpretedAs }`, where each
`SearchItemDto` is `{ id, title, imageUrl, type, score }`. `type` is `"Movie"`,
`"TVSeries"`, or `"Person"`.

### Ratings — `/api/ratings`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/media/{mediaId}` | All ratings for a title |
| GET | `/media/{mediaId}/average` | Average rating (raw, from the DB) |
| GET | `/media/{mediaId}/count` | Number of ratings |
| 🔒 GET | `/by-user-media?userId=&mediaId=` | A user's rating for a title |
| 🔒 GET | `/user/{userId}` | All ratings by a user |
| 🔒 POST | `/add` | Create/update a rating (`CreateRatingDto`) |
| 🔒 DELETE | `/delete?userId=&mediaId=` | Remove a rating |

### Reviews — `/api/reviews`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/media/{mediaId}` | Reviews for a title |
| GET | `/{reviewId}` | A single review |
| 🔒 GET | `/by-user-media?userId=&mediaId=` | A user's review for a title |
| 🔒 GET | `/user/{userId}` | All reviews by a user |
| 🔒 POST | `/add` | Create a review (`CreateReviewDto`) |
| 🔒 PUT | `/edit/{reviewId}` | Update a review |
| 🔒 DELETE | `/delete/{reviewId}` | Delete a review |

### Genres — `/api/genres` (🔒 reads)
`GET /`, `GET /{id}`, `GET /by-name?name=`, `GET /exists?name=`, 🔒 `POST /add`, 🔒 `DELETE /delete/{id}`.

### People — `/api/persons` (🔒 reads)
`GET /`, `GET /{id}`, `GET /search?name=`, 🔒 `POST /add`, 🔒 `PUT /edit/{id}`, 🔒 `DELETE /delete/{id}`.

---

## 8. Feature subsystems

### 8.1 Ratings and the average-rating calculation

Ratings are an **upsert**: `RatingServiceImpl.rateMedia(userId, mediaId, value)`
looks up an existing `(user, media)` rating and updates it, or creates a new one
(the `(user, media)` pair is unique). Every time a rating is added, changed, or
deleted, the title's stored average is recomputed:

```java
private void syncAverageRating(Media media) {
    Double avg = ratingRepository.findAverageRatingByMediaId(media.getId());
    media.setAverageRating(roundToOneDecimal(avg)); // null when no ratings remain
    mediaRepository.save(media);
}
```

The average itself is a single SQL aggregate (`AVG(r.rating)`), so it is always
consistent with the underlying rows rather than being incrementally maintained.
The result is rounded to one decimal place (`8.6666… → 8.7`) for clean display,
and reset to `null` when the last rating is removed. Submitting a rating also
updates the user's learned preferences (see 8.4).

### 8.2 Search

`SearchServiceImpl` normalizes the query and scores three sources, merging the
results (highest score wins per item, capped at 20 results):

- **Media titles** — exact/contains scoring plus a fuzzy (edit-distance ≤ 2) match,
  so small typos still match.
- **People** — matches on full name; when a person matches, the titles they worked
  on are also surfaced (a lower score), so searching a director returns their films.
- **Year** — a 4-digit query is treated as a release year and returns titles from
  that year.

The response includes an `interpretedAs` field listing which sources contributed
(`"media"`, `"person"`, `"year"`), which the UI can use to explain results.

### 8.3 Recommendations

`RecommendationServiceImpl.getRecommendationsForUser` combines two strategies and
merges them with rank-weighted scoring, then falls back to popular titles if it
has nothing to go on:

- **Content-based** — starts from the user's highly-rated titles (rating ≥ 8) and
  watchlist, then scores every other title by similarity (shared genres/cast via
  `MediaSimilarityHelper`) and by the user's learned `UserPreference`s
  (`PreferenceMatchingHelper`).
- **Collaborative** — finds other users who highly rated the same titles, then
  recommends what those similar users also rated highly.

Titles the user has already rated or watchlisted are excluded. The endpoint is
per-user and requires authentication.

### 8.4 User preferences

When a user rates or watchlists a title, `UserPreferenceServiceImpl` adjusts
weighted preferences for that title's genres, director, and main actors. Ratings
are weighted: ≥ 8 strongly positive, 6–7 mildly positive, ≤ 3 negative. These
weights feed the content-based recommender so suggestions improve as a user
interacts with more titles.

### 8.5 Watchlist

A many-to-many relation between `User` and `Media`. Endpoints let an authenticated
user add, remove, list, and check membership. Watchlisting also contributes to
preferences and to the recommendation base set.

---

## 9. Seed data & demo accounts

On the **first** startup against an empty database, `DataInitializer` (profile
`dev`) seeds a realistic dataset. It is idempotent — each entity type is only
seeded if its table is empty.

**What gets seeded:**

- **5 users** (all password `password123`)
- **9 genres** — Action, Sci-Fi, Drama, Comedy, Crime, Thriller, Romance, Adventure, Biography
- **20 movies** — incl. Inception, The Godfather, Interstellar, The Matrix,
  Goodfellas, The Dark Knight, Dune, Forrest Gump, and two Macedonian classics
  (*Пред дождот*, *Црно семе*)
- **7 TV series** — Breaking Bad, Severance, The Bear, Succession, The Last of Us,
  Stranger Things, The Office
- **20 people** — directors and main actors (Nolan, Coppola, Scorsese, Villeneuve,
  DiCaprio, Pacino, Brando, Cranston, etc.)
- **30 cast/crew links** — directors and named main-actor roles across the catalogue
- **37 ratings** spread across all 5 users (designed so averages, top-rated lists,
  search, and both recommendation strategies all produce visible results)
- **12 reviews** across multiple titles and users

Title averages are computed from the seeded ratings (same one-decimal rounding as
the live calculation), so the displayed averages always match the data. A couple
of titles (e.g. *The Hangover*, *Superbad*) are intentionally left unrated to show
the "no ratings yet" state.

**Demo accounts (taste profiles, useful for showing recommendations):**

| Username | Password | Taste profile |
|----------|----------|---------------|
| `john_doe` | `password123` | Nolan / sci-fi + a crime classic |
| `jane_smith` | `password123` | Crime, drama, prestige TV |
| `moviefan99` | `password123` | Mainstream, romance, comedy |
| `alice_w` | `password123` | Science fiction & thrillers |
| `carlos_m` | `password123` | Crime films & prestige TV |

`alice_w` and `john_doe` share sci-fi tastes (good for content-based recs);
`jane_smith` and `carlos_m` overlap heavily on crime/prestige TV (good for
collaborative recs).

To reset to a fresh seeded database:

```bash
docker compose --env-file backend/.env down -v
docker compose --env-file backend/.env up --build
```

---

## 10. Running the project locally

**Prerequisites:** Java 17+, Node.js 16+, Maven (wrapper included), Docker + Docker Compose.

**1) Configure local environment**
```bash
cp backend/.env.example backend/.env
```

Set `JWT_SECRET` and `TMDB_API_TOKEN` in `backend/.env`.

**2) Start the full Docker stack**
```bash
docker compose --env-file backend/.env up --build
```

Local services:
- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- Postgres: `localhost:2345`

Swagger UI is available at `http://localhost:8080/swagger-ui`.

---

## 11. Configuration & profiles

- `application.properties` activates `dev,jwt,postgres` by default.
  - **dev** — runs `DataInitializer` to seed the database.
  - **jwt** — enables stateless JWT security (`JwtSecurityWebConfig`) + CORS for
    `http://localhost:3000`.
  - **postgres** — Postgres connection settings (`application-postgres.properties`).
- Overridable via env vars: `POSTGRES_JDBC_URL`, `POSTGRES_USER`,
  `POSTGRES_PASSWORD`, `POSTGRES_DB`, `POSTGRES_PORT`. Put local overrides in
  `backend/.env` (read automatically by Docker Compose).
- Frontend API base URL is set in `src/lib/api.js` (`http://localhost:8080`); the
  axios interceptor attaches the JWT and clears it on 401/403.

---

## 12. Testing

The backend includes JUnit tests under `src/test`:

- **Repository tests** (`@DataJpaTest`) for Movie, TVSeries, Genre, Person, Review,
  Rating, User repositories.
- **Domain service tests** for the same areas, including
  `RatingServiceTest` which verifies that the average is recalculated correctly on
  add / update / delete.
- **Controller tests** for Rating, Review, and User endpoints.

Run them with:
```bash
cd backend
./mvnw test
```

> Note: the seed (`DataInitializer`) only runs under the `dev` profile and is not
> loaded by `@DataJpaTest`, so the test fixtures are independent of the seed size.

---

## 13. Changelog (this iteration)

Backend and data changes made to round out the project for presentation:

1. **Public browse & search endpoints** — `JwtSecurityWebConfig` now permits
   anonymous `GET` access to `/api/movies`, `/api/tv-series`, their browse/filter
   sub-paths, and `/api/search`, so the catalogue and search work without logging
   in. Write operations and personalized data remain protected.
2. **Average-rating rounding** — `RatingServiceImpl.syncAverageRating` now rounds
   the recomputed average to one decimal place and resets it to `null` when the
   last rating is removed. (The recalculation itself already existed.)
3. **Expanded seed data** — `DataInitializer` grew from 3 users / 16 movies / 5
   series / 8 ratings to **5 users, 9 genres, 20 movies, 7 series, 20 people, 30
   cast links, 37 ratings, and 12 reviews**, with averages computed from the
   actual ratings and demo accounts shaped into distinct taste profiles.

### Suggested next steps (frontend, out of scope here)

- Dedicated **Movies** and **TV Shows** pages (consume `GET /api/movies` and
  `GET /api/tv-series`, filter client-side by genre/year).
- Wire the navbar **search box** to `GET /api/search` and render a results page.
- Show **recommendations** on the home page for logged-in users
  (`GET /api/user/{userId}/recommendations`).
- Deployment (Dockerize backend + frontend, host on a PaaS or VPS).
