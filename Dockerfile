FROM openjdk:8-jre-alpine

EXPOSE 8080

RUN mkdir -p /opt/app
WORKDIR /opt/app

COPY  ./DINSTestTask.jar ./run_jar.sh ./
RUN ["chmod", "+x", "/opt/app/run_jar.sh"]
ENTRYPOINT ./run_jar.sh