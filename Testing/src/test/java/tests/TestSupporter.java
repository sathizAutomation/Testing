package tests;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import com.paulhammant.ngwebdriver.NgWebDriver;
import com.utils.TestUtils;

//import atu.alm.wrapper.enums.StatusAs;

//import support.ALMConnection;
import support.Configurations;
import support.DBConnection;
import support.Log;
import support.DataReader;
import support.DataReaderEXCEL;
import support.DataReaderXML;
import support.Report;
import support.Settings;
import support.UiDriver;
import support.Utility;


public class TestSupporter {
    protected Settings settings = Settings.getInstance();
    String dataSourcePath = settings.getDataSource();
    String dataXmlSourcePath = settings.getXMLDataSource();
    String driversPath = settings.getDriverEXEDir();
    String screenshotsPath = settings.getScreenshotsDir();
    String Time;
    Configurations configurations = Configurations.getInstance();
    protected Report report = Report.getInstance();
    protected Utility appData = Utility.getInstance();
    protected UiDriver uidriver = new UiDriver();
    protected DBConnection dbConnection = new DBConnection();
    protected DataReader data;
    protected WebDriver driver;
    NgWebDriver ngDriver;
    protected ExtentTest test;
    protected Faker testData = new Faker();
    protected Configurations configurationsfile;
    protected Log log;
    protected TestUtils testutils;

    @BeforeSuite
    public void clearScreenshots() {

        try {
            FileUtils.cleanDirectory(new File(settings.getScreenshotsDir()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method name  : setUpTest
     * Return types : void
     * Description  : Set up the tests
     */

    @Parameters({
        "Browser",
        "URL",
        "Datasource",
        "SheetName"
    })
    @BeforeMethod(alwaysRun = true)
    public void initializeBrowser(@Optional("Google Chrome") String browserName, String url, String datasource, String sheetName, Method method, ITestContext context) {
        String dataSource = configurations.getProperty("DataSource");
        String testId;
        String testName;
        Test testMethod = method.getAnnotation(Test.class);
        if (testMethod.description().length() > 0) {
            testName = testMethod.description().trim();
        } else {
            testName = "Test description is not given";
        }

        if (dataSource.equalsIgnoreCase("excel")) {
            System.out.println(dataSourcePath + datasource);
            data = new DataReaderEXCEL(dataSourcePath + datasource, sheetName, testName);
            testId = data.get("Test_ID");
        } else {
            data = new DataReaderXML(dataXmlSourcePath + "SearchCustomerTests.xml", testName);
            testId = data.getTestId();
        }


        System.out.println("Test Running :" + testName);

        String testDescription = context.getCurrentXmlTest().getName();
        test = report.createNewReport(testId, testDescription + " - " + testName);

        //Instantiate the driver instance
        driver = uidriver.getDriver(browserName);

        //Initialize the pages	
        configurationsfile = PageFactory.initElements(driver, Configurations.class);
        testutils = PageFactory.initElements(driver, TestUtils.class);
        log = PageFactory.initElements(driver, Log.class);

        //browser initial set ups
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(600, TimeUnit.SECONDS);
        driver.get(url);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Time = formatter.format(new Date());

    }

    /**
     * 
     * Method Name : cleanUp
     * Return Type : void
     * Author      : Sathish
     * Date		   : Sep 29, 2021
     * Description : 
     * @param result
     * @param method
     */
    @AfterMethod(alwaysRun = true)
    public void cleanUp(ITestResult result,Method method) {
		String testName;

		Test testMethod = method.getAnnotation(Test.class);
		if (testMethod.description().length()>0) {
			testName = testMethod.description().trim();

		}else {
			testName = "Test description is not given";
		}
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				test.log(Status.FAIL, "Exception occurred in the test -  Error Detail : "+result.getThrowable().getMessage(),takeScreenshot());
				//updateALMStatus(testName,StatusAs.FAILED);
				if(configurationsfile.getProperty("updateqTest").equalsIgnoreCase("True")) {
				//qtestConnection.submitAutomationTestLog(testName,"Failed");
				}
				testutils.updateStatusinExcel(testName, "Failed",Time);
			}
			if(result.getStatus() == ITestResult.SUCCESS)
			{
				//updateALMStatus(testName,StatusAs.PASSED);
				if(configurationsfile.getProperty("updateqTest").equalsIgnoreCase("True")) {
				//qtestConnection.submitAutomationTestLog(testName,"passed");
				}
				testutils.updateStatusinExcel(testName, "Passed",Time);
			}
			//Close and quite driver instance
			driver.quit();
		}catch(Exception e) {
			e.printStackTrace();
			test.log(Status.FAIL, "Exception occurred in the test -  Error Detail : "+result.getThrowable().getMessage(),takeScreenshot());
			//updateALMStatus(testName,StatusAs.FAILED);
		}
		//Close report
		report.closeReport();
	}

    /**
	 * 
	 * @param testName
	 * @param status
	
	private void updateALMStatus(String testName,StatusAs status) {
		if(configurations.getProperty("UpdateALM").equalsIgnoreCase("yes")) {
			ALMConnection alm = ALMConnection.getInstance();
			alm.updateResultsInALM(testName, status);	
		}
	}
 */

    /**
     * 
     * Method Name : captureScreen
     * Return Type : String
     * Author      : Sathish
     * Date		   : Sep 29, 2021
     * Description : 
     * @return
     */
    public String captureScreen() {
        try {
            Random rand = new Random();
            int randomNo = rand.nextInt(100000000) + 1;
            //WebDriver augmentedDriver = new Augmenter().augment(driver); 
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenShotName = screenshotsPath + "screenshot_" + randomNo + ".png";
            String relativeScreenShotName = "screenshots\\screenshot_" + randomNo + ".png";
            FileUtils.copyFile(source, new File(screenShotName));
            return relativeScreenShotName;
        } catch (Exception e) {
            return "ERROR_IN_SCREEN_CAPTURE";
        }
    }

    /**
     * 
     * Method Name : takeScreenshot
     * Return Type : MediaEntityModelProvider
     * Author      : Sathish
     * Date		   : Sep 29, 2021
     * Description : 
     * @return
     */
    public MediaEntityModelProvider takeScreenshot() {
        try {
            return MediaEntityBuilder.createScreenCaptureFromPath(captureScreen()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * Method Name : takeExecutionHistory
     * Return Type : void
     * Author      : Sathish
     * Date		   : Sep 29, 2021
     * Description :
     */

    @AfterSuite(alwaysRun = true)
    public void testsTearDown() {
        String reportPath = settings.getProjectPath();
        String history = System.getProperty("user.dir") + System.getProperty("file.separator") + "History" + System.getProperty("file.separator");
        new File("history").mkdirs();
        File historyDir = new File(history);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains("Execution_");
            }
        };
        String[] resultsDir = historyDir.list(filter);
        int executionDirIndex = resultsDir.length + 1;
        String executionDir = history + System.getProperty("file.separator") + "Execution_" + executionDirIndex;
        String screenshots = history + System.getProperty("file.separator") + "Execution_" + executionDirIndex + System.getProperty("file.separator") + "screenshots";
        new File(executionDir).mkdirs();
        try {
            FileUtils.copyFileToDirectory(new File(reportPath + System.getProperty("file.separator") + "TestSummaryReport.html"), new File(executionDir), false);
            FileUtils.copyDirectory(new File(reportPath + System.getProperty("file.separator") + "screenshots"), new File(screenshots), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if(configurations.getProperty("UpdateALM").equalsIgnoreCase("yes")) {
        //	ALMConnection.getInstance().closeALMConnectionInstance();
        //}
    }

    /**
     * 
     * @param length
     * @return
     */
    public String randomNumber(int length) {
        Random rand = new Random();
        String randomNo = "";
        for (int len = 1; len <= length; len++) {
            randomNo = randomNo + (rand.nextInt(9) + 1);
            if (randomNo.length() == length) {
                return randomNo;
            }
        }
        return "NOT_RANDOM";
    }

    /**
     * 
     * Method Name : switchFrame
     * Description : To switch frame
     * Author      : Sathish
     * Return Types: void
     * Parameters  : 
     * Date        : Nov 06, 2021
     * Version     : 1.0
     */
    public void switchFrame() {
        WebElement ele = driver.findElements(By.tagName("iframe")).get(0);
        driver.switchTo().frame(ele);
    }

    /**
     * 
     * Method Name : switchToDefault
     * Description : To switch To Default frame
     * Author      : Sathish
     * Return Types: void
     * Parameters  : 
     * Date        : Nov 06, 2021
     * Version     : 1.0
     */
    public void switchToDefault() {
        driver.switchTo().defaultContent();
    }

    /**
     * 
     * Method Name : sleepFor5Mins
     * Description : To sleepFor5Mins
     * Author      : Sathish
     * Return Types: void
     * Parameters  : 
     * Date        : Nov 08, 2021
     * Version     : 1.0
     */
    public void sleepFor5Mins() {
        try {
            Thread.sleep(300000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}