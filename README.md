# About the Forum Application

## Prerequisites
- Docker Engine Community Edition 27.3.1
- Apache Maven 3.8.7
- Java 21

## Setup

TO build the application container image:
```shell
docker build . -t forum:<version>
# e.g. ..... . -t forum:1.0.0
```

To start Postgres and PGAdmin:

```shell
# To build the app image
docker compose build
# To start the components
docker compose up -d
```

_This will start the Postgres database and expose it on the host machine's
port `5432`, and the Postgres Admin Panel on port `5050`._

**Note:** If building a docker image (via docker build) you must specify
the version in the docker compose

Install dependencies:

```shell
mvn clean install
```

## Testing credentials:
Declared in [testing.env](testing.env) file.


## Roles

### Administrator
Activities:
- All *Moderator* activities +
- Manage topics
- Manage users
- Manage authorization

### Moderator
Activities:
- All *Authenticated User* activities +
- Manage topics content and replies
- Flag users

### Authenticated User
- All *Guest* activities +
- Create topics
- Reply in a topic

### Guest
- Browse topics
- Register
- Login