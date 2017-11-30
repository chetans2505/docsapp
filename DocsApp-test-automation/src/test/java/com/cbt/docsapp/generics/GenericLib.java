/***********************************************************************
* @author 			:		Srinivas Hippargi,Raghukiran
* @description		: 		Application Indepent Methods
* @methods 			: 		getConfigValue(),setConfigValue(),toReadExcelData(),setCellExeclData().
*/
package com.cbt.docsapp.generics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class GenericLib {

	public static String sFile;
	static public String sDirPath = System.getProperty("user.dir");
	public static String sConfigFile = null;
	public static String sKirwaFile =  null;
	public static String sTestDataFile = null;

	/*
	 * @author:Srinivas Hippargi  
	 * 
	 * Description: To read the basic environment settings data from config file based on Property file value
	 */
	public static String getCongigValue(String sFile, String sKey) {
		Properties prop = new Properties();
		String sValue = null;
		try {
			InputStream input = new FileInputStream(sFile);
			prop.load(input);
			sValue = prop.getProperty(sKey);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sValue;
	}

	/*
	 * @author:Raghukiran MR /Srinivas Hippargi /Prerana Bhatt 
	 * Description:To set the basic environment settings data from config file
	 */
	public static void setCongigValue(String sFile, String sKey, String sValue) {
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(new File(sFile));
			prop.load(fis);
			fis.close();

			FileOutputStream fos = new FileOutputStream(new File(sFile));
			prop.setProperty(sKey, sValue);
			prop.store(fos, "Updating folder path");
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @author: Srinivas Hippargi
	 * Description:To read test data from excel sheet based on TestcaseID
	 */
	public static String[] toReadExcelData(String sSheet, String sTestCaseID) {
		String sData[] = null;
		try {

			FileInputStream fis = new FileInputStream("ExcelSheetPath");
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			for (int i = 1; i <= iRowNum; i++) {
				if (sht.getRow(i).getCell(0).toString().equals(sTestCaseID)) {
					int iCellNum = sht.getRow(i).getPhysicalNumberOfCells();
					sData = new String[iCellNum];
					for (int j = 0; j < iCellNum; j++) {
						sData[j] = sht.getRow(i).getCell(j).getStringCellValue();
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}
	
	/*
	 * @author: Srinivas Hippargi
	 * Description:Method is used to set data in excel sheet
	 */

	public static void setCellData(String Result,String sSheet ,String  RowNum, int ColNum) throws Exception	{
		 
	       try{
	    	   FileInputStream fis = new FileInputStream("FileExcelSheetPath");
				Workbook wb = (Workbook) WorkbookFactory.create(fis);
				Sheet sht = wb.getSheet(sSheet);
				System.out.println("----------Sheet " + sSheet);
				Row rowNum=sht.getRow(Integer.parseInt(RowNum));
				System.out.println("----------RowNum " + RowNum);
				System.out.println("-----------ColNum " + ColNum);
				Cell cell=rowNum.getCell(ColNum);
				
				if (cell == null) {
					cell = rowNum.createCell(ColNum);
					cell.setCellValue(Result);
					}
				
				else {
					cell.setCellValue(Result);
				}
				FileOutputStream fileOut = new FileOutputStream("FileExcelSheetPath");
				
				wb.write(fileOut);
				fileOut.flush();

				fileOut.close();

				}catch(Exception e){

				throw (e);

				}
	}


	public static void executeBatchCommmand(String command)
	{
		try {
		      String line;
		      ArrayList<String> deviceUDID = new ArrayList<String>();
		      Process p = Runtime.getRuntime().exec("cmd /c "+command);
		      
		      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		      //System.out.println(line);
		      deviceUDID.add(line);
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		       System.out.println(line);  
		      }

		      bre.close();
		      p.waitFor();

		    }
		    catch (Exception err) {
		      err.printStackTrace();
		    }
	}

	public static String executionType()
	{
		
		String Selenium = ExcelLibrary.getExcelData("./config/config.xlsx", "config", 1, 4);
		String Android =ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 4);
		String ios =  ExcelLibrary.getExcelData("./config/config.xlsx", "config", 7, 4);
		
		String type = null;
		
		ArrayList<String> al = new ArrayList<String>();
		al.add(Selenium);
		al.add(Android);
		al.add(ios);
		
		String []names = new String[al.size()];
		al.toArray(names);
		
		 Set<String> store = new HashSet<>();
		 for (String name : names) {
	            if (store.add(name) == false) {
	                System.out.println("Please check the configuration in config.xlsx --> config");
	                break;
	            }
	            else if(Selenium.equalsIgnoreCase("Yes") && Android.equalsIgnoreCase("No") && ios.equalsIgnoreCase("No"))
				{
					//System.out.println("Please check the configuration in config.xlsx --> Local");
					type = "WEB";
					break;
				}
				
	            else if(Selenium.equalsIgnoreCase("No") && Android.equalsIgnoreCase("Yes") && ios.equalsIgnoreCase("No"))
				{
					//System.out.println("Please check the configuration in config.xlsx --> Local");
					type = "ANDROID";
					break;
				}
	            
	            else if(Selenium.equalsIgnoreCase("No") && Android.equalsIgnoreCase("No") && ios.equalsIgnoreCase("Yes"))
				{
					//System.out.println("Please check the configuration in config.xlsx --> Local");
					type = "ios";
					break;
				}   
	        }
		return type;	
}
	
	public static String[] testConfigutration(String filePath, String sheetName)
	{
		String [] str1 = null;
		int row_no = ExcelLibrary.getExcelRowCount(filePath, "test execution");
		
		ArrayList<String> al = new ArrayList<String>(); 		
		int cntr=0;
		for(int i=1;i<=row_no;i++)
		{
			
			if(ExcelLibrary.getExcelData(filePath, sheetName, i, 1).trim().equalsIgnoreCase("Yes"))
			{
				al.add(ExcelLibrary.getExcelData(filePath,sheetName, i, 0));
				cntr++;
			
			}

		}
		
		str1 = new String[cntr];
		
		for(int i=0;i<al.size();i++)
		{
			//System.out.println(al.get(i).toString());
			str1[i]= al.get(i).toString();
		}
		
		//return al;
		return str1;
	}
	
	public static void test()
	{
		
	}
	
	
	
}
