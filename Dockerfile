# base
FROM eclipse-temurin:19-jdk-jammy as backend-base
WORKDIR /app
COPY backend/.mvn/ .mvn
COPY backend/mvnw backend/pom.xml ./
RUN ./mvnw dependency:resolve
COPY backend/src ./src

# build
FROM node:18-alpine as frontend-build
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend .
RUN npm run build

FROM backend-base as backend-build
WORKDIR /app
COPY --from=frontend-build /app/build src/main/resources/static
RUN ./mvnw package -D maven.test.skip

# production
FROM eclipse-temurin:19-jre-jammy as production
WORKDIR /app
ENV MONGO_URI=mongodb://mongo:27017
ENV MONGO_DATABASE=mixify
EXPOSE 8080
COPY --from=backend-build /app/target/backend-*.jar ./app.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]