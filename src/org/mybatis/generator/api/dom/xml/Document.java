/**
 *    Copyright 2006-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api.dom.xml;

import java.util.Optional;

import org.mybatis.generator.api.dom.OutputUtilities;

public class Document {
	
    private DocType docType;

    private XmlElement rootElement;
    
    //
    private String publicId;
    private String systemId;
    
    public Document(String publicId, String systemId) {
        super();
        docType = new PublicDocType(publicId, systemId);
    }

    public Document(String systemId) {
        super();
        docType = new SystemDocType(systemId);
    }

    public Document() {
        super();
    }

    public XmlElement getRootElement() {
        return rootElement;
    }

    public void setRootElement(XmlElement rootElement) {
        this.rootElement = rootElement;
    }
    
    public Optional<DocType> getDocType() {
        return Optional.ofNullable(docType);
    }
    
    public String getFormattedContent() {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"); //$NON-NLS-1$

		if (publicId != null && systemId != null) {
			OutputUtilities.newLine(sb);
			sb.append("<!DOCTYPE "); //$NON-NLS-1$
			sb.append(rootElement.getName());
			sb.append(" PUBLIC \""); //$NON-NLS-1$
			sb.append(publicId);
			sb.append("\" \""); //$NON-NLS-1$
			sb.append(systemId);
			sb.append("\" >"); //$NON-NLS-1$
		}

		OutputUtilities.newLine(sb);
		sb.append(rootElement.getFormattedContent(0));

		return sb.toString();
	}

	/**
	 * @return publicId
	 */
	public String getPublicId() {
		return publicId;
	}

	/**
	 * @param publicId 要设置的 publicId
	 */
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	/**
	 * @return systemId
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId 要设置的 systemId
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/**
	 * @param docType 要设置的 docType
	 */
	public void setDocType(DocType docType) {
		this.docType = docType;
	}
}
