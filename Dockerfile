FROM openjdk:11.0.11-jdk-oracle
ADD target/ms-wow-authentication-0.0.1-SNAPSHOT.jar .
EXPOSE 8081
ENTRYPOINT java -jar ms-wow-authentication-0.0.1-SNAPSHOT.jar