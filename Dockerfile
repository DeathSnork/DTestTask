FROM amazonlinux:2017.03

#ENV SCALA_VERSION 2.11.8
#ENV SBT_VERSION 0.13.13

# Install Java8
RUN yum install -y java-1.8.0-openjdk-devel


RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY ./run_jar.sh ./DINSTestTask.jar ./
ENTRYPOINT ["./run_jar.sh"]