package com.perfectomobile.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class WindTunnelUtils {
	
	private static final String WIND_TUNNEL_LOCATION_CAPABILITY = "windTunnelLocation";
	private static final String WIND_TUNNEL_LOCATION_ADDRESS_CAPABILITY = "windTunnelLocationAddress";
	private static final String WIND_TUNNEL_ORIENTATION_CAPABILITY = "windTunnelOrientation";
	private static final String WIND_TUNNEL_VNETWORK_CAPABILITY = "windTunnelVNetwork";
	private static final String WIND_TUNNEL_BACKGROUND_RUNNING_APPS_CAPABILITY = "windTunnelBackgroundRunningApps";

	private static final String IMAGE = "image";
	private static final String DESCRIPTION = "description";
	private static final String NAME = "name";
	private static final String PROPERTIES = "properties";
	private static final String SETTINGS = "settings";

	//All available windTunnel Settings
	//All available network profiles
	public static enum availableDefaultPersonas {
		EMPTY("Empty"), GEORGIA("Georgia"), ROSS("Ross"), PETER(
				"Peter"), SARA("Sara"), SAM(
				"Sam");

		public String name;

		
		availableDefaultPersonas(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	//All available windTunnel Settings
	public static enum availableWindTunnelSettings {
		windTunnelLocation, windTunnelLocationAddress, windTunnelBackgroundRunningApps, windTunnelVNetwork, windTunnelOrientation
	}

	//All available network profiles
	public static enum availableNetworkProfiles {
		Good2GGPRS("2G GPRS Good"), Average2GGPRS("2G GPRS Average"), Poor2GGPRS(
				"2G GPRS Poor"), Good2GEdge("2G Edge Good"), Average2GEdge(
				"2G Edge Average"), Poor2GEdge("2G Edge Poor"), Good3GUMTS(
				"3G UMTS Good"), Average3GUMTS("3G UMTS Average"), Poor3GUMTS(
				"3G UMTS Poor"), Good35GHSPA("3.5G HSPA Good"), Average35GHSPA(
				"3.5G HSPA Average"), Poor35GHSPA("3.5G HSPA Poor"), Good35GHSPAPLUS(
				"3.5G HSPA PLUS Good"), Average35GHSPAPLUS(
				"3.5G HSPA PLUS Average"), Poor35GHSPAPLUS(
				"3.5G HSPA PLUS Poor"), Good4GLTE("4G LTE Good"), Average4GLTE(
				"4G LTE Average"), Poor4GLTE("4G LTE Poor"), Good4GLTEAdvanced(
				"4G LTE Advanced Good"), Average4GLTEAdvanced(
				"4G LTE Advanced Average"), Poor4GLTEAdvanced(
				"4G LTE Advanced Poor");

		public String name;

		
		availableNetworkProfiles(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
	//Available point of interest status 
	public static enum pointOfInterestStatus {
		SUCCESS("Success"), FAILURE("Failure");

		public String name;

		
		pointOfInterestStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	//Available orientations
	public enum availableOrientation {
		landscape, portrait
	}


	
	/**
	 * Example:
	 * String persona = createWindTunnelPersona("Pedro", "This is Pedro's profile", "PUBLIC:personas\\Perdo.jpg", null, "Boston", 
	 * "landscape", "4G LTE Advanced Good", "Waze,YouTube");
	 * @param name persona name
	 * @param description persona description
	 * @param image persona image as repository item
	 * @param location location as coordinates with format latitude,longitude
	 * @param locationAddress location as address
	 * @param orientation device orientation (landscape or portrait)
	 * @param vnetworkProfile virtual network profile
	 * @param applications list of application names
	 * @return persona in json format
	 * @throws JsonProcessingException
	 */
	public static String createWindTunnelPersona(String name, String description, String image, String location, String locationAddress, String orientation, 
			String vnetworkProfile, String applications) throws JsonProcessingException {
		Map<String, Object> persona = new HashMap<>();
		Map<String, Object> properties = createProperties(name, description, image);
		persona.put(PROPERTIES, properties);
		Map<String, Object> settings = createSettings(location, locationAddress, orientation, vnetworkProfile, applications);
		persona.put(SETTINGS, settings);
		String json = convertToJson(persona);
		return json;
	

	}
	
	/**
	 * Example: String settings = createWindTunnelSettings(null, "Boston", "landscape", "4G LTE Advanced Good", "Waze,YouTube");
	 * @param location location as coordinates with format latitude,longitude
	 * @param locationAddress location as address
	 * @param orientation device orientation (landscape or portrait)
	 * @param vnetworkProfile virtual network profile
	 * @param applications list of application names
	 * @return settings in json format
	 * @throws JsonProcessingException
	 */
	public static String createWindTunnelSettings(String location, String locationAddress, String orientation, String vnetworkProfile, String applications) 
			throws JsonProcessingException {
		Map<String, Object> settings = createSettings(location, locationAddress, orientation, vnetworkProfile, applications);
		String json = convertToJson(settings);
		return json;
	}
	
	private static Map<String, Object> createProperties(String name, String description, String image) {
		Map<String, Object> properties =  new HashMap<>();
		if (name != null) {
			properties.put(NAME, name);
		}
		if (description != null) {
			properties.put(DESCRIPTION, description);
		}
		if (image != null) {
			properties.put(IMAGE, image);
		}
		return properties;
	}

	private static Map<String, Object> createSettings(String location, String locationAddress, String orientation, String vnetworkProfile, String applications) {
		Map<String, Object> settings =  new HashMap<>();
		if (location != null) {
			settings.put(WIND_TUNNEL_LOCATION_CAPABILITY, location);
		}
		if (locationAddress != null) {
			settings.put(WIND_TUNNEL_LOCATION_ADDRESS_CAPABILITY, locationAddress);
		}
		if (orientation != null) {
			settings.put(WIND_TUNNEL_ORIENTATION_CAPABILITY , orientation);
		}
		if (vnetworkProfile != null) {
			settings.put(WIND_TUNNEL_VNETWORK_CAPABILITY , vnetworkProfile);
		}
		if (applications != null) {
			settings.put(WIND_TUNNEL_BACKGROUND_RUNNING_APPS_CAPABILITY , applications);
		}
		return settings;
	}

	// Wind Tunnel: Adds a point of interest to the Wind Tunnel report
	public static String setPointOfInterest(RemoteWebDriver driver, String poiName, String poiStatus) {
		String command;
		Map<String, Object> params = new HashMap<String, Object>();
		command = "mobile:status:event";
		params.put("description", poiName);
		params.put("status", poiStatus);
		return (String) driver.executeScript(command, params);
	}

	// Wind Tunnel: Adds a timer report to the Wind Tunnel report
	public static String setTimerReport(RemoteWebDriver driver,long timerResult, long threshold,
			String description, String name) {
		String command;
		Map<String, Object> params = new HashMap<String, Object>();
		command = "mobile:status:timer";
		params.put("result", timerResult);
		params.put("threshold", threshold);
		params.put("description", description);
		params.put("name", name);
		return (String) driver.executeScript(command, params);
	}

	// start device vitals
	public static String startDeviceVitals(RemoteWebDriver driver,long interval) {
		Map<String, Object> params = new HashMap<>();
		List<String> vitals = new ArrayList<>();
		vitals.add("all");
		params.put("vitals", vitals);
		params.put("interval", Long.toString(interval));
		List<String> sources = new ArrayList<>();
		sources.add("device");
		params.put("sources", sources);
		return (String) driver.executeScript("mobile:monitor:start", params);
	}

	// start app vitals and optionally device vitals
	public static String startAppVitals(RemoteWebDriver driver,String app, boolean startDeviceVitals) {
		Map<String, Object> params = new HashMap<>();
		List<String> vitals = new ArrayList<>();
		vitals.add("all");
		params.put("vitals", vitals);
		params.put("interval", Long.toString(1));
		List<String> sources = new ArrayList<>();
		sources.add(app);
		if (startDeviceVitals)
			sources.add("device");
		params.put("sources", sources);
		return (String) driver.executeScript("mobile:monitor:start", params);
	}

	// stop vitals
	public static String stopVitals(RemoteWebDriver driver) {
		Map<String, Object> params = new HashMap<>();
		List<String> vitals = new ArrayList<>();
		vitals.add("all");
		params.put("vitals", vitals);
		return (String) driver.executeScript("mobile:monitor:stop", params);
	}
	
	// block domains
	public static String blockDomains(RemoteWebDriver driver, String... domains){
		if (domains.toString().isEmpty())
			return null;
		Map<String, Object> params = new HashMap<>();
		List<String> blockedDomains = new ArrayList<>();
		for (String domain:domains)
			blockedDomains.add(domain);
		params.put("blockedDestinations", blockedDomains);
		return (String) driver.executeScript("mobile:vnetwork:update", params);                     
	}
	
	// block domains
	public static String unblockDomains(RemoteWebDriver driver, String... domains){
		Map<String, Object> params = new HashMap<>();
		List<String> blockedDomains = new ArrayList<>();
		for (String domain:domains)
			blockedDomains.add(domain);
		params.put("blockedDestinations", "-"+blockedDomains);
		return (String) driver.executeScript("mobile:vnetwork:update", params);                     
	}

	private static String convertToJson(Map<String, Object> content) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(content);
		return json;
	}
	
}
