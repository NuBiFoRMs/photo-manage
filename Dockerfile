FROM openjdk:8-jre-slim
COPY ./target/PhotoManage-0.0.1.jar /usr/bin/photo-manage/PhotoManage.jar
WORKDIR /usr/bin/photo-manage
ENTRYPOINT ["java", "-jar", "PhotoManage.jar"]
EXPOSE 8080