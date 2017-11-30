package com.cbt.docsapp.web.tests.Module2;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cbt.docsapp.util.BaseClass_Android;
import com.cbt.docsapp.util.ExecuteMapping_AndroidTestcases;

public class TC_2 extends BaseClass_Android{

	@Test(description = "Add description here")
	@Parameters({"Device Name"})
	public void TC_2(String DeviceName) throws Exception
	{
		// Write code for adding reports or logs
		System.out.println("In test");
		System.out.println("Test case parameters " + DeviceName);
		ExecuteMapping_AndroidTestcases eq = new ExecuteMapping_AndroidTestcases(aDriver, "android", "Module1", "TC_2");
		eq.executeAction();
	}
}
