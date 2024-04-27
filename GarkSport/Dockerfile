FROM maven:3.8.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml install package -DskipTests
EXPOSE 8089
ENTRYPOINT ["java","-jar","/home/app/target/demo-0.0.1-SNAPSHOT.jar"]