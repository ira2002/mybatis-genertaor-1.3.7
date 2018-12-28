package org.mybatis.generator.plugins.userdefined;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;


/**
 * 生成service类
 * @author	chdaba
 * @version	1.0.0
 * @date	2018年12月21日 下午4:13:08
 */
public class MybatisServicePlugin extends PluginAdapter {
	private FullyQualifiedJavaType slf4jLogger;
	private FullyQualifiedJavaType slf4jLoggerFactory;
	private FullyQualifiedJavaType serviceType;
	private FullyQualifiedJavaType daoType;
	private FullyQualifiedJavaType interfaceType;
	private FullyQualifiedJavaType pojoType;
	private FullyQualifiedJavaType pojoCriteriaType;
	private FullyQualifiedJavaType listType;
	private FullyQualifiedJavaType autowired;
	private FullyQualifiedJavaType service;
	private FullyQualifiedJavaType returnType;
	private FullyQualifiedJavaType pageList;
	private String servicePack;
	private String serviceImplPack;
	private String project;
	private String pojoUrl;

	private List<Method> methods;
	
	private boolean enableAnnotation = true;
	
	private boolean enableInsert = true;
	private boolean enableInsertSelective = true;
	private boolean enableDeleteByPrimaryKey = true;
	private boolean enableDeleteByExample = true;
	private boolean enableUpdateByExample = true;
	private boolean enableUpdateByExampleSelective = true;
	private boolean enableUpdateByPrimaryKey = true;
	private boolean enableUpdateByPrimaryKeySelective = true;

	//其他扩展功能：给insert*,update*,delete*方法增加@Transactional, 增加尚未开发
	//private boolean enableTransactionalAnnotation = false;
	
	
	public MybatisServicePlugin() {
		super();
		//log4j
		slf4jLogger = new FullyQualifiedJavaType("org.apache.log4j.Logger");
		//slf4j
		//slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
		//slf4jLoggerFactory = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
		methods = new ArrayList<Method>();
	}
	
	@Override
	public void setContext(Context context) {
		super.setContext(context);
	}
	
	@Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }
	
	
	@Override
	public boolean validate(List<String> warnings) {
		if (StringUtility.stringHasValue(properties.getProperty("enableAnnotation")))
			enableAnnotation = StringUtility.isTrue(properties.getProperty("enableAnnotation"));

		String enableInsert = properties.getProperty("enableInsert");

		String enableUpdateByExampleSelective = properties.getProperty("enableUpdateByExampleSelective");

		String enableInsertSelective = properties.getProperty("enableInsertSelective");

		String enableUpdateByPrimaryKey = properties.getProperty("enableUpdateByPrimaryKey");

		String enableDeleteByPrimaryKey = properties.getProperty("enableDeleteByPrimaryKey");

		String enableDeleteByExample = properties.getProperty("enableDeleteByExample");

		String enableUpdateByPrimaryKeySelective = properties.getProperty("enableUpdateByPrimaryKeySelective");

		String enableUpdateByExample = properties.getProperty("enableUpdateByExample");

		if (StringUtility.stringHasValue(enableInsert))
			this.enableInsert = StringUtility.isTrue(enableInsert);
		if (StringUtility.stringHasValue(enableUpdateByExampleSelective))
			this.enableUpdateByExampleSelective = StringUtility.isTrue(enableUpdateByExampleSelective);
		if (StringUtility.stringHasValue(enableInsertSelective))
			this.enableInsertSelective = StringUtility.isTrue(enableInsertSelective);
		if (StringUtility.stringHasValue(enableUpdateByPrimaryKey))
			this.enableUpdateByPrimaryKey = StringUtility.isTrue(enableUpdateByPrimaryKey);
		if (StringUtility.stringHasValue(enableDeleteByPrimaryKey))
			this.enableDeleteByPrimaryKey = StringUtility.isTrue(enableDeleteByPrimaryKey);
		if (StringUtility.stringHasValue(enableDeleteByExample))
			this.enableDeleteByExample = StringUtility.isTrue(enableDeleteByExample);
		if (StringUtility.stringHasValue(enableUpdateByPrimaryKeySelective))
			this.enableUpdateByPrimaryKeySelective = StringUtility.isTrue(enableUpdateByPrimaryKeySelective);
		if (StringUtility.stringHasValue(enableUpdateByExample))
			this.enableUpdateByExample = StringUtility.isTrue(enableUpdateByExample);

		servicePack = properties.getProperty("targetPackage");
		serviceImplPack = properties.getProperty("implementationPackage");
		project = properties.getProperty("targetProject");

		pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();

		if (enableAnnotation) {
			autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
			service = new FullyQualifiedJavaType("org.springframework.stereotype.Service");
		}
		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
		String table = introspectedTable.getBaseRecordType();
		String tableName = table.replaceAll(this.pojoUrl + ".", "");
		interfaceType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Service");

		// mybatis
		daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());

		// logger.info(toLowerCase(daoType.getShortName()));
		serviceType = new FullyQualifiedJavaType(serviceImplPack + "." + tableName + "ServiceImpl");

		pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);

		pojoCriteriaType = new FullyQualifiedJavaType(pojoUrl + "." + "Criteria");
		pageList = new FullyQualifiedJavaType(pojoUrl + "." + "PageList");
		listType = new FullyQualifiedJavaType("java.util.List");
		Interface interface1 = new Interface(interfaceType);
		TopLevelClass topLevelClass = new TopLevelClass(serviceType);
		// 导入必要的类
		addImport(interface1, topLevelClass);

		// 接口
		addService(interface1,introspectedTable, tableName, files);
		//log4j属性
		addLogger(topLevelClass);
		// 实现类
		addServiceImpl(topLevelClass,introspectedTable, tableName, files);

		return files;
	}
	
	/**
	 * 添加接口
	 * 
	 * @param tableName
	 * @param files
	 */
	protected void addService(Interface interface1,IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {

		interface1.setVisibility(JavaVisibility.PUBLIC);
		
		//添加接口注释
		context.getCommentGenerator().addInterfaceComment(interface1, introspectedTable);
		
		// 添加方法
		//Method method = countByExample(introspectedTable, tableName, true);
		
		Method method = getMethod("countByExample", introspectedTable, tableName, 3, "int", "根据条件查询记录总数", true);
		method.removeAllBodyLines();
		interface1.addMethod(method, true);
		
		method = getMethod("selectByPrimaryKey", introspectedTable, tableName, 2, "record", "根据主键查询记录", true);
		method.removeAllBodyLines();
		interface1.addMethod(method, true);

		method = getMethod("selectByExample", introspectedTable, tableName, 3, "list<record>", "根据条件查询记录集", true);
		method.removeAllBodyLines();
		interface1.addMethod(method, true);

		if (enableDeleteByPrimaryKey) {
			method = getMethod("deleteByPrimaryKey", introspectedTable, tableName, 2, "int", "根据主键删除记录", true);
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		if (enableUpdateByPrimaryKeySelective) {
			method = getMethod("updateByPrimaryKeySelective", introspectedTable, tableName, 1, "int", "根据主键更新属性不为空的记录", true);
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		if (enableUpdateByPrimaryKey) {
			method = getMethod("updateByPrimaryKey", introspectedTable, tableName, 1, "int", "根据主键更新记录", true);
			method.addAnnotation("@Deprecated");
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		if (enableDeleteByExample) {
			method = getMethod("deleteByExample", introspectedTable, tableName, 3, "int", "根据条件删除记录，注意：空条件会清空表记录，请谨慎使用。", true);
			method.addAnnotation("@Deprecated");
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		if (enableUpdateByExampleSelective) {
			method = getMethod("updateByExampleSelective", introspectedTable, tableName, 4, "int", "根据条件更新属性不为空的记录，注意：空条件会导致更新所有表记录。", true);
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		if (enableUpdateByExample) {
			method = getMethod("updateByExample", introspectedTable, tableName, 4, "int", "根据条件更新记录，注意：null值字段也会被更新，空条件会导致更新所有表记录。", true);
			method.addAnnotation("@Deprecated");
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		if (enableInsert) {
			method = getMethod("insert", introspectedTable, tableName, 1, "int", "保存记录,不管记录里面的属性是否为空", true);
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		if (enableInsertSelective) {
			method = getMethod("insertSelective", introspectedTable, tableName, 1, "int", "保存属性不为空的记录", true);
			method.removeAllBodyLines();
			interface1.addMethod(method, true);
		}
		
		//TODO by liping 
		//method = selectListPage(introspectedTable, tableName);
		//method.removeAllBodyLines();
		//interface1.addMethod(method, true);

		//1.2.x	自定义格式
		//GeneratedJavaFile file = new GeneratedJavaFile(interface1, project);	//自定义格式无法移除抽象方法体
		//mybatis-generator -v 1.3.7
		GeneratedJavaFile file = new GeneratedJavaFile(interface1,
				project,
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                 context.getJavaFormatter());
		
		files.add(file);
	}

	/**
	 * 添加实现类
	 * 
	 * @param introspectedTable
	 * @param tableName
	 * @param files
	 */
	protected void addServiceImpl(TopLevelClass topLevelClass,IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		// 设置实现的接口
		topLevelClass.addSuperInterface(interfaceType);

		if (enableAnnotation) {
			topLevelClass.addAnnotation("@Service");
			topLevelClass.addImportedType(service);
		}
		//添加默认格式注释
		//context.getCommentGenerator().addModelClassComment(topLevelClass, introspectedTable);
		//自定义的注释
		//addaddClassComment(topLevelClass, introspectedTable.getRemarks()+"\t<"+introspectedTable.getFullyQualifiedTable()+">");
		// 添加引用dao
		addField(topLevelClass, tableName);
		// 添加方法
		//topLevelClass.addMethod(countByExample(introspectedTable, tableName, false));
		topLevelClass.addMethod(getMethod("countByExample", introspectedTable, tableName, 3, "int", null, false));
		topLevelClass.addMethod(getMethod("selectByPrimaryKey", introspectedTable, tableName, 2, "record", null, false));
		topLevelClass.addMethod(getMethod("selectByExample", introspectedTable, tableName, 3, "list<record>", null, false));

		/**
		 * type 的意义	无参数	0 ;  pojo 1 ;key 2 ;example 3 ;pojo+example 4
		 */
		if (enableDeleteByPrimaryKey) {
			topLevelClass.addMethod(getMethod("deleteByPrimaryKey", introspectedTable, tableName, 2, "int", null, false));
		}
		if (enableUpdateByPrimaryKeySelective) {
			topLevelClass.addMethod(getMethod("updateByPrimaryKeySelective", introspectedTable, tableName, 1, "int", null, false));

		}
		if (enableUpdateByPrimaryKey) {
			topLevelClass.addMethod(getMethod("updateByPrimaryKey", introspectedTable, tableName, 1, "int", null, false));
		}
		if (enableDeleteByExample) {
			Method method = getMethod("deleteByExample", introspectedTable, tableName, 3, "int", null, false);
			method.addAnnotation("@Deprecated");
			topLevelClass.addMethod(method);
		}
		if (enableUpdateByExampleSelective) {
			topLevelClass.addMethod(getMethod("updateByExampleSelective", introspectedTable, tableName, 4, "int", null, false));
		}
		if (enableUpdateByExample) {
			topLevelClass.addMethod(getMethod("updateByExample", introspectedTable, tableName, 4, "int", null, false));
		}
		if (enableInsert) {
			topLevelClass.addMethod(getMethod("insert", introspectedTable, tableName, 1, "int", null, false));
		}
		if (enableInsertSelective) {
			topLevelClass.addMethod(getMethod("insertSelective", introspectedTable, tableName, 1, "int", null, false));
		}
		
		//TODO by liping 添加分页方法
		//topLevelClass.addMethod(selectListPage(introspectedTable, tableName));
		
		// 生成文件格式方法
		//GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project);	//可自定义格式
		//mybatis-generator -v 1.3.7
		GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass,				//默认格式
				project,
                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                context.getJavaFormatter());
		files.add(file);
	}

	/**
	 * 添加字段
	 * 
	 * @param topLevelClass
	 */
	protected void addField(TopLevelClass topLevelClass, String tableName) {
		// 添加 dao
		Field field = new Field();
		field.setName(toLowerCase(daoType.getShortName())); // 设置变量名
		topLevelClass.addImportedType(daoType);
		field.setType(daoType); // 类型
		field.setVisibility(JavaVisibility.PRIVATE);
		if (enableAnnotation) {
			field.addAnnotation("@Autowired");
		}
		topLevelClass.addField(field);
	}

	/**
	 * selectByPrimaryKey方法
	 * @param introspectedTable
	 * @param tableName
	 * @param isAbstract
	 * @return
	 */
	@Deprecated
	protected Method selectByPrimaryKey(IntrospectedTable introspectedTable, String tableName, boolean isAbstract) {
		Method method = new Method();
		method.setName("selectByPrimaryKey");
		method.setReturnType(pojoType);
		if (introspectedTable.getRules().generatePrimaryKeyClass()) {
			FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
			method.addParameter(new Parameter(type, "key"));
		} else {
			for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
				FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
				method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
			}
		}
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		// method.addBodyLine("try {");
		sb.append("return this.");
		sb.append(getDaoShort());
		sb.append("selectByPrimaryKey");
		sb.append("(");
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(");");
		method.addBodyLine(sb.toString());
		if(isAbstract) {
			method.setAbstract(isAbstract);
			context.getCommentGenerator().addMethodComment(method, introspectedTable, "根据主键查询记录");
		} else {
			method.addAnnotation("@Override");
		}
		// method.addBodyLine("} catch (Exception e) {");
		// method.addBodyLine("logger.error(\"Exception: \", e);");
		// method.addBodyLine("return null;");
		// method.addBodyLine("}");
		return method;
	}

	/**
	 * 添加方法
	 * 
	 */
	@Deprecated
	protected Method countByExample(IntrospectedTable introspectedTable, String tableName, boolean isAbstract) {
		Method method = new Method("countByExample");
		//method.setName("countByExample");
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.addParameter(new Parameter(pojoCriteriaType, "example"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		sb.append("int count = this.");
		sb.append(getDaoShort());
		sb.append("countByExample");
		sb.append("(");
		sb.append("example");
		sb.append(");");
		method.addBodyLine(sb.toString());
		method.addBodyLine("logger.debug(\"count: \"+count);");
		method.addBodyLine("return count;");
		if(isAbstract) {
			method.setAbstract(isAbstract);
			context.getCommentGenerator().addMethodComment(method, introspectedTable, "根据条件查询记录总数");
		} else {
			method.addAnnotation("@Override");
		}
		return method;
	}

	/**
	 * selectByExample
	 * @param introspectedTable	数据库表对象
	 * @param tableName	数据库表<entity>名
	 * @param isAbstract	是否为抽象方法
	 * @return
	 */
	@Deprecated
	protected Method selectByExample(IntrospectedTable introspectedTable, String tableName, boolean isAbstract) {
		Method method = new Method();
		method.setName("selectByExample");
		method.setReturnType(new FullyQualifiedJavaType("List<" + tableName + ">"));
		method.addParameter(new Parameter(pojoCriteriaType, "example"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		sb.append("return this.");
		sb.append(getDaoShort());
		if (introspectedTable.hasBLOBColumns()) {
			sb.append("selectByExampleWithoutBLOBs");
		} else {
			sb.append("selectByExample");
		}
		sb.append("(");
		sb.append("example");
		sb.append(");");
		method.addBodyLine(sb.toString());
		if(isAbstract) {
			method.setAbstract(isAbstract);
			context.getCommentGenerator().addMethodComment(method, introspectedTable, "根据条件查询记录集");
		} else {
			method.addAnnotation("@Override");
		}
		return method;
	}
	
	/**
	  * 自定义分页方法  	 
	 * @author	chdaba
	 * @version 1.0.0
	 * @date	2018年12月19日
	 * @param introspectedTable
	 * @param tableName
	 * @return
	 */
	@Deprecated
	protected Method selectListPage(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("selectListPage");
		method.setReturnType(new FullyQualifiedJavaType("PageList"));
		method.addParameter(new Parameter(pojoCriteriaType, "criteria"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		
		sb.append("if(criteria==null)");
		sb.append("\n{");
		sb.append("criteria = new Criteria();\n criteria.setMysqlLength(15);\n criteria.setMysqlOffset(1);\n }\n");
		sb.append("if(criteria.getMysqlOffset()==null)\n{");
		sb.append("criteria.setMysqlOffset(1);\n}\n");
		
		sb.append("if(criteria.getMysqlLength()==null)");
		sb.append("criteria.setMysqlLength(15);\n}\n");
		
		sb.append("int pageSize= criteria.getMysqlLength();\n");
		sb.append("int pageNumber=criteria.getMysqlOffset();\n");
		
		sb.append("if (criteria.getMysqlLength() != 0 && criteria.getMysqlOffset() != 0) {\n");
		sb.append("criteria.setMysqlOffset((criteria.getMysqlOffset() - 1) * criteria.getMysqlLength());\n");
		sb.append("criteria.setMysqlLength(criteria.getMysqlLength());\n");
		sb.append("}\n");
		sb.append("PageList pageList = new PageList();\n");
		sb.append("pageList.setCount("+getDaoShort()+".countByExample(criteria));\n");
		sb.append("pageList.setRecords("+getDaoShort()+".selectListPage(criteria));\n");
		sb.append("pageList.setPageNumber(pageNumber);\n");
		sb.append("pageList.setPageSize(pageSize);\n");
		
		sb.append("return pageList");
		method.addBodyLine(sb.toString());
		return method;
	}

	/**
	 * 通用方法设置
	 * @param methodName	方法名
	 * @param introspectedTable	数据库表对象<t_user_info>
	 * @param tableName	表对应的entity类名<TUserInfo>
	 * @param type	参数列表类型	type 的意义	无参数	0 ;  pojo 1 ;key 2 ;example 3 ;pojo+example 4
	 * @param returnType	返回参数类型	int,long,string,list<record>,map<String, record>
	 * @param comment	方法注释
	 * @param isAbstract	是否为抽象方法,即接口方法
	 * @return
	 */
	protected Method getMethod(String methodName, IntrospectedTable introspectedTable, String tableName, 
						int type, String returnType,  String comment, boolean isAbstract) {
		Method method = new Method();
		method.setName(methodName);
		//TODO service接口定义返回类型
		if(returnType==null || "".equals(returnType.trim())) {
			//void
		} else if("int".equals(returnType)) {
			method.setReturnType(new FullyQualifiedJavaType("int"));
		} else if("long".equals(returnType)) {
			method.setReturnType(new FullyQualifiedJavaType("long"));
		} else if("boolean".equals(returnType)) {
			method.setReturnType(new FullyQualifiedJavaType("boolean"));
		} else if("String".equals(returnType)) {
			method.setReturnType(new FullyQualifiedJavaType("String"));
		} else if("list<record>".equals(returnType)) {
			method.setReturnType(new FullyQualifiedJavaType("List<" + tableName + ">"));
		} else if("map".equals(returnType)) {
			method.setReturnType(new FullyQualifiedJavaType("Map<" + tableName + ", record>"));
		} else if("record".equals(returnType)) {
			method.setReturnType(pojoType);
		} else {
			method.setReturnType(new FullyQualifiedJavaType("Object"));
		}
		
		String params = addParams(introspectedTable, method, type);
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		// method.addBodyLine("try {");
		sb.append("return this.");
		sb.append(getDaoShort());
		if (introspectedTable.hasBLOBColumns()
				&& (!"updateByPrimaryKeySelective".equals(methodName) && !"deleteByPrimaryKey".equals(methodName)
						&& !"deleteByExample".equals(methodName) && !"updateByExampleSelective".equals(methodName))) {
			sb.append(methodName + "WithoutBLOBs");
		} else {
			sb.append(methodName);
		}
		sb.append("(");
		sb.append(params);
		sb.append(");");
		method.addBodyLine(sb.toString());
		//是否抽象
		if(isAbstract) {
			method.setAbstract(isAbstract);
			context.getCommentGenerator().addMethodComment(method, introspectedTable, comment);
		} else {
			method.addAnnotation("@Override");
		}
		//其他扩展、补充
		return method;
	}

	/**
	 * 添加方法
	 * o
	 */
	@Deprecated
	protected Method getOtherInsertboolean(String methodName, IntrospectedTable introspectedTable, String tableName, String comment, boolean isAbstract) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(returnType);
		method.addParameter(new Parameter(pojoType, "record"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		if (returnType==null) {
			sb.append("this.");
		} else {
			sb.append("return this.");
		}
		sb.append(getDaoShort());
		sb.append(methodName);
		sb.append("(");
		sb.append("record");
		sb.append(");");
		method.addBodyLine(sb.toString());
		if(isAbstract) {
			method.setAbstract(isAbstract);
			context.getCommentGenerator().addMethodComment(method, introspectedTable, comment);
		} else {
			method.addAnnotation("@Override");
		}
		return method;
	}

	/**
	 * type 的意义 pojo 1 key 2 example 3 pojo+example 4
	 */
	protected String addParams(IntrospectedTable introspectedTable, Method method, int type1) {
		switch (type1) {
		case 0:
			return "";
		case 1:
			method.addParameter(new Parameter(pojoType, "record"));
			return "record";
		case 2:
			if (introspectedTable.getRules().generatePrimaryKeyClass()) {
				FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
				method.addParameter(new Parameter(type, "key"));
			} else {
				for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
					FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
					method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
				}
			}
			StringBuffer sb = new StringBuffer();
			for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
				sb.append(introspectedColumn.getJavaProperty());
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
		case 3:
			method.addParameter(new Parameter(pojoCriteriaType, "example"));
			return "example";
		case 4:

			method.addParameter(0, new Parameter(pojoType, "record"));
			method.addParameter(1, new Parameter(pojoCriteriaType, "example"));
			return "record, example";
		default:
			break;
		}
		return null;
	}

	/**
	 * addFieldComment
	 * @author	chdaba
	 * @version 1.0.0
	 * @date	2018年12月19日
	 * @param field
	 * @param comment
	 */
	@Deprecated
	protected void addaddFieldComment(JavaElement field, String comment) {
		StringBuilder sb = new StringBuilder();
		field.addJavaDocLine("/**");
		sb.append(" * ");
		comment = comment.replaceAll("\n", "<br>\n\t * ");
		sb.append(comment);
		field.addJavaDocLine(sb.toString());
		field.addJavaDocLine(" */");
	}
	
	/**
	 * addaddClassComment
	 * @author	chdaba
	 * @version 1.0.0
	 * @date	2018年12月20日
	 * @param topLevelClass
	 * @param comment
	 */
	@Deprecated
	protected void addaddClassComment(TopLevelClass topLevelClass, String comment) {
		StringBuilder sb = new StringBuilder();
		sb.append("/**\n");
		sb.append(" * "+comment + "\n");
		sb.append(" * @author\tchdaba\n");
		sb.append(" * @version\t1.0.0\n");
		sb.append(" * @date\t" +new Date()+ "\n");
		sb.append("*/");
		topLevelClass.addJavaDocLine(sb.toString());
	}
	
	
	

	/**
	 * 添加字段
	 * 
	 * @param topLevelClass
	 */
	@Deprecated
	protected void addField(TopLevelClass topLevelClass) {
		// 添加 success
		Field field = new Field();
		field.setName("success"); // 设置变量名
		field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance()); // 类型
		field.setVisibility(JavaVisibility.PRIVATE);
		addaddFieldComment(field, "执行结果");
		topLevelClass.addField(field);
		// 设置结果
		field = new Field();
		field.setName("message"); // 设置变量名
		field.setType(FullyQualifiedJavaType.getStringInstance()); // 类型
		field.setVisibility(JavaVisibility.PRIVATE);
		addaddFieldComment(field, "消息结果");
		topLevelClass.addField(field);
	}

	/**
	 * 添加方法
	 * 
	 */
	@Deprecated
	protected void addMethod(TopLevelClass topLevelClass) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setSuccess");
		method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "success"));
		method.addBodyLine("this.success = success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
		method.setName("isSuccess");
		method.addBodyLine("return success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setMessage");
		method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "message"));
		method.addBodyLine("this.message = message;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
		method.setName("getMessage");
		method.addBodyLine("return message;");
		topLevelClass.addMethod(method);
	}

	/**
	 * 添加方法
	 * 
	 */
	@Deprecated
	protected void addMethod(TopLevelClass topLevelClass, String tableName) {
		Method method2 = new Method();
		for (int i = 0; i < methods.size(); i++) {
			Method method = new Method();
			method2 = methods.get(i);
			method = method2;
			method.removeAllBodyLines();
			method.removeAnnotation();
			StringBuilder sb = new StringBuilder();
			sb.append("return this.");
			sb.append(getDaoShort());
			sb.append(method.getName());
			sb.append("(");
			List<Parameter> list = method.getParameters();
			for (int j = 0; j < list.size(); j++) {
				sb.append(list.get(j).getName());
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append(");");
			method.addBodyLine(sb.toString());
			topLevelClass.addMethod(method);
		}
		methods.clear();
	}

	/**
	 * BaseUsers to baseUsers
	 * 
	 * @param tableName
	 * @return
	 */
	protected String toLowerCase(String tableName) {
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * BaseUsers to baseUsers
	 * 
	 * @param tableName
	 * @return
	 */
	protected String toUpperCase(String tableName) {
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * 导入需要的类
	 */
	private void addImport(Interface interfaces, TopLevelClass topLevelClass) {
		interfaces.addImportedType(pojoType);
		interfaces.addImportedType(pojoCriteriaType);
		interfaces.addImportedType(listType);
		topLevelClass.addImportedType(daoType);
		topLevelClass.addImportedType(interfaceType);
		topLevelClass.addImportedType(pojoType);
		topLevelClass.addImportedType(pojoCriteriaType);
		topLevelClass.addImportedType(listType);
		topLevelClass.addImportedType(slf4jLogger);
		topLevelClass.addImportedType(slf4jLoggerFactory);
		if (enableAnnotation) {
			topLevelClass.addImportedType(service);
			topLevelClass.addImportedType(autowired);
		}
	}

	/**
	 * 	在serviceImpl类中导入logger
	 * @author	chdaba
	 * @version 1.0.0
	 * @date	2018年12月19日
	 * @param topLevelClass
	 */
	private void addLogger(TopLevelClass topLevelClass) {
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("Logger"+(slf4jLoggerFactory!=null?"Factory":"")
				+".getLogger(" + topLevelClass.getType().getShortName() + ".class)"); // 设置值
		field.setName("logger"); // 设置变量名
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("Logger")); // 类型
		field.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(field);
	}

	private String getDaoShort() {
		return toLowerCase(daoType.getShortName()) + ".";
	}
	
	@Deprecated
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		returnType = method.getReturnType_2();
		return true;
	}

	
}
