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

import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/SocketEndpoint")
public class SocketEndpoint {
	public static Session currentSession = null;

	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {
		currentSession = session;
	}

	@OnMessage
	public void receiveMessage(String message) {
	}

	public void sendMessage(String message){
		for (Session session: currentSession.getOpenSessions()){
			try {
				if (session.isOpen()){
					session.getBasicRemote().sendText(message);
				}
			} catch (IOException ioe){
				ioe.printStackTrace();
			}
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {	
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}
}
