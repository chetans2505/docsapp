/***********************************************************************
* @author 			:		Srinivas Hippargi,Raghukiran
* @description		: 		Listener class to Generate NXG Report
*/
package com.cbt.docsapp.reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.testng.IInvokedMethod;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

import com.cbt.docsapp.generics.GenericLib;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kirwa.nxgreport.NXGReports;
import com.kirwa.nxgreport.listners.NXGTestListner;
import com.kirwa.nxgreport.utils.Platform;
import com.kirwa.nxgreport.utils.TestDirectory;
import com.kirwa.nxgreport.writer.IndexPageWriter;
import com.kirwa.nxgreport.writer.TestCaseReportsPageWriter;

public class MyNXGTestListner extends NXGTestListner {
	int runCount = 0;
	List<ITestResult> passedTests = new ArrayList<ITestResult>();
	List<ITestResult> failedTests = new ArrayList<ITestResult>();
	List<ITestResult> skippedTests = new ArrayList<ITestResult>();
	List<ITestResult> testsResults = new ArrayList<ITestResult>();

	private boolean isSuiteStarted = false;
	private boolean isSuiteFinshed = false;
	public static File sHtmlReports = null;
	public static List<String> sDevice = null;
	public static String reportDir=GenericLib.sDirPath;

	static {
		_setExcelSheetPath();
		_setProprtyFilePath();
	}
	
	/**
	 * Initiating the excel sheet path for test data
	 */

	public  static void _setExcelSheetPath() {

		GenericLib.sConfigFile=GenericLib.sDirPath + "/Config.Properties";
		GenericLib.sKirwaFile= GenericLib.sDirPath + "/KIRWA.properties";
	}

	/**
	 * Initiating the properties file for test input
	 */

	public static void _setProprtyFilePath() {

		GenericLib.sTestDataFile=GenericLib.sDirPath + "/ZoomTestData.xlsx";
	}
	
	
	
	public MyNXGTestListner() throws IOException {
		GenericLib.setCongigValue(GenericLib.sKirwaFile, "KIRWA.reports.dir",reportDir + "/Reports/HTMLReports" );
		System.setProperty("KIRWA.reporter.config", "KIRWA.properties");
		reportDir=GenericLib.getCongigValue(GenericLib.sKirwaFile, "KIRWA.reports.dir");
		System.out.println("Constructor MyNXGTestListner is running " + reportDir);
	}

	public void onFinish(ITestContext arg0) {
	}

	public void onStart(ITestContext context) {

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		
		
	}

	public void onTestFailure(ITestResult result) {
		this.failedTests.add(result);
		this.testsResults.add(result);
		//NXGReports.addStep(result.getThrowable().getLocalizedMessage(), LogAs.FAILED, new CaptureScreen(ScreenshotOf.BROWSER_PAGE));

	}

	public void onTestSkipped(ITestResult result) {
		//MyNXGTestListner.createReportDir(result);
		this.skippedTests.add(result);
		this.testsResults.add(result);
		// System.out.println("*****************************" + sDevice.get(0));

	}

	public void onTestStart(ITestResult testResult) {
		MyNXGTestListner.createReportDir(testResult);
		MyNXGTestListner.setPlatfromBrowserDetails(testResult);
	}

	public void onTestSuccess(ITestResult result) {
		this.passedTests.add(result);
		this.testsResults.add(result);

	}

	public void onFinish(ISuite suite) {
		if (!this.isSuiteFinshed) {
			this.isSuiteFinshed = true;
			System.out.println("Suite finished");
			suite.setAttribute("endExecution", (Object) System.currentTimeMillis());
			long startTime = Long.parseLong(suite.getAttribute("startExecution").toString());
			this.generateIndexPage();
			this.generateCurrentRunPage(startTime, System.currentTimeMillis());
			this.generateJsonJs();
			this.generateTestsResults(this.passedTests);
			this.generateTestsResults(this.failedTests);
			this.generateTestsResults(this.skippedTests);
			this.generateTestsResults(this.testsResults);
		}
	}

	private void generateJsonJs() {
		block14: {
			File jsDataFile = new File(String.valueOf(TestDirectory.JSDIR) + TestDirectory.SEP + "runData.js");
			JsonArray jsResults = new JsonArray();
			JsonObject jsResult = new JsonObject();
			jsResult.addProperty("date", TestDirectory.runStamp);
			jsResult.addProperty("Passed", (Number) this.passedTests.size());
			jsResult.addProperty("Failed", (Number) this.failedTests.size());
			jsResult.addProperty("Skipped", (Number) this.skippedTests.size());
			try {
				jsResult.addProperty("displayname", new SimpleDateFormat(TestDirectory.runDisplayFormat)
						.format(new SimpleDateFormat("dd_MM_yy_hh_mm_ss_SS").parse(TestDirectory.runStamp)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			OutputStreamWriter fileWs = null;
			try {
				try {
					if (jsDataFile.exists()) {
						JsonParser parser = new JsonParser();
						String jsonStr = new String(Files.readAllBytes(jsDataFile.toPath()), StandardCharsets.UTF_8);
						JsonElement obj = parser
								.parse(jsonStr.replace("function getChartDataForMe(){return(", "").replace(");}", ""));
						JsonArray jsonTestsObject = (JsonArray) obj;
						jsonTestsObject.add((JsonElement) jsResult);
						fileWs = new FileWriter(jsDataFile);
						fileWs.write("function getChartDataForMe(){return(" + jsonTestsObject.toString() + ");}");
						break block14;
					}
					fileWs = new FileWriter(jsDataFile);
					jsDataFile.createNewFile();
					jsResults.add((JsonElement) jsResult);
					fileWs.write("function getChartDataForMe(){return(" + jsResults.toString() + ");}");
				} catch (IOException e) {
					e.printStackTrace();
					try {
						fileWs.flush();
						fileWs.close();
					} catch (IOException ie) {
						ie.printStackTrace();
					}
				}
			} finally {
				try {
					fileWs.flush();
					fileWs.close();
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	private void generateTestsResults(List<ITestResult> tests) {
		PrintWriter printWriter = null;
		for (ITestResult testResult : tests) {
			String reportDir = testResult.getAttribute("reportDir").toString();
			try {
				try {
					printWriter = new PrintWriter(
					String.valueOf(reportDir) + TestDirectory.SEP + testResult.getName() + ".html");
					TestCaseReportsPageWriter.printHeader(printWriter, testResult);
					TestCaseReportsPageWriter.printMenuLink(printWriter, testResult, 0);
					TestCaseReportsPageWriter.printContent(printWriter, testResult, this.runCount);
					TestCaseReportsPageWriter.printFooter(printWriter);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					try {
						printWriter.close();
					} catch (Exception ie) {
						printWriter = null;
					}
					continue;
				}
			} catch (Throwable var7_11) {
				try {
					printWriter.close();
				} catch (Exception e) {
					printWriter = null;
				}
				/*throw var7_11; */
			}
			try {
				printWriter.close();
				continue;
			} catch (Exception e) {
				printWriter = null;
			}
		}
	}

	public void onStart(ISuite suite) {
		if (!this.isSuiteStarted) {
			this.isSuiteStarted = true;
			try {
				String ts = Long.valueOf(System.currentTimeMillis()).toString();
				suite.setAttribute("startExecution", (Object) ts);
				TestDirectory.init(ts);
				TestDirectory.mkDirs(String.valueOf(TestDirectory.RUNDIR) + TestDirectory.SEP + suite.getName());

				for (Object xmlTest : suite.getXmlSuite().getTests()) {
					XmlTest test = (XmlTest) xmlTest;
					TestDirectory.mkDirs(String.valueOf(TestDirectory.RUNDIR) + TestDirectory.SEP + suite.getName()
							+ TestDirectory.SEP + test.getName());
				}
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public void afterInvocation(IInvokedMethod method, ITestResult result) {
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		method.isConfigurationMethod();
		method.isTestMethod();
	}

	private static void createReportDir(ITestResult method) {
		String reportPath = NXGTestListner.getReportDir(method);
		TestDirectory.mkDirs(reportPath);
		TestDirectory.mkDirs(String.valueOf(reportPath) + TestDirectory.SEP + TestDirectory.SCREENSHOT_DIRName);
	}

	private static void setPlatfromBrowserDetails(ITestResult method) {
		Platform.prepareDetails(NXGReports.getWebDriver());
		method.setAttribute(Platform.BROWSER_NAME_PROP, (Object) Platform.BROWSER_NAME);
		method.setAttribute(Platform.BROWSER_VERSION_PROP, (Object) Platform.BROWSER_VERSION);
	}

	public static String getRelativePathFromSuiteLevel(ITestResult testResult) {

		String suiteName = testResult.getTestContext().getSuite().getName();
		String testName = testResult.getTestContext().getCurrentXmlTest().getName();
		String classname = testResult.getTestClass().getName().replace(".", TestDirectory.SEP);
		String methodName = testResult.getMethod().getMethodName();
		methodName = String.valueOf(methodName) + "_Iteration"
				+ (testResult.getMethod().getCurrentInvocationCount() + 1);
		return String.valueOf(suiteName) + TestDirectory.SEP + testName + TestDirectory.SEP + classname
				+ TestDirectory.SEP + methodName;
	}

	public static String getReportDir(ITestResult testResult) {
		String relativePath = NXGTestListner.getRelativePathFromSuiteLevel(testResult);
		testResult.setAttribute("relativeReportDir", (Object) relativePath);
		String rptDir = String.valueOf(TestDirectory.RUNDIR) + TestDirectory.SEP + relativePath;
		testResult.setAttribute("iteration", (Object) (testResult.getMethod().getCurrentInvocationCount() + 1));
		testResult.setAttribute("reportDir", (Object) rptDir);
		return rptDir;
	}

	public void generateIndexPage() {
		PrintWriter printWriter = null;
		try {
			try {
				printWriter = new PrintWriter(
						String.valueOf(TestDirectory.REPORTSDIR) + TestDirectory.SEP + "index.html");
				IndexPageWriter.printHeader(printWriter);
				IndexPageWriter.printContent(printWriter, "");
				IndexPageWriter.printFooter(printWriter);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				try {
					printWriter.close();
				} catch (Exception ie) {
					printWriter = null;
				}
			}
		} finally {
			try {
				printWriter.close();
			} catch (Exception e) {
				printWriter = null;
			}
		}
	}

	private void generateCurrentRunPage(long startTime, long endTime) {
		PrintWriter printWriter = null;
		try {
			try {
				printWriter = new PrintWriter(String.valueOf(TestDirectory.RUNDIR) + TestDirectory.SEP + "Result.html");
				MyResultPageWriter.printHeader(printWriter, this.passedTests.size(), this.failedTests.size(),
						this.skippedTests.size());
				MyResultPageWriter.printMenuLink(printWriter, 0);
				MyResultPageWriter.printContent(printWriter, this.passedTests, this.failedTests, this.skippedTests,
						this.testsResults, startTime, endTime);
				MyResultPageWriter.printFooter(printWriter);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				try {
					printWriter.close();
				} catch (Exception ie) {
					printWriter = null;
				}
			}
		} finally {
			try {
				printWriter.close();
			} catch (Exception e) {
				printWriter = null;
			}
		}
	}
}
