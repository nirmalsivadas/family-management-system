# Family Management System - Microservices Issues & Solutions

**Date Tested:** 2026-08-18  
**Services Tested:** auth-service, family-service, master-service

---

## Executive Summary

All three microservices failed to start due to configuration loading issues from Spring Cloud Config Server. The services are configured to fetch their configuration from `http://localhost:8888`, but the necessary dependencies and bootstrap configuration files are missing.

---

## Issues Found

### ❌ Issue #1: Auth-Service Startup Failure

**Error Message:**
```
org.springframework.beans.factory.UnsatisfiedDependencyException: 
Error creating bean with name 'jwtAuthenticationFilter': 
Injection of autowired dependencies failed
```

**Root Causes:**

1. **Missing Config Server Dependency:** The auth-service pom.xml is missing the `spring-cloud-starter-config` dependency
2. **No Bootstrap Configuration:** Missing `bootstrap.yaml` file prevents proper config server connection on startup
3. **JWT Property Name Mismatch:** 
   - JwtService.java tries to inject: `@Value("${application.security.jwt.secret-key}")`
   - Config file defines: `spring.application.security.jwt.secret-key`
   - The `spring.` prefix is incorrect in the config-repo YAML structure
4. **Missing Environment Variables:** The config references `${JWT_SECRET_KEY}` and `${JWT_EXPIRATION}` which are undefined

**Technical Details:**

File: `auth-service/src/main/java/com/family_management_system/auth_service/security/JwtService.java`
```java
@Value("${application.security.jwt.secret-key}")  // Looking for THIS property
private String secretKey;

@Value("${application.security.jwt.expiration}")  // Looking for THIS property
private String jwtExpiration;
```

File: `config-repo/auth-service.yml`
```yaml
spring:
  application:  // <-- EXTRA "spring." prefix
    security:
      jwt:
        secret-key: ${JWT_SECRET_KEY}  // These env vars not set
        expiration: ${JWT_EXPIRATION}
```

---

### ❌ Issue #2: Family-Service Startup Failure

**Error Message:**
```
org.springframework.beans.factory.BeanCreationException: 
Failed to configure a DataSource: 'url' attribute is not specified 
and no embedded datasource could be configured

Reason: Failed to determine a suitable driver class
```

**Root Causes:**

1. **Missing Config Server Dependency:** No `spring-cloud-starter-config` in pom.xml
2. **No Bootstrap Configuration:** Missing `bootstrap.yaml` file
3. **Database Configuration Not Loaded:** Spring Boot attempts to create datasource before connecting to config server
4. **Incorrect Dependency:** The pom.xml is missing PostgreSQL driver dependency

**Technical Details:**

The application tries to auto-configure a datasource but:
- application.yaml only defines: `spring.application.name: family-service` and config import
- No local datasource URL, username, or password available
- Spring Cloud Config connection hasn't happened yet (no bootstrap config)
- Result: Failed to auto-configure datasource

---

### ❌ Issue #3: Master-Service Startup Failure

**Error Message:** (Same as Issue #2)
```
Failed to configure a DataSource: 'url' attribute is not specified
```

**Root Causes:** Identical to family-service

---

## Solution

### Step 1: Add Missing Spring Cloud Config Dependency

Add this dependency to **all three services** pom.xml (auth-service, family-service, master-service):

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

**Where to add:** After the existing `spring-cloud-starter-netflix-eureka-client` dependency in the `<dependencies>` section

---

### Step 2: Create bootstrap.yaml for Each Service

Create a new file: `{service}/src/main/resources/bootstrap.yaml`

**Content for all three services:**

```yaml
spring:
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: false
      retry:
        initial-interval: 1000
        max-interval: 2000
        max-attempts: 6
  application:
    name: {service-name}
```

**Files to create:**
1. `auth-service/src/main/resources/bootstrap.yaml`
2. `family-service/src/main/resources/bootstrap.yaml`
3. `master-service/src/main/resources/bootstrap.yaml`

**Why this is needed:**
- Spring Cloud Config requires bootstrap configuration to connect to the config server BEFORE the main application context is created
- The `fail-fast: false` allows services to start even if config server is temporarily unavailable
- The retry configuration ensures robust connection attempts

---

### Step 3: Fix Configuration File Property Names

**File:** `config-repo/auth-service.yml`

**Current (WRONG):**
```yaml
spring:
  application:           # <-- WRONG: extra level
    security:
      jwt:
        secret-key: ${JWT_SECRET_KEY}
        expiration: ${JWT_EXPIRATION}
    cors:
      allowed-origin: ${FRONTEND_URL}
```

**Corrected:**
```yaml
application:            # <-- CORRECT: no spring. prefix
  security:
    jwt:
      secret-key: ${JWT_SECRET_KEY}
      expiration: ${JWT_EXPIRATION}
  cors:
    allowed-origin: ${FRONTEND_URL}

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/family_management_system_db
    username: postgres
    password: 1234
    driver-class-name: org.postgresql.Driver
    jpa:
      hibernate:
        ddl-auto: update
      properties:
        hibernate:
          dialect: org.hibernate.dialect.PostgreSQLDialect
          format_sql: true

  eureka:
    client:
      service-url:
        defaultZone: http://localhost:8761/eureka

  server:
    port: 8082
```

---

### Step 4: Set Required Environment Variables

Before running the services, set these environment variables:

**PowerShell (Windows):**
```powershell
$env:JWT_SECRET_KEY = "your-very-secure-secret-key-min-256-bits-recommended"
$env:JWT_EXPIRATION = "86400000"  # 24 hours in milliseconds
$env:FRONTEND_URL = "http://localhost:3000"
```

**Command Prompt (Windows):**
```cmd
set JWT_SECRET_KEY=your-very-secure-secret-key-min-256-bits-recommended
set JWT_EXPIRATION=86400000
set FRONTEND_URL=http://localhost:3000
```

**Linux/Mac (Bash):**
```bash
export JWT_SECRET_KEY="your-very-secure-secret-key-min-256-bits-recommended"
export JWT_EXPIRATION="86400000"
export FRONTEND_URL="http://localhost:3000"
```

**⚠️ Important:** These variables must be set BEFORE starting the config server and services.

---

### Step 5: Add PostgreSQL Driver to Services (If needed)

Some services may need the PostgreSQL driver explicitly. Add to pom.xml if missing:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## Prerequisites - Ensure All Infrastructure is Running

Before starting the microservices, verify these are running:

```powershell
# Check PostgreSQL (port 5432)
netstat -ano | Select-String "5432"

# Check Eureka Server (port 8761)
netstat -ano | Select-String "8761"

# Check Config Server (port 8888)
netstat -ano | Select-String "8888"
```

All three should show LISTENING status.

---

## Startup Sequence (After All Fixes)

1. **Set environment variables** (see Step 4)
2. **Start Config Server** (if not already running)
3. **Start Eureka Server** (if not already running)
4. **Build and run services:**

```powershell
# Auth Service
cd "c:\Users\Nirmal\OneDrive\Desktop\Family Management System\auth-service"
mvn clean package -DskipTests
java -jar target/auth-service-0.0.1-SNAPSHOT.jar

# In a new terminal - Family Service
cd "c:\Users\Nirmal\OneDrive\Desktop\Family Management System\family-service"
mvn clean package -DskipTests
java -jar target/family-service-0.0.1-SNAPSHOT.jar

# In a new terminal - Master Service
cd "c:\Users\Nirmal\OneDrive\Desktop\Family Management System\master-service"
mvn clean package -DskipTests
java -jar target/master-service-0.0.1-SNAPSHOT.jar
```

---

## Expected Success Indicators

When all fixes are applied:

✅ Auth-Service should start on port 8082  
✅ Family-Service should start on port 8081  
✅ Master-Service should start on port 8083  

Logs should show:
- Successfully loaded configuration from config server
- Connected to PostgreSQL database
- Registered with Eureka discovery server

---

## Summary of Changes Required

| File/Service | Change | Type |
|---|---|---|
| auth-service/pom.xml | Add spring-cloud-starter-config | Dependency |
| family-service/pom.xml | Add spring-cloud-starter-config | Dependency |
| master-service/pom.xml | Add spring-cloud-starter-config | Dependency |
| auth-service/src/main/resources/bootstrap.yaml | Create new file | Config |
| family-service/src/main/resources/bootstrap.yaml | Create new file | Config |
| master-service/src/main/resources/bootstrap.yaml | Create new file | Config |
| config-repo/auth-service.yml | Fix property paths (remove spring. prefix) | Config |
| Environment | Set JWT_SECRET_KEY, JWT_EXPIRATION, FRONTEND_URL | Env Vars |

---

## Notes

- The config server is already running and configured to fetch from GitHub repository
- All infrastructure (PostgreSQL, Eureka, Config Server) is verified working
- The root cause is missing Spring Cloud Config Client setup in microservices
- All services use Spring Boot 4.0.7 with Java 17
- Spring Cloud version: 2025.1.2
