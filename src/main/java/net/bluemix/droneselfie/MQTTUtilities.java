package net.bluemix.droneselfie;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTUtilities {
	
	static public String COMMAND_TAKE_PICTURE = "takepic";
	static public String COMMAND_LAND = "land";
	static public String COMMAND_ROTATE_CLOCKWISE = "rotatec";
	static public String COMMAND_ROTATE_COUNTER_CLOCKWISE = "rotatecc";
	static public String COMMAND_TAKEOFF = "takeoff";
	static public String COMMAND_UP = "up";
	static public String COMMAND_DOWN = "down";

	static public void sendMQTTMessage(String command, String callback) throws MqttException {

		String deviceId = ConfigUtilities.getSingleton().getIOTDeviceId();
		String apikey = ConfigUtilities.getSingleton().getIOTAPIKey();
		String apitoken = ConfigUtilities.getSingleton().getIOTAPIToken();
		String deviceType = ConfigUtilities.getSingleton().getIOTDeviceType();

		String org = null;
		String topic = "iot-2/type/" + deviceType + "/id/" + deviceId
				+ "/cmd/fly/fmt/json";
		int qos = 2;

		boolean configExists = true;
		if (apikey == null)
			configExists = false;
		else {
			if (apikey.equalsIgnoreCase(""))
				configExists = false;
		}
		if (apitoken == null)
			configExists = false;
		else {
			if (apitoken.equalsIgnoreCase(""))
				configExists = false;
		}
		String[] tokens = apikey.split("-", -1);
		if (tokens == null)
			configExists = false;
		else {
			if (tokens.length != 3)
				configExists = false;
			else {
				org = tokens[1];
			}
		}

		String broker = "tcp://" + org
				+ ".messaging.internetofthings.ibmcloud.com:1883";
		String clientId = "a:" + org + ":" + deviceId;

		String content = "";
		
		if (command.equalsIgnoreCase(COMMAND_TAKE_PICTURE)) {
			content = "{\"d\":{\"action\":\"#takepicture\",\"callback\":\""
				+ callback + "\"}}";
		} else if (command.equalsIgnoreCase(COMMAND_TAKEOFF)) {
			content = "{\"d\":{\"action\":\"#takeoff\"}}";
		} else if (command.equalsIgnoreCase(COMMAND_LAND)) {
			content = "{\"d\":{\"action\":\"#land\"}}";
		} else if (command.equalsIgnoreCase(COMMAND_ROTATE_CLOCKWISE)) {
			content = "{\"d\":{\"action\":\"#rotatec\"}}";
		} else if (command.equalsIgnoreCase(COMMAND_ROTATE_COUNTER_CLOCKWISE)) {
			content = "{\"d\":{\"action\":\"#rotatecc\"}}";
		} else if (command.equalsIgnoreCase(COMMAND_UP)) {
			content = "{\"d\":{\"action\":\"#up\"}}";
		} else if (command.equalsIgnoreCase(COMMAND_DOWN)) {
			content = "{\"d\":{\"action\":\"#down\"}}";
		}

		if (configExists == false)
			throw new MqttException(0);

		MemoryPersistence persistence = new MemoryPersistence();
		MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setPassword(apitoken.toCharArray());
		connOpts.setUserName(apikey);
		connOpts.setCleanSession(true);

		sampleClient.connect(connOpts);

		MqttMessage message = new MqttMessage(content.getBytes());

		message.setQos(qos);
		sampleClient.publish(topic, message);

		sampleClient.disconnect();
	}
}
