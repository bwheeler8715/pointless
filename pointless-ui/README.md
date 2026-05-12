# Pointless UI

Pointless UI is a lightweight, open-source web application for agile planning poker. It provides a real-time,
collaborative interface for teams to estimate tasks using standard Fibonacci scales or custom values.

Built with **Vue 3**, **Vite**, **TypeScript**, and **Tailwind CSS**.

## 🚀 Features

- **Real-time Collaboration**: See who's in the room and when they've voted.
- **Role-based Views**:
    - **Organizer**: Create rooms, manage voting sessions (open/close), and set ticket details.
    - **Voter**: Join rooms and cast estimates.
    - **Observer**: Watch the voting process without participating.
- **Live Updates**: Utilizes Server-Sent Events (SSE) for instantaneous UI updates.
- **Clean UI**: Responsive design powered by Tailwind CSS and Heroicons.

## 🛠 Tech Stack

- **Framework**: [Vue 3](https://vuejs.org/) (Composition API)
- **Build Tool**: [Vite](https://vitejs.dev/)
- **Styling**: [Tailwind CSS](https://tailwindcss.com/)
- **Icons**: [Heroicons](https://heroicons.com/)
- **Language**: [TypeScript](https://www.typescriptlang.org/)
- **HTTP Client**: [Axios](https://axios-http.com/)

## 🏃 Getting Started

### 📋 Prerequisites

- Node.js (v20.19.0 or >=22.12.0)
- npm

### 🔧 Local Development

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start the development server:

    ```bash
    npm run dev
    ```

The service will be available at http://localhost:5173 (default Vite port).

### 🐳 Running with Docker

The project includes a `Dockerfile` for easy deployment using Nginx:

1. Build the production assets:
   ```bash
   npm run build
   ```

2. Build the Docker image:
   ```bash
   docker build -t pointless-ui .
   ```

3. Run the container:
   ```bash
   docker run -p 8080:8080 pointless-ui
   ```

The service will be available at http://localhost:8080.

## ⚙️ Configuration

Configuration can be adjusted in `public/config.json`:

```json
{
  "API_URL": "http://localhost:8081/api"
}
```