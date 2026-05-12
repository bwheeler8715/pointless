# Pointless Service

Pointless Service is a lightweight, Spring Boot-based backend for real-time collaborative voting and planning poker. It
provides a RESTful API and Server-Sent Events (SSE) for seamless room management, voting orchestration, and live
updates.

## 🚀 Features

- **Room Management**: Create, join, and leave voting rooms.
- **Real-time Updates**: Live broadcasting of voting states via Server-Sent Events (SSE).
- **Voting Orchestration**: Open and close voting rounds with ease.
- **Auto-Cleanup**: Scheduled tasks to purge inactive rooms and connections.
- **Container Ready**: Includes a Dockerfile for easy deployment.
- **Health Monitoring**: Integrated Spring Boot Actuator for health checks.

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot 4.x** (Web MVC, Actuator)
- **Maven**
- **Docker**
- **In-Memory Storage** (Default)

## 🏃 Getting Started

### 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker** (optional, for containerized deployment)

### 🔧 Local Development

1. **Build the project**:
   ```bash
   mvn clean install
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

The service will be available at http://localhost:8081.

### 🐳 Running with Docker

The project includes a `Dockerfile` for easy deployment using Docker:

1. **Build the JAR**:
   ```bash
   mvn clean package
   ```

2. **Build the Docker image**:
   ```bash
   docker build -t pointless-service .
   ```

3. **Run the container**:
   ```bash
   docker run -p 8081:8081 pointless-service
   ```

The service will be available at http://localhost:8081.

## 📡 API Overview

### Room Endpoints

- `POST /api/room/get` - Get current room state.
- `POST /api/room/create` - Create a new voting room.
- `POST /api/room/join` - Join an existing room.
- `POST /api/room/leave` - Leave a room.

### Voting Endpoints

- `POST /api/room/open` - Open voting for a room.
- `POST /api/room/close` - Close voting and reveal results.
- `POST /api/room/vote` - Cast a vote.

### Real-time Updates

- `GET /api/room/{roomId}/stream` - Subscribe to SSE stream for live updates.

## ⚙️ Configuration

Configuration can be adjusted in `src/main/resources/application.yaml`:

```yaml
server:
  port: 8081

pointless:
  purge-rooms-cron: "0 0 0 * * *"       # Daily at midnight
  purge-connections-cron: "0 0 1 * * *" # Daily at 1 AM
  sse-timeout: 86400000                 # 24 hours
  cors:
    allowed-origins: "*"               # Adjust for production
```