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
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.mybatis.generator.api.dom.OutputUtilities;

public class Method extends JavaElement {

    private List<String> bodyLines = new ArrayList<>();

    private boolean constructor;

    private FullyQualifiedJavaType returnType;

    private String name;

    private List<TypeParameter> typeParameters = new ArrayList<>();

    private List<Parameter> parameters = new ArrayList<>();

    private List<FullyQualifiedJavaType> exceptions = new ArrayList<>();

    private boolean isSynchronized;

    private boolean isNative;

    private boolean isDefault;

    private boolean isAbstract;
    
    private boolean isFinal;
    
    /**
	 * 增加无参构造器
	 * @author 一剑倾天下
	 * @Date	2018-12-18
	 */
	public Method() {
		// TODO 自动生成的构造函数存根
	}
    
    public Method(String name) {
        this.name = name;
    }

    /**
     * Copy constructor. Not a truly deep copy, but close enough for most purposes.
     *
     * @param original
     *            the original
     */
    public Method(Method original) {
        super(original);
        this.bodyLines.addAll(original.bodyLines);
        this.constructor = original.constructor;
        this.exceptions.addAll(original.exceptions);
        this.name = original.name;
        this.typeParameters.addAll(original.typeParameters);
        this.parameters.addAll(original.parameters);
        this.returnType = original.returnType;
        this.isNative = original.isNative;
        this.isSynchronized = original.isSynchronized;
        this.isDefault = original.isDefault;
        this.isAbstract = original.isAbstract;
        this.isFinal = original.isFinal;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void addBodyLine(String line) {
        bodyLines.add(line);
    }

    public void addBodyLine(int index, String line) {
        bodyLines.add(index, line);
    }

    public void addBodyLines(Collection<String> lines) {
        bodyLines.addAll(lines);
    }

    public void addBodyLines(int index, Collection<String> lines) {
        bodyLines.addAll(index, lines);
    }
    
    /**
     * 增加快速清空<bodyLines>集合方法
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     */
    public void removeAllBodyLines() {
        bodyLines.clear();
    }
    
    public boolean isConstructor() {
        return constructor;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void addTypeParameter(TypeParameter typeParameter) {
        typeParameters.add(typeParameter);
    }

    public void addTypeParameter(int index, TypeParameter typeParameter) {
        typeParameters.add(index, typeParameter);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    public void addParameter(int index, Parameter parameter) {
        parameters.add(index, parameter);
    }

    public Optional<FullyQualifiedJavaType> getReturnType() {
        return Optional.ofNullable(returnType);
    }
    
    /**
     * 重载<returnType>的getter方法,直接返回该值
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     * @return
     */
    public FullyQualifiedJavaType getReturnType_2() {
        return returnType;
    }

    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    public List<FullyQualifiedJavaType> getExceptions() {
        return exceptions;
    }

    public void addException(FullyQualifiedJavaType exception) {
        exceptions.add(exception);
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public boolean isNative() {
        return isNative;
    }

    public void setNative(boolean isNative) {
        this.isNative = isNative;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public String getFormattedContent(int indentLevel, boolean interfaceMethod) {
		StringBuilder sb = new StringBuilder();

		addFormattedJavadoc(sb, indentLevel);
		addFormattedAnnotations(sb, indentLevel);

		OutputUtilities.javaIndent(sb, indentLevel);

		if (!interfaceMethod) {
			sb.append(getVisibility().getValue());

			if (isStatic()) {
				sb.append("static "); //$NON-NLS-1$
			}

			if (isFinal()) {
				sb.append("final "); //$NON-NLS-1$
			}

			if (bodyLines.size() == 0) {
				sb.append("abstract "); //$NON-NLS-1$
			}
		}

		if (!constructor) {
			if (getReturnType_2() == null) {
				sb.append("void"); //$NON-NLS-1$
			} else {
				sb.append(getReturnType_2().getShortName());
			}
			sb.append(' ');
		}

		sb.append(getName());
		sb.append('(');

		boolean comma = false;
		for (Parameter parameter : getParameters()) {
			if (comma) {
				sb.append(", "); //$NON-NLS-1$
			} else {
				comma = true;
			}

			sb.append(parameter.getFormattedContent());
		}

		sb.append(')');

		if (getExceptions().size() > 0) {
			sb.append(" throws "); //$NON-NLS-1$
			comma = false;
			for (FullyQualifiedJavaType fqjt : getExceptions()) {
				if (comma) {
					sb.append(", "); //$NON-NLS-1$
				} else {
					comma = true;
				}

				sb.append(fqjt.getShortName());
			}
		}

		// if no body lines, then this is an abstract method
		if (bodyLines.size() == 0) {
			sb.append(';');
		} else {
			sb.append(" {"); //$NON-NLS-1$
			indentLevel++;

			ListIterator<String> listIter = bodyLines.listIterator();
			while (listIter.hasNext()) {
				String line = listIter.next();
				if (line.startsWith("}")) { //$NON-NLS-1$
					indentLevel--;
				}

				OutputUtilities.newLine(sb);
				OutputUtilities.javaIndent(sb, indentLevel);
				sb.append(line);

				if ((line.endsWith("{") && !line.startsWith("switch")) //$NON-NLS-1$ //$NON-NLS-2$
						|| line.endsWith(":")) { //$NON-NLS-1$
					indentLevel++;
				}

				if (line.startsWith("break")) { //$NON-NLS-1$
					// if the next line is '}', then don't outdent
					if (listIter.hasNext()) {
						String nextLine = listIter.next();
						if (nextLine.startsWith("}")) { //$NON-NLS-1$
							indentLevel++;
						}

						// set back to the previous element
						listIter.previous();
					}
					indentLevel--;
				}
			}

			indentLevel--;
			OutputUtilities.newLine(sb);
			OutputUtilities.javaIndent(sb, indentLevel);
			sb.append('}');
		}

		return sb.toString();
	}
    
    /**
     * removeParameter
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     * @param index
     */
    public void removeParameter(int index) {
		parameters.remove(index);
	}
}
