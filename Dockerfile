# our base image
FROM openjdk:8-jre-alpine

# specify the port number the container should expose
EXPOSE 8080

RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY  ./target/scala-2.12/DINSTestTask.jar ./run_jar.sh ./
RUN ["chmod", "+x", "/run_jar.sh"]
ENTRYPOINT ./run_jar.sh