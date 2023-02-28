# base
FROM eclipse-temurin:19-jdk-jammy as backend-base
WORKDIR /app
COPY backend/.mvn/ .mvn
COPY backend/mvnw backend/pom.xml ./
RUN ./mvnw dependency:resolve
COPY backend/src ./src

# development
FROM backend-base as backend-development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005'"]

# build
FROM node:18-alpine as frontend-build
ENV NODE_ENV=production
WORKDIR /app
COPY ["package.json", "package-lock.json*", "./"]
RUN npm install --production
COPY frontend .
RUN npm run build

FROM backend-base as backend-build
COPY --from=frontend-build /app/build src/main/resources/static
RUN ./mvnw package

# production
FROM eclipse-temurin:19-jre-jammy as production
ENV MONGO_URI=mongodb://mongo:27017
ENV MONGO_DATABASE=mixify
EXPOSE 8080
COPY --from=backend-build /app/target/backend-*.jar /app.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]