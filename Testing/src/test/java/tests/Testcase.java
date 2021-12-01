package tests;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

public class Testcase extends TestSupporter {
	
	@Test(enabled=false,groups= {"rate-price","priority1"},description="Test Case 1")
	public void testcase1() {
	System.out.println("test executed");
	test.log(Status.INFO, "Selecting Utility in the broker landing page",takeScreenshot());
	}

}
