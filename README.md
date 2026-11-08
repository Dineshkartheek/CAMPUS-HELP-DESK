# CAMPUSCARE - Campus Help Desk

CAMPUSCARE is a campus support application for submitting, tracking, and managing student complaints and service requests. Students can create accounts, submit tickets, and follow their status. Administrators can review tickets and post updates, while main administrators can also manage admin accounts and confidential campus support reports.

## Features

- Student registration and sign-in
- Complaint submission and ticket status tracking
- Admin ticket management and remarks
- Confidential campus support reports
- Admin account management for main administrators
- Java HTTP server, browser-based interface, and MySQL database
- Java desktop utilities for help desk administration

## Project structure

- `web/` - CAMPUSCARE browser interface
- `src/` - Java server and desktop application source
- `lib/` - MySQL Connector/J dependency

## Running the server

1. Start MySQL and configure the `DB_PASSWORD` environment variable for the MySQL `root` account.
2. In VS Code, open **Run and Debug** and launch **Launch CAMPUSCARE Server**. The launch configuration includes the MySQL Connector/J driver.
3. Open [http://localhost:8080](http://localhost:8080).

The MySQL server must have the `helpdesk_db` database and the tables used by the application.

## Deploying the web app

The browser UI can be deployed to Vercel. The Java API cannot run on Vercel, so deploy it as a Docker web service (for example, on Render) and connect both services to a managed MySQL database.

### Deploy the API

1. Create a managed MySQL database and import the CAMPUSCARE schema/data. This repository does not include a database schema export.
2. Create a Render web service from this repository using the included `render.yaml` blueprint.
3. Set `DB_URL` to the provider's JDBC URL, for example `jdbc:mysql://HOST:3306/helpdesk_db?sslMode=REQUIRED`, and set `DB_USER` and `DB_PASSWORD` to the database credentials.
4. Set `CORS_ORIGIN` to the exact Vercel production origin, such as `https://your-project.vercel.app` (no trailing slash). Keep a single API instance because login sessions are stored in memory.

The service listens on the `PORT` environment variable. Cross-site cookies are configured for the Vercel/Render hostnames by the blueprint. For production, custom domains on the same parent domain are recommended because some browsers restrict third-party cookies.

### Deploy the frontend

1. Import the repository into Vercel. The included `vercel.json` publishes the `web/` directory.
2. Set `window.CAMPUSCARE_API_BASE_URL` in `web/config.js` to the deployed API origin, for example `https://your-api.onrender.com`, and redeploy. Do not add a trailing slash.
3. Ensure the API's `CORS_ORIGIN` exactly matches the deployed Vercel origin.

`PORT`, `DB_URL`, `DB_USER`, `DB_PASSWORD`, `CORS_ORIGIN`, `COOKIE_SAME_SITE`, and `COOKIE_SECURE` are read from the environment. Do not commit credentials.
