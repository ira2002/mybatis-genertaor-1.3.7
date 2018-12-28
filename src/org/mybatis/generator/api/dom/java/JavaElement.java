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
import java.util.List;

import org.mybatis.generator.api.dom.OutputUtilities;

public abstract class JavaElement {

    private List<String> javaDocLines = new ArrayList<>();

    private JavaVisibility visibility = JavaVisibility.DEFAULT;

    private boolean isStatic;
    
    private boolean isFinal;

    private List<String> annotations = new ArrayList<>();

    public JavaElement() {
        super();
    }

    public JavaElement(JavaElement original) {
        this.annotations.addAll(original.annotations);
        this.isStatic = original.isStatic;
        this.javaDocLines.addAll(original.javaDocLines);
        this.visibility = original.visibility;
    }

    public List<String> getJavaDocLines() {
        return javaDocLines;
    }

    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }
    
    public JavaVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(JavaVisibility visibility) {
        this.visibility = visibility;
    }

    public void addSuppressTypeWarningsAnnotation() {
        addAnnotation("@SuppressWarnings(\"unchecked\")"); //$NON-NLS-1$
    }

    public boolean isStatic() {
        return isStatic;
    }
    
    public boolean isFinal() {
		return isFinal;
	}

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
    
    /**
     * 增加一个快速清空注解集合的方法
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     */
    public void removeAnnotation() {
        annotations.clear();
    } 
    
    /**
     * addDeprecated
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     */
    public void addDeprecated() {
		addAnnotation("@Deprecated"); //$NON-NLS-1$
	}	
    
    /**
     * addFormattedJavadoc
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     * @param sb
     * @param indentLevel
     */
    public void addFormattedJavadoc(StringBuilder sb, int indentLevel) {
		for (String javaDocLine : javaDocLines) {
			OutputUtilities.javaIndent(sb, indentLevel);
			sb.append(javaDocLine);
			OutputUtilities.newLine(sb);
		}
	}
    
    /**
     * addFormattedAnnotations
     * @author	chdaba
     * @version 1.0.0
     * @date	2018年12月19日
     * @param sb
     * @param indentLevel
     */
    public void addFormattedAnnotations(StringBuilder sb, int indentLevel) {
		for (String annotation : annotations) {
			OutputUtilities.javaIndent(sb, indentLevel);
			sb.append(annotation);
			OutputUtilities.newLine(sb);
		}
	}
}
