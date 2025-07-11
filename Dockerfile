FROM eclipse-temurin:21-jdk-jammy as build

WORKDIR /app

COPY . .

RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]