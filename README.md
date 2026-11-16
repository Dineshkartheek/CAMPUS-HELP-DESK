# CAMPUSCARE - Campus Help Desk

CAMPUSCARE is a campus support application for submitting, tracking, and managing student complaints and service requests. Students can create accounts, submit tickets, and follow their status. Administrators can review tickets and post updates, while main administrators can also manage admin accounts and confidential campus support reports.

## Features

- Student registration and sign-in
- Complaint submission and ticket status tracking
- Admin ticket management and remarks
- Confidential campus support reports
- Admin account management for main administrators
- Java 17 `HttpServer` API (not Spring Boot), browser-based interface, and MySQL database
- Java desktop utilities for help desk administration

## Project structure

- `web/` - CAMPUSCARE browser interface, served by the Node.js frontend
- `app.js`, `server.js`, `api/` - Node.js frontend server and Vercel handler
- `src/` - Java API server and desktop application source
- `lib/` - MySQL Connector/J dependency

## Running the server

1. Start MySQL and ensure the `helpdesk_db` database and application tables exist. The repository does not include a schema export. Set `DB_PASSWORD` in the environment used to launch VS Code; `.vscode/launch.json` supplies local `DB_URL`, `DB_USER`, and `CORS_ORIGIN`.
2. In VS Code, open **Run and Debug** and launch **Launch CAMPUSCARE Server**. The Java API listens on port 8080 and includes the MySQL Connector/J driver.
3. Run `npm install` once, then `npm start` to start the Node.js frontend on port 3000. It forwards `/api` requests to the Java API.
4. Open [http://localhost:3000](http://localhost:3000). You can also use [http://localhost:8080](http://localhost:8080) to access the Java server's built-in frontend.

The Java server requires `DB_URL`, `DB_USER`, `DB_PASSWORD`, and one exact `CORS_ORIGIN`; it does not fall back to a localhost database URL or allow wildcard CORS. `PORT` defaults to 8080 locally and is supplied by Render in production.

## Deploying the web app

The Node.js frontend can be deployed to Vercel. The Java API cannot run on Vercel, so deploy it as a Docker web service (for example, on Render) and connect both services to a managed MySQL database. The Node.js server proxies `/api` requests to the Java service so browser sessions stay same-origin.

### Deploy the API

1. Create a managed MySQL database and import the CAMPUSCARE schema/data. This repository does not include a database schema export.
2. Create a Render web service from this repository using the included `render.yaml` blueprint.
3. Set `DB_URL` to the provider's JDBC URL, for example `jdbc:mysql://HOST:3306/helpdesk_db?sslMode=REQUIRED`, and set `DB_USER` and `DB_PASSWORD` to the database credentials. Do not commit these values.
4. The blueprint restricts `CORS_ORIGIN` to the production Vercel origin supplied for CAMPUSCARE. If you change the frontend domain, update it to that exact origin without a trailing slash. Keep a single API instance because login sessions are stored in memory.

The service listens on Render's `PORT` environment variable and checks `/api/health`, including database connectivity. Cross-site cookies are configured as `SameSite=None; Secure` by the blueprint. For production, custom domains on the same parent domain are recommended because some browsers restrict third-party cookies.

### Deploy the frontend

1. Import the repository into Vercel. The Node.js handler serves the files in `web/`.
2. After Render has created the backend, copy its actual public service origin. In Vercel **Settings → Environment Variables**, set `CAMPUSCARE_BACKEND_URL` for Production to that origin (format: `https://<your-render-service>.onrender.com`, no trailing slash), then redeploy. Do not substitute a guessed hostname.
3. Ensure the API's `CORS_ORIGIN` exactly matches the deployed Vercel origin.

`PORT`, `DB_URL`, `DB_USER`, `DB_PASSWORD`, `CORS_ORIGIN`, `COOKIE_SAME_SITE`, and `COOKIE_SECURE` are read from the Java service environment. In production, the frontend returns a configuration error instead of falling back to localhost when `CAMPUSCARE_BACKEND_URL` is unset.

To run the Node.js frontend locally, run `npm install` and then `npm start`. It listens on port 3000 by default and proxies API requests to `http://localhost:8080`. Set `CAMPUSCARE_BACKEND_URL` if the Java API is running elsewhere. The API health check is `GET /api/health`; it returns 200 only when the database is reachable.
