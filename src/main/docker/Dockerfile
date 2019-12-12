FROM java:8
VOLUME /tmp


#WORKDIR /application/jenkins/deploy/src/main/docker
#RUN pwd

ADD service-users-1.0-SNAPSHOT.jar service-users.jar
ENTRYPOINT ["java","-Xms200m","-Xmx300m","-jar","/service-users.jar"]