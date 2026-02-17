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

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
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
