# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Full-stack car management application with:
- **Backend**: Spring Boot 3.2.5 REST API with Spring Data JPA, Spring Security (HTTP Basic), H2 in-memory database
- **Frontend**: React 17 SPA with Bootstrap 4, React Router 5, Axios for API calls

The application manages cars (Voiture) and owners (Proprietaire) with CRUD operations.

## Development Commands

### Backend (Spring Boot)

```bash
cd backend

# Run application (default port 8080)
./mvnw spring-boot:run
# Windows: mvnw.cmd spring-boot:run

# Run tests
./mvnw test

# Clean and build
./mvnw clean package

# Run specific test
./mvnw test -Dtest=VoitureRepoTest
```

### Frontend (React)

```bash
cd frontend

# Initial setup (copy environment config)
cp .env.example .env

# Install dependencies
npm install

# Start development server (port 3000)
npm start

# Run tests
npm test

# Production build
npm build
```

## Architecture

### Backend Structure

**Package organization** (`org.cours`):
- `model/`: JPA entities (Voiture, Proprietaire) with bidirectional @ManyToOne/@OneToMany relationship
- `repo/`: Spring Data JPA repositories with custom query methods
- `web/`: REST controllers exposing `/voitures` and `/proprietaires` endpoints
- `config/`: SecurityConfig for HTTP Basic authentication

**Key architectural patterns**:
- REST controllers use `@CrossOrigin(origins = "*")` for CORS
- Repositories annotated with `@RepositoryRestResource` expose Spring Data REST endpoints at `/api/*`
- Controllers provide custom endpoints at `/voitures` and `/proprietaires` (not under `/api`)
- `VoitureController.VoitureRequest` record for request DTOs with validation
- `@Transactional` on update operations to enable dirty checking

**Database initialization**:
- `CommandLineRunner` bean in main application class seeds database on startup
- Creates 3 owners and 6 cars with realistic test data

**Security**:
- HTTP Basic authentication required for all endpoints except H2 console and Swagger UI
- Default credentials: user/password (configured in application.properties)
- CSRF disabled for H2 console
- Frame options set to SAMEORIGIN for H2 console access

### Frontend Structure

**Component hierarchy**:
- `App.js`: Router configuration with routes: `/` (Bienvenue), `/add` (Voiture form), `/list` (VoitureListe)
- `NavigationBar.js`: Bootstrap navbar with navigation links
- `Voiture.js`: Form component for creating/editing cars
- `VoitureListe.js`: Table component displaying all cars with delete functionality
- `Bienvenue.js`: Welcome/home page
- `Footer.js`: Application footer

**API integration**:
- `api.js`: Axios instance configured with base URL and Basic Auth interceptor
- Credentials read from environment variables (REACT_APP_API_BASE_URL, REACT_APP_API_USERNAME, REACT_APP_API_PASSWORD)
- Authorization header automatically added to all requests

**Routing**:
- Uses React Router 5 (`react-router-dom`) with BrowserRouter
- Route components: `<Route path="/add" exact component={Voiture} />`

## Configuration

### Backend Configuration (application.properties)

- H2 console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:voituresdb`)
- Swagger UI: http://localhost:8080/swagger-ui.html
- JPA: `spring.jpa.hibernate.ddl-auto=create-drop` (recreates schema on each run)
- Spring Data REST base path: `/api`

To switch to MariaDB, uncomment the MariaDB configuration section in application.properties.

### Frontend Configuration (.env)

Create from `.env.example`:
- `REACT_APP_API_BASE_URL`: Backend URL (default: http://localhost:8080)
- `REACT_APP_API_USERNAME`: Auth username (default: user)
- `REACT_APP_API_PASSWORD`: Auth password (default: password)

## Data Model

**Voiture** (Car):
- Fields: id, marque (brand), modele (model), couleur (color), matricule (license plate - unique), immatricule (registration), prix (price)
- Validation: @NotBlank on string fields, @Min(0) on prix
- Relationship: @ManyToOne to Proprietaire (LAZY fetch)

**Proprietaire** (Owner):
- Fields: id, nom (last name), prenom (first name)
- Relationship: @OneToMany to Voiture (cascade ALL, orphanRemoval)
- Bidirectional helper methods: addVoiture(), removeVoiture()

## Common Workflows

### Adding a new REST endpoint

1. Add method to repository (VoitureRepo/ProprietaireRepo) if custom query needed
2. Add controller method in web package with appropriate annotations (@GetMapping, @PostMapping, etc.)
3. Use `@Valid` with DTOs/entities for automatic validation
4. Return appropriate HTTP status codes (@ResponseStatus or ResponseEntity)

### Testing repository methods

- Extend existing `VoitureRepoTest` or create similar test class
- Use `@DataJpaTest` for repository layer testing
- Test data is isolated per test method

### Frontend API calls

- Import `apiClient` from `api.js`
- Use async/await with try/catch for error handling
- Authentication is handled automatically via interceptor
