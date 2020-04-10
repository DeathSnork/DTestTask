FROM openjdk:8-jre-alpine

ADD target/scala-2.12/DINSTestTask.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]