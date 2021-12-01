package com.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;

import com.utils.TestUtils;

public class AnnotationTransformer implements IAnnotationTransformer{
	int count=0;

	/**
	 * 
	 * *Author		: Sathish
	 * Method name  : transform
	 * Return types : Nil
	 * Description  : Method to change the TestNG annotations on run time based on the value provide
	 */
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method Method) {
		
		annotation.setRetryAnalyzer(RetryFailedTestCases.class);
		try {
			if(count==0) {
				TestUtils.getRunStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<TestUtils.testcases.size();i++) {
			Test testMethod = Method.getAnnotation(Test.class);
			if(testMethod.description().equalsIgnoreCase(TestUtils.testcases.get(i)))
			{
				annotation.setEnabled(true); 
				break;
			} 
			
		}
		
		count++;
	}
}


