# Hindu Digital Japa Counter — Unified Spring Boot 3 REST API Backend

A production-grade, containerized standalone microservice built with **Java 21**, **Spring Boot 3.3.x**, and **Maven**. Designed to run in its own Kubernetes pod and pushed to its own dedicated GitHub repository.

---

## 1. Quick Start: Push to a Separate GitHub Repository

To host this backend in its own standalone repository:

```bash
# Navigate to this directory
cd backend-repo

# Initialize a separate git repository
git init -b main

# Add all backend files
git add .
git commit -m "feat: Initial commit for Spring Boot 3 Japa backend microservice"

# Link to your new GitHub repository
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/japa-backend.git

# Push to GitHub
git push -u origin main
```

Once pushed to GitHub:
- The GitHub Actions pipeline (`.github/workflows/backend-ci.yml`) will automatically build, test, and publish your Docker container image to the **GitHub Container Registry** (`ghcr.io/YOUR_GITHUB_USERNAME/japa-backend:latest`).

---

## 2. Local Development & Testing

### Option A: Run via Docker Compose (Recommended)
This starts both **PostgreSQL 16** and the **Spring Boot 3 Backend** container:

```bash
docker compose up -d --build
```
- API Base URL: `http://localhost:8080`
- Interactive Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health Probe: `http://localhost:8080/actuator/health`

### Option B: Run via Maven with Local PostgreSQL

1. Ensure PostgreSQL is running on port `5432` with database `japa_db`.
2. Run the application:
```bash
mvn clean spring-boot:run
```

Liquibase will automatically execute the database changesets on startup:
- `001-create-users-table.xml`: User accounts and SSO credentials
- `002-create-chant-records-table.xml`: Chanting records and composite indexes
- `003-create-ads-table.xml`: Devotional ads configuration and seed promotions

---

## 3. Kubernetes Pod Deployment (Separate Pod Architecture)

To deploy the backend into a dedicated Kubernetes pod:

```bash
# 1. Apply Secret & ConfigMap
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/configmap.yaml

# 2. Deploy the Backend Pods (3 replicas with rolling updates)
kubectl apply -f k8s/deployment.yaml

# 3. Expose the Internal ClusterIP Service (Port 8080)
kubectl apply -f k8s/service.yaml

# 4. Enable Horizontal Pod Autoscaling (HPA)
kubectl apply -f k8s/hpa.yaml
```

### Pod-to-Pod Communication
- **Frontend Pod**: Can reach this backend via internal Kubernetes DNS: `http://japa-backend-service.default.svc.cluster.local:8080`
- **Ingress / Gateway**: Route external `/api/*` traffic directly to `japa-backend-service:8080`.

---

## 4. Key REST Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/health` | Kubernetes health check and DB status |
| `POST` | `/api/v1/auth/login` | Email/password sign-in and registration (returns JWT) |
| `POST` | `/api/v1/auth/sso-token-exchange` | 3rd party token exchange (Google, Yahoo, LinkedIn) |
| `POST` | `/api/v1/japa/sync` | Batch sync queued offline chant records |
| `POST` | `/api/v1/japa/increment` | Real-time chant count increment (+1 or batch) |
| `GET` | `/api/v1/japa/history` | Historical records by date |
| `GET` | `/api/v1/japa/analytics/daily` | Time-series daily summaries for charts (7, 14, 30 days) |
| `GET` | `/api/v1/japa/analytics/summary`| Streaks, lifetime counts, and total malas |
| `GET` | `/api/v1/ads/config` | Google AdSense & devotional campaign parameters |
| `POST` | `/api/v1/ads/track` | Track ad impressions and clicks |
| `GET` | `/swagger-ui.html` | Interactive Swagger UI API documentation |
| `GET` | `/actuator/health` | Kubernetes liveness and readiness probe |

---

## 5. Security & E2E Privacy
- **Stateless Authentication**: Uses HMAC-SHA256 JWT tokens.
- **Client-Side E2EE Ready**: Encrypted notes and sadhana vows pass transparently through the backend in AES-GCM format, preserving zero-knowledge security on the server.
- **Container Hardening**: Dockerfile drops all Linux capabilities and runs as a non-root system user (`spring:spring`).
