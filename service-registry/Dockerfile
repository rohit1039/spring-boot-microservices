FROM openjdk:11
ADD target/service-registry.jar service-registry.jar
ENTRYPOINT ["java","-jar","service-registry.jar"]
EXPOSE 8761