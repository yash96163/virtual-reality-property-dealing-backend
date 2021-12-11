FROM openjdk:11
ADD target/application-0.0.1-SNAPSHOT.jar vr-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","vr-app.jar"]
