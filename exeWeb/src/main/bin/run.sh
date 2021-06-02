#!/bin/sh
#echo $dirname
DIR=$(cd $(dirname $0)/../ ; pwd)
echo ${DIR}
APP_NAME="${DIR}/"

# 检查是否在运行中
exists_run(){
  echo "cmd is:ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'"
  pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
  echo "pid is:${pid}"
  # 如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
    return 1
  else
    return 0
  fi
}

# 启动jar包应用程序
start(){
  exists_run
  if [ $? -eq 0 ]; then
    echo "${APP_NAME} is already running. pid=${pid}"
  else
    #nohup java -Xms128m -Xmx256m -jar ${APP_NAME} "$1" "$2" "$3" > /upsoft/app/datamiddle/logs/compare-provider.log 2>&1 &
    #nohup java -Droot=${APP_NAME} -Dlogback.configurationFile=${APP_NAME}/conf/logback.xml -Djava.ext.dirs=${APP_NAME}/lib/ com.exe.web.AppServerMain > ${APP_NAME}/static/logs/std.log 2>&1 &
    #nohup java -Droot=${APP_NAME} -Dlogback.configurationFile=${APP_NAME}/conf/logback.xml -Djava.ext.dirs=${APP_NAME}/lib/ com.exe.web.AppServerMain > /dev/null 2>&1 &
    nohup java -p8080 -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8800 -Droot=${APP_NAME} -Dlogback.configurationFile=${APP_NAME}/conf/logback.xml .:${JAVA_HOME}/jre/lib/ext:${APP_NAME}/lib/ com.exe.web.AppServerMain > ${APP_NAME}/static/logs/std.log 2>&1 &
    echo "$APP_NAME is starting"
  fi
}

# 停止应用
stop(){
  exists_run
  if [ $? -eq "0" ]; then
	kill -9 $pid
    echo "$APP_NAME is stoping pid=$pid"
  else
    echo "${APP_NAME} is not running"
  fi
}

#输出运行状态
status(){
  exists_run
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is running. Pid is ${pid}"
  else
    echo "${APP_NAME} is NOT running."
  fi
}

#  重新启动
restart(){
  stop
  sleep 5
  start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start $2 $3 $4
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    echo "Usage: sh run.sh [start|stop|restart|status]"
    ;;
esac
exit 0
