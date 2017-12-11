package com.cbt.docsapp.init;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.cbt.docsapp.generics.ExcelLibrary;
import com.cbt.docsapp.generics.GenericLib;



public class GenerateXmlFile {

	/** The suite. */
	static XmlSuite suite= new XmlSuite();
	
	static String mappack = null;
	
	/** The par. */
	static Map<String, String> parameters = null;
	
	/** The suites. */
	static List<XmlSuite> suites = new ArrayList<XmlSuite>();
	
	
	/**
	 * Generates XML Class.
	 */ 
	public static int count = 1;
	public static int listner_count = 0;
	
	public static void xmlclassgeneration_sequential(HashMap<String, String>testCases, HashMap<String, String>testParameters, String executionType)
	{
		XmlTest test  =  new XmlTest(suite);
		
		// Thread count need to be provided for running scripts sequential
		if(listner_count<count)
		{
			suite.addListener("com.cbt.docsapp.reports.MyNXGTestListner");
			listner_count = listner_count+1;
		}
			suite.setGroupByInstances(false);
			suite.setThreadCount(1);
			suite.setParallel("tests");
			Set<String> keys = testCases.keySet();
			ArrayList<String> testNames = new ArrayList<String>(); 
	        for(String key: keys){
	            testNames.add(key);
	        }
	        String testname = Arrays.toString(testNames.toArray());
			test.setName(testname.replace("[", "").replace(",", "").replace("]", "").replace(" ", "_")+"_"+executionType);
			test.setParameters(testParameters);
			ArrayList<XmlClass>  classes =  new ArrayList<XmlClass>();
			Iterator it = testCases.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        String[] testcases=	pair.getValue().toString().replace(",", "")	.split(" ");
		        for(int i=0;i<testcases.length;i++)
		        {
			    	String className = "com.cbt.docsapp."+executionType.toLowerCase()+".tests."+pair.getKey()+"."+testcases[i];
					System.out.println(className);
					classes.add(new XmlClass(className));
		        }
				 test.setXmlClasses(classes);
				 it.remove(); // avoids a ConcurrentModificationException
		        
		    }
		suites.add(suite);
		suite.setName("Suite");		
		try
		{
			FileWriter writer = new FileWriter("testng.xml");
			writer.write(suite.toXml());
			writer.flush();			
			writer.close();		
		}
		catch(Exception e)
		{
		
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	

	@SuppressWarnings("deprecation")
	public static void xmlclassgeneration_parallel(int device_count, String executionType)
	{
		/*XmlTest test  =  new XmlTest(suite);*/
		
		// Thread count need to be provided for running scripts Parallel
		if(listner_count<count)
		{
			suite.addListener("com.cbt.docsapp.reports.MyNXGTestListner");
			listner_count = listner_count+1;
		}
			suite.setGroupByInstances(false);
			suite.setThreadCount(device_count);
			suite.setParallel("tests");
			
			ArrayList<String> moduleNames = new ArrayList<String>();
			for(int i=10;i<=16;i++)
			{
				//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 3));
				if(ExcelLibrary.getExcelData("./config/config.xlsx", "Test Execution Control", i, 3).trim().equals("Yes"))
				{
					//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 2));
					moduleNames.add(ExcelLibrary.getExcelData("./config/config.xlsx", "Test Execution Control", i, 2));
				}
				
			}
			
			
	        int rowCount = ExcelLibrary.getExcelRowCount("./config/config.xlsx", "devicesList");
			ArrayList<String> deviceCount = new ArrayList<String>();
			ArrayList<String> siNo = new ArrayList<String>();
			for(int i=1;i<=rowCount;i++)
			{
				if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
				{
					deviceCount.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
					siNo.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 0));
				}
			}
			
	        if(device_count>1)
	        {
	        	if(moduleNames.size() != 0)
				{
					
	        	
	        	
	        	for(int i=1,n=0;i<=device_count;i++,n=n+10)
	        	{
	        		XmlTest test  =  new XmlTest(suite);
	        		HashMap<String, String> testCases = new HashMap<String, String>();
					
					for(int l=0;l<moduleNames.size();l++)
					{
						String modulenames[]= GenericLib.testConfigutration("./test-scripts/android/"+moduleNames.get(l)+".xlsx", "test execution");
						testCases.put(moduleNames.get(l),  Arrays.toString(modulenames).replace("[", "").replace("]",""));
					}
					
			        
	        		System.out.println("loop" +i);
	        		System.out.println(testCases.size());
	        		Set<String> keys = testCases.keySet();
					ArrayList<String> testNames = new ArrayList<String>(); 
			        for(String key: keys){
			            testNames.add(key);
			            System.out.println(key);
			        }
	        		
	        		
					HashMap<String,String> testParameters = new HashMap<String, String>();
					
					int m=i-1;
						if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 5).equalsIgnoreCase("Yes"))
						{
							if(device_count==1)
							{
								testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 1), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 1).trim());
							}
							else
							{
								testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 1), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 4)+":"+ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 6));	
							}
							testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 2), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 2));
							testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 3), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 3));
							
							testParameters.put("port",  String.valueOf(Integer.parseInt(ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 5))+n));			
							for(Map.Entry entry:testParameters.entrySet()){
							    System.out.println(entry.getKey() + " : " + entry.getValue());
							}
							String testname = Arrays.toString(testNames.toArray());
							//System.out.println(testname);
							
							test.setName(testname.replace("[", "").replace(",", "").replace("]", "").replace(" ", "_")+"_"+executionType+"device"+i);
							// test.setPreserveOrder("true"); 
							test.setPreserveOrder(true);
							test.setParameters(testParameters);
							
							//testParameters.clear();
							ArrayList<XmlClass>  classes =  new ArrayList<XmlClass>();
							Iterator it = testCases.entrySet().iterator();
						    while (it.hasNext()) {
						    	System.out.println("inside");
						        Map.Entry pair = (Map.Entry)it.next();
						        System.out.println(pair.getKey() + " = " + pair.getValue());
						        String[] testcases=	pair.getValue().toString().replace(",", "")	.split(" ");
						        System.out.println("Test cases length "+testcases.length);
						        for(int k=0;k<testcases.length;k++)
						        {
						        	//System.out.println("inside");
							    	String className = "com.cbt.docsapp."+executionType.toLowerCase()+".tests."+pair.getKey()+"."+testcases[k];
									System.out.println(className);
									classes.add(new XmlClass(className));
									
						        }
						        
						        test.setXmlClasses(classes);
								it.remove();
							
							
							
						}
					}
					
	        }
	        	
	        }
	        }
			else if(moduleNames.size() == 0)
			{
				System.out.println("Please select modules which you wan execute in under config_execution --> Test Execution Control");
			}
			else
			{
						System.out.println("Please configure > 1 device as Yes in deviceList --> config for Parallel execution");
			}
				
		suites.add(suite);
		suite.setName("Suite");		
		try
		{
			FileWriter writer = new FileWriter("testng.xml");
			writer.write(suite.toXml());
			writer.flush();			
			writer.close();		
		}
		catch(Exception e)
		{
		
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


	public static void xmlclassgeneration_parallel_web(Integer noOfBrowsers, String executionType) {
		// TODO Auto-generated method stub
		System.out.println(noOfBrowsers);
		System.out.println(executionType);
		
		if(listner_count<count)
		{
			suite.addListener("com.cbt.docsapp.reports.MyNXGTestListner");
			listner_count = listner_count+1;
		}
			suite.setGroupByInstances(false);
			suite.setThreadCount(noOfBrowsers);
			suite.setParallel("tests");
			
			ArrayList<String> moduleNames = new ArrayList<String>();
			for(int i=1;i<=7;i++)
			{
				//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 3));
				if(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 3).trim().equals("Yes"))
				{
					//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 2));
					moduleNames.add(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 2));
				}
				
			}
				ArrayList<String> browserNames = new ArrayList<String>();
				for(int i=1;i<=noOfBrowsers;i++)
				{
					browserNames.add(ExcelLibrary.getExcelData("./config/config.xlsx", "config", i, 3));
				}
		
				 if(noOfBrowsers>1)
			        {
			        	if(moduleNames.size() != 0)
						{
							
			        	
			        	
			        	for(int i=1;i<=noOfBrowsers;i++)
			        	{
			        		XmlTest test  =  new XmlTest(suite);
			        		HashMap<String, String> testCases = new HashMap<String, String>();
							
							for(int l=0;l<moduleNames.size();l++)
							{
								String modulenames[]= GenericLib.testConfigutration("./test-scripts/android/"+moduleNames.get(l)+".xlsx", "test execution");
								testCases.put(moduleNames.get(l),  Arrays.toString(modulenames).replace("[", "").replace("]",""));
							}
							
					        
			        		System.out.println("loop" +i);
			        		System.out.println(testCases.size());
			        		Set<String> keys = testCases.keySet();
							ArrayList<String> testNames = new ArrayList<String>(); 
					        for(String key: keys){
					            testNames.add(key);
					            System.out.println(key);
					        }
			        		
			        		
							HashMap<String,String> testParameters = new HashMap<String, String>();

							testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "config", 0, 3).replace(" ", ""), ExcelLibrary.getExcelData("./config/config.xlsx", "config", i,3));
									/*testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 2), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 2));
									testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 3), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(m)), 3));
									
									testParameters.put("port",  String.valueOf(Integer.parseInt(ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 5))+n));			
									*/for(Map.Entry entry:testParameters.entrySet()){
									    System.out.println(entry.getKey() + " : " + entry.getValue());
									}
									String testname = Arrays.toString(testNames.toArray());
									//System.out.println(testname);
									
									test.setName(testname.replace("[", "").replace(",", "").replace("]", "").replace(" ", "_")+"_"+executionType+"browser"+i);
									// test.setPreserveOrder("true"); 
									test.setPreserveOrder(true);
									test.setParameters(testParameters);
									
									//testParameters.clear();
									ArrayList<XmlClass>  classes =  new ArrayList<XmlClass>();
									Iterator it = testCases.entrySet().iterator();
								    while (it.hasNext()) {
								    	System.out.println("inside");
								        Map.Entry pair = (Map.Entry)it.next();
								        System.out.println(pair.getKey() + " = " + pair.getValue());
								        String[] testcases=	pair.getValue().toString().replace(",", "")	.split(" ");
								        System.out.println("Test cases length "+testcases.length);
								        for(int k=0;k<testcases.length;k++)
								        {
								        	//System.out.println("inside");
									    	String className = "com.cbt.docsapp."+executionType.toLowerCase()+".tests."+pair.getKey()+"."+testcases[k];
											System.out.println(className);
											classes.add(new XmlClass(className));
											
								        }
								        
								        test.setXmlClasses(classes);
										it.remove();
									
									
									
								}
							}
							
			        }
			        }
					else if(moduleNames.size() == 0)
					{
						System.out.println("Please select modules which you wan execute in under config_execution --> Test Execution Control");
					}
					else
					{
								System.out.println("Please configure > 1 browser in config.xlsx--> config for Parallel execution");
					}
						
				suites.add(suite);
				suite.setName("Suite");		
				try
				{
					FileWriter writer = new FileWriter("testng.xml");
					writer.write(suite.toXml());
					writer.flush();			
					writer.close();		
				}
				catch(Exception e)
				{
				
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				
		
	}
	
	
}
