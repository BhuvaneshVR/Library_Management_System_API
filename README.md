# Library Management System - Backend API

## Quick Start

### Prerequisites
- Java 17 JDK
- Maven 3.8+
- MySQL 8.0+

### Setup

1. **Create Database**
   ```bash
   mysql -u root -p
   CREATE DATABASE library_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   EXIT;
   ```

2. **Configure Database Connection**
   - Copy `.env.example` to `.env` (optional, uses defaults)
   - Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/library_management
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. **Build Project**
   ```bash
   mvn clean install
   ```

4. **Run Application**
   ```bash
   mvn spring-boot:run
   ```

   Backend runs on: **http://localhost:8080**

### Access Swagger Documentation
```
http://localhost:8080/swagger-ui/index.html
```

### Run with Docker MySQL

If you have Docker installed, run MySQL in a container:

1. **Start MySQL Container**
   ```bash
   cd ../
   docker-compose up -d
   ```

2. **Run Backend**
   ```bash
   cd Library_Management_System_API
   mvn spring-boot:run
   ```

3. **Stop Container**
   ```bash
   docker-compose down
   ```

## Project Structure

```
src/main/java/com/company/library/
├── config/              - Spring configuration
├── controller/          - REST API endpoints
├── dto/                 - Request/Response DTOs
│   ├── request/
│   └── response/
├── entity/              - JPA entities
├── enums/               - Java enums
├── exception/           - Custom exceptions & global handler
├── mapper/              - MapStruct mappers
├── repository/          - Data access layer
├── service/             - Business logic
│   └── impl/
├── specification/       - JPA specifications
├── util/                - Utility classes
└── LibraryManagementApplication.java
```

## Key Features

- **Layered Architecture**: Controllers → Services → Repositories
- **DTO Pattern**: Clean API contracts
- **Global Exception Handling**: Centralized error management
- **Pagination**: Efficient list retrieval
- **Search API**: Advanced book search by title, author, ISBN
- **Stock Management**: Automatic inventory updates
- **Validation**: Bean validation on all inputs
- **Swagger/OpenAPI**: Auto-generated API documentation
- **Flyway Migrations**: Version-controlled database schema

## API Endpoints

### Books
- `POST /api/v1/books` - Create book
- `GET /api/v1/books` - Get all books (paginated)
- `GET /api/v1/books/{id}` - Get book by ID
- `GET /api/v1/books/search?keyword=...` - Search books
- `PUT /api/v1/books/{id}` - Update book
- `DELETE /api/v1/books/{id}` - Delete book (soft delete)

### Members
- `POST /api/v1/members` - Create member
- `GET /api/v1/members` - Get all members (paginated)
- `GET /api/v1/members/{id}` - Get member by ID
- `PUT /api/v1/members/{id}` - Update member
- `PATCH /api/v1/members/{id}/deactivate` - Deactivate member

### Transactions
- `POST /api/v1/transactions/issue` - Issue book
- `POST /api/v1/transactions/{id}/return` - Return book
- `GET /api/v1/transactions/members/{memberId}` - Member transactions
- `GET /api/v1/transactions/books/{bookId}` - Book transactions

## Business Rules

### Book Issue
✓ Available copies > 0
✓ Member must be ACTIVE
✓ No duplicate ISSUED transaction for same book+member

### Stock Management
- Issue: availableCopies -= 1
- Return: availableCopies += 1
- availableCopies ≤ totalCopies

## Database

Flyway automatically runs migrations from `src/main/resources/db/migration/`:
- V1__create_books_table.sql
- V2__create_members_table.sql
- V3__create_transactions_table.sql

## Testing

Sample API calls:

```bash
# Create Book
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "0132350882",
    "category": "Programming",
    "totalCopies": 5,
    "availableCopies": 5,
    "shelfLocation": "A-101"
  }'

# Create Member
curl -X POST http://localhost:8080/api/v1/members \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }'

# Issue Book
curl -X POST http://localhost:8080/api/v1/transactions/issue \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": 1,
    "memberId": 1,
    "dueDate": "2026-06-22T23:59:59"
  }'
```

## Troubleshooting

### Database Connection Error
- Verify MySQL is running: `mysql -u root -p`
- Check connection string in `application.properties`
- Ensure database exists: `SHOW DATABASES;`

### Port Already in Use
- Change `server.port=8080` in `application.properties`
- Or kill the process: `lsof -i :8080` and `kill -9 <PID>`

### Flyway Migration Error
- Check `db/migration/` folder for .sql files
- Ensure database is empty on first run
- Check MySQL error logs

## Development

### IDE Setup (IntelliJ IDEA)
1. Open project as Maven project
2. Enable Annotation Processing: Settings → Compiler → Annotation Processors → Enable
3. Install Lombok plugin: Settings → Plugins → Search "Lombok" → Install

### IDE Setup (VS Code)
1. Install Extension Pack for Java
2. Install Lombok Annotations Support for VS Code

## Performance Tips

- Use pagination for large datasets
- Indexes on frequently queried columns (ISBN, email, status)
- Lazy loading for relationships
- Enable query optimization in logs

## Security (Production)

- Add JWT Authentication
- Role-Based Access Control
- HTTPS enforcement
- Rate limiting
- Input validation & sanitization

## Deployment

### Docker Build
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
COPY target/library-management-api-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build and Run
```bash
mvn clean package
docker build -t library-management-api .
docker run -p 8080:8080 library-management-api
```

---

**Version**: 1.0.0
**Last Updated**: May 22, 2026
