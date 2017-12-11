package com.cbt.docsapp.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.cbt.docsapp.generics.GenericLib;
import com.kirwa.nxgreport.NXGReports;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class BaseClass_Android {

	private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;
	protected AndroidDriver aDriver = null;
	int port = 0;
	int count = 1;

	@Parameters({"port","Device UDID","Device Version","Device Name"})
	@BeforeTest
	public void startAppiumServer(int port,String UDID, String version,String deviceName) {
		System.out.println("------------------" + port);
		this.port = port;
		builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(port);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		//builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		//builder.withLogFile(new File("./logs.log"));
		// Start the server with the builder
		service = AppiumDriverLocalService.buildService(builder);
		service.start();
		System.out.println("appium started on " + port + " port");
		/*try {
			launchApp(port,UDID,version,deviceName);
		} catch (MalformedURLException | InterruptedException e) {
			System.out.println("unable to launch application");
			e.printStackTrace();
		}*/
		System.out.println("Before test");
	}

	public void launchApp(int port,String UDID, String version,String deviceName) throws MalformedURLException, InterruptedException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,deviceName);
		capabilities.setCapability(MobileCapabilityType.UDID, UDID);
		capabilities.setCapability("app", new File(GenericLib.sDirPath + "/DocsApp_com.docsapp.patients.apk").getAbsolutePath());
		capabilities.setCapability("appPackage", "com.docsapp.patients");
		capabilities.setCapability("appActivity", "com.docsapp.patients.app.MainActivity");
		capabilities.setCapability("fullReset", true);
		capabilities.setCapability("noReset", false);
		capabilities.setCapability("autoGrantPermissions", "true");
		capabilities.setCapability("autoAcceptAlerts", "true");
		capabilities.setCapability("newCommandTimeout", 60000);
		aDriver = new AndroidDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
		aDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		NXGReports.setWebDriver(aDriver);
		System.out.println("----------------appium driver initialised");
		
	}
	
	@Parameters({"port","Device UDID","Device Version","Device Name"})
	@BeforeClass
	public void launch(int port,String UDID, String version,String deviceName)
	{
		try {
			launchApp(port,UDID,version,deviceName);
		} catch (MalformedURLException | InterruptedException e) {
			System.out.println("unable to launch application");
			e.printStackTrace();
		}
	}
	

	@AfterClass
	public void resrtApp() {
		//aDriver.resetApp();
		aDriver.closeApp();
		/*String reportsFolderPath = System.getProperty("user.dir");
		System.out.println("sys path:"+reportsFolderPath);
		ChromeDriver drive=new ChromeDriver();
		
		System.setProperty("webdriver.chrome.driver", reportsFolderPath+"\\"+"resources\\exe\\chromedriver.exe");
		drive.get(reportsFolderPath+"\\"+"Reports\\HTMLReports\\index.html");*/
	}
	
	
	@AfterSuite
	public void stopAppiumServer() {
		service.stop();
		aDriver.quit();
		
		
		
		System.out.println("appium server stopped" );
		System.out.println("After Suite");
	}

}
