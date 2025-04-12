# About the Forum Application

## Prerequisites
- Docker Engine Community Edition 27.3.1
- Apache Maven 3.8.7
- Java 21

## Setup

To start Postgres and PGAdmin:

```shell
docker compose up -d
```

_This will start the Postgres database and expose it on the host machine's
port `5432`, and the Postgres Admin Panel on port `5050`._

Install dependencies:

```shell
mvn clean install
```

## Testing credentials:
Declared in [testing.env](testing.env) file.
