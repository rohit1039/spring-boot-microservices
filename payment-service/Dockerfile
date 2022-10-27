FROM openjdk:11
ADD target/payment-service.jar payment-service.jar
ENTRYPOINT ["java","-jar","payment-service.jar"]
EXPOSE 8082