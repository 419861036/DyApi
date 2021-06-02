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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.logging.Level;

public class Order1  extends Cmd{

    private final static Logger logger = LoggerFactory.getLogger(OrderCmd.class);

    public void exe(CmdVo caiAction, Map<String, String> param) {
        try{
            Map<String, Object> sys=cmdHandle.getSys();
            if(true){
                test(sys);
                return;
            }
            Date zuoTian=DateUtil.addDays(new Date(),-1);
            String date=DateUtil.format(zuoTian);
            logger.info("task is start ");
            logger.info("find data create_date> $date ");
            List<Map<String,String>> list=getNotHandle(date);




            if(list==null || list.isEmpty()){
                logger.info("list is null");
                return;
            }
            if(overLimit(date)){
                return;
            }else{
                logger.info("date:$date,未超过限制");
            }
            WebDriver driver= getWebDriver(sys);
            logger.info("get web driver:$driver");
            for (Map map:list) {
                logger.info("handle data:$map");
                String userId=map.get("user_id").toString();
                if(inBlack(userId)){
                    logger.info("userId:$userId,黑名单中");
                    map.put("description","黑名单用户");
                    update(map);
                    continue;
                }else{
                    logger.info("userId:$userId,不在黑名单中");
                }

                logger.info("userId:$userId,start runOrder");
                runOrder(map,driver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(driver!=null){
                driver.quit();
            }
            driver=null;
            js=null;
        }

    }
    public void test(Map<String, Object> sys){
        driver= getWebDriver(sys);
        String str="StbID=&STBType=IHO-3300AD&mac=c0%3A13%3A2b%3Aeb%3A1a%3A64&userId=TEB20%3A66%2F45%3A%2F7%3EKR&token=dSDiC%3C_znlGtpf%3Fq3ZCCm7VG2hqMPy&jjsp_userID=CD02884127815@ITV&jjsp_userToken=Ao5XEAo5XE4fsKRwdrrIjpxa29ZEgFQf&jjsp_key=16%3A2&jjsp_platform=2&jjsp_enCodeUserID=TEB20%3A66/45%3A/7%3EKR&jjsp_enCodeUserToken=dSDiC%3C_znlGtpf%3Fq3ZCCm7VG2hqMPy&userAgent=webkit;Resolution(PAL,720P,1080P)&X-Requested-With=com.android.smart.terminal.iptv, user_id:TEB20%3A66/45%3A/7%3EKR";

        str="jjsp_userID=YB4703607@ITV&jjsp_userToken=AnoAdAnoAd0DUSmue91YNKqfvXA1LeEn&jjsp_key=5%3A2";
        str=str+"&jjsp_platform=2&jjsp_enCodeUserID=TVGB5245WD29.&jjsp_enCodeUserToken=lGcN/CVxdsIPW37gsoQWB2bCmp%3FClq%3Ff";
        str=str+"&jjsp_index_jsp_url=http%3A//182.131.0.215%3A8086/CQCA/index.jsp";
//        String name, String value, String path, Date expiry
        Map<String,String> map= StringUtil.getParaCollection(str,"&","=");
        String jjsp_userID=map.get("jjsp_userID").toString();
        String jjsp_userToken=map.get("jjsp_userToken").toString();
        String jjsp_key=map.get("jjsp_key").toString();
        String jjsp_platform=map.get("jjsp_platform").toString();
        String jjsp_enCodeUserID=map.get("jjsp_enCodeUserID").toString();
        String jjsp_enCodeUserToken=map.get("jjsp_enCodeUserToken").toString();
        String STBType=map.get("STBType").toString();
        String mac=map.get("STBType").toString();


        Date d=DateUtil.addDays(new Date(),999);
        System.out.println("---------------0");
        driver.manage().window().setSize(new Dimension(1280,720));
        driver.get("http://182.131.0.215:8086/CQCA/page/specialfive1.html?seriesCode=17b0f56e57b04265a54f62b9d5d01784");
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

        Set<Cookie> sets=driver.manage().getCookies();
        Iterator it = sets.iterator();
        while (it.hasNext()) {
            Cookie cookie1 = (Cookie) it.next();
            System.out.println("---------------1.cookie,"+cookie1.getName()+",val:"+cookie1.getValue());
        }
//        System.out.println("---------------2");
        driver.get("http://182.131.0.215:8086/CQCA/page/specialfive1.html?seriesCode=17b0f56e57b04265a54f62b9d5d01784");

        System.out.println("---------------3");
        getCutImg(driver,"testA");
////        String c=driver.getPageSource();
////        System.out.println(c);
//        System.out.println("---------------4");
//        JavascriptExecutor js= (JavascriptExecutor) driver;
//        exejs(js,"","TY1208-Z","b8%3A22%3A4f%3A1d%3Adf%3Ad7");
//        System.out.println("---------------5");
//        Object rs=js.executeScript("return Authentication.CTCGetConfig('mac')");
//
//        System.out.println("---------------5.rs:"+rs);
//        Cookie token=driver.manage().getCookieNamed("jjsp_enCodeUserToken");
//        System.out.println("---------------5.jjsp_enCodeUserToken:"+token.getValue());
        try {
            Object ok=js.executeScript("return ok()");
            System.out.println("---------------7 ok:"+ok);
        }catch (Exception e){
            e.printStackTrace();
        }
        sleep(1000l);
        getConsoleLog();
//        WebElement bodyEle=getEle(driver,"#box2_0");
//        System.out.println("---------------6");
//        bodyEle.sendKeys(Keys.ENTER);

        sleep(10000l);
        getCutImg(driver,"testB");
        System.out.println("---------------8 url:"+driver.getCurrentUrl());
//        System.out.println("---------------8content："+driver.getPageSource());
        try {
            Object ok=js.executeScript("return order()");
            sleep(10000l);
            getConsoleLog();
            System.out.println("---------------9 ok:"+ok);
            System.out.println("---------------9 url:"+driver.getCurrentUrl());
//            System.out.println("---------------9content："+driver.getPageSource());
        }catch (Exception e){
            e.printStackTrace();
        }
        getCutImg(driver,"testC");

//        try {
//            Object ok=js.executeScript("return passwordVerify()");
//            sleep(10000l);
//            getConsoleLog();
////
//            WebElement password=getEle(driver,"#password");
//            password.sendKeys("654321");
//
//            Object ok1=js.executeScript("return validatePassword()");
//
//            System.out.println("---------------10 ok:"+ok);
//            System.out.println("---------------10content："+driver.getPageSource());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        getCutImg(driver,"testD");


    }

    public void exejs(JavascriptExecutor js,String StbID,String STBType,String mac){
        String jsStr="Authentication = new Object();";
        jsStr=jsStr+"Authentication.CTCGetConfig = function(cvalue){ if(cvalue=='StbID'){return '"+StbID+"'};if(cvalue=='STBType'){return '"+STBType+"'};if(cvalue=='mac'){return '"+mac+"'};}";
        System.out.println("---------------js-content"+jsStr);
        js.executeScript(jsStr);
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
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String root=MyConfUtil.getRootPath();
            File targetFile=new File(root+"/static/logs/"+name+c+".png");
            FileUtil.copy(srcFile,targetFile);
            c++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getCutImg1(WebDriver driver,String name){
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String root=MyConfUtil.getRootPath();
            File targetFile=new File(root+"/static/logs/"+name+c+".png");
            WebElement element=getEle(driver,"body");
            BufferedImage img = ImageIO.read(srcFile);
            int width = element.getSize().getWidth();
            int height = element.getSize().getHeight();
            //获取指定元素的坐标
            Point point = element.getLocation();
            //从元素左上角坐标开始，按照元素的高宽对img进行裁剪为符合需要的图片
            BufferedImage dest = img.getSubimage(point.getX(), point.getY(), width, height);
            ImageIO.write(dest, "png", targetFile);
            c++;
//          FileUtil.copy(srcFile,targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static  WebDriver driver=null;
    private static JavascriptExecutor js=null;
    private WebDriver getWebDriver(Map<String, Object> sys){
        if(driver!=null){
            try{
                js.executeScript("console.log('test')");
            }catch (Exception e){
                e.printStackTrace();
                driver=null;
            }
        }
        if(driver==null){
            System.setProperty("webdriver.chrome.driver",
                    "/usr/bin/chromedriver");

            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("disable-infobars");
            opt.addArguments("--no-sandbox");
            opt.addArguments("--headless");
            opt.addArguments("--disable-gpu");
            opt.addArguments("--disable-dev-shm-usage");
            opt.addArguments("X-Requested-With:com.android.smart.terminal.iptv");
            opt.addArguments("User-Agent:webkit;Resolution(PAL,720P,1080P)");
//            TODO
            opt.setExperimentalOption("excludeSwitches",
                    Arrays.asList("enable-automation"));

            //为了获取console的日志输出
            DesiredCapabilities caps = DesiredCapabilities.chrome();
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, Level.INFO);//输入为info的日志
            caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
            caps.setCapability(ChromeOptions.CAPABILITY, opt);


            driver = new ChromeDriver(caps);

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
    private void runOrder(Map map,WebDriver driver) {
        String url="http://182.131.0.215:8080/";
        driver.get(url);
        String title=driver.getTitle();
        logger.info("请求标题：$title");

        WebElement btnEle=getEle(driver,"#kw111");
        if(btnEle!=null){
            map.put("description","订购成功");
            logger.info("userId:"+map.get("user_id")+",订购成功");
            String type=btnEle.getAttribute("type");
            logger.info("找到文本框type：$type");
            update(map);
        }else{
            logger.info("userId:"+map.get("user_id")+",订购失败");
            map.put("description","订购失败");
            update(map);
            putBlackUser(map);
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
            String sql="select * from iptv_order_user where description='未处理' and create_date>'$date' limit 100";
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
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            return map;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return false;
    }

    /**
     是否超过限制
     */
    public boolean overLimit(String date){
        boolean flag=false;
        if(flag){
            logger.info("date:$date,超过限制");
        }
        return false;
    }

    /**
     * 更新数据
     * @return
     */
    public boolean update(Map<String,String> map){
        JDBC.DbHelper  dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_id")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_type")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("product_id")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("create_date")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("description")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("id")));
            String sql="update iptv_order_user set user_id=?,user_type=?,product_id=?,create_date=?,description=? where id=?";
            int cnt=dh.executeUpdate(sql,parameters);
            if(cnt>0){
                logger.info("更新用户：$map.user_id 成功");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除数据
     * @return
     */
    public boolean delLog(Map<String,String> map){
        Date create_date=new java.util.Date();

        JDBC.DbHelper  dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("id")));
            String sql="delete from  iptv_order_user where id=?";
            int cnt=dh.executeUpdate(sql,parameters);
            if(cnt>0){
                logger.info("删除用户：$user_id 历史");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean putBlackUser(Map map){
        Date create_date=new java.util.Date();

        JDBC.DbHelper dh=JDBC.getDefaultNoTransactionDbHelper();
        List<SqlParameter> parameters=new ArrayList<>();
        try{
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_id")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("user_type")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("product_id")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("create_date")));
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("description")));
            String sql="insert into iptv_black_user(user_id,user_type,product_id,create_date,description)values";
            sql=sql+"(?,?,?,?,?)";
            int cnt=dh.executeUpdate(sql,parameters);
            if(cnt>0){
                logger.info("插入黑名单用户：$map.user_id ");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}