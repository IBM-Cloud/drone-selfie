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
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ektorp.AttachmentInputStream;

@WebServlet("/pic")
public class PictureServlet extends HttpServlet {

	private static final long serialVersionUID = -1625556324694499109L;

	public PictureServlet() {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = request.getParameter("id");
		if (id == null)
			return;
		if (id.equals(""))
			return;

		String contentType = "image/png";
		AttachmentInputStream data = DatabaseUtilities.getSingleton().getDB()
				.getAttachment(id, id);
		response.setContentType(contentType);
		response.setContentLength(longToInt(data.getContentLength()));
		OutputStream out = response.getOutputStream();
		byte[] buffer = new byte[1024];
		int count = 0;
		while ((count = data.read(buffer)) >= 0) {
			out.write(buffer, 0, count);
		}
		out.close();
		data.close();
	}

	private static int longToInt(long numberAsLong) {
		if (numberAsLong < Integer.MIN_VALUE
				|| numberAsLong > Integer.MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		return (int) numberAsLong;
	}
}
