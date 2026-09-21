# Falcon Booking — E2E Test Suite

End-to-end testing suite for [Falcon Booking](https://falconbooking.org), an airline reservation system. Built with **Playwright 1.49 + JUnit 5 + Java 21 (Maven)**.

**Related repositories:** [Backend (Spring Boot)](https://github.com/CamiloLVZ/falcon-booking-api) · [Frontend (React/Vite)](https://github.com/CamiloLVZ/FalconFrontend)

---

## Coverage — 37 tests across 12 test classes

| Area | Class | Tests | Environment |
|---|---|---|---|
| Smoke | `SmokeTest` | 1 | Local + Prod |
| Smoke | `ServerWakeUpSmokeTest` | 1 | Prod only |
| Navigation | `NavBarTest` | 5 | Local + Prod |
| Flight search | `FlightSearchTest` | 2 | Local + Prod |
| Booking flow | `BookingTest` | 8 | Local only |
| Manage reservation | `ManageTest` | 2 | Local only |
| Check-in | `CheckInTest` | 2 | Local only |
| Authentication | `LoginTest` | 3 | Local + Prod |
| Protected URLs | `ProtectedURLsTest` | 3 | Local + Prod |
| API authorization | `AuthApiTest` | 5 | Local only |
| Admin panel | `AdminDashboardTest` | 4 | Local only |
| Concurrency | `BookingConcurrencyTest` | 1 | Local only |

📋 **[Traceability Matrix](https://docs.google.com/spreadsheets/d/1xxQBuRYA_vUW2toXpodI68_XBMNllf5Pxot8aH-5cPA/edit?usp=sharing)** — full test catalog with priority, environment, automation status and current state for each case.

### Technical highlights

- **Page Object Model** with step-level decomposition for the multi-step booking wizard (`ConfirmFlightStep` → `PassengersStep` → `PaymentSummaryStep` → `ConfirmationStep`).
- **Component Objects** (`PassengerFormFragment`, `SeatSelectionComponent`) for reusable UI fragments.
- **`BookingFlowHelper` fixture** creates a real reservation via UI and returns a typed `Reservation` record, used as setup by `CheckInTest` and `ManageTest` to test against live data instead of mocked values.
- **API-level concurrency test** that uses `CountDownLatch` to release two simultaneous booking requests against the last available seat, validating the backend's `PESSIMISTIC_WRITE` lock. The test asserts that exactly one request succeeds (201) and exactly one fails (400), not merely that one of them fails.
- **`navigateSafely(url)`** in `BaseTest` handles the Render free-tier cold start: detects the Server Wakeup Gate overlay (5s probe), waits up to 5 minutes for it to clear, then proceeds — so no individual test needs to manage this.
- All locators use `data-testid` attributes; no CSS selectors or XPath.
- Dates computed dynamically (`LocalDate.now().plusDays(N)`); passenger IDs generated with `ThreadLocalRandom` — no hardcoded state that expires or collides between runs.

### Out of scope

| Feature | Reason |
|---|---|
| Boarding pass QR scan | No manual fallback in the UI; would require Chromium camera mock |
| Email confirmation delivery | Would require a mail testing service (Mailtrap/Mailosaur) |

---

## Architecture

```
falcon-e2e-testing/          ← this repo
falcon-booking-api/          ← backend (sibling directory)
falcon-frontend/             ← frontend (sibling directory)
```

The Docker Compose file expects the three repos as sibling directories. Running `docker compose up --build` from this repo starts all four services in order:

```
db (Postgres 17) → backend (Spring Boot) → seed → frontend (nginx/Vite build)
```

`backend` runs Flyway migrations on first boot. `seed` runs after `backend` is healthy, resets all transactional data, and inserts a trimmed test catalog (9 airports, 16 routes, ~672 dynamically-dated flights, 2 test users). Both services start cleanly on `docker compose down -v && docker compose up --build`.

---

## Prerequisites

- Java 21
- Maven 3.9+
- Docker Desktop (for the local stack)

---

## Setup

### 1. Clone the related repos as siblings

```bash
git clone https://github.com/CamiloLVZ/falcon-booking-api
git clone https://github.com/CamiloLVZ/FalconFrontend falcon-frontend
git clone https://github.com/CamiloLVZ/falcon-e2e-testing
```

### 2. Configure environment variables

```bash
cd falcon-e2e-testing
cp .env.example .env
```

Edit `.env` and fill in real values. All fields are required:

```env
POSTGRES_DB=falcon_local
POSTGRES_USER=falcon
POSTGRES_PASSWORD=falcon_local_pw

# Generate a secret with at least 32 characters — never reuse the production key
JWT_SECRET_KEY=changeme-generate-a-new-secret

# Resend API key (used by the backend to send confirmation emails)
RESEND_API_KEY=re_xxxxxxxxxxxxxxxxxxxxx
MAIL_USERNAME=your-test-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### 3. Start the local stack

```bash
docker compose up --build
```

Wait for the seed container to exit with code 0 — you'll see the verification output:

```
seed-1 |  airports            |     9
seed-1 |  routes              |    16
seed-1 |  flights             |   672
seed-1 |  users               |     2
seed-1 exited with code 0
```

### 4. Install Playwright browsers (first time only)

```bash
mvn compile
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

---

## Running the tests

All commands require `-Denv=local` or `-Denv=prod`. Omitting it throws an `IllegalStateException`.

```bash
# Full suite against the local stack
mvn test -Denv=local

# Prod-safe subset against falconbooking.org (read-only, no data creation)
mvn test -Denv=prod -Dgroups=prod-safe

# Single test class
mvn test -Denv=local -Dtest=BookingTest

# Exclude destructive tests
mvn test -Denv=local -DexcludedGroups=admin,concurrency

# Reset the database and run (required before each full regression run)
docker compose down -v && docker compose up --build -d
mvn test -Denv=local
```

> **Note on idempotency:** most tests generate unique data on each run (random passenger IDs, dynamic dates) and are safe to run multiple times without resetting the database. The exception is `BookingConcurrencyTest`, which relies on a seed-controlled flight with exactly one seat available. Running it a second time without resetting the stack will find that seat already taken and the test will fail. Always run `docker compose down -v && docker compose up --build -d` before executing the concurrency test or the full suite.

### Test users (seeded locally)

| Role | Email | Password |
|---|---|---|
| Client | `e2e.client@falcon.test` | `Test1234!` |
| Admin | `e2e.admin@falcon.test` | `Admin1234!` |

These credentials exist only in the local seed database, not in production.

### Environment matrix

| Tag | Runs in | What it covers |
|---|---|---|
| `smoke` | Local + Prod | App loads, Server Wakeup Gate |
| `prod-safe` | Local + Prod | Read-only flows safe to run against production |
| `prod-only` | Prod | Cold start behavior (skipped locally via `Assumptions`) |
| `regression` | Local | Business flows, auth, API authorization |
| `admin` | Local | Admin panel operations |