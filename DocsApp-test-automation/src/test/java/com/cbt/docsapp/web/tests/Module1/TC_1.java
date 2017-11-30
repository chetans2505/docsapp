package com.cbt.docsapp.web.tests.Module1;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cbt.docsapp.util.BaseClass_Web;
import com.cbt.docsapp.util.ExecuteMapping_WebTestcases;

public class TC_1 extends BaseClass_Web{
	
	@Parameters("BrowserName")
	@Test(description = "Add description here")
	public void tc_1(String browserName)
	{
		// Write code for adding reports or logs
		System.out.println("In test");
		System.out.println("Test case parameters "+browserName);
		
		
		ExecuteMapping_WebTestcases eq = new ExecuteMapping_WebTestcases(driver, "web", "Module1", "TC_1");
		eq.executeAction();
	}
	
	
}
