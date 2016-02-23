package com.perfectomobile.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;


public abstract class SpecificUtils {
	// Wind Tunnel: Gets the requested timer
	public static long timerGet(RemoteWebDriver driver, String timerType) {
		String command = "mobile:timer:info";
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", timerType);
		long result = (long) driver.executeScript(command, params);
		return result;
	}

	//returns ux timer
	public static long getUXTimer(RemoteWebDriver driver) {
		return timerGet(driver, "ux");
	}
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	//Switched driver context
	public static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	//Gets current context
	public static String getCurrentContextHandle(RemoteWebDriver driver) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context = (String) executeMethod.execute(
				DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	//Get available context
	public static List<String> getContextHandles(RemoteWebDriver driver) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		List<String> contexts = (List<String>) executeMethod.execute(
				DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}

	//Perform text check ocr function
	public static String ocrTextCheck(RemoteWebDriver driver, String text, int threshold, int timeout) {
		// Verify that arrived at the correct page, look for the Header Text
		
		Map<String, Object> params = new HashMap<>();
		params.put("content", text);
		params.put("timeout",timeout);
		params.put("measurement", "accurate");
	    params.put("source", "camera");
		params.put("analysis", "automatic");
		if (threshold>0)
			params.put("threshold", Integer.toString(threshold));
		return (String) driver.executeScript("mobile:checkpoint:text", params);

	}

	//Performs text click ocr function
	public static String ocrTextClick(RemoteWebDriver driver, String text, int threshold, int timeout) {
		Map<String, Object> params = new HashMap<>();
		params.put("content", text);
		params.put("timeout", Integer.toString(timeout));
		if (threshold>0)
			params.put("threshold", Integer.toString(threshold));
		return (String) driver.executeScript("mobile:text:select", params);
	}


	//Performs image click ocr function
	public static String ocrImageSelect(RemoteWebDriver driver, String img) {
		Map<String, Object> params = new HashMap<>();
		params.put("content", img);
		params.put("screen.top", "0%");
		params.put("screen.height", "100%");
		params.put("screen.left", "0%");
		params.put("screen.width", "100%");
		return (String) driver.executeScript("mobile:image:select", params);
	}
	
	//Performs image click ocr function
		public static String ocrImageCheck(RemoteWebDriver driver, String img, int timeout) {
			Map<String, Object> params = new HashMap<>();
			params.put("content", img);
			params.put("match", "bounded");
			params.put("imageBounds.needleBound", "30");
			params.put("timeout", Integer.toString(timeout));
			return (String) driver.executeScript("mobile:checkpoint:image", params);
		}

	//Launches application
	public static String launchApp(RemoteWebDriver driver, String app) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", app);
		return (String) driver.executeScript("mobile:application:open", params);
	}

	//Closes application
	public static String closeApp(RemoteWebDriver driver, String app) {
		String result = "false";
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("name", app);
			result = (String) driver.executeScript("mobile:application:close",
					params);
		} catch (Exception ex) {		
		}
		return result;
	}
	
	//Add a comment
	public static String comment(RemoteWebDriver driver, String comment) {
		Map<String, Object> params = new HashMap<>();
		params.put("text", comment);
		return (String) driver.executeScript("mobile:comment", params);
	}
	


	// checks if element exists
	public static Boolean elementExists(RemoteWebDriver driver,String xPath) {

		try {
			driver.findElementByXPath(xPath);
			return true;
		} catch (Exception ex) {
			return false;
		}

	}
	//public static nativeClick 
	public static Object PMexecutScript(RemoteWebDriver driver,String command,String... args){
		
		Map<String, Object> params = new HashMap<>();
		
		for( int i = 0; i < args.length; i=i+2){
			params.put(args[i], args[i+1]);
		}
		return driver.executeScript(command, params);
		
	}

}