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

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.OutputUtilities;

public class XmlElement extends Element {

    private List<Attribute> attributes = new ArrayList<>();

    private List<Element> elements = new ArrayList<>();

    private String name;

    public XmlElement(String name) {
        super();
        this.name = name;
    }

    /**
     * Copy constructor. Not a truly deep copy, but close enough for most purposes.
     *
     * @param original
     *            the original
     */
    public XmlElement(XmlElement original) {
        super();
        attributes.addAll(original.attributes);
        elements.addAll(original.elements);
        this.name = original.name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public List<Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void addElement(int index, Element element) {
        elements.add(index, element);
    }

    public String getName() {
        return name;
    }
    
    public boolean hasChildren() {
        return elements.size() > 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <R> R accept(ElementVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
	public String getFormattedContent(int indentLevel) {
		StringBuilder sb = new StringBuilder();

		OutputUtilities.xmlIndent(sb, indentLevel);
		sb.append('<');
		sb.append(name);

		for (Attribute att : attributes) {
			sb.append(' ');
			sb.append(att.getFormattedContent());
		}

		if (elements.size() > 0) {
			sb.append(" >"); //$NON-NLS-1$
			for (Element element : elements) {
				OutputUtilities.newLine(sb);
				sb.append(element.getFormattedContent(indentLevel + 1));
			}
			OutputUtilities.newLine(sb);
			OutputUtilities.xmlIndent(sb, indentLevel);
			sb.append("</"); //$NON-NLS-1$
			sb.append(name);
			sb.append('>');

		} else {
			sb.append(" />"); //$NON-NLS-1$
		}

		return sb.toString();
	}
    
    /**
     * removeElement
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     * @param index
     */
    public void removeElement(int index) {
		elements.remove(index);
	}

}