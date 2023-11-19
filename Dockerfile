FROM openjdk:17-jdk-slim

EXPOSE 5500

ADD target/Diplom-0.0.1-SNAPSHOT.jar /diplom.jar

ENTRYPOINT ["java", "-jar", "/diplom.jar"]
