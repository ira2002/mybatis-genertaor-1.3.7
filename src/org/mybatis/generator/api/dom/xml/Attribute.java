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

public class Attribute implements Comparable<Attribute> {

    private String name;

    private String value;

    public Attribute(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(Attribute o) {
        if (this.name == null) {
            return o.name == null ? 0 : -1;
        } else {
            if (o.name == null) {
                return 0;
            } else {
                return this.name.compareTo(o.name);
            }
        }
    }
    
    /**
     * getFormattedContent
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     * @return
     */
    public String getFormattedContent() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("=\""); //$NON-NLS-1$
		sb.append(value);
		sb.append('\"');

		return sb.toString();
	}

	/**
	 * @param name 要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param value 要设置的 value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
