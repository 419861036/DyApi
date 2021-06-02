# 目录介绍
1、bin 启动脚本  
2、conf 配置文件  
3、java 源码  
4、lib 本地jar  
5、static 静态资源  

# 启动 
AppServerMain



# https配置
## 1、使用java 自带工具 keytool 生成证书

keytool -genkey -alias server -keypass 123456 -storepass 123456 -storetype PKCS12 -keyalg RSA -keysize 2048  -keystore keystore.p12 -validity 3650 -dname "C=CN,ST=BJ,L=BJ,O=ca.wbq.com,OU=ca.wbq.com,CN=ca.wbq.com"

## 2、代码
`
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
public class JeetSslTest {
public static void main(String[] args) {

Server server = new Server();
HttpConfiguration https_config = new HttpConfiguration();
https_config.setSecureScheme("https");
SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
sslContextFactory.setKeyStoreType("PKCS12");
sslContextFactory.setKeyStorePath("e:/temp/key/keystore.p12");
sslContextFactory.setTrustStorePath("e:/temp/key/keystore.p12");
sslContextFactory.setKeyStorePassword("123456");
sslContextFactory.setKeyManagerPassword("123456");
try{
ServerConnector httpsConnector = new ServerConnector(server,
new SslConnectionFactory(sslContextFactory,HttpVersion.HTTP_1_1.asString()),
new HttpConnectionFactory(https_config));
httpsConnector.setPort(8443);
server.addConnector(httpsConnector);
server.setHandler(new HelloHandler());//处理逻辑
server.start();
server.join();
}catch(Exception e){
e.printStackTrace();
}
}
}`

