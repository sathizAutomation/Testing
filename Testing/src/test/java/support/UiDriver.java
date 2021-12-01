package support;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * File name   :UIDriver.java
 * Description  : 
 * Date created :13 Sep 2021
 * Author 		:Sathish
 */
public class UiDriver {
    Configurations configurations = Configurations.getInstance();
    WebDriver wDriver;
    EventFiringWebDriver eventDriver;

    public DesiredCapabilities capability;
    /**
     * 
     * Method name  : getDriver
     * Return types : WebDriver
     * Description  :The method that returns the various driver instance based on the parameter
     */

    public WebDriver getDriver(String driver) {

        if (configurations.getProperty("RunMode").equalsIgnoreCase("local")) {
            startBrowserForLocal(driver);
        } else if (configurations.getProperty("RunMode").equalsIgnoreCase("Remote")) {
            if (configurations.getProperty("RemoteMachine").equalsIgnoreCase("BrowserStack")) {
                startBrowserStack(driver);
            } else if (configurations.getProperty("RemoteMachine").equalsIgnoreCase("Docker")) {
                startBrowserDocker(driver);
            }
        } else {
            try {
                throw new Exception("Please set up the run mode properly in Configurations.properties");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wDriver;
    }


    public WebDriver startBrowserForLocal(String driver) {

        try {

            if (driver.equalsIgnoreCase("Firefox")) {
                String geckoDriver = Settings.getInstance().getDriverEXEDir() + "geckodriver.exe";
                System.setProperty("webdriver.gecko.driver", geckoDriver);
                wDriver = new FirefoxDriver();
                wDriver.manage().window().maximize();
                return wDriver;
            } else if (driver.equalsIgnoreCase("Google Chrome")) {
                WebDriverManager.chromedriver().setup();
                ChromeOptions option = new ChromeOptions();
                option.addArguments("--dns-prefetch-disable");
                option.addArguments("--start-maximized");
                option.addArguments("--disable-extensions");
                wDriver = new ChromeDriver(option);
                return wDriver;
            } else if (driver.equalsIgnoreCase("Edge")) {
                WebDriverManager.edgedriver().setup();
                wDriver = new EdgeDriver();
                return wDriver;
            } else if (driver.equalsIgnoreCase("Safari")) {
                //Yet to implement
            } else {
                WebDriverManager.chromedriver().setup();
                ChromeOptions option = new ChromeOptions();
                option.addArguments("--dns-prefetch-disable");
                option.addArguments("--start-maximized");
                option.addArguments("--disable-extensions");
                wDriver = new ChromeDriver(option);
                return wDriver;
            }

        } catch (Exception e) {
            e.printStackTrace();
            WebDriverManager.chromedriver().setup();
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--dns-prefetch-disable");
            option.addArguments("--start-maximized");
            option.addArguments("--disable-extensions");
            wDriver = new ChromeDriver(option);
            return wDriver;
        }
        return wDriver;
    }

    private void startBrowserDocker(String browser) {

        try {
            switch (browser) {
                case "Google Chrome":
                    capability = DesiredCapabilities.chrome();
                    capability.setBrowserName("chrome");
                    capability.setPlatform(Platform.ANY);
                    wDriver = new RemoteWebDriver(new URL(configurations.getProperty("RemoteURL")), capability);
                    wDriver.manage().window().maximize();

                    break;
                case "firefox":
                    capability = DesiredCapabilities.firefox();
                    capability.setBrowserName("firefox");
                    capability.setPlatform(Platform.ANY);
                    wDriver = new RemoteWebDriver(new URL(configurations.getProperty("RemoteURL")), capability);
                    wDriver.manage().window().maximize();
                    break;
                default:
                    capability = DesiredCapabilities.chrome();
                    capability.setBrowserName("chrome");
                    capability.setPlatform(Platform.ANY);
                    wDriver = new RemoteWebDriver(new URL(configurations.getProperty("RemoteURL")), capability);
                    wDriver.manage().window().maximize();
                    break;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startBrowserStack(String driver) {
        final String AUTOMATE_USERNAME = configurations.getProperty("BrowserStack_UserName");
        final String AUTOMATE_ACCESS_KEY = configurations.getProperty("BrowserStack_AccessKey");
        final String BrowserstackURL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("os_version", "10");
            caps.setCapability("resolution", "1920x1080");
            caps.setCapability("browser", "Chrome");
            caps.setCapability("browser_version", "latest");
            caps.setCapability("os", "Windows");
            caps.setCapability("name", "BStack-[Java] Sample Test"); // test name
            caps.setCapability("build", "BStack Build Number 1"); // CI/CD job or build name
            wDriver = new RemoteWebDriver(new URL(BrowserstackURL), caps);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 
     * Method name  : quitDriver
     * Return types : boolean
     * Description  : This method is to quit the driver instance after completing a test
     */

    public boolean quitDriver(WebDriver wDriver) {
        try {
            wDriver.quit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}