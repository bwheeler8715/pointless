# Pointless

Pointless is a lightweight, open-source collaborative tool for agile planning poker and real-time voting. Designed for
simplicity and speed, it allows teams to estimate tasks together in real-time using a clean, intuitive web interface.

The project is structured as a mono-repo containing a Java Spring Boot backend and a Vue 3 frontend.

## 🚀 Features

- **Real-time Collaboration**: Instant updates across all participants using Server-Sent Events (SSE).
- **Planning Poker**: Estimate tasks using Fibonacci or custom scales.
- **Room Management**: Create private rooms and manage voting sessions.
- **Role-based Interaction**: Support for Organizers, Voters, and Observers.
- **Zero Configuration**: Works out of the box with in-memory storage.
- **Dockerized**: Easy deployment as a single, combined container.

## 🏗 Project Structure

- [`pointless-service`]: Java 21 & Spring Boot backend.
    - See the [Backend README](pointless-service/README.md) for more details.
- [`pointless-ui`]: Vue 3, TypeScript, and Tailwind CSS frontend.
    - See the [Frontend README](pointless-ui/README.md) for more details.
- `Dockerfile`: Combined build for serving both components from a single container.

## 📋 Prerequisites

- **Docker**: Recommended for the easiest setup.
- **Java 21** & **Maven 3.9+**: For backend development.
- **Node.js (v20+)** & **npm**: For frontend development.

## 🐳 Getting Started with Docker

The project includes a root-level `Dockerfile` that packages both the backend and frontend into a single image, served
via Nginx.

### 1. Build the sub-projects

Before building the Docker image, you need to build the frontend and backend artifacts:

**Build the Backend:**

```bash
cd pointless-service
mvn clean package
cd ..
```

**Build the Frontend:**

```bash
cd pointless-ui
npm install
npm run build
cd ..
```

### 2. Build and Run the Combined Image

Once the artifacts are ready, build the combined image from the project root:

```bash
docker build -t pointless .
```

Run the container:

```bash
docker run -p 8080:80 pointless
```

The application will be available at http://localhost:8080.

## 🔧 Local Development

If you prefer to run the components separately for development:

### Backend (`pointless-service`)

1. `cd pointless-service`
2. `mvn spring-boot:run`
3. Backend runs on http://localhost:8080.

### Frontend (`pointless-ui`)

1. `cd pointless-ui`
2. `npm install`
3. `npm run dev`
4. Frontend runs on http://localhost:5173 (default Vite port).

*Note: In development mode, you may need to update the `API_URL` in `pointless-ui/public/config.json` to point to your
local backend.*

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details (if available).