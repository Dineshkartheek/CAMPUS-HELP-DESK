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
