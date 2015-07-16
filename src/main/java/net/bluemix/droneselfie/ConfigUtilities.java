/*
 * Copyright IBM Corp. 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bluemix.droneselfie;

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

public class ConfigUtilities {

	public ConfigUtilities() {
	}

	private static ConfigUtilities singleton;

	static public ConfigUtilities getSingleton() {
		if (singleton == null) {
			singleton = new ConfigUtilities();
			singleton.initialize();
		}
		return singleton;
	}

	private String DS_TW_CONSUMER_KEY;
	private String DS_TW_CONSUMER_SECRET;
	private String DS_TW_ACCESS_TOKEN;
	private String DS_TW_ACCESS_TOKEN_SECRET;
	private String DS_ALCHEMY;
	private String DS_IOT_DEVICEID;
	private String DS_IOT_DEVICETYPE;
	private String DS_IOT_APIKEY;
	private String DS_IOT_APITOKEN;
	private String DS_APPNAME;
	
	public String getIOTDeviceId() {
		return DS_IOT_DEVICEID;
	}
	
	public String getBluemixAppName() {
		if (DS_APPNAME == null) return null;
		if (DS_APPNAME.equalsIgnoreCase("")) return null;
		return DS_APPNAME;
	}
	
	public String getIOTDeviceType() {
		return DS_IOT_DEVICETYPE;
	}
	
	public String getIOTAPIKey() {
		return DS_IOT_APIKEY;
	}
	
	public String getIOTAPIToken() {
		return DS_IOT_APITOKEN;
	}
	
	public String getAlchemyAPIKey() {
		return DS_ALCHEMY;
	}

	public String getTwitterConsumerKey() {
		return DS_TW_CONSUMER_KEY;
	}
	
	public String getTwitterConsumerSecret() {
		return DS_TW_CONSUMER_SECRET;
	}
	
	public String getTwitterAccessToken() {
		return DS_TW_ACCESS_TOKEN;
	}
	
	public String getTwitterAccessTokenSecret() {
		return DS_TW_ACCESS_TOKEN_SECRET;
	}

	public void initialize() {

		try {
			String value = System.getenv("DS_LOCAL");
			if ((value != null) && (!value.equalsIgnoreCase(""))) {
					
				DS_TW_CONSUMER_KEY = System.getenv("DS_TW_CONSUMER_KEY");
				DS_TW_CONSUMER_SECRET = System.getenv("DS_TW_CONSUMER_SECRET");
				DS_TW_ACCESS_TOKEN = System.getenv("DS_TW_ACCESS_TOKEN");
				DS_TW_ACCESS_TOKEN_SECRET = System.getenv("DS_TW_ACCESS_TOKEN_SECRET");
				DS_ALCHEMY = System.getenv("DS_ALCHEMY");
				DS_IOT_DEVICEID = System.getenv("DS_IOT_DEVICEID");
				DS_IOT_DEVICETYPE = System.getenv("DS_IOT_DEVICETYPE");
				DS_IOT_APIKEY = System.getenv("DS_IOT_APIKEY");
				DS_IOT_APITOKEN = System.getenv("DS_IOT_APITOKEN");
				DS_APPNAME = System.getenv("DS_APPNAME");
			} else {
				String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

				if (VCAP_SERVICES != null) {
					Object jsonObject = JSON.parse(VCAP_SERVICES);
					JSONObject json = (JSONObject) jsonObject;
					String key = null;
					JSONArray list = null;
					java.util.Set<String> keys = json.keySet();
					for (String eachkey : keys) {
						if (eachkey.contains("user-provided")) {
							key = eachkey;
							list = (JSONArray) json.get(key);
							for (int i = 0; i < list.size(); i++) {
								JSONObject jsonService = (JSONObject) list.get(i);
								String serviceName = (String) jsonService.get("name");
								JSONObject credentials = (JSONObject) jsonService
										.get("credentials");
								if (serviceName != null) {
									if (serviceName.contains("config-drone")) {
										DS_TW_CONSUMER_KEY = (String) credentials.get("DS_TW_CONSUMER_KEY");
										DS_TW_CONSUMER_SECRET = (String) credentials.get("DS_TW_CONSUMER_SECRET");
										DS_TW_ACCESS_TOKEN = (String) credentials.get("DS_TW_ACCESS_TOKEN");
										DS_TW_ACCESS_TOKEN_SECRET = (String) credentials.get("DS_TW_ACCESS_TOKEN_SECRET");
										DS_IOT_DEVICEID = (String) credentials.get("DS_IOT_DEVICEID");
										DS_IOT_DEVICETYPE = (String) credentials.get("DS_IOT_DEVICETYPE");
										DS_IOT_APIKEY = (String) credentials.get("DS_IOT_APIKEY");
										DS_IOT_APITOKEN = (String) credentials.get("DS_IOT_APITOKEN");
									}
									else {
										DS_ALCHEMY = (String) credentials.get("apikey");
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
