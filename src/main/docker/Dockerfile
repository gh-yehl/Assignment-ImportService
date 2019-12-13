FROM java:8
VOLUME /tmp


#WORKDIR /application/jenkins/deploy/src/main/docker
#RUN pwd

ADD import-service-1.0-SNAPSHOT.jar import-service.jar
ENTRYPOINT ["java","-Xms200m","-Xmx300m","-jar","/import-service.jar"]