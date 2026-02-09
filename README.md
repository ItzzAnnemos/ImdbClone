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
git checkout main
git pull origin main
```

This ensures you have the latest changes from the team.

### Creating a Feature Branch
```bash
# Make sure you're on main and up to date
git checkout main
git pull origin main

# Create your feature branch
git checkout -b feature/your-feature-name
```

### Branch Naming Convention

- Features: `feature/movie-details-page`

### Making Changes
```bash
# Make your changes, then:
git add .
git commit -m "Add movie details page"
git push origin feature/your-feature-name
```

### Creating a Pull Request

1. Go to the repository on GitHub
2. Click **Pull requests** → **New pull request**
3. Select your branch to merge into `main`
4. Add a clear title and description of your changes
5. Request a team member to review
6. Wait for approval before merging

### After Your PR is Merged
```bash
# Switch back to main and update
git checkout main
git pull origin main

# Delete your local feature branch (optional)
git branch -d feature/your-feature-name
```

## Team Guidelines

- **Always pull from main before creating a new branch**
- **Never commit directly to main** - always use a feature branch
- Write clear, descriptive commit messages
- Keep pull requests focused on a single feature or fix
- Review code thoroughly before approving PRs
- Test your changes locally before pushing

## Features

- [ ] Movie search and browse
- [ ] Movie details page
- [ ] User ratings and reviews
- [ ] Watchlist functionality
- [ ] User authentication
- [ ] [Add your features here]
