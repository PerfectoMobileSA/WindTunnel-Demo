package com.perfectomobile.beton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.perfectomobile.test.BasicTest;
//import com.perfectomobile.test.BasicTest;

import com.perfectomobile.test.Init;

import io.appium.java_client.AppiumDriver;
import com.perfectomobile.utils.*;


import com.perfectomobile.dataDrivers.excelDriver.ExcelDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class CommunitySignIn_Android.
 */
public class DunkinWT extends BasicTest {

	public AppiumDriver<?> appiumDriver;
	public long measuredLaunchTime;
	public long measuredNavTime;
	public String appName;
	public boolean testFail;
	public String testMsg;
	public int testFailnum;
	
	@Test (dataProvider="dunkinData")
	public void testDunkin(String letsBlockSomeDomains, String launchTreshhold, String navigateTreshhold) throws Exception{
				
	  String str;
	  long launchT = Long.parseLong(launchTreshhold);
	long navigateT = Long.parseLong(navigateTreshhold);
		
		if(this.driver == null){
			/*resultSheet.setResultByColumnName(false, this.testName,"Persona="+this.caps.getCapability("windTunnelPersona"),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		//String errorFile = reportFailWithMessage(this.testMsg, this.caps.getCapability("windTunnelPersona").toString(),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		reportFailWithMessage2("Device not available: " + caps,"ExpectedLaunchTime="+launchTreshhold,"ActualLaunchTime="+Long.toString(this.measuredLaunchTime),"ExpectedNavTime="+navigateTreshhold,"ActualNavTime="+Long.toBinaryString(this.measuredNavTime) ,"Persona="+ this.caps.getCapability("windTunnelPersona").toString());
	 		//String linkToWTReport = (String) appiumDriver.getCapabilities().getCapability("windTunnelReportUrl");
	 		//resultSheet.addWTReportByRowNameAsLink("",  this.testName,"Persona="+this.caps.getCapability("windTunnelPersona"),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		
			Assert.fail("Device not available: " + caps);*/
			validateAndMessage("false", "Device not available: " + caps, true);
		}
		appiumDriver=(AppiumDriver<?>) this.driver;
	 	  
	 	try{
	 		
	 		//## ===>>	Block domains
	 		if (letsBlockSomeDomains.toLowerCase().equals("true"))
	 			WindTunnelUtils.blockDomains(appiumDriver, "www.google.com", "maps.google.com");
	 					
	 		//## ===>>	Start Script
	 		WindTunnelUtils.setPointOfInterest(appiumDriver, "Script Start", WindTunnelUtils.pointOfInterestStatus.SUCCESS.getName()); 

	 		// ## ===>>	Restart Application
	 		appiumDriver.closeApp();
	 		Thread.sleep(500);
	 		WindTunnelUtils.setPointOfInterest(appiumDriver, "Launch "+this.appName+"App", WindTunnelUtils.pointOfInterestStatus.SUCCESS.getName());

	 		//## ===>>	Start Application & device vitals measurement
	 		WindTunnelUtils.startAppVitals(appiumDriver, this.appName, true);
	 		appiumDriver.launchApp();
	 		//## ===>>	Validate the app launched- use OCR to get real UX metrics
	 		str = (String) SpecificUtils.PMexecutScript(appiumDriver, "mobile:checkpoint:text", "content", "\"REWARDS PROGRAM\"","timeout", "30","analysis", "automatic", "ignorecase","case","ignorespace","space","measurement", "accurate","source", "camera");
	 		
	 		
	 		measuredLaunchTime = SpecificUtils.getUXTimer(appiumDriver);
	 		validateAndMessage(str,"Failed to launch Application", true);
	 					
	 		str = WindTunnelUtils.setTimerReport(this.driver, measuredLaunchTime, launchT, this.appName + " Launch Time", "Launch");
	 		//if exceeded threshold, mark as fail but continue with script
	 		validateAndMessage(str,"Expected launch time="+ launchT+" But Actual launch time="+measuredLaunchTime, false);
	 		
	 		//## ===>>	Start Navigation to Stores:
			PerfectoUtils.switchToContext(driver, "NATIVE_APP");
			WindTunnelUtils.setPointOfInterest(appiumDriver, "Find Stores", WindTunnelUtils.pointOfInterestStatus.SUCCESS.getName());
			
			appiumDriver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
			try {
				appiumDriver.findElementByXPath("//*[contains(@resource-id,'maybe_later') or @label='Maybe Later']").click();
			} catch (Exception e) {
				validateAndMessage("false", "Failed to click Maybe Later button ", true);
			}
			try {
				appiumDriver.findElementByXPath("//*[contains(@resource-id,'home_find') or @label='button home find']").click();
			} catch (Exception e) {
				validateAndMessage("false", "Failed to click Find button button ", true);
			}
			/*try {
				appiumDriver.findElementByXPath("(//*[contains(@resource-id,'locatebtn') or @label='button map target'])[1]").click();
			} catch (Exception e) {
				
			}*/
			
			str= SpecificUtils.ocrImageCheck(appiumDriver, "PUBLIC:Dunkin\\Store_Found_"+this.caps.getCapability("platformName")+".png", 30);
			
			validateAndMessage(str,"Failed to Locate Store",true);
			
			this.measuredNavTime = SpecificUtils.getUXTimer(this.driver);			
	 		//## ===>>	get directions to store
			Thread.sleep(1000);
			SpecificUtils.PMexecutScript(appiumDriver, "mobile:button-image:click", "label","PUBLIC:Dunkin\\Store_Found_"+this.caps.getCapability("platformName")+".png","timeout","15","match","bounded","imageBounds.needleBound", "30");

			/*PerfectoUtils.ocrTextCheck(this.driver, "Get Directions", 95, 60);
			this.measuredNavTime[counter] += PerfectoUtils.getUXTimer(this.driver);*/
			try {
				appiumDriver.findElementByXPath("//*[contains(@resource-id,'desc') or @label='Get Directions']").click();
			} catch (Exception e) {
				validateAndMessage("false", "Failed to get the get directions page ", true);
			}
			
			
			this.measuredNavTime += SpecificUtils.getUXTimer(this.driver); 
			
			WindTunnelUtils.setPointOfInterest(appiumDriver, "Navigate", WindTunnelUtils.pointOfInterestStatus.SUCCESS.getName());
			
			str = (String) SpecificUtils.PMexecutScript(appiumDriver,"mobile:checkpoint:text","content", "'Start' 'START NAVIGATION' 'Fasetest route'","timeout","20","context", "body","threshold", "99","ignorecase","case","target", "any","analysis", "automatic","measurement", "accurate","source", "camera");
			validateAndMessage(str, "Failed to get Directions", true);
			
			//"measurement", "accurate","source", "camera",
	 					
	 		//## ===>>	How long did it take to launch?
			this.measuredNavTime += SpecificUtils.getUXTimer(appiumDriver);
			str = WindTunnelUtils.setTimerReport(appiumDriver, this.measuredNavTime, navigateT, this.appName + " Navigate to store", "Nav 2 store");
			validateAndMessage(str, "Expected Nav time="+ navigateT+" But Actual nav time="+measuredNavTime, false);
			
			WindTunnelUtils.stopVitals(appiumDriver);
	 		
	 	}
	 	catch(Exception e){
//	 		e.printStackTrace();
//	 		resultSheet.setResultByColumnName(false, this.testName, username, password, message);
			testFail = true;
//        	String errorFile = PerfectoAppiumUtils.takeScreenshot(driver);
//        	resultSheet.addScreenshotByRowNameAsLink(errorFile, this.testName, username, password, message);
//    		Reporter.log(e.toString());
//    		Reporter.log("Error screenshot saved in file: " + errorFile);
//    		Reporter.log("<br> <img src=" + errorFile + ".png style=\"max-width:50%;max-height:50%\" /> <br>");
	 	} 
	 
	 	if(testFail){
	 		resultSheet.setResultByColumnName(false, this.testName,"Persona="+this.caps.getCapability("windTunnelPersona"),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		//String errorFile = reportFailWithMessage(this.testMsg, this.caps.getCapability("windTunnelPersona").toString(),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		reportFailWithMessage2(this.testMsg,"ExpectedLaunchTime="+launchTreshhold,"ActualLaunchTime="+Long.toString(this.measuredLaunchTime),"ExpectedNavTime="+navigateTreshhold,"ActualNavTime="+Long.toBinaryString(this.measuredNavTime) ,"Persona="+ this.caps.getCapability("windTunnelPersona").toString());
	 		String linkToWTReport = (String) appiumDriver.getCapabilities().getCapability("windTunnelReportUrl");
	 		resultSheet.addWTReportByRowNameAsLink(linkToWTReport,  this.testName,"Persona="+this.caps.getCapability("windTunnelPersona"),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		Assert.fail(this.testMsg);
	       
	    }else {
	    	resultSheet.setResultByColumnName(true, this.testName,"Persona="+this.caps.getCapability("windTunnelPersona"),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		reportPass("passed", "","ExpectedLaunchTime="+launchTreshhold,"ActualLaunchTime="+Long.toString(this.measuredLaunchTime),"ExpectedNavTime="+navigateTreshhold,"ActualNavTime="+Long.toBinaryString(this.measuredNavTime) ,"Persona="+ this.caps.getCapability("windTunnelPersona").toString());
	 		String linkToWTReport = (String) appiumDriver.getCapabilities().getCapability("windTunnelReportUrl");
	 		resultSheet.addWTReportByRowNameAsLink(linkToWTReport,  this.testName,"Persona="+this.caps.getCapability("windTunnelPersona"),"launchTreshHold="+launchTreshhold, "navigateTreshhold="+navigateTreshhold);
	 		
	    }
	 	
		
       

	}

	/**
	 * Search items data.
	 *
	 * @return the object[][]
	 * @throws Exception the exception
	 */
	@DataProvider (name = "dunkinData", parallel = false)
	public Object[][] searchItemsData() throws Exception{
		  ExcelDriver ed = new ExcelDriver(sysProp.get("inputWorkbook"), sysProp.get("dunkinSheet"), false);
		  Object[][] s = ed.getData(3);

		  return s;
	}
	
	/**
	 * Instantiates a new community .
	 *
	 * @param caps the caps
	 */
	@Factory(dataProvider="factoryData")
	public DunkinWT(DesiredCapabilities caps) {
		super(caps);
		
		this.testFail = false;
		this.testMsg = "";
		this.testFailnum=0;
		this.appName = "Dunkin'";
		this.measuredLaunchTime = 0;
		this.measuredNavTime = 0;
		
	}
	
	private void validateAndMessage(String str, String msg, boolean throwException){
		if (str.equals("false")){
			++this.testFailnum;
 			this.testFail = true;
 			this.testMsg += "Fail#"+this.testFailnum+":"+msg+"*** ";
			String errorFile = PerfectoUtils.takeScreenshot(appiumDriver);
			Reporter.log("Error screenshot saved in file: " + errorFile);
			if (throwException)
				throw new IllegalStateException();
 		}
	}
}
