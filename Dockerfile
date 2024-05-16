FROM openjdk:21-jdk-alpine
EXPOSE 8082
ARG JAR_FILE=target/*.jar
COPY ./target/WeatherWise-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# FROM openjdk:17
# EXPOSE 8082
# ENV APP_HOME /usr/src/app
# COPY target/*.jar $APP_HOME/app.jar
# WORKDIR $APP_HOME
# CMD ["java", "-jar", "app.jar"]
