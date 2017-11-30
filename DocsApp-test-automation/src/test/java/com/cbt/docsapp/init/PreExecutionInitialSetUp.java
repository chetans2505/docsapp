package com.cbt.docsapp.init;


import java.util.ArrayList;

import com.cbt.docsapp.generics.ExcelLibrary;
import com.cbt.docsapp.generics.GenericLib;
import com.cbt.docsapp.generics.MobileParameters;

public class PreExecutionInitialSetUp {

	public static void main(String args[])
	{
		
		/////////////////////////////////Device Count ///////////////////////////////////
		int rowCount = ExcelLibrary.getExcelRowCount("./config/config.xlsx", "devicesList");
		//System.out.println(rowCount);
		
		
		ArrayList<String> deviceCount = new ArrayList<String>();
		
		for(int i=1;i<=rowCount;i++)
		{
			 //System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
			if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
			{
				deviceCount.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
			}
			else
			{
				System.out.println("Please add valid devices in config -> device list");
			}
			
		}
		
		///////////////////////////////////////////////////////////////////////////////////

		
		try
		{
			if(GenericLib.executionType() != null && GenericLib.executionType().equalsIgnoreCase("Android"))
			{
				//System.out.println(String.valueOf(deviceCount.size()) );
				//System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 3));
				if(String.valueOf(deviceCount.size()).equalsIgnoreCase(ExcelLibrary.getExcelData("./config/config.xlsx", "config", 5, 3)))
				{
					System.out.println("Device count matched");
					
					/*To connect devices through wi fi*/
					//MobileParameters.getudidConnected(udid, portNumber);
					//MobileParameters.getDeviceConnected(ip, portNumber);
					/////////////////////////////////////
					
					ArrayList<String> udid = new ArrayList<String>();
					ArrayList<String> ip = new ArrayList<String>();
					ArrayList<String> portNumber = new ArrayList<String>();
					
					
					for(int i=1;i<=rowCount;i++)
					{
						 //System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
						if(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5).equalsIgnoreCase("Yes"))
						{
							//deviceCount.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 5));
							/*System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 1));
							System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 4));
							System.out.println(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 6));*/
							udid.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 1));
							ip.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 4));
							portNumber.add(ExcelLibrary.getExcelData("./config/config.xlsx", "devicesList", i, 6));
						}
					}
					if(udid != null && ip!=null && portNumber!=null)
					{
						if(udid.size() == ip.size() && udid.size() == portNumber.size())
						{
							
							for(int i=0;i<=udid.size();i++)
							{
									MobileParameters.getDeviceConnected(ip.get(i).toString(), portNumber.get(i).toString());
									try
									{
										Thread.sleep(10000);
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
							
							}
						}
					}
					
					
					
				}
				else
				{
					System.out.println("Not matched");
				}
				
				
			}
			else
			{
				System.out.println("Please select Android as execution type in config excel sheet");
			}
		}
		catch(Exception e)
		{
			
		}
        
    }
		
	/*	MobileParameters.getudidConnected(udid, portNumber);
		MobileParameters.getDeviceConnected(ip, portNumber);*/
		
		
	
	
	
	
}
