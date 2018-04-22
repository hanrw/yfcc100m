FROM openjdk:8-jdk-slim
MAINTAINER slam.han <slam.han1987@hotmail.com>

ADD ./target/scala-2.12/yfcc100m-assembly-1.0.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/yfcc100m-assembly-1.0.jar"]