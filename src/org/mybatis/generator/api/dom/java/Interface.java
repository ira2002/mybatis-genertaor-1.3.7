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

import static org.mybatis.generator.api.dom.OutputUtilities.calculateImports;
import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.api.dom.OutputUtilities.newLine;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Interface extends InnerInterface implements CompilationUnit {
    
    private Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();

    private Set<String> staticImports = new TreeSet<>();

    private List<String> fileCommentLines = new ArrayList<>();
    
    //
//    private FullyQualifiedJavaType type;
//	private Set<FullyQualifiedJavaType> superInterfaceTypes;
//	private List<Method> methods;
    
	/**
	 * 
	 * @param type
	 */
    public Interface(FullyQualifiedJavaType type) {
        super(type);
        this.type = type;
		superInterfaceTypes = new LinkedHashSet<FullyQualifiedJavaType>();
		methods = new ArrayList<Method>();
		importedTypes = new TreeSet<FullyQualifiedJavaType>();
		fileCommentLines = new ArrayList<String>();
		staticImports = new TreeSet<String>();
    }

    public Interface(String type) {
        this(new FullyQualifiedJavaType(type));
    }

    @Override
    public Set<FullyQualifiedJavaType> getImportedTypes() {
        return importedTypes;
    }

    @Override
    public void addImportedType(FullyQualifiedJavaType importedType) {
        if (importedType.isExplicitlyImported()
                && !importedType.getPackageName().equals(getType().getPackageName())) {
            importedTypes.add(importedType);
        }
    }

    @Override
    public void addFileCommentLine(String commentLine) {
        fileCommentLines.add(commentLine);
    }

    @Override
    public List<String> getFileCommentLines() {
        return fileCommentLines;
    }

    @Override
    public void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes) {
        this.importedTypes.addAll(importedTypes);
    }

    @Override
    public Set<String> getStaticImports() {
        return staticImports;
    }

    @Override
    public void addStaticImport(String staticImport) {
        staticImports.add(staticImport);
    }

    @Override
    public void addStaticImports(Set<String> staticImports) {
        this.staticImports.addAll(staticImports);
    }

    @Override
    public <R> R accept(CompilationUnitVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    /**
     * removeImportedType
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     * @param importedType
     */
    public void removeImportedType(FullyQualifiedJavaType importedType) {
		if (importedType.isExplicitlyImported() && !importedType.getPackageName().equals(type.getPackageName())) {
			importedTypes.remove(importedType);
		}
	}
    
    /**
	 * @return Returns the methods.
	 */
	public List<Method> getMethods() {
		return methods;
	}

	public void addMethod(Method method) {
		methods.add(method);
	}
	
	/**
	 * java8支持接口拥有具体的方法,则重载一个接口添加方法时是否为抽象方法
	 * @author	chdaba
	 * @version 1.0.0
	 * @date	2018年12月20日
	 * @param method	方法对象
	 * @param isAbstract	是否为抽象方法
	 */
	public void addMethod(Method method, boolean isAbstract) {
		method.setAbstract(isAbstract);
		methods.add(method);
	}
	
	/**
	 * @return Returns the type.
	 */
	public FullyQualifiedJavaType getType() {
		return type;
	}

	public FullyQualifiedJavaType getSuperClass() {
		// interfaces do not have superclasses
		return null;
	}
	
	public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
		return superInterfaceTypes;
	}
	
	public String getFormattedContent() {
		StringBuilder sb = new StringBuilder();

		for (String commentLine : fileCommentLines) {
			sb.append(commentLine);
			newLine(sb);
		}

		if (stringHasValue(getType().getPackageName())) {
			sb.append("package "); //$NON-NLS-1$
			sb.append(getType().getPackageName());
			sb.append(';');
			newLine(sb);
			newLine(sb);
		}

		for (String staticImport : staticImports) {
			sb.append("import static "); //$NON-NLS-1$
			sb.append(staticImport);
			sb.append(';');
			newLine(sb);
		}

		if (staticImports.size() > 0) {
			newLine(sb);
		}

		Set<String> importStrings = calculateImports(importedTypes);
		for (String importString : importStrings) {
			sb.append(importString);
			newLine(sb);
		}

		if (importStrings.size() > 0) {
			newLine(sb);
		}

		int indentLevel = 0;

		addFormattedJavadoc(sb, indentLevel);
		addFormattedAnnotations(sb, indentLevel);

		sb.append(getVisibility().getValue());

		if (isStatic()) {
			sb.append("static "); //$NON-NLS-1$
		}

		if (isFinal()) {
			sb.append("final "); //$NON-NLS-1$
		}

		sb.append("interface "); //$NON-NLS-1$
		sb.append(getType().getShortName());

		if (getSuperInterfaceTypes().size() > 0) {
			sb.append(" extends "); //$NON-NLS-1$

			boolean comma = false;
			for (FullyQualifiedJavaType fqjt : getSuperInterfaceTypes()) {
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

		Iterator<Method> mtdIter = getMethods().iterator();
		while (mtdIter.hasNext()) {
			newLine(sb);
			Method method = mtdIter.next();
			sb.append(method.getFormattedContent(indentLevel, true));
			if (mtdIter.hasNext()) {
				newLine(sb);
			}
		}

		indentLevel--;
		newLine(sb);
		javaIndent(sb, indentLevel);
		sb.append('}');

		return sb.toString();
	}
}
