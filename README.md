# 📚 SwapShelf

**SwapShelf** is a book exchange management platform built using **Spring Boot**.  
It enables users to list, exchange, and manage books easily with secure authentication and an intuitive workflow for both users and admins.

---

## 🚀 Features

- User registration and authentication (secured with Spring Security)
- Book listing, search, and management
- Book exchange request and approval flow
- Admin dashboard for managing users and books
- DTO-layered architecture for clean data transfer
- Database-backed persistence with JPA/Hibernate
- MVC structure with modular controllers and services

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-------------|
| Backend | Java, Spring Boot |
| Security | Spring Security |
| Database | JPA / Hibernate |
| Build Tool | Maven |
| IDE Support | IntelliJ IDEA / Eclipse |
| Version Control | Git & GitHub |

---

## 🧩 Project Structure

```
swapshelf/
 ├── src/
 │   ├── main/
 │   │   ├── java/com/swapshelf/
 │   │   │   ├── config/          # App configuration & security
 │   │   │   ├── controller/      # MVC controllers
 │   │   │   ├── dto/             # Data Transfer Objects
 │   │   │   ├── entity/          # Entity models
 │   │   │   ├── repository/      # DAO layer (JPA Repositories)
 │   │   │   └── service/         # Business logic
 │   │   └── resources/           # Application properties, static, templates
 │   └── test/                    # Unit and integration tests
 ├── pom.xml                      # Maven dependencies and build config
 ├── mvnw, mvnw.cmd               # Maven wrapper scripts
 ├── .gitignore                   # Git ignore rules
 └── HELP.md                      # Spring Boot generated help file
```

---

## ⚙️ Setup & Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/swapshelf.git
   cd swapshelf
   ```

2. **Build the project**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. The app will start at:  
   👉 [http://localhost:8080](http://localhost:8080)

---

## 🧑‍💻 Default Admin (if configured)
If the project includes an admin initializer:
```
Username: admin
Password: admin123
```

*(You can change this in `AdminUserInitializer.java`)*

---

## 📂 Environment Configuration

Edit the following in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/swapshelf
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🧠 Future Enhancements

- REST API support for mobile apps
- Email notifications for book exchanges
- Image upload for book covers
- Role-based access expansion

---

## 👨‍💻 Author

**Yogesh Patil**  
📧 [Add your email or LinkedIn if you wish]  

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
