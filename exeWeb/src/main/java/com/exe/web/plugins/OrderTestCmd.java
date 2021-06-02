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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class OrderTestCmd extends Cmd{

    org.slf4j.Logger logger= org.slf4j.LoggerFactory.getLogger("order");
    //限制条数
    private int limit=100;
    //更新状态
    private boolean updateData=true;
    //截图
    private boolean cutImg=true;
    //调试
    private boolean test=true;

    public void exe(CmdVo caiAction, Map<String, String> param) {
        try{
            Map<String, Object> sys=cmdHandle.getSys();
            String op=param.get("op");
            WebDriver driver= getWebDriver(sys);
            if(test){
                String r=test(param,null,driver);
                logger.debug("执行结果："+r);
                return;
            }
            Date zuoTian=DateUtil.addDays(new Date(),-30);
            String date=DateUtil.format(zuoTian);
            logger.debug("task is start ");
            logger.debug("find data create_date> $date ");
            List<Map<String,String>> list=getNotHandle(date);
            if(list==null || list.isEmpty()){
                logger.debug("list is null");
                return;
            }
            if(overLimit(date)){
                return;
            }else{
                logger.debug("date:$date,未超过限制");
            }

            logger.debug("get web driver:$driver");
            for (Map map:list) {
                logger.debug("handle data:$map");
                Map<String,String> dataMap=StringUtil.getParaCollection(map.get("data").toString(),"&","=");
                logger.debug("handle dataMap:$dataMap");
                String userId=map.get("user_id").toString();
                if(inBlack(userId)){
                    logger.debug("userId:$userId,黑名单中");
                    map.put("description","订购失败-黑名单用户");
                    update(map);
                    continue;
                }else{
                    logger.debug("userId:$userId,不在黑名单中");
                }

                logger.debug("userId:$userId,start runOrder");
                String r=test(param,dataMap,driver);
                if(r.indexOf("success")==0){
                    map.put("description","订购成功-"+r);
                    logger.info("userId:"+map.get("user_id")+",订购成功-"+r);
                    update(map);
                }else if(r.indexOf("exp")==0){
                    map.put("description","订购异常-"+r);
                    logger.info("userId:"+map.get("user_id")+",订购异常-"+r);
                    update(map);
                }else{
                    logger.info("userId:"+map.get("user_id")+",订购失败-"+r);
                    map.put("description","订购失败-"+r);
                    update(map);
                    putBlackUser(map);
                }
            }
        }catch (Exception e){
            logger.error("",e);
        }finally {
            if(driver!=null){
                driver.quit();
            }
            driver=null;
            js=null;
        }

    }
    public void getCode(){

    }

    public String test(Map<String, String> param,Map<String,String> map,WebDriver driver){
        try{
            String userInfo=param.get("user");
            if(test){
//                userInfo="StbID=&STBType=TY1608&mac=c0%3A1b%3A23%3A32%3A71%3A94&userId=TVGB36.57212%2FPA4&token=4%3B%2F45%3B.8%2FGa61gMLV6XXmlXNAISmbh3%5B&jjsp_userID=NC2103093045@ITV&jjsp_userToken=f5YdkUGCLZjoVZ4XJOe34cE160972196&jjsp_key=3%3A2&jjsp_platform=1&jjsp_enCodeUserID=TVGB36.57212/PA4&jjsp_enCodeUserToken=4%3B/45%3B.8/Ga61gMLV6XXmlXNAISmbh3%5B&userAgent=webkit;Resolution(PAL,720P,1080P)&X-Requested-With=com.android.smart.terminal.iptv";
//                str="StbID=&STBType=B860AV2.1&mac=D4%3AC1%3AC8%3AAE%3A5F%3AB5&userId=TVGB36.6180RXJ%3F43&token=SlsliGOdd5kI4lhZmfukw5Nfsp%3FpsfNC&jjsp_userID=PZHA252634045@ITV&jjsp_userToken=AnudPAnudP3yiwdoXjj6Gm3fbQEkjujU&jjsp_key=6%3A2&jjsp_platform=2&jjsp_enCodeUserID=TVGB36.6180RXJ%3F43&jjsp_enCodeUserToken=SlsliGOdd5kI4lhZmfukw5Nfsp%3FpsfNC&userAgent=B700-V2A|Mozilla|5.0|ztebw(Chrome)|1.2.0;Resolution(PAL,720p,1080i) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7&X-Requested-With=null";
                userInfo="";
                map= StringUtil.getParaCollection(userInfo,"&","=");
            }
            String op=param.get("op");
            if(op=="code"){
                map= StringUtil.getParaCollection(userInfo,"&","=");
                logger.info("code---"+map);
            }else if(op=="ok"){
                map= StringUtil.getParaCollection(userInfo,"&","=");
            }
            putParam(map,driver);
            getConsoleLog();


            driver.get("http://182.131.0.215:8086/CQCA/page/specialfive1.html?seriesCode=17b0f56e57b04265a54f62b9d5d01784");
            logger.info("---------------step2. visit url");
//            getCutImg(driver,"testA");
            logger.info("---------------step2. exejs");
            JavascriptExecutor js= (JavascriptExecutor) driver;
            exejs(js,"",map.get("STBType"),map.get("mac"));
            getConsoleLog();
            logger.info("---------------step2. exejs end");
//            WebElement bodyEle=getEle(driver,"body");
//            logger.info("---------------step2. bodyEle："+bodyEle);
            sleep(1000l);
            logger.info("---------------step2:"+driver.getCurrentUrl());

            String jscmd="if(typeof(ok)!='undefined'){ok();return 'true';}else{return 'false';}";
            logger.info("---------------step2. js:"+jscmd);
            Object ok=js.executeScript(jscmd);
            logger.info("---------------step3. exe ok:"+ok);
            if("false".equalsIgnoreCase(ok.toString())){
                return "ok 不存在";
            }
            sleep(2000l);
            //http://110.190.90.140:8296/main/sichuan/ordernew/order_overall.action 订单选择页
//            if(true){
//                getCutImg(driver,"ok");
//                logger.info("---------------step4. ok");
//                logger.info("---------------4 url:"+driver.getCurrentUrl());
//                return "";
//            }
//            getCutImg(driver,"testB");
            //套餐选择页面
            //http://110.190.90.140:8296/main/sichuan/ordercheck/order_overall.action
//            logger.info("---------------8 url:"+driver.getCurrentUrl());
//            logger.info("---------------8 title:"+driver.getTitle());
//            System.out.println("---------------8 url:"+driver.getPageSource());

            if(driver.getCurrentUrl().indexOf("/main/sichuan/ordernew/thirdPayOrder.action")!=-1){
                logger.info("-----------三方支付用户----url1. "+driver.getCurrentUrl());
                return "三方支付用户";
            }
            if(driver.getCurrentUrl().indexOf("/main/sichuan/ordercheck/order_overall.action")==-1){
                logger.info("---------------url1. "+driver.getCurrentUrl());
                getCutImg(driver,"order_overall");
                return "非目标用户";
            }
            Object ok4=js.executeScript("mac='"+map.get("mac")+"';");
            if("false".equalsIgnoreCase(ok4.toString())){
                return "mac set error :";
            }
            Object ok0=js.executeScript("if(typeof(order)!='undefined'){order();return 'true';}else{return 'false';}");
            logger.debug("---------------8 ok:"+ok0);
            if("false".equalsIgnoreCase(ok0.toString())){
                return "order 不存在";
            }

            sleep(1000l);
            //非影视全能包页面
            //http://110.190.90.140:8296/main/sichuan/ordernew/order_intial.action
            if(true){

                if(driver.getCurrentUrl().indexOf("/main/sichuan/ordercheck/sms_Verify.action")!=-1){
                    logger.info("-----------短信认证----url. "+driver.getCurrentUrl());
                    return "非目标用户-短信认证";
                }
                if(driver.getCurrentUrl().indexOf("/main/sichuan/ordercheck/chooseCheckMethod.action")!=-1){
                    logger.info("-----------选择认证方式----url2. "+driver.getCurrentUrl());
                    return "非目标用户-选择认证方式";
                }

                if(driver.getCurrentUrl().indexOf("/main/sichuan/ordernew/order_intial.action")==-1){
                    logger.info("---------------url3. "+driver.getCurrentUrl());
                    getCutImg(driver,"order_intial");
                    return "非目标用户1";
                }
//                getCutImg(driver,"order");
//                logger.info("---------------order:"+driver.getCurrentUrl());
//                System.out.println("--------------- order:"+driver.getPageSource());

//                js.executeScript("if(typeof(order)!='undefined'){order();return 'true';}else{return 'false';}");
//                sleep(1000l);
//                getCutImg(driver,"order1");

                return "success";
            }
            logger.info("---------------9 ok:"+ok0);
            //支付方式选择 密码支付
            //http://110.190.90.140:8296/main/sichuan/ordercheck/chooseCheckMethod.action
            //短信验证
            //http://110.190.90.140:8296/main/sichuan/ordercheck/sms_Verify.action
            logger.info("---------------9 url:"+driver.getCurrentUrl());
            logger.info("---------------9 title:"+driver.getTitle());
//            System.out.println("---------------9 url:"+driver.getPageSource());
            if(op=="code"){
                boolean pwdV=driver.getCurrentUrl().indexOf("/main/sichuan/ordercheck/sms_Verify.action")!=-1;
                logger.info("---------------9 pwdV:"+pwdV);
                if(pwdV){
                    WebElement phoneNo=getEle(driver,"#phoneNo");
                    String tel=param.get("tel");
                    phoneNo.sendKeys(tel);
                    WebElement code=getEle(driver,"#code");
                    js.executeScript("getSMS()");
                    sleep(1000l);
                    getCutImg(driver,"code");
                    return "";
                }
            }else if(op=="ok"){
                //确认
                boolean pwdV=driver.getCurrentUrl().indexOf("/main/sichuan/ordercheck/sms_Verify.action")!=-1;
                logger.info("---------------9 pwdV:"+pwdV);
                if(pwdV){
                    WebElement phoneNo=getEle(driver,"#phoneNo");
                    String tel=param.get("tel");
                    phoneNo.sendKeys(tel);
                    WebElement codeEle=getEle(driver,"#code");
                    String code=param.get("code");
                    codeEle.sendKeys(code);
                    js.executeScript("validatePassword()");
                    logger.info("---------------9 3");
                    sleep(1000l);
                    getCutImg(driver,"ok");
                    return "";
                }
            }

            js.executeScript("logUserInfo.mac='"+map.get("mac")+"';");
            Object ok5=js.executeScript("mac='"+map.get("mac")+"';");
            if("false".equalsIgnoreCase(ok5.toString())){
                return "mac set error :";
            }
            Object ok1=js.executeScript("if(typeof(passwordVerify)!='undefined'){passwordVerify();return 'true';}else{return 'false';}");
            if("false".equalsIgnoreCase(ok1.toString())){
                return "passwordVerify 不存在";
            }
            List<String> pwds=new ArrayList<>();
            pwds.add("666666");
            pwds.add("123456");
            pwds.add("111111");
            pwds.add("1234");
            pwds.add("000000");
            pwds.add("1111");
            for(String pwd:pwds){
                logger.info("---------------9 test pwd:"+pwd);
                Object ok6=js.executeScript("mac='"+map.get("mac")+"';");
                if("false".equalsIgnoreCase(ok6.toString())){
                    return "mac set error :";
                }
                WebElement password=getEle(driver,"#password");
                password.sendKeys(pwd);
                WebElement pwdErr=getEle(driver,"#phone_error_div");
                logger.info("---------------10 display1:"+pwdErr.getCssValue("display"));
//                if(true){
//                    return "success";
//                }

                Object ok2=js.executeScript("if(typeof(validatePassword)!='undefined'){validatePassword();return 'true';}else{return 'false';}");
                if("false".equalsIgnoreCase(ok2.toString())){
                    return "validatePassword 不存在";
                }
                sleep(1000l);
                logger.info("---------------10 ok:"+ok2);
                //支付页面 密码支付
                //http://110.190.90.140:8296/main/sichuan/ordercheck/password_Verify.action?mac=undefined
                logger.info("---------------10 url:"+driver.getCurrentUrl());
                logger.info("---------------10 title:"+driver.getTitle());
//            System.out.println("---------------10 url:"+driver.getPageSource());
                pwdErr=getEle(driver,"#phone_error_div");
                String display=pwdErr.getCssValue("display");
                logger.info("---------------10 display2:"+display);
                if("block".equalsIgnoreCase(display)){
//                closePasswordErrorDiv()
//                    getCutImg(driver,"testD");
                    //关闭错误提示
                    Object ok3=js.executeScript("if(typeof(closePasswordErrorDiv)!='undefined'){closePasswordErrorDiv();return 'true';}else{return 'false';}");
//                    System.out.println("---------------10 url:"+driver.getPageSource());
                    continue;
                }else if("none".equalsIgnoreCase(display)){
                    logger.info("---------------10 订购成功:"+pwd);
                    if(op=="code"){

                            WebElement phoneNo=getEle(driver,"#phoneNo");
                            String tel=param.get("tel");
                            phoneNo.sendKeys(tel);
                            WebElement code=getEle(driver,"#code");
                            js.executeScript("getSMS()");
                            sleep(1000l);
                            getCutImg(driver,"code");
                            return "";
                    }else if(op=="ok"){
                        //确认
                        WebElement phoneNo=getEle(driver,"#phoneNo");
                        String tel=param.get("tel");
                        phoneNo.sendKeys(tel);
                        WebElement codeEle=getEle(driver,"#code");
                        String code=param.get("code");
                        codeEle.sendKeys(code);
                        js.executeScript("validatePassword()");
                        sleep(1000l);
                        getCutImg(driver,"ok");


                        js.executeScript("thinkAgain()");
                        sleep(1000l);
                        getCutImg(driver,"thinkAgain");

                        js.executeScript("order()");
                        sleep(1000l);
                        getCutImg(driver,"order");

//                            System.out.println("---------------10 url:"+driver.getPageSource());
//                            js.executeScript("agree()");
//                            sleep(1000l);
//                            getCutImg(driver,"agree");
                            return "";
                    }
//                    getCutImg(driver,"testF");
//                    logger.info("---------------10 订购成功title:"+driver.getTitle());
//                    WebElement phoneNo=getEle(driver,"#phoneNo");
//                    phoneNo.sendKeys("18723385113");
//                    WebElement code=getEle(driver,"#code");
//                    code.sendKeys("1308");
////                    WebElement code=getEle(driver,"#phone_set_ok_a");
////                    code.click();
////                    js.executeScript("getSMS()");
//                    js.executeScript("validatePassword()");
//                    sleep(1000l);
//                    js.executeScript("agree()");
//                    sleep(1000l);
//                    getCutImg(driver,"testE");
////                    printCookie(driver);
//                    System.out.println("---------------10 url:"+driver.getPageSource());
                    return "success"+"-"+pwd;
                }
//                getCutImg(driver,"testE");
            }
        }catch (Exception e){
            logger.error("",e);
            e.printStackTrace();
            return "exp-"+e.getMessage();
        }
        getConsoleLog();
        return "pwd Error";
    }

    private String getJsCode(WebDriver driver) {
        if(false){
            String url="http://110.190.90.140:8296/js/staryea.js";
            driver.get(url);
            logger.info("---------------step1."+ driver.getPageSource());
            url="http://110.190.90.140:8296/js/common/filelog/common.js";
            driver.get(url);
            logger.info("---------------step2."+ driver.getPageSource());
            url="http://110.190.90.140:8296/js/common/filelog/sc_order.js";
            driver.get(url);
            logger.info("---------------step3."+ driver.getPageSource());
            url="http://110.190.90.140:8296/resource/js/common_village.js";
            driver.get(url);
            logger.info("---------------step4."+ driver.getPageSource());
            return "";
        }
        return null;
    }

    public void printCookie(WebDriver driver){
        Set<Cookie> sets= driver.manage().getCookies();
        Iterator it = sets.iterator();
        while (it.hasNext()) {
            Cookie cookie1 = (Cookie) it.next();
            logger.info("---------------1.cookie,"+cookie1.getName()+",val:"+cookie1.getValue());
        }
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
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String root=MyConfUtil.getRootPath();
            File targetFile=new File(root+"/static/logs/"+name+c+".png");
            FileUtil.copy(srcFile,targetFile);
            c++;
        } catch (Exception e) {
            logger.error("",e);
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
            logger.error("",e);
        }
    }
    private  WebDriver driver=null;
    private  JavascriptExecutor js=null;
    private WebDriver getWebDriver(Map<String, Object> sys){
        if(driver!=null){
            try{
                js.executeScript("console.log('test')");
            }catch (Exception e){
                logger.error("",e);
                driver=null;
            }
        }
        if(driver==null){
            System.setProperty("webdriver.chrome.driver",
                    "/usr/bin/chromedriver");

            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("disable-debugbars");
            opt.addArguments("--no-sandbox");
            opt.addArguments("--headless");
            opt.addArguments("--disable-gpu");
            opt.addArguments("--disable-dev-shm-usage");
            opt.addArguments("X-Requested-With:com.android.smart.terminal.iptv");
            opt.addArguments("User-Agent:webkit;Resolution(PAL,720P,1080P)");
            String root=MyConfUtil.getRootPath()+"/conf";
            //添加插件
//            opt.addExtensions(new File(root+"/test.crx"));
//            TODO
            opt.setExperimentalOption("excludeSwitches",
                    Arrays.asList("enable-automation"));

            //为了获取console的日志输出
            DesiredCapabilities caps = DesiredCapabilities.chrome();
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, Level.INFO);//输入为debug的日志
            caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
            caps.setCapability(ChromeOptions.CAPABILITY, opt);


            driver = new ChromeDriver(caps);
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
            sql="select * from iptv_order_user where description='未处理' and create_date>'$date' order by id asc limit "+limit;
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
     是否超过限制
     */
    public boolean overLimit(String date){
        boolean flag=false;
        if(flag){
            logger.debug("date:$date,超过限制");
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
            parameters.add(new SqlParameter(Types.VARCHAR,map.get("create_date")));
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