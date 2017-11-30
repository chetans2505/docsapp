package com.cbt.docsapp.util;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.cbt.docsapp.generics.ExcelLibrary;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.kirwa.nxgreport.NXGReports;
import com.kirwa.nxgreport.logging.LogAs;
import com.kirwa.nxgreport.selenium.reports.CaptureScreen;
import com.kirwa.nxgreport.selenium.reports.CaptureScreen.ScreenshotOf;

public class Execution_Web {

	WebDriver driver;

	public Execution_Web(WebDriver driver) {
		this.driver = driver;
	}

	public void getTestStepInfo(String executionType, String moduleName, String testCaseName) {
		try
		{	
			String testscriptspath = "./test-scripts/"+executionType+"/"+moduleName+".xlsx";
			String testObjectMappath = "./object_repo/"+executionType+"/"+moduleName+".json";

			System.out.println(testscriptspath);
			int testRowCount = ExcelLibrary.getExcelRowCount(testscriptspath, testCaseName);

			List<String> testHeader = new ArrayList<String>();
			Map<Integer, Map<String, String>> stepInfo = new LinkedHashMap<Integer, Map<String,String>>();
			int noOfTestHeadCells = ExcelLibrary.getExcelCellCount(testscriptspath, testCaseName, 1);
			for (int i = 0; i < noOfTestHeadCells; i++) 
			{
				testHeader.add(ExcelLibrary.getExcelData(testscriptspath, testCaseName, 0, i));
				//System.out.println(ExcelLibrary.getExcelData(testscriptspath, testCaseName, 0, i));
			}

			int stepNo =1;
			for (int i = 1; i <= testRowCount; i++)
			{
				Map<String, String> stepAttribute = new LinkedHashMap<String, String>();
				for (int j = 0; j < ExcelLibrary.getExcelCellCount(testscriptspath, testCaseName, i); j++) 
				{
					try
					{
						String info = ExcelLibrary.getExcelData(testscriptspath, testCaseName, i, j);
						//System.out.println("info"+info);
						stepAttribute.put(testHeader.get(j), info);
					}
					catch(Exception e)
					{
					}
				}
				stepInfo.put(stepNo, stepAttribute);
				//System.out.println(stepNo + " " +stepInfo);
				stepNo++;
			}
			extractEachStep(stepInfo,executionType, moduleName, testObjectMappath);
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}

	public void extractEachStep(Map<Integer, Map<String, String>> step, String executionType, String moduleName, String testObjectMappath)
	{
		try
		{
			Set<Integer> noOfSteps = step.keySet();
			System.out.println("no of steps"+noOfSteps);
			for (Integer key : noOfSteps)
			{
				Map<String, String> stepAttribute = step.get(key);
				executeStep(stepAttribute, executionType, moduleName, testObjectMappath);
				//System.out.println(step.get(key));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}


	private void executeStep(Map<String, String> stepAttribute, String executionType, String moduleName, String testObjectMappath) {
		// TODO Auto-generated method stub
		//Set<String> step = stepAttribute.keySet();
		String locator = stepAttribute.get("ElementLocator");
		String locatorValue = stepAttribute.get("ElementName");
		String elementName=stepAttribute.get("ElementName");
		String webele = null;

		try {
			System.out.println("locator value" + locatorValue);
			webele = ReadJsonFile.readJsonKeyValue(testObjectMappath, moduleName, locatorValue, locator);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String functionality = stepAttribute.get("ElementAction");
		String value = stepAttribute.get("Value");

		System.out.println(locator + " " + locatorValue + " " + functionality + " " + value);
		System.out.println("new webele" + webele);

		WebElement element = null;
		List<WebElement> elements = null;
		if (!locator.equalsIgnoreCase("none")) {
			if (locatorValue.contains("Lst")) {
				elements = getWebElements(locator, webele, value, locatorValue);
				try {
					performAction(functionality, elements, value, elementName);
					if(Character.isDigit(value.charAt(0))) {
						performAction(functionality, elements.get(Integer.parseInt(value)), value, elementName);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println(locator + "  " + webele);
				try
				{
					element = getWebElement(locator, webele,value,locatorValue);
				}
				catch(Exception e)
				{
					throw e;
				}
				try {
					performAction(functionality, element, value, elementName);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			try {
				performAction(functionality, element, value, elementName);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					throw e;
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}


	}

	public List<WebElement> getWebElements(String locator, String webele, String value, String elementName) {
		List<WebElement> element = null;
		By ref = getLocator(locator, webele,value);
		System.out.println("our value "+value);
		System.out.println("reference " + ref);

		if (ref.equals(null)) {
			System.out.println("Please select valid locator in test cases");
		} else {
			try {
				if (locator.equalsIgnoreCase("id")) {
					System.out.println("====================================before trim" + webele);
					System.out.println("=====================================after trim and replace");
					element = driver.findElements(By.id(webele.trim().replace("\"", "").replace("\"", "")));

				} else if (locator.equalsIgnoreCase("classname")) {
					element = driver.findElements(By.className(webele));
				} else if (locator.equalsIgnoreCase("xpath")) {
					System.out.println("actual" + webele);
					element = driver.findElements(By.xpath(webele.trim().replace("\"", "").replace("\"", "")));
				} else if (locator.equalsIgnoreCase("d_xpath")) {
					webele = webele.trim().replace("\"", "").replace("\"", "");
					webele = webele.replace("{", "");
					System.out.println(webele);
					webele = webele.replace("}", "");
					System.out.println(webele);
					element = driver.findElements(By.xpath(webele.replace(elementName, value)));

				}

				System.out.println("element-----------" + element);
			} catch (NoSuchElementException e) {
				System.out.println(e.toString());
				return null;
			} catch (StaleElementReferenceException e) {
				System.out.println(e.toString());
				getWebElements(locator, webele, value, elementName);
				return null;
			} catch (ElementNotVisibleException e) {
				System.out.println(e.toString());
				return null;
			} catch (TimeoutException e) {
				System.out.println(e.toString());
				return null;
			}
		}
		return element;
	}
	public WebElement getWebElement(String locator, String webele,String value,String locatorValue)
	{
		WebElement element = null;
		System.out.println("new value "+value);
		/*	By ref = getLocator(locator, locatorValue,value);

		if (ref.equals(null)) {
			System.out.println("Please select valid locator in test cases");
		} else {*/
		try {
			if (locator.equalsIgnoreCase("id")) {
				System.out.println("====================================before trim" + webele);
				System.out.println("=====================================after trim and replace");
				element = driver.findElement(By.id(webele.trim().replace("\"", "").replace("\"", "")));

			} else if (locator.equalsIgnoreCase("classname")) {
				element = driver.findElement(By.className(webele));
			} else if (locator.equalsIgnoreCase("xpath")) {
				System.out.println("expected" + "//android.widget.TextView[@text='GET STARTED']");
				System.out.println("actual" + webele);

				element = driver.findElement(By.xpath(webele.trim().replace("\"", "").replace("\"", "")));
			} else if (locator.equals("d_xpath")) {
				webele = webele.trim().replace("\"", "").replace("\"", "");
				webele = webele.replace("{", "");
				System.out.println(webele);
				webele = webele.replace("}", "");
				System.out.println(webele);
				System.out.println(locatorValue);
				element = driver.findElement(By.xpath(webele.replace(locatorValue, value)));

			}
			if (locator.equals("xpath")) {

				element = driver.findElement(By.xpath(webele.trim().replace("\"", "").replace("\"", "")));

			}

			System.out.println("element-----------" + element);
		} catch (NoSuchElementException e) {
			System.out.println(e.toString());

			return null;

		} catch (StaleElementReferenceException e) {
			System.out.println(e.toString());
			getWebElement(locator, webele,value,locatorValue);
			return null;
		} catch (ElementNotVisibleException e) {
			System.out.println(e.toString());
			return null;
		} catch (TimeoutException e) {
			System.out.println(e.toString());
			return null;
		}
		//}

		return element;
	}
	public By getLocator(String locator, String locatorValue,String value)
	{
		if(locator.equalsIgnoreCase("id"))
		{
			return By.id(locatorValue);
		}
		else if(locator.equalsIgnoreCase("classname"))
		{
			return By.className(locatorValue);   
		}
		else if(locator.equalsIgnoreCase("xpath"))
		{
			return By.xpath(locatorValue);
		}
		else if(locator.equalsIgnoreCase("d_xpath"))
		{

			return By.xpath(locatorValue);
		}
		else if(locator.equalsIgnoreCase("linkText"))
		{
			return By.linkText(locatorValue);
		}
		else if(locator.equalsIgnoreCase("partialLinkText"))
		{
			return By.partialLinkText(locatorValue);
		}
		else if(locator.equalsIgnoreCase("name"))
		{
			return By.name(locatorValue);
		}
		else if(locator.equalsIgnoreCase("tagName"))
		{
			return By.tagName(locatorValue);
		}
		else if(locator.equalsIgnoreCase("cssSelector"))
		{
			return By.cssSelector(locatorValue);
		}
		return null;
	}
	public void performAction(String functionality, WebElement element, String value, String locatorValue) throws InterruptedException
	{
		executeAction(functionality, element, value, locatorValue);
	}
	public void performAction(String functionality, List<WebElement> elements, String value, String elementName)
			throws InterruptedException {
		executeAction(functionality, elements.get(0), value, elementName);
	}
	public void executeAction(String functionality, WebElement element, String value, String locatorValue) throws InterruptedException
	{	
		if(!functionality.equalsIgnoreCase("none"))
		{
			switch(functionality)
			{
			case "click" :	click(element,locatorValue);
			break;
			case "type" :	type(element,value,locatorValue);
			break;
			case "display" : display(element,value,locatorValue);
			break;
			case "clear" :	clear(element,locatorValue);
			break;
			case "alert_accept" :	alert_accept(element);
			break;
			case "scroll" :	scroll();
			break;

			default : System.out.println("Provide Proper data functionality");	
			}
		}
	}

	private void explicit_wait(WebElement element) {
		try {
			System.out.println("Inside explicit wait");
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(2, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			
			Assert.assertTrue(element.isEnabled(),"- is not enabled");
			System.out.println("explicit wait completed");
			NXGReports.addStep("Element is  enabled to click", LogAs.PASSED,null);
		}catch (AssertionError e) {
			NXGReports.addStep("Element is not enabled to click", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			//	Assert.fail("Element is not enabled to click" );
			Assert.fail();
		} 
		catch (RuntimeException e) {
			NXGReports.addStep("Element is not enabled to click", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
		}
		

	}
 
	public void waitTillPageLoad() {
		try {
		System.out.println("waiting for page load");
		WebDriverWait wait = new WebDriverWait(driver, 120);
		wait.until((ExpectedCondition<Boolean>) driver ->
	   ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
		System.out.println("waiting for page loaded");
		}catch (Exception e) {
			System.out.println(" Page is not in ready state");
		}
	}

	private void alert_accept(WebElement element) {
/*		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
				.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
		boolean alretStatus=wait.until(ExpectedConditions.alertIsPresent())!=null;
		
		if(alretStatus) {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}
*/		if(driver instanceof FirefoxDriver) {
			try {
				System.out.println("in robot class");
			Robot robot=new Robot();
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyPress(KeyEvent.VK_ALT);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
	}


	public void click(WebElement element, String locatorValue) {
		try {
			waitTillPageLoad();
			if(element.isEnabled())
			{
				element.click();
				NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is able tap on" + locatorValue, LogAs.PASSED, null);
				waitTillPageLoad();
				try {
				Thread.sleep(3000);
				}catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			else
			{
				explicit_wait(element);
				element.click();
				try {
					Thread.sleep(3000);
					}catch (Exception e) {
						// TODO: handle exception
					}
				NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is able tap on" + locatorValue, LogAs.PASSED, null);
				waitTillPageLoad();
			}
			//explicit_wait(element);
			//Assert.assertTrue(element.isEnabled(),locatorValue+" - is not enabled");


			/*element.click();
			NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is able tap on" + locatorValue, LogAs.PASSED, null);
			 */
		} catch (AssertionError error) {
			NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is not able tap on" + locatorValue, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail();
		}
		catch (Exception e) {
			NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is not able tap on" + locatorValue, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
		}

	}


	public void type(WebElement element, String value,String locatorValue) {
		try {
			Assert.assertTrue(element.isEnabled(), locatorValue + " - is not enabled");
			element.sendKeys(value);
			NXGReports.addStep("Enter " + value + " on " + locatorValue, "User Should able Enter " + value + " on " + locatorValue, "User is able Enter " + value + " on " + locatorValue, LogAs.PASSED, null);
		} catch (AssertionError error) {
			NXGReports.addStep("Enter " + value + " on " + locatorValue, "User Should able Enter " + value + " on " + locatorValue, "User is not able Enter " + value + " on " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail();
		}catch (Exception e) {
			NXGReports.addStep("Enter " + value + " on " + locatorValue, "User Should able Enter " + value + " on " + locatorValue, "User is not able Enter " + value + " on " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
		}

	}

	public void display(WebElement element, String value,String locatorValue) {
		try {
			Assert.assertTrue(element.isEnabled(), locatorValue + " - is not enabled");
			element.isDisplayed();
			NXGReports.addStep("Verify "+ locatorValue + "is displayed",locatorValue +  "Should be displayed", locatorValue +  " is  displayed", LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		} catch (AssertionError error) {
			NXGReports.addStep("Verify "+ locatorValue + "is displayed",locatorValue +  "Should be displayed", locatorValue +  " is  not displayed", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail();
		}catch (Exception e) {
			NXGReports.addStep("Verify "+ locatorValue + "is displayed",locatorValue +  "Should be displayed", locatorValue +  " is  not displayed", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
		}

	}

	public void clear(WebElement element,String locatorValue) {
		try {
			Assert.assertTrue(element.isEnabled(), locatorValue + " - is not enabled");
			element.clear();
			NXGReports.addStep("Clear input from " + locatorValue, "User Should able clear input from " + locatorValue, "User is able clear input from " + locatorValue, LogAs.PASSED, null);
		} catch (AssertionError error) {
			NXGReports.addStep("Clear input from " + locatorValue, "User Should able clear input from " + locatorValue, "User is not able clear input from " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail();
		}catch (Exception e) {
			NXGReports.addStep("Clear input from " + locatorValue, "User Should able clear input from " + locatorValue, "User is not able clear input from " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
		}

	}
	public void scroll(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,250)", "");
	}
}
