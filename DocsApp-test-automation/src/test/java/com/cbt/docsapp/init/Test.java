package com.cbt.docsapp.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cbt.docsapp.generics.ExcelLibrary;
import com.cbt.docsapp.generics.GenericLib;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx" ,"config", 5, 3));
		/*int rowCount = ExcelLibrary.getExcelRowCount("./config/config.xlsx", "devicesList");
		System.out.println(rowCount);
		ArrayList<String> deviceCount = new ArrayList<String>();
		for(int i=1;i<=rowCount;i++)
		{
			if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
			{
				deviceCount.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
				System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 1));
				System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 1));
			}
		}
		if(String.valueOf(deviceCount.size()).equalsIgnoreCase("1"))
		{
			System.out.println("in");
		}*/

		//System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 5));

		/*String str[]= GenericLib.testConfigutration("./test-scripts/android/Module1.xlsx", "test execution");

		 String testString = Arrays.toString(str);
		 System.out.println(testString);*/
		/*String s1 = "TC_1, TC_2, TC_3";
		String[] s2=	s1.replace(",", "")	.split(" ");

		for(int i=0;i<s2.length;i++){
			System.out.println(s2[i]);
		}*/
		//System.out.println(s1.indexOf(","));
		int rowCount = ExcelLibrary.getExcelRowCount("./config/config.xlsx", "devicesList");
		ArrayList<String> deviceCount = new ArrayList<String>();
		ArrayList<String> siNo = new ArrayList<String>();
		for(int i=0;i<=rowCount;i++)
		{
			if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
			{
				deviceCount.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
				siNo.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 0));
				//System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 0));
			}
		}
		if(Integer.valueOf(deviceCount.size())>1)
		{
		HashMap<String,String> testParameters = new HashMap<String, String>();
		for(int i=0;i<siNo.size();i++)
		{
			System.out.println(Integer.parseInt(siNo.get(i)));
			if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(i)), 5).equalsIgnoreCase("Yes"))
			{
				System.out.println("check");
				testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 1), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(i)), 1).trim());
				testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 2), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(i)), 2));
				testParameters.put(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", 0, 3), ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", Integer.parseInt(siNo.get(i)), 3));
				testParameters.put("port", ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 5));			
			}
			for(Map.Entry entry:testParameters.entrySet()){
			    System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		}

		
		
		/*for(int i=0;i<siNo.size();i++)
		{
			System.out.println(siNo.get(i));
		}*/
		//System.out.println(siNo.size());
		
		}
	}

}
