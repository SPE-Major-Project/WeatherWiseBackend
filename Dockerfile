FROM openjdk:17-alpine
EXPOSE 8082
WORKDIR /opt
ENV PORT 8082

COPY ./target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
# FROM openjdk:17
# EXPOSE 8082
# ENV APP_HOME /usr/src/app
# COPY target/*.jar $APP_HOME/app.jar
# WORKDIR $APP_HOME
# CMD ["java", "-jar", "app.jar"]
