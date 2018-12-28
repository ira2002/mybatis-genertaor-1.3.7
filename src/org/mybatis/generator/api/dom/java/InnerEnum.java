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
package org.mybatis.generator.api.dom.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * This class encapsulates the idea of an inner enum - it has methods that make
 * it easy to generate inner enum.
 * 
 * @author Jeff Butler
 */
public class InnerEnum extends AbstractJavaType {

    private List<String> enumConstants = new ArrayList<>();

    private List<InitializationBlock> initializationBlocks = new ArrayList<>();
    
    
    //
//    private List<Field> fields;
//	private List<InnerClass> innerClasses;
//	private List<InnerEnum> innerEnums;
//	private FullyQualifiedJavaType type;
//	private Set<FullyQualifiedJavaType> superInterfaceTypes;
//	private List<Method> methods;

    
    public InnerEnum(FullyQualifiedJavaType type) {
        super(type);
    }

    public InnerEnum(String type) {
        super(type);
    }

    public List<String> getEnumConstants() {
        return enumConstants;
    }

    public void addEnumConstant(String enumConstant) {
        enumConstants.add(enumConstant);
    }

    public List<InitializationBlock> getInitializationBlocks() {
        return initializationBlocks;
    }

    public void addInitializationBlock(InitializationBlock initializationBlock) {
        initializationBlocks.add(initializationBlock);
    }
    
    public String getFormattedContent(int indentLevel) {
		StringBuilder sb = new StringBuilder();

		addFormattedJavadoc(sb, indentLevel);
		addFormattedAnnotations(sb, indentLevel);

		OutputUtilities.javaIndent(sb, indentLevel);
		if (getVisibility() == JavaVisibility.PUBLIC) {
			sb.append(getVisibility().getValue());
		}

		sb.append("enum "); //$NON-NLS-1$
		sb.append(getType().getShortName());

		if (superInterfaceTypes.size() > 0) {
			sb.append(" implements "); //$NON-NLS-1$

			boolean comma = false;
			for (FullyQualifiedJavaType fqjt : superInterfaceTypes) {
				if (comma) {
					sb.append(", "); //$NON-NLS-1$
				} else {
					comma = true;
				}

				sb.append(fqjt.getShortName());
			}
		}

		sb.append(" {"); //$NON-NLS-1$
		indentLevel++;

		Iterator<String> strIter = enumConstants.iterator();
		while (strIter.hasNext()) {
			OutputUtilities.newLine(sb);
			OutputUtilities.javaIndent(sb, indentLevel);
			String enumConstant = strIter.next();
			sb.append(enumConstant);

			if (strIter.hasNext()) {
				sb.append(',');
			} else {
				sb.append(';');
			}
		}

		if (fields.size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<Field> fldIter = fields.iterator();
		while (fldIter.hasNext()) {
			OutputUtilities.newLine(sb);
			Field field = fldIter.next();
			sb.append(field.getFormattedContent(indentLevel));
			if (fldIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (methods.size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<Method> mtdIter = methods.iterator();
		while (mtdIter.hasNext()) {
			OutputUtilities.newLine(sb);
			Method method = mtdIter.next();
			sb.append(method.getFormattedContent(indentLevel, false));
			if (mtdIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (innerClasses.size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<InnerClass> icIter = innerClasses.iterator();
		while (icIter.hasNext()) {
			OutputUtilities.newLine(sb);
			InnerClass innerClass = icIter.next();
			sb.append(innerClass.getFormattedContent(indentLevel));
			if (icIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (innerEnums.size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<InnerEnum> ieIter = innerEnums.iterator();
		while (ieIter.hasNext()) {
			OutputUtilities.newLine(sb);
			InnerEnum innerEnum = ieIter.next();
			sb.append(innerEnum.getFormattedContent(indentLevel));
			if (ieIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		indentLevel--;
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append('}');

		return sb.toString();
	}
}
