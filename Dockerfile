FROM openjdk:17
LABEL authors="golla"
EXPOSE 8080
ENV APP_HOME /usr/src/app
COPY target/Shopping-Application.jar $APP_HOME/app.jar
WORKDIR APP_HOME
ENTRYPOINT ["java","-jar","app.jar"]