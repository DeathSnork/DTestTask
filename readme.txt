###Requirements
    * SBT
    * Java 8+

### Work on
    * SBT 1.3.9
    * Java 11.0.6
    * Docker 19.03.8
    * docker-compose 1.17.1

## Installation

### Run via sbt
    sbt run

### Build jar and run jar
    sbt clean assembly
    java -jar DINSTestTask.jar

### Run via Docker-compose
    sbt clean assembly
    docker-compose build --no-cache
    docker-compose up

## Feedback
    I will be glad to feedback and comments
    You can leave them in a file feedback.txt
    and then send pull request to https://github.com/DeathSnork/DTestTask
