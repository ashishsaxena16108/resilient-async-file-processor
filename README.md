# Resilient Async File Processor

A high-throughput, fault-tolerant batch processing engine built with Spring Boot. This application simulates heavy asynchronous file reading coupled with database persistence under severe connection pool starvation, demonstrating how to isolate transactional boundaries and recover gracefully using exponential backoff retry mechanisms.

---

## Key Features

* **Asynchronous Multi-Threading:** Leverages a dedicated thread pool (`@Async`) to execute heavy file I/O operations concurrently.
* **Connection Bottleneck Simulation:** Simulates enterprise database stress by intentionally constraining the HikariCP pool (`maximum-pool-size: 2`).
* **Resilient Retry Mechanism:** Decouples file I/O from database transactions and wraps persistence logic in `@Retryable` with an exponential backoff matrix (`1s`, `2s`, `4s`).
* **Real-Time Health Monitoring:** Implements a custom `RetryListener` (`LogRetryListener`) to intercept failures, log retry counts, and track task recovery without throwing unhandled exceptions.

---

## Architecture Overview

```text
[ Concurrent File Reader Task Queue ]
                │
                ▼
   [ FileReaderService (@Async) ]  ◄── Non-transactional File I/O
                │
                ▼
    [ LogWriterService (@Retryable + @Transactional) ]
                │
        ┌───────┴───────┐
        ▼               ▼
 (Hikari Pool OK)   (Pool Starvation)
        │               │
  [ Save to DB ]   [ Backoff (1s -> 2s -> 4s) ]
                        │
                  [ Auto Retry ] ──► [ LogRetryListener Output ]
```

### The Resource Starvation Problem
When dozens of background threads attempt to persist records simultaneously while holding open database transactions, connection pools exhaust rapidly, leading to `JDBCConnectionException: Connection is not available, request timed out after 3000ms`.

### The Solution
By extracting the database persistence logic into a dedicated, standalone bean (`LogWriterService`), Spring's AOP proxy intercepts connection failures and retries the save operation gracefully as connections free up.

---

## Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Modules:** Spring Async, Spring Retry, Spring Starter AOP, Spring Data JPA
* **Database:** MySQL / H2
* **Build Tool:** Gradle / Maven

---

## Configuration (`application.yml`)

```yaml
spring:
  application:
    name: filereader
  datasource:
    url: jdbc:mysql://localhost:3306/filereader?useSSL=false&serverTimezone=UTC
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:your_password}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 2
      connection-timeout: 3000
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

---

## Project Setup & Execution

### 1. Prerequisites
* Java 17 or higher installed.
* Local MySQL instance running with a target database:
  ```sql
  CREATE DATABASE IF NOT EXISTS filereader;
  ```

### 2. Build & Run

```bash
# Clone the repository
git clone [https://github.com/your-username/resilient-async-file-processor.git](https://github.com/your-username/resilient-async-file-processor.git)
cd resilient-async-file-processor

# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

---

## Sample Execution Log

During high concurrency, tasks will log recovery steps via the custom `RetryListener`:

```text
⏳ FileAsyncThread-14 is attempting DB save for Task #104
⚠️ [RETRY WARNING] saveLogToDb failed on attempt #1. Backing off... Reason: JDBCConnectionException
⏳ FileAsyncThread-14 is attempting DB save for Task #104
✅ [RETRY SUCCESS] A task successfully recovered after 1 failed attempts!
>>> FileAsyncThread-14 successfully PROCESSED Task #104
```

---

## License
MIT License. Free for educational and commercial adaptation.
