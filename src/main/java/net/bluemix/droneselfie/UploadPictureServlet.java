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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.annotation.MultipartConfig;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.websocket.Session;
import org.apache.http.client.fluent.Request;
import org.ektorp.AttachmentInputStream;
import com.google.gson.Gson;

@WebServlet("/uploadpic")
@MultipartConfig(maxFileSize = 16177215)
public class UploadPictureServlet extends HttpServlet {

	private static final long serialVersionUID = -1623656324694499889L;

	public UploadPictureServlet() {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = request.getParameter("id");
		if (id == null)
			return;
		if (id.equals(""))
			return;
		InputStream inputStream = null;
		Part filePart = request.getPart("my_file");
		if (filePart != null) {
			inputStream = filePart.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(inputStream, baos);
			byte[] bytes = baos.toByteArray();
			ByteArrayInputStream bistream = new ByteArrayInputStream(bytes);
			String contentType = "image/png";
			
			java.util.Date date = new java.util.Date();
			String uniqueId = String.valueOf(date.getTime());
			AttachmentDoc document = new AttachmentDoc(id,
					AttachmentDoc.TYPE_FULL_PICTURE, date);
			DatabaseUtilities.getSingleton().getDB()
					.create(document.getId(), document);
			document = DatabaseUtilities.getSingleton().getDB()
					.get(AttachmentDoc.class, id);
			AttachmentInputStream ais = new AttachmentInputStream(id, bistream,
					contentType);
			DatabaseUtilities.getSingleton().getDB()
					.createAttachment(id, document.getRevision(), ais);

			javax.websocket.Session ssession;
			ssession = net.bluemix.droneselfie.SocketEndpoint.currentSession;
			if (ssession != null) {
				for (Session session : ssession.getOpenSessions()) {
					try {
						if (session.isOpen()) {
							session.getBasicRemote().sendText("fpic?id=" + id);
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}

			String alchemyUrl = "";
			String apiKey = ConfigUtilities.getSingleton().getAlchemyAPIKey();
			String bluemixAppName = ConfigUtilities.getSingleton().getBluemixAppName();
			if (bluemixAppName == null) {
				String host = request.getServerName();
				alchemyUrl = "http://access.alchemyapi.com/calls/url/URLGetRankedImageFaceTags?url=http://" + host +"/pic?id="
						+ id + "&apikey=" + apiKey + "&outputMode=json";
			}
			else {
				alchemyUrl = "http://access.alchemyapi.com/calls/url/URLGetRankedImageFaceTags?url=http://" + bluemixAppName +".mybluemix.net/pic?id="
						+ id + "&apikey=" + apiKey + "&outputMode=json";
			}
			org.apache.http.client.fluent.Request req = Request.Post(alchemyUrl);
			org.apache.http.client.fluent.Response res = req.execute();

			String output = res.returnContent().asString();
			Gson gson = new Gson();
			AlchemyResponse alchemyResponse = gson.fromJson(output,
					AlchemyResponse.class);
			if (alchemyResponse != null) {
				List<ImageFace> faces = alchemyResponse.getImageFaces();
				if (faces != null) {
					for (int i = 0; i < faces.size(); i++) {
						ImageFace face = faces.get(i);
						String sH = face.getHeight();
						String sPX = face.getPositionX();
						String sPY = face.getPositionY();
						String sW = face.getWidth();
						int height = Integer.parseInt(sH);
						int positionX = Integer.parseInt(sPX);
						int positionY = Integer.parseInt(sPY);
						int width = Integer.parseInt(sW);

						int fullPictureWidth = 640;
						int fullPictureHeight = 360;
						positionX = positionX - width / 2;
						positionY = positionY - height / 2;
						height = height * 2;
						width = width * 2;
						if (positionX < 0)
							positionX = 0;
						if (positionY < 0)
							positionY = 0;
						if (positionX + width > fullPictureWidth)
							width = width - (fullPictureWidth - positionX);
						if (positionY + height > fullPictureHeight)
							height = height - (fullPictureHeight - positionY);

						bistream = new ByteArrayInputStream(bytes);
						javaxt.io.Image image = new javaxt.io.Image(bistream);
						image.crop(positionX, positionY, width, height);
						byte[] croppedImage = image.getByteArray();
						
						ByteArrayInputStream bis = new ByteArrayInputStream(
								croppedImage);
						date = new java.util.Date();
						uniqueId = String.valueOf(date.getTime());
						document = new AttachmentDoc(uniqueId,
								AttachmentDoc.TYPE_PORTRAIT, date);
						DatabaseUtilities.getSingleton().getDB()
								.create(document.getId(), document);
						document = DatabaseUtilities.getSingleton().getDB()
								.get(AttachmentDoc.class, uniqueId);
						ais = new AttachmentInputStream(uniqueId, bis,
								contentType);
						DatabaseUtilities
								.getSingleton()
								.getDB()
								.createAttachment(uniqueId,
										document.getRevision(), ais);

						ssession = net.bluemix.droneselfie.SocketEndpoint.currentSession;
						if (ssession != null) {
							for (Session session : ssession.getOpenSessions()) {
								try {
									if (session.isOpen()) {
										session.getBasicRemote().sendText(
												"ppic?id=" + uniqueId);
									}
								} catch (IOException ioe) {
									ioe.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
