# Artifacts - MMO Simulator

## Project Overview
Artifacts is a backend application for an "MMO Simulator," built using **Spring Boot 3.5.11** and **Java 25**. The project is designed to leverage **Spring AI** for integration with Anthropic and Google GenAI models, potentially for simulation logic or NPC behavior. It uses a robust stack including **PostgreSQL** for relational data, **Redis** for caching/messaging, and **WebSockets** for real-time communication.

### Main Technologies
- **Framework:** Spring Boot 3.5.11 (Java 25)
- **AI Integration:** Spring AI (Anthropic, Google GenAI)
- **Database:** PostgreSQL (JPA, JDBC, Flyway)
- **Caching/Messaging:** Redis (Spring Data Redis)
- **Real-time:** Spring WebSocket
- **Cloud:** Google Cloud (Spring Cloud GCP)
- **Documentation:** Springdoc OpenAPI (Swagger UI)
- **Monitoring:** Spring Boot Actuator, Micrometer (Prometheus)
- **Utilities:** Lombok

## Building and Running

### Prerequisites
- **Java 25 SDK**
- **Maven** (optional, use the provided wrapper `mvnw`)
- **Docker** (recommended for running PostgreSQL and Redis locally)

### Key Commands
- **Build the project:**
  ```powershell
  ./mvnw clean install
  ```
- **Run the application:**
  ```powershell
  GOOGLE_APPLICATION_CREDENTIALS="C:\Users\hokep\Downloads\cakenet-cloud-services-9c207cc4ebd8.json" ./mvnw spring-boot:run
  ```
- **Run tests:**
  ```powershell
  ./mvnw test
  ```

### API Documentation
Once running, the OpenAPI documentation should be available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## Development Conventions

### Project Structure
- Standard Maven structure:
  - `src/main/java`: Source code
  - `src/main/resources`: Configuration and static assets
  - `src/test/java`: Unit and integration tests
- **Database Migrations:** SQL scripts should be placed in `src/main/resources/db/migration` for Flyway.

### Coding Style
- Use **Lombok** for boilerplate reduction (e.g., `@Data`, `@Getter`, `@Setter`, `@Builder`, `@Slf4j`).
- Follow **2-space indentation** for all files.
- Follow standard Spring Boot architectural patterns (Controller -> Service -> Repository).
- Ensure new entities are mapped with JPA and have corresponding Flyway migration scripts.

### Testing
- Place unit tests in `src/test/java`.
- Use `@SpringBootTest` for integration tests.
- Leverage Spring Boot's testing support for JPA, Redis, and Web.

### JSON Deserialization

Uses Jackson ObjectMapper with:
- `@JsonIgnoreProperties(ignoreUnknown = true)` on all models
- `spring.jackson.deserialization.fail-on-unknown-properties=false`
- TypeReference for generic type handling
- Java 21 features (`.toList()` for immutable collections)

## GitHub Workflow

### Branch Naming Convention
**Always** create new branches with the issue number at the beginning:
```bash
# Good
git checkout -b 3-remove-list-object
git checkout -b 42-add-user-stats

# Bad
git checkout -b remove-list-object
git checkout -b feature/add-user-stats
```

### Commit Message Format
```
Short description (50 chars or less)

Detailed explanation of changes including:
- What was changed
- Why it was changed
- Any breaking changes

Fixes #issue-number

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Pull Request Process
1. Create feature branch with issue number prefix
2. Move the issue to "In Progress" in the GoopStats project
3. Make changes and commit with descriptive messages
4. Push branch to origin
5. Create PR with comprehensive description
6. Link PR to issue with "Fixes #N"
7. Move the issue to "In Review" in the GoopStats project

## Code Style Guidelines

- **Indentation**: 2 spaces for ALL files (Java, HTML, CSS, etc.) - not 4 spaces, not tabs
- **Annotations**: Use Lombok where appropriate (`@Data`, `@RequiredArgsConstructor`, `@Slf4j`)
- **Jackson**: Always use `@JsonProperty` for field mapping, `@JsonIgnoreProperties(ignoreUnknown = true)` for flexibility
- **Logging**: Use `@Slf4j` and log at appropriate levels (debug, info, error)
- **Null Safety**: Use Java streams with filters for null checks where appropriate
- **Immutability**: Prefer `.toList()` over `.collect(Collectors.toList())` for immutable results
- **HTML/Thymeleaf**: Follow 2-space indentation in templates, use Thymeleaf attributes (`th:text`, `th:if`, etc.)

## Files to Ignore

The following files should NOT be committed to version control:
- `.claude/settings.local.json` (user-specific Claude Code settings)
- `target/` (build artifacts)
- IDE-specific files (`.idea/`, `.vscode/`, etc.)

All ignored files are specified in `.gitignore`.
