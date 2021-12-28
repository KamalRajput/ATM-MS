FROM openjdk:8-jdk-alpine
EXPOSE 9093
COPY target/atm-aggregator-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","/atm-aggregator-service-0.0.1-SNAPSHOT.jar"]