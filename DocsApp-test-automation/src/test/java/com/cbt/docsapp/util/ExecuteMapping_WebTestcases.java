package com.cbt.docsapp.util;

import org.openqa.selenium.WebDriver;

public class ExecuteMapping_WebTestcases {

	WebDriver driver;
	String ModuleName;
	String testCaseName;
	String executionType;
	
	public ExecuteMapping_WebTestcases(WebDriver driver,String executionType,String ModuleName, String testCaseName)
	{
		this.driver = driver;
		this.ModuleName = ModuleName;
		this.testCaseName = testCaseName;
		this.executionType = executionType;
	}
	public void executeAction()
	{
		
		System.out.println(executionType +" "+ ModuleName + " " + testCaseName);
		
		Execution_Web e = new Execution_Web(driver);
		//e.getTestStepInfo(bankName, flow, testCaseName, driver);
		e.getTestStepInfo(executionType, ModuleName, testCaseName);
	}
	
	
}
