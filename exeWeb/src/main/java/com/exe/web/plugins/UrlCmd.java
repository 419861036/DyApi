package com.exe.web.plugins;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;
import com.exe.web.util.MyConfUtil;
import kkd.common.util.FileUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class UrlCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(OrderCmd.class);

    @Override
    public void exe(CmdVo cmd, Map<String, String> param) {
        logger.info("执行-----------------start");
        Map<String, Object> sys=cmdHandle.getSys();
        WebDriver driver= getWebDriver(sys);
        String op=param.get("op");
        if("quit".equalsIgnoreCase(op)){
            if(driver!=null){
                driver.quit();
            }
            driver=null;
            js=null;
        }else if("get".equalsIgnoreCase(op)){
            String url=param.get("val");
            driver.get(url);
            sleep(1000l);
            getCutImg(driver,"test");
        }else if("click".equalsIgnoreCase(op)){
            String ele=param.get("ele");
            WebElement webELe=getEle(driver,ele);
            webELe.click();
            sleep(1000l);
            getCutImg(driver,"test");
        }else if("input".equalsIgnoreCase(op)){
            String ele=param.get("ele");
            String val=param.get("val");
            WebElement webELe=getEle(driver,ele);
            webELe.sendKeys(val);
            sleep(1000l);
            getCutImg(driver,"test");
        }

    }


    public void sleep(Long l){
        try {
            Thread.sleep(l);
        }catch (Exception e){

        }
    }

    public WebElement getEle(WebDriver driver, String cssSelector){
        try {
            return  driver.findElement(By.cssSelector(cssSelector));
        }catch (Exception e){
            return null;
        }
    }

    public void getCutImg(WebDriver driver,String name){
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String root=MyConfUtil.getRootPath();
            File targetFile=new File(root+"/static/logs/"+name+".png");
            FileUtil.copy(srcFile,targetFile);
        } catch (Exception e) {
            logger.error("",e);
        }
    }
    private static WebDriver driver=null;
    private static JavascriptExecutor js=null;
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
            String root= MyConfUtil.getRootPath()+"/conf";
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


    @Override
    public String op() {
        return null;
    }
}
