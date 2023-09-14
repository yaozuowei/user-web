#!/usr/bin/env bash

#### 远程服务器上的启动脚本 ###

# 项目名称
service_name=user-web
# 启动最小内存
xms_size=512m
# 启动最大内存
xmx_size=512m
# 源码目录
source_path=F:/yzw/idea/My/user-web
# jdk目录
jdk_path=/usr/local/yzw/config/jdk-11/bin

# 检测微服务进程id
pid=`ps -ef | grep ${service_name}_dev.jar | grep -v grep | awk '{print $2}'`
if [[ ! ${pid} ]]; then
    echo "==========not found ${service_name} pid=========="
else
    echo "==========kill ${service_name} pid: ${pid}=========="
    kill -9 ${pid}
fi

echo "==========开始启动服务:${service_name}_dev, Xms=${xms_size}m, Xmx=${xmx_size}m=========="
nohup java -jar -Xms${xms_size} -Xmx${xmx_size} -Dspring.profiles.active=stage -Djasypt.encryptor.password=jasypt-hnxh-harvey -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:18083 ${source_path}/${service_name}_dev.jar >/dev/null 2>&1 &
echo "==========${service_name}服务启动完成=========="

exit