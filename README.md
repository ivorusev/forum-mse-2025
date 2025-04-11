# About the Forum Application 

## Prerequisites
- Java 21

## Setup

```shell
mvn clean install
```

## How To Start the Postgres and PGAdmin
- Install Docker 
- Navigate to the root folder of the project
- Execute 
```shell
docker compose up -d
```


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
- Manage topics content
- Flag users

### Authenticated User
- All *Guest* activities +
- Create topics
- Write in a topic

### Guest
- Look at topics
- Register
- Login