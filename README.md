# Mini IoT Dashboard

A small end-to-end IoT telemetry system: a sensor publishes temperature readings over MQTT, a Spring Boot backend ingests and stores them, and a web page shows them live over WebSocket.

It's a learning project that mirrors the architecture of a real drone cloud platform (DJI Cloud API) in miniature — the same pipeline of **MQTT → message routing → JSON parsing → database → WebSocket → browser**, just with a fake temperature sensor instead of a drone.

## Architecture

```
Fake Sensor ──MQTT──▶ EMQX ──MQTT──▶ Spring Boot ──WebSocket──▶ Browser
  (Python)           (broker)         (backend)                (dashboard)
                                          │
                                          ▼
                                        MySQL
```

A reading flows like this:

1. The Python publisher sends a JSON reading to the topic `sensors/sensor-1/temperature`.
2. EMQX (the MQTT broker) receives it.
3. The Spring Boot backend is subscribed to `sensors/+/temperature` and receives the message.
4. Jackson parses the JSON into a `Reading` object.
5. The reading is saved to MySQL via Spring Data JPA.
6. The same reading is broadcast over a WebSocket to any connected browser.
7. The dashboard page appends the new reading in real time.

## Tech stack

- **Java 17** + **Spring Boot 4.1**
- **Spring Integration (MQTT)** with the Eclipse Paho client — message ingestion
- **Spring Data JPA** + **Hibernate** — persistence
- **MySQL 8** — storage (runs in Docker)
- **EMQX 4.4** — MQTT broker (runs in Docker)
- **Spring WebSocket** — live push to the browser
- **Python (paho-mqtt)** — fake sensor publisher

## Project structure

```
demo/
├── src/main/java/com/example/demo/
│   ├── DemoApplication.java        # entry point
│   ├── Reading.java                # JPA entity (a sensor reading)
│   ├── ReadingRepository.java      # data access (auto-generated queries)
│   ├── ReadingController.java      # REST API for readings
│   ├── MqttConfig.java             # MQTT broker connection + inbound wiring
│   ├── MqttHandler.java            # parses each message, saves, broadcasts
│   ├── WebSocketConfig.java        # registers the /live WebSocket endpoint
│   └── LiveSocketHandler.java      # broadcasts readings to browsers
├── src/main/resources/
│   ├── application.yml             # config (port, datasource, JPA)
│   └── static/index.html           # the live dashboard page
└── pom.xml
publisher.py                        # fake sensor that publishes over MQTT
```

## Prerequisites

- Java 17 (Temurin recommended)
- Maven
- Docker Desktop (for MySQL and EMQX)
- Python 3 with `paho-mqtt` (for the publisher)

## Setup

### 1. Start the infrastructure (MySQL + EMQX) in Docker

```bash
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0
docker run -d --name emqx -p 1883:1883 -p 8083:8083 -p 18083:18083 -e EMQX_ALLOW_ANONYMOUS=true emqx/emqx:4.4.0
```

Create the database:

```bash
docker exec -it mysql mysql -uroot -proot -e "CREATE DATABASE IF NOT EXISTS sensors;"
```

(The EMQX dashboard is available at http://localhost:18083 — login `admin` / `public`.)

### 2. Run the backend

Open the `demo` project in your IDE and run `DemoApplication`, or from the command line:

```bash
cd demo
./mvnw spring-boot:run
```

The backend starts on **http://localhost:7070**. Hibernate auto-creates the `reading` table on first run.

### 3. Run the sensor publisher

Install the MQTT library and start the publisher (in a separate terminal):

```bash
pip3 install paho-mqtt
python3 publisher.py
```

It publishes a fake temperature reading every 2 seconds.

### 4. Open the dashboard

Visit **http://localhost:7070/index.html** — new readings appear live, no refresh needed.

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/readings` | The 20 most recent readings, as JSON |
| POST | `/api/readings` | Manually add a reading (for testing) |
| WS | `/live` | WebSocket stream of new readings |
| — | `/index.html` | Live dashboard page |

## Message format

Readings are published as JSON:

```json
{
  "sensorId": "sensor-1",
  "temperature": 22.7,
  "timestamp": 1781512062190
}
```

## Configuration

Key settings in `src/main/resources/application.yml`:

```yaml
server:
  port: 7070

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sensors?useSSL=false&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

The MQTT broker address (`tcp://localhost:1883`) and subscribed topic (`sensors/+/temperature`) are set in `MqttConfig.java`.

## Notes

- Both the backend and the publisher must be running at the same time, and the EMQX + MySQL containers must be up, for data to flow end to end.
- The `+` in the topic `sensors/+/temperature` is an MQTT wildcard — it matches any sensor id, so multiple sensors can publish to the same backend.

## Possible extensions

- Add a warning rule (e.g. log when temperature exceeds a threshold)
- Publish a command back to the sensor to change its reporting rate
- Run multiple publishers with different sensor ids
- Add a login/token endpoint
- Rebuild the dashboard as a richer frontend with a live chart

## License

Personal learning project — use freely.# Mini-IoT-Dashboard