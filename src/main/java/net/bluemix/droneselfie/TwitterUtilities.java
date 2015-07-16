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

import org.ektorp.AttachmentInputStream;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterUtilities {

	private static TwitterUtilities singleton;

	static public TwitterUtilities getSingleton() {
		if (singleton == null) {
			singleton = new TwitterUtilities();
		}
		return singleton;
	}

	private TwitterUtilities() {
	}

	public String tweetPicture(String pictureId, String text) {
		String message = "Picture taken via the #bluemix Selfie Drone";
		if (text != null) {
			if (!text.equalsIgnoreCase("")) message = text;
		}
		if (pictureId == null) return null;
		if (pictureId.equalsIgnoreCase("")) return null;
		
		if (message.length() > 100) message = message.substring(0, 100);
			
		return tweet(pictureId, message);			
	}	
	
	private String tweet(String pictureId, String message) {
		String output = null;
		if (message == null) return null;
		if (message.equalsIgnoreCase("")) return null;
		
		try {
			String consumerKey = ConfigUtilities.getSingleton().getTwitterConsumerKey();
			String consumerSecret = ConfigUtilities.getSingleton().getTwitterConsumerSecret();
			String accessToken = ConfigUtilities.getSingleton().getTwitterAccessToken();
			String accessTokenSecret = ConfigUtilities.getSingleton().getTwitterAccessTokenSecret();
			
			TwitterFactory twitterFactory = new TwitterFactory();			 
	        Twitter twitter = twitterFactory.getInstance();
	        twitter.setOAuthConsumer(consumerKey, consumerSecret);
	        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
	 
	        StatusUpdate statusUpdate = new StatusUpdate(message);
      
	        AttachmentInputStream data = DatabaseUtilities.getSingleton().getDB().getAttachment(pictureId, pictureId);
	        statusUpdate.setMedia("picture", data); 

	        Status status = twitter.updateStatus(statusUpdate);
	        if (status == null) return null;
	        output = "https://twitter.com/bluedroneselfie/status/" + String.valueOf(status.getId());

			return output;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}	
	
	public static void main(String[] args) {
		TwitterUtilities.getSingleton().tweetPicture("1436513841947", "test");
	}
}