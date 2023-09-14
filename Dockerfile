FROM openjdk:11
VOLUME /tmp
COPY springboot2-1.0.jar springboot2-1.0.jar
RUN bash -c "touch /springboot2-1.0.jar"
# 声明时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
#声明运行时容器提供服务端口，这只是一个声明，在运行时并不会因为这个声明应用就会开启这个端口的服务
EXPOSE 8081
ENTRYPOINT ["java","-jar","/user-web-0.0.1-SNAPSHOT.jar"]