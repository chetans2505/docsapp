package com.cbt.docsapp.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cbt.docsapp.generics.ExcelLibrary;
import com.cbt.docsapp.generics.GenericLib;

public class GenerateSuiteFile {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
System.out.println(GenericLib.executionType());
		if(GenericLib.executionType() != null)
		{
			if(GenericLib.executionType().equalsIgnoreCase("WEB"))
			{
				
				if(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 1, 2).trim().equalsIgnoreCase("Sequential"))
				{
				
					System.out.println("Sequential");
					if(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 3, 2).trim().equalsIgnoreCase("1"))
					{
					HashMap<String,String> testParameters = new HashMap<String, String>();
					testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "config", 0, 3), ExcelLibrary.getExcelData("./config/config.xlsx", "config", 1, 3));
	
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
						if(moduleNames.size() != 0)
						{
							HashMap<String, String> testCases = new HashMap<String, String>();
						
								for(int i=0;i<moduleNames.size();i++)
								{
									String modulenames[]= GenericLib.testConfigutration("./test-scripts/web/"+moduleNames.get(i)+".xlsx", "test execution");
									testCases.put(moduleNames.get(i),  Arrays.toString(modulenames).replace("[", "").replace("]",""));
								}

							    GenerateXmlFile.xmlclassgeneration_sequential(testCases ,testParameters, GenericLib.executionType());	
						}
						else if(moduleNames.size() == 0)
						{
							System.out.println("Please select modules which you wan execute in under config_execution --> Test Execution Control");
						}
					
					
					}
					else
					{
						System.out.println("Please select browser count as 1 for sequential execution");
					}
				}
				if(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 1, 2).trim().equalsIgnoreCase("Parallel"))
				{
				
					System.out.println("Parallel");
					if(Integer.parseInt(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 3, 2).trim())>1)
					{
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
						if(moduleNames.size() != 0)
						{
							 GenerateXmlFile.xmlclassgeneration_parallel_web(Integer.valueOf(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 3, 2).trim()), GenericLib.executionType());	
						}
						else if(moduleNames.size() == 0)
						{
							System.out.println("Please select modules which you wan execute in under config_execution --> Test Execution Control");
						}
					}
					else
					{
						System.out.println("Please select >1 browser for parallel execution");
					}
				
				
			}
			}
		
			else if(GenericLib.executionType().equalsIgnoreCase("ANDROID"))
			{
				System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 5, 2));
				if(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 5, 2).trim().equalsIgnoreCase("Sequential"))
				{
					if(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 5, 3).trim().equalsIgnoreCase("1"))
					{
						//Add parameters picking code
						int rowCount = ExcelLibrary.getExcelRowCount("./config/config.xlsx", "devicesList");
						ArrayList<String> deviceCount = new ArrayList<String>();
						for(int i=1;i<=rowCount;i++)
						{
							if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
							{
								deviceCount.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
							}
						}
						if(String.valueOf(deviceCount.size()).equalsIgnoreCase("1"))
						{
							HashMap<String,String> testParameters = new HashMap<String, String>();
							for(int i=1;i<=rowCount;i++)
							{
								if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
								{
									testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 1), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 1).trim());
									testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 2), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 2));
									testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 3), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 3));
									testParameters.put("port", ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 5));
								}
							}
								ArrayList<String> moduleNames = new ArrayList<String>();
								for(int i=10;i<=16;i++)
								{
									//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 3));
									if(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 3).trim().equals("Yes"))
									{
										//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 2));
										moduleNames.add(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 2));
									}
									
								}
								if(moduleNames.size() != 0)
								{
									HashMap<String, String> testCases = new HashMap<String, String>();
								
										for(int i=0;i<moduleNames.size();i++)
										{
											String modulenames[]= GenericLib.testConfigutration("./test-scripts/android/"+moduleNames.get(i)+".xlsx", "test execution");
											testCases.put(moduleNames.get(i),  Arrays.toString(modulenames).replace("[", "").replace("]",""));
										}

									    GenerateXmlFile.xmlclassgeneration_sequential(testCases ,testParameters, GenericLib.executionType());	
								}
								else if(moduleNames.size() == 0)
								{
									System.out.println("Please select modules which you wan execute in under config_execution --> Test Execution Control");
								}
						}
						else
						{
							System.out.println("Please configure 1 device as Yes in deviceList --> config for Sequentail execution");
						}
					}
				
					else
					{
						System.out.println("Please select Number of devices as 1 in config --> config for Sequentail execution");
					}
				}
				
				if(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 5, 2).trim().equalsIgnoreCase("Parallel"))
				{
					if(Integer.valueOf(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 5, 3).trim())>1)
					{
						//Add parameters picking code
						int rowCount = ExcelLibrary.getExcelRowCount("./config/config.xlsx", "devicesList");
						ArrayList<String> deviceCount = new ArrayList<String>();
						
						for(int i=1;i<=rowCount;i++)
						{
							if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
							{
								deviceCount.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
								
							}
						}
								ArrayList<String> moduleNames = new ArrayList<String>();
								for(int i=10;i<=16;i++)
								{
									//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 3));
									if(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 3).trim().equals("Yes"))
									{
										//System.out.println(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 2));
										moduleNames.add(ExcelLibrary.getExcelData("./execution control/config_execution.xlsx", "Test Execution Control", i, 2));
									}
									
								}
								if(moduleNames.size() != 0)
								{
									 GenerateXmlFile.xmlclassgeneration_parallel(Integer.valueOf(deviceCount.size()), GenericLib.executionType());	
								}
								else if(moduleNames.size() == 0)
								{
									System.out.println("Please select modules which you wan execute in under config_execution --> Test Execution Control");
								}
					}
				
					else
					{
						System.out.println("Please select Number of devices as > 1 in config --> config for Parallel execution");
					}
				}
				
				
			}
			else if(GenericLib.executionType().equalsIgnoreCase("IOS"))
			{
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
			else
			{
				System.out.println("Please select execution type in config --> config");
			}
			
			
			
		
		
		
		
		
		
		
		
		
	}

	}
}
