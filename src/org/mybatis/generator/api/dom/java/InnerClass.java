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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * This class encapsulates the idea of an inner class - it has methods that make
 * it easy to generate inner classes.
 * 
 * @author Jeff Butler
 */
public class InnerClass extends AbstractJavaType {

    private List<TypeParameter> typeParameters = new ArrayList<>();

    private FullyQualifiedJavaType superClass;

    private boolean isAbstract;

    private List<InitializationBlock> initializationBlocks = new ArrayList<>();
    
    private boolean isFinal;
    
    
    //
    //private FullyQualifiedJavaType type;
    //private Set<FullyQualifiedJavaType> superInterfaceTypes;
    //private List<Method> methods;
    //private List<Field> fields;
    //private List<InnerClass> innerClasses;
    //private List<InnerEnum> innerEnums;
    
    public InnerClass(FullyQualifiedJavaType type) {
        super(type);
        this.type = type;
		fields = new ArrayList<Field>();
		innerClasses = new ArrayList<InnerClass>();
		innerEnums = new ArrayList<InnerEnum>();
		superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
		methods = new ArrayList<Method>();
		initializationBlocks = new ArrayList<InitializationBlock>();
    }

    public InnerClass(String type) {
        super(type);
    }

    public Optional<FullyQualifiedJavaType> getSuperClass() {
        return Optional.ofNullable(superClass);
    }

    public void setSuperClass(FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }

    public void setSuperClass(String superClassType) {
        this.superClass = new FullyQualifiedJavaType(superClassType);
    }

    public List<TypeParameter> getTypeParameters() {
        return this.typeParameters;
    }

    public void addTypeParameter(TypeParameter typeParameter) {
        this.typeParameters.add(typeParameter);
    }

    public List<InitializationBlock> getInitializationBlocks() {
        return initializationBlocks;
    }

    public void addInitializationBlock(InitializationBlock initializationBlock) {
        initializationBlocks.add(initializationBlock);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbtract) {
        this.isAbstract = isAbtract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public String getFormattedContent(int indentLevel) {
		StringBuilder sb = new StringBuilder();

		addFormattedJavadoc(sb, indentLevel);
		addFormattedAnnotations(sb, indentLevel);

		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append(getVisibility().getValue());

		if (isAbstract()) {
			sb.append("abstract "); //$NON-NLS-1$
		}

		if (isStatic()) {
			sb.append("static "); //$NON-NLS-1$
		}

		if (isFinal()) {
			sb.append("final "); //$NON-NLS-1$
		}

		sb.append("class "); //$NON-NLS-1$
		sb.append(getType().getShortName());

		if (superClass != null) {
			sb.append(" extends "); //$NON-NLS-1$
			sb.append(superClass.getShortName());
		}

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

		Iterator<Field> fldIter = fields.iterator();
		while (fldIter.hasNext()) {
			OutputUtilities.newLine(sb);
			Field field = fldIter.next();
			sb.append(field.getFormattedContent(indentLevel));
			if (fldIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (initializationBlocks.size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<InitializationBlock> blkIter = initializationBlocks.iterator();
		while (blkIter.hasNext()) {
			OutputUtilities.newLine(sb);
			InitializationBlock initializationBlock = blkIter.next();
			sb.append(initializationBlock.getFormattedContent(indentLevel));
			if (blkIter.hasNext()) {
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

	/**
	 * @return type
	 */
	public FullyQualifiedJavaType getType() {
		return type;
	}

	/**
	 * @param type 要设置的 type
	 */
	public void setType(FullyQualifiedJavaType type) {
		this.type = type;
	}

	/**
	 * @return superInterfaceTypes
	 */
	public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
		return superInterfaceTypes;
	}

	/**
	 * @param superInterfaceTypes 要设置的 superInterfaceTypes
	 */
	public void setSuperInterfaceTypes(Set<FullyQualifiedJavaType> superInterfaceTypes) {
		this.superInterfaceTypes = superInterfaceTypes;
	}

	/**
	 * @return methods
	 */
	public List<Method> getMethods() {
		return methods;
	}

	/**
	 * @param methods 要设置的 methods
	 */
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	/**
	 * @return fields
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields 要设置的 fields
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * @return innerClasses
	 */
	public List<InnerClass> getInnerClasses() {
		return innerClasses;
	}

	/**
	 * @param innerClasses 要设置的 innerClasses
	 */
	public void setInnerClasses(List<InnerClass> innerClasses) {
		this.innerClasses = innerClasses;
	}

	/**
	 * @return innerEnums
	 */
	public List<InnerEnum> getInnerEnums() {
		return innerEnums;
	}

	/**
	 * @param innerEnums 要设置的 innerEnums
	 */
	public void setInnerEnums(List<InnerEnum> innerEnums) {
		this.innerEnums = innerEnums;
	}

	/**
	 * @param typeParameters 要设置的 typeParameters
	 */
	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}

	/**
	 * @param initializationBlocks 要设置的 initializationBlocks
	 */
	public void setInitializationBlocks(List<InitializationBlock> initializationBlocks) {
		this.initializationBlocks = initializationBlocks;
	}
}
