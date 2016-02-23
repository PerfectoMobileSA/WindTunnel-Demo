package com.perfectomobile.test;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfectomobile.dataDrivers.excelDriver.ExcelDriver;
import com.perfectomobile.utils.PerfectoUtils;

public class KeywordDrivenTestAdapter {
	
	ExcelDriver ed;
	Object [][] testFlow;
	
	public enum Commands{
		GET, TYPE, ELEMENTFIND, CLICK, TEXTCHECKPOINT
	}
	
	public KeywordDrivenTestAdapter(String path, String sheetName) throws Exception{
		ed = new ExcelDriver(path,sheetName,false);
		testFlow = ed.getData(3);
	}
	
	public void run(RemoteWebDriver driver){
		for(Object obj[] : testFlow){
			String command = (String) obj[0].toString().toLowerCase();
			String element = (String) obj[1];
			String data = (String) obj[2];
			switch(command){
			case "":
				System.out.println("Empty command");
				return;
			case "get":
				driver.get(data);
				break;
			case "type":
				PerfectoUtils.fluentWait(By.xpath(element),driver, 20).sendKeys(data);
				break;
			case "click":
				PerfectoUtils.fluentWait(By.xpath(element),driver, 20).click();
				break;
			case "find":
				PerfectoUtils.fluentWait(By.xpath(element),driver, 20);
				break;
			default:
				System.out.println("Unknown command: " + command);
				return;
			}
		}
	}
}
