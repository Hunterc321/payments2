FROM amazoncorretto:21
WORKDIR /app
COPY target/payments-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
