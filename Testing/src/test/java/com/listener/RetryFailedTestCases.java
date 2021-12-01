package com.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import support.Configurations;


/**
 * 
 * *Author		: Sathish
 * Method name  : RetryFailedTestCases
 * Return types : Nil
 * Description  : method to retry failed  cases. No of retries depends on parameter from properties file
 */

public class RetryFailedTestCases implements IRetryAnalyzer {

	int counter = 0;
	Configurations config= new Configurations();
	int limit = Integer.parseInt(config.getProperty("NoOfRetries"));

	@Override
	public boolean retry(ITestResult result) {
		if (counter < limit) {
			counter++;
			return true;
		}
		return false;
	}

}