#!/usr/bin/env bash

#./build-common.sh

# 本地源码目录
loc_source_path=F://yzw/idea/My/user-web
# 远程服务器源码目录
remote_source_path=/usr/local/yzw/project/user-web
# 远程服务器日志目录
remote_log_path=/yzw/idea/My/user-web/logs
# 服务器host
server_ip=81.68.253.151
server_password=Yaozuowei1025


cd ${loc_source_path}
mvn clean install -Dmaven.test.skip=true

#echo '------upload jar------'
#scp -o StrictHostKeyChecking=no ${loc_source_path}/target/user-web.jar root@${server_ip}:${remote_source_path}/user-web_dev.jar
#echo '------upload jar finish------'

echo '------start shell------'
ssh -o StrictHostKeyChecking=no root@${server_ip} "/bin/bash -s" < ${loc_source_path}/shell/start-dev.sh
echo '------start shell finish------'

ssh -o StrictHostKeyChecking=no root@${server_ip} "tail -f ${remote_log_path}/info.log"
exit