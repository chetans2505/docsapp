package com.cbt.docsapp.util;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.kirwa.nxgreport.NXGReports;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

public class BaseClass_Web {

	protected WebDriver driver = null;

	@Parameters("BrowserName")
	@BeforeClass
	public void browserLaunch(String browser)
	{
		if(browser.equalsIgnoreCase("Firefox"))
		{
			System.out.println("......."+browser+"..........");
			//FirefoxDriverManager.getInstance().version("0.17.0").setup();
			/*DesiredCapabilities caps=new DesiredCapabilities();
			caps.setCapability(FirefoxDriver.MARIONETTE, false);*/
			System.setProperty("webdriver.gecko.driver", "./resources/exe/geckodriver.exe");
			driver=new FirefoxDriver();
			System.out.println("....Firefox browser instantiated......");
		}
		else if(browser.equalsIgnoreCase("Chrome"))
		{
			System.out.println("......."+browser+"..........");
			ChromeDriverManager.getInstance().setup();
			driver=new ChromeDriver();
			System.out.println("....Chrome browser instantiated......");
		}
		else{
			System.out.println("......."+browser+"..........");
			InternetExplorerDriverManager.getInstance().setup();
			driver=new InternetExplorerDriver();
			System.out.println("....Internet Explorer browser instantiated......");
		}
		driver.get("https://www.zoomcar.com");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		NXGReports.setWebDriver(driver);
	}
	
	@AfterClass
	public void tearDown(){
		driver.close();
	}
	
}
