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

import java.util.logging.Logger;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.DesignDocument;

public class DatabaseDesign {

	private static final Logger LOGGER = Logger.getLogger(DatabaseDesign.class
			.getName());
	
	public static void main(String[] args) {
		LOGGER.info("initDatabaseDesign invoked");
		
		try {
			CouchDbConnector db = DatabaseUtilities.getSingleton().getDB();
			
			createDesign(db);

			LOGGER.info("initDatabaseDesign succeeded");
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("initDatabaseDesign failed");
		}
	}

	public static void createDesign(CouchDbConnector db) {
		if (!db.contains("_design/views")) {
			DesignDocument dd = new DesignDocument("_design/views");
			
			String mapFullPicturesByCreationDate = "function(doc) {\n  if (doc.type == \"FullPicture\") {\n           emit(doc.creationDate, doc);\n       }\n}";
			String mapPortraitsByCreationDate = "function(doc) {\n  if (doc.type == \"Portrait\") {\n           emit(doc.creationDate, doc);\n       }\n}";
				
			DesignDocument.View view = new DesignDocument.View(mapFullPicturesByCreationDate);
			dd.addView("fullPicturesByCreationDate", view);
		
			view = new DesignDocument.View(mapPortraitsByCreationDate);
			dd.addView("portraitsByCreationDate", view);
			
			db.create(dd);
		}
	}
}