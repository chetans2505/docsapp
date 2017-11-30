package com.cbt.docsapp.generics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MobileParameters {

	public static String getUDIDFromDeviceConnected()
	{
		
		String UDID = null;
		try {
		      String line;
		      ArrayList<String> deviceUDID = new ArrayList<String>();
		      Process p = Runtime.getRuntime().exec("cmd /c adb devices");
		      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		    //  System.out.println(line);
		      deviceUDID.add(line);
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		       // System.out.println(line);  
		      }

		      bre.close();
		      p.waitFor();
		      String dev[] = deviceUDID.get(1).toString().trim().split("device");
		      UDID = dev[0];
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		    }
			return UDID;
	}
	
	public static String getDeviceNameFromDeviceConnected()
	{
		
		String deviceName = null;
		try {
		      String line;
		      ArrayList<String> deviceUDID = new ArrayList<String>();
		      Process p = Runtime.getRuntime().exec("cmd /c adb shell getprop ro.product.model");
		      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		      //System.out.println(line);
		      deviceUDID.add(line);
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		      // System.out.println(line);  
		      }

		      bre.close();
		      p.waitFor();
		     // System.out.println(deviceUDID.get(0).toString());
		      deviceName = deviceUDID.get(0).toString();
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		    }
			return deviceName;
	}
	
	public static String getDeviceVersionFromDeviceConnected()
	{
		
		String deviceVersion = null;
		try {
		      String line;
		      ArrayList<String> deviceUDID = new ArrayList<String>();
		      Process p = Runtime.getRuntime().exec("cmd /c adb shell getprop ro.build.version.release");
		      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		      //System.out.println(line);
		      deviceUDID.add(line);
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		      // System.out.println(line);  
		      }

		      bre.close();
		      p.waitFor();
		     // System.out.println(deviceUDID.get(0).toString());
		      deviceVersion = deviceUDID.get(0).toString();
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		    }
			return deviceVersion;
	}
	
	
	public static String getDeviceIPFromDeviceConnected()
	{
		
		String ipV4 = null;
		try {
		      String line;
		      ArrayList<String> deviceIP = new ArrayList<String>();
		      Process p = Runtime.getRuntime().exec("cmd /c adb shell ifconfig");
		      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		     // System.out.println(line);
		      deviceIP.add(line);
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		       System.out.println(line);  
		      }

		      bre.close();
		      p.waitFor();

		      String ipconfig = deviceIP.toString();
		
		      String ipconf1[]= {};
		      if(ipconfig.replaceAll(" ", "").contains("rmnet0Linkencap"))
		      {
		    
		    	  ipconf1= ipconfig.replaceAll(" ", "").split("rmnet0Linkencap");
		    	  
		      }
		      else if(ipconfig.replaceAll(" ", "").contains("ccmni0Linkencap"))
		      {

		    	  ipconf1 = ipconfig.replaceAll(" ", "").split("ccmni0Linkencap");
		    	 
		      }
		      //String ipconf1[]= ipconfig.replaceAll(" ", "").split("rmnet0Linkencap");
		      String ipconf2[] = ipconf1[1].split("inetaddr:");
		      String ipconf3[] = ipconf2[1].split("Mask:");
		      //System.out.println(ipconf3[0]);
		      if(ipconf3[0].contains(":"))
		      {
		    	  String ipconf4[] = ipconf3[0].split(":");
		    	  //System.out.println(ipconf4[1]);
		    	  ipV4 = ipconf4[1];
		      }
		      else
		      {
		    	 // System.out.println(ipconf3[0]);
		    	  ipV4 = ipconf3[0];
		      }
		      
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		    }
			return ipV4;
	}
	
	public static void getudidConnected(String udid, String portNumber)
	{
		try {
		      String line;
		      Process p = Runtime.getRuntime().exec("cmd /c adb -s "+udid+" tcpip "+portNumber+"");
		      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		    	 System.out.println(line); 
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
	
	public static void getDeviceConnected(String ip, String portNumber)
	{
		try {
		      String line;
		      Process p = Runtime.getRuntime().exec("cmd /c adb connect "+ip+":"+portNumber+"");
		      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		      while ((line = bri.readLine()) != null) {
		    	 System.out.println(line); 
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
}
	

