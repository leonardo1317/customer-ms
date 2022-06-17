FROM openjdk:11
RUN mkdir /opt/app
EXPOSE 8080
COPY build/libs/customer-ms-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/customer-ms-0.0.1-SNAPSHOT.jar"]