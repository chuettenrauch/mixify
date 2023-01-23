FROM eclipse-temurin:19-jre-jammy

ENV MONGO_URI=mongodb://mongo:27017
ENV MONGO_DATABASE=mixify

ADD backend/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]