FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY ./forum_backend .

RUN mvn clean package -DskipTests

FROM gcr.io/distroless/java21-debian12 AS production

WORKDIR /app

USER 1000:3000

COPY --from=build /app/target/forum.jar .

CMD ["/app/forum.jar"]
