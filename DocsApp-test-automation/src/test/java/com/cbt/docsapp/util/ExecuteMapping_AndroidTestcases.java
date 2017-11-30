package com.cbt.docsapp.util;

import io.appium.java_client.android.AndroidDriver;

public class ExecuteMapping_AndroidTestcases {
	
	AndroidDriver driver;
	String ModuleName;
	String testCaseName;
	String executionType;
	
	public ExecuteMapping_AndroidTestcases(AndroidDriver driver, String executionType,String ModuleName, String testCaseName)
	{
		this.driver = driver;
		this.ModuleName = ModuleName;
		this.testCaseName = testCaseName;
		this.executionType = executionType;
	}
	
	public void executeAction() throws Exception
	{
		System.out.println(executionType +" "+ ModuleName + " " + testCaseName);
		
		Execution_Android e = new Execution_Android(driver);
		//e.getTestStepInfo(bankName, flow, testCaseName, driver);
		e.getTestStepInfo(executionType, ModuleName, testCaseName);
	}
	
}
