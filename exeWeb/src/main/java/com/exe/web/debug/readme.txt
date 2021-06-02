https://blog.csdn.net/iteye_17963/article/details/82637167
-XDebug 启用调试。
-Xrunjdwp 加载JDWP的JPDA参考执行实例。
  transport 用于在调试程序和 VM 使用的进程之间通讯。
   dt_socket 套接字传输。
   server=y/n VM 是否需要作为调试服务器执行。
   address=8000 调试服务器的端口号，客户端用来连接服务器的端口号。
suspend=y/n 是否在调试客户端建立连接之后启动 VM

java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8800 -cp . com.exe.bo.debug.Test