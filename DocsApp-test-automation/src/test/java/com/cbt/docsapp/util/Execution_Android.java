package com.cbt.docsapp.util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import com.cbt.docsapp.generics.ExcelLibrary;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.kirwa.nxgreport.NXGReports;
import com.kirwa.nxgreport.logging.LogAs;
import com.kirwa.nxgreport.selenium.reports.CaptureScreen;
import com.kirwa.nxgreport.selenium.reports.CaptureScreen.ScreenshotOf;

import io.appium.java_client.android.AndroidDriver;

public class Execution_Android {

	AndroidDriver driver = null;;

	public Execution_Android(AndroidDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
	}

	public void getTestStepInfo(String executionType, String moduleName, String testCaseName) throws Exception {
		// TODO Auto-generated method stub
			String testscriptspath = "./test-scripts/" + executionType + "/" + moduleName + ".xlsx";
			String testObjectMappath = "./object_repo/" + executionType + "/" + moduleName + ".json";
			System.out.println(testscriptspath);
			int testRowCount = ExcelLibrary.getExcelRowCount(testscriptspath, testCaseName);

			List<String> testHeader = new ArrayList<String>();
			Map<Integer, Map<String, String>> stepInfo = new LinkedHashMap<Integer, Map<String, String>>();
			int noOfTestHeadCells = ExcelLibrary.getExcelCellCount(testscriptspath, testCaseName, 1);
			for (int i = 0; i <= noOfTestHeadCells; i++) {
				testHeader.add(ExcelLibrary.getExcelData(testscriptspath, testCaseName, 0, i));
				// System.out.println(ExcelLibrary.getExcelData(testscriptspath, testCaseName,
				// 0, i));
			}

			int stepNo = 1;
			for (int i = 1; i <= testRowCount; i++) {
				Map<String, String> stepAttribute = new LinkedHashMap<String, String>();
				for (int j = 0; j < ExcelLibrary.getExcelCellCount(testscriptspath, testCaseName, i); j++) {
					try {
						String info = ExcelLibrary.getExcelData(testscriptspath, testCaseName, i, j);
						System.out.println("info"+info);
						stepAttribute.put(testHeader.get(j), info);
					} catch (Exception e) {
						throw e;
					}
				}
				stepInfo.put(stepNo, stepAttribute);
				// System.out.println(stepNo + " " +stepInfo);
				stepNo++;
			}
			extractEachStep(stepInfo, executionType, moduleName, testObjectMappath);
	}

	public void extractEachStep(Map<Integer, Map<String, String>> step, String executionType, String moduleName,
			String testObjectMappath) throws Exception {
			Set<Integer> noOfSteps = step.keySet();
			System.out.println("no of steps" + noOfSteps);
			for (Integer key : noOfSteps) {
				Map<String, String> stepAttribute = step.get(key);
				executeStep(stepAttribute, executionType, moduleName, testObjectMappath);
				// System.out.println(step.get(key));
			}

	}

	private void executeStep(Map<String, String> stepAttribute, String executionType, String moduleName,
			String testObjectMappath) throws Exception {
		// TODO Auto-generated method stub
		// Set<String> step = stepAttribute.keySet();
		String locator = stepAttribute.get("ElementLocator");
		String locatorValue = stepAttribute.get("ElementName");
		String elementName=stepAttribute.get("ElementName");
		String webele = null;
			try {
			System.out.println("locator value" + locatorValue);
			webele = ReadJsonFile.readJsonKeyValue(testObjectMappath, moduleName, locatorValue, locator);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw e;
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
					throw e;
				}
			} else {
				System.out.println(locator + "  " + webele);
				element = getWebElement(locator, webele, value, elementName);
				try {
					performAction(functionality, element, value, elementName);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					throw e;
				}
			}
		} else {
			try {
				// element = getWebElement(locator, webele);
				performAction(functionality, element, value, elementName);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		}
	}

	public WebElement getWebElement(String locator, String webele, String value, String locatorValue)  {
		WebElement element = null;
		By ref = getLocator(locator, webele);
		System.out.println("reference " + ref);

		if (ref.equals(null)) {
			System.out.println("Please select valid locator in test cases");
		} else {
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
				} else if (locator.equalsIgnoreCase("d_xpath")) {
					System.out.println("expected" + "//android.widget.TextView[@text='GET STARTED']");
					System.out.println("D_XPATH------------------------" + webele.trim().replace("\"", "")
							.replace("\"", "").replace("\\\"", "\"+").replace(locatorValue, value));
					webele = webele.trim().replace("\"", "").replace("\"", "");
					webele = webele.replace("{", "");
					System.out.println(webele);
					webele = webele.replace("}", "");
					System.out.println(webele);
					element = driver.findElement(By.xpath(webele.replace(locatorValue, value)));

				}

				System.out.println("element-----------" + element);
			} catch (NoSuchElementException e) {
				System.out.println(e.toString());
				return null;
			} catch (StaleElementReferenceException e) {
				System.out.println(e.toString());
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

	public List<WebElement> getWebElements(String locator, String webele, String value, String elementName) throws Exception {
		List<WebElement> element = null;
		By ref = getLocator(locator, webele);
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
					System.out.println("expected" + "//android.widget.TextView[@text='GET STARTED']");
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

	public By getLocator(String locator, String locatorValue) {

		if (locator.equalsIgnoreCase("id")) {
			return By.id(locatorValue);
		} else if (locator.equalsIgnoreCase("classname")) {
			return By.className(locatorValue);
		} else if (locator.equalsIgnoreCase("xpath")) {
			return By.xpath(locatorValue);
		} else if (locator.equalsIgnoreCase("d_xpath")) {
			return By.xpath(locatorValue);
		}
		return null;
	}

	public void performAction(String functionality, WebElement element, String value, String elementName)
			throws Exception {
		executeAction(functionality, element, value, elementName);
	}
	
	public void performAction(String functionality, List<WebElement> elements, String value, String elementName)
			throws Exception {
		executeAction(functionality, elements.get(0), value, elementName);
	}

	public void executeAction(String functionality, WebElement element, String value, String elementName)
			throws Exception {
		if (!functionality.equalsIgnoreCase("none")) {
			switch (functionality) {
			case "click":
				click(element, elementName);
				break;
			case "swipeLeft":
				swipeLeft(value);
				break;
			case "swipeUp":
				swipeUp(value);
				break;
			case "type":
				type(element, value,elementName);
			case "display":
				display(element,elementName);
				break;
			case "wait":
				Thread.sleep(Integer.parseInt(value));
				break;

			default:
				System.out.println("Provide Proper data functionality");
			}
		} else {

		}
	}

	public void click(WebElement element, String locatorValue) {
		try {
			Assert.assertTrue(element.isEnabled(),locatorValue+" - is not enabled");
			element.click();
			NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is able tap on" + locatorValue, LogAs.PASSED, null);
		} catch (AssertionError error) {
			NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is not able tap on" + locatorValue, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail("unable to tap on " + locatorValue );
		}catch (Exception error) {
			NXGReports.addStep("Tap on " + locatorValue, "User Should be Tap on " + locatorValue, "User is not able tap on" + locatorValue, LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail("unable to tap on " + locatorValue );
		}

	}
	
	
	public void hideKeyboard() {
		try {
		driver.hideKeyboard();
		}catch (Exception e) {
			NXGReports.addStep("Hide Keyboard", "User Should able Hide Keyboard ", "User is not able to Hide Keyboard ", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
		}
	}

	public void type(WebElement element, String value,String locatorValue) throws Exception {
		try {
			Assert.assertTrue(element.isEnabled(), locatorValue + " - is not enabled");
			element.clear();
			element.sendKeys(value);
			hideKeyboard();
			NXGReports.addStep("Enter " + value + " on " + locatorValue, "User Should able Enter " + value + " on " + locatorValue, "User is able Enter " + value + " on " + locatorValue, LogAs.PASSED, null);
		} catch (AssertionError error) {
			NXGReports.addStep("Enter " + value + " on " + locatorValue, "User Should able Enter " + value + " on " + locatorValue, "User is not able Enter " + value + " on " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail("Unable to type on " + locatorValue );
		}
		catch (Exception e) {
			NXGReports.addStep("Enter " + value + " on " + locatorValue, "User Should able Enter " + value + " on " + locatorValue, "User is not able Enter " + value + " on " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail("Unable to type on " + locatorValue );
		}

	}
	
	public void display(WebElement element,String locatorValue) throws Exception {
		try {
			//Assert.assertTrue(element.isEnabled(), locatorValue + " - is not displayed");
			//element.isDisplayed();
			NXGReports.addStep("Verify "+ locatorValue + "is displayed as"+element,locatorValue +  "Should be displayed", locatorValue +  " is  displayed as "+element.getText(), LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
		} catch (AssertionError error) {
			NXGReports.addStep("Verify "+ locatorValue + "is displayed",locatorValue +  "Should be displayed", locatorValue +  " is  not displayed", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail("element " + locatorValue + " is not displayed" );
		}
		catch (Exception error) {
			NXGReports.addStep("Verify "+ locatorValue + "is displayed",locatorValue +  "Should be displayed", locatorValue +  " is  not displayed", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail("element " + locatorValue + " is not displayed" );
		}

	}
	
	
	/*public void ClearText(WebElement element,String locatorValue) throws Exception {
		try {
			Assert.assertTrue(element.isEnabled(), locatorValue + " - is not enabled");
			element.clear();
			NXGReports.addStep("Clear input from " + locatorValue, "User Should able clear input from " + locatorValue, "User is able clear input from " + locatorValue, LogAs.PASSED, null);
		} catch (AssertionError error) {
			NXGReports.addStep("Clear input from " + locatorValue, "User Should able clear input from " + locatorValue, "User is not able clear input from " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			Assert.fail();
		}
		catch (NoSuchElementException error) {
			NXGReports.addStep("Clear input from " + locatorValue, "User Should able clear input from " + locatorValue, "User is not able clear input from " + locatorValue, LogAs.PASSED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw new Exception();
		}

	}*/
	
	public void swipeLeft(String value) throws Exception {
		try {
		Thread.sleep(3000);
		int iValue = Integer.parseInt(value);
		System.out.println("inside swipe");
		for (int i = 1; i <= iValue; i++) {
			Dimension dSize = driver.manage().window().getSize();
			int startx = (int) (dSize.width * .90);
			int endx = (int) (dSize.width * .20);
			int starty = dSize.height / 2;
			driver.swipe(startx, starty, endx, starty, 3000);
			
		}
		}catch (Exception e) {
			NXGReports.addStep("Check Swipe", "User Should able swipe Left ", "User is not able swipe Left ", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
		}
	}

	public void swipeRight(String value) throws Exception {
		try {
			Thread.sleep(3000);
		int iValue = Integer.parseInt(value);
		System.out.println("inside swipe");
		for (int i = 1; i <= iValue; i++) {
		Dimension dSize = driver.manage().window().getSize();
		int startx = (int) (dSize.width * .20);
		int endx = (int) (dSize.width * .90);
		int starty = dSize.height / 2;
		driver.swipe(startx, starty, endx, starty, 3000);
		}
		}catch (Exception e) {
			NXGReports.addStep("Check Swipe", "User Should able swipe Right ", "User is not able swipe Right ", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
			
		}
	}

	public void swipeUp(String value) throws Exception {
		try{
			Thread.sleep(3000);
		int iValue = Integer.parseInt(value);
		System.out.println("inside swipe");
		for (int i = 1; i <= iValue; i++) {
		Dimension dSize = driver.manage().window().getSize();
		int starty = (int) (dSize.height * .90);
		int endy = (int) (dSize.height * .20);
		int startx = dSize.width / 2;
		driver.swipe(startx, starty, startx, endy, 3000);
		}
		}catch (Exception e) {
			NXGReports.addStep("Check Swipe", "User Should able swipe Up ", "User is not able swipe Up ", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
			
		}
	}

	public void swipeBottom(String value) throws Exception {
		try {
			Thread.sleep(3000);
		int iValue = Integer.parseInt(value);
		System.out.println("inside swipe");
		for (int i = 1; i <= iValue; i++) {
		Dimension dSize = driver.manage().window().getSize();
		int starty = (int) (dSize.height * .20);
		int endy = (int) (dSize.height * .90);
		int startx = dSize.width / 2;
		driver.swipe(startx, starty, startx, endy, 3000);
		}
		}catch (Exception e) {
			NXGReports.addStep("Check Swipe", "User Should able swipe Bottom ", "User is not able swipe Bottom ", LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));
			throw e;
			
		}
	}

}
