package com.cbt.docsapp.init;
import java.io.IOException;

import com.cbt.docsapp.generics.ExcelLibrary;
import com.cbt.docsapp.generics.MobileParameters;

public class PreExecutionForDeviceDetection {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		System.out.println(MobileParameters.getUDIDFromDeviceConnected());
		System.out.println(MobileParameters.getDeviceNameFromDeviceConnected());
		System.out.println(MobileParameters.getDeviceVersionFromDeviceConnected());
		System.out.println(MobileParameters.getDeviceIPFromDeviceConnected());

		int rowCount = ExcelLibrary.getExcelRowCount("./config/config.xlsx", "devicesList")+1;
		
		ExcelLibrary.writeExcelData("./config/config.xlsx", "devicesList", rowCount, 0, String.valueOf(rowCount));
		ExcelLibrary.writeExcelData("./config/config.xlsx", "devicesList", rowCount, 1, MobileParameters.getUDIDFromDeviceConnected());
		ExcelLibrary.writeExcelData("./config/config.xlsx", "devicesList", rowCount, 2, MobileParameters.getDeviceNameFromDeviceConnected());
		ExcelLibrary.writeExcelData("./config/config.xlsx", "devicesList", rowCount, 3, MobileParameters.getDeviceVersionFromDeviceConnected());	
		ExcelLibrary.writeExcelData("./config/config.xlsx", "devicesList", rowCount, 4, MobileParameters.getDeviceIPFromDeviceConnected());
		ExcelLibrary.writeExcelData("./config/config.xlsx", "devicesList", rowCount, 5, "Yes");
		ExcelLibrary.writeExcelData("./config/config.xlsx", "devicesList", rowCount, 6, "5555");
		MobileParameters.getudidConnected(MobileParameters.getUDIDFromDeviceConnected(), "5555");
	}
	
		
}


