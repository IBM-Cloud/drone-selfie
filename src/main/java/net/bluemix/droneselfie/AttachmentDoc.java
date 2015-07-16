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

import java.util.Date;

import org.ektorp.support.CouchDbDocument;

public class AttachmentDoc extends CouchDbDocument {

	private static final long serialVersionUID = 1L;
	
	final static String DOC_TYPE = "AttachmentDocument";
	public final static String TYPE_FULL_PICTURE = "FullPicture";
	public final static String TYPE_PORTRAIT = "Portrait";
	private String docType;
	private String type;
	Date creationDate;

	public AttachmentDoc() {		
		this.docType = DOC_TYPE;
	}
	 
	public AttachmentDoc(String id, String type, Date creationDate) {
		setId(id);
		this.type = type;
		this.creationDate = creationDate;
		this.docType = DOC_TYPE;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
