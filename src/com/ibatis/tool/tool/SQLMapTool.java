package com.ibatis.tool.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ibatis.tool.db.ColumnInfo;

/**
 * 不支持多个seq的情况
 * @author Administrator
 * 
 */
public class SQLMapTool {
	/**				fi[0] = //字段java类
					fi[1] = filed[1].toLowerCase();//字段名小写
					fi[2] =//注释
					fi[3] = //字段名
					fi[4] = //类全名
					fi[5] = //主键字段名
					fi[6] = //jdbctype
	 */
	private List<ColumnInfo> columnInfoList;
	private String xmlName;//xml文件名
	private String xmlNameAlias;//xml别名 首字母小写的model名
	private String className;//model全名 sq.getway.manager.mybatis.model.User
	private String packge;//xml包名 sq.getway.manager.mybatis.config
	private Document doc;
	private Element root;
	private String tableName;//原始表名
	private String src="src/";
	private String replace="true";
	private String seqName;
	private List<ColumnInfo> prmryKeyField;
	boolean initPrmry = false;
	boolean hasOtherCloumn; //除主键外是否还有其他键
	public String getReplace() {
		return replace;
	}
	
	public void setReplace(String replace) {
		this.replace = replace;
	}


	public List<ColumnInfo> getColumnInfoList() {
		return columnInfoList;
	}

	public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
		this.columnInfoList = columnInfoList;
	}

	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
		if(xmlName.length() == 1){
			this.xmlNameAlias = xmlName.toLowerCase();
		}else{
			this.xmlNameAlias = xmlName.substring(0,1).toLowerCase()+xmlName.substring(1);
		}
		
	}

	public String getPackge() {
		return packge;
	}

	public void setPackge(String packge) {
		this.packge = packge;
	}
	
	public void save() throws Exception {
		try {
			initPrmry();
			doc = DocumentHelper.createDocument();
			setRoot();
			//addAlias();
			addResultMap();
			String beanName=className.substring(className.lastIndexOf(".")+1);
			addCondition();
			addSelectById("query"+beanName+"ById");
			addSelectByMap("query"+beanName+"ByMap");
			
			addInsert("insert"+beanName);
			//addSelect("query"+beanName);
			addUpdate("update"+beanName);
			addUpdateNotNull("update"+beanName+"NotNull");
			//addDelete("del"+beanName);
			addDeleteById("del"+beanName+"ById");
			createFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(packge.replace(".", "/") + "/" + xmlName
					+ ".xml create faild");
		}
	}
	
	
	private void setRoot() {
		doc.setXMLEncoding("UTF-8");
		doc.addDocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN",
				"http://mybatis.org/dtd/mybatis-3-mapper.dtd");

		doc.addComment("add by myBatisTool "+tableName);
		
		root = doc.addElement("mapper");
		root.addAttribute("namespace", className);
	}
	
	public void addAlias() {
		Element e = root.addElement("typeAlias");
		e.addAttribute("alias", xmlNameAlias);
		e.addAttribute("type", className);

	}
	
	public void addResultMap() {
		if(!initPrmry){
			initPrmry();
		}
		Element e = root.addElement("resultMap");
		e.addAttribute("id", xmlNameAlias+"Map");
		e.addAttribute("type", xmlNameAlias);
		//e.addAttribute("class", xmlNameAlias);
		for (ColumnInfo ci : prmryKeyField) {
			// <result property="id" column="userid" columnIndex="1" />
			String nodeName = "id";
			Element ce = e.addElement(nodeName);
			ce.addAttribute("property", ci.getJavaName());
			//ce.addAttribute("property", field[1]);//edit by maxy
			ce.addAttribute("column", ci.getColumnName());
			//ce.addAttribute("columnIndex", String.valueOf(i));
		}
		for (ColumnInfo ci : columnInfoList) {
			// <result property="id" column="userid" columnIndex="1" />
			String nodeName = "result";
			if(ci.isPk()){
				continue;
			}
			Element ce = e.addElement(nodeName);
			ce.addAttribute("property", ci.getJavaName());
			
			//ce.addAttribute("property", field[1]);//edit by maxy
			ce.addAttribute("column", ci.getColumnName());
			//ce.addAttribute("columnIndex", String.valueOf(i));
		}

	}

	public void addSelect(String name) {
		root.addComment(" add method "+name );

		Element e = root.addElement("select");
	//	e.addAttribute("id", tableName + "." + name);
		e.addAttribute("id",name);
		e.addAttribute("resultMap", "result");
		e.addAttribute("parameterType", xmlName);
	//	e.addAttribute("method", name);
		
		/**
		 * select * 的生成
		 */
		
		StringBuffer text = new StringBuffer("\r\n \t\tselect * from " + tableName);
		e.setText(text.toString());
		
		/**
		 *使用select * 如果要指定定段，使用下面的代码
		 */
		/*
		StringBuffer text = new StringBuffer("\r\n \t\tselect ");
		for (Iterator<String[]> iterator = fields.iterator(); iterator
				.hasNext();) {
			String[] field = (String[]) iterator.next();
			text.append("\r\n\t\t\t\t" + tableName + "." + field[3] + ",");
		}
		text.append("_@_");
		text.append("\r\n\t\t\tfrom\r\n\t\t\t" + tableName);
		e.setText(text.toString().replaceAll(",_@_", ""));
		*/
		
		Element el = e.addElement("include");
		el.addAttribute("refid", tableName+"Condition");
		
	}
	
	public void addSelectById(String name) throws Exception{
		root.addComment(" add method "+name );
		if(!initPrmry){
			initPrmry();
		}
		
		if(prmryKeyField == null || prmryKeyField.isEmpty()){
			root.addComment(" no prmry key, can not create querybyid statement");
		}else if(prmryKeyField.size() >1){
			root.addComment(" more than one prmry key, please user selectByMap");
		}else{
			ColumnInfo info = prmryKeyField.get(0);
			Element e = root.addElement("select");
			//	e.addAttribute("id", tableName + "." + name);
				e.addAttribute("id",name);
				e.addAttribute("resultMap", xmlNameAlias+"Map");
				e.addAttribute("parameterType", transParameClass(info.getClassNameShort()));
			//	e.addAttribute("method", name);
				/*
				 * select * 的生成
				 */
				
				StringBuffer text = new StringBuffer("\r\n \t\tselect * from " + tableName);
				text.append("\r\n  \t\twhere \r\n \t \t\t");
				text.append(info.getColumnName()+" = #{value}");
				e.setText(text.toString());
				
				/**
				 *使用select * 如果要指定定段，使用下面的代码
				 */
				/*
				StringBuffer text = new StringBuffer("\r\n \t\tselect ");
				for (Iterator<String[]> iterator = fields.iterator(); iterator
						.hasNext();) {
					String[] field = (String[]) iterator.next();
					text.append("\r\n\t\t\t\t" + tableName + "." + field[3] + ",");
				}
				text.append("_@_");
				text.append("\r\n\t\t\tfrom\r\n\t\t\t" + tableName);
				text.append("\r\n  \t\twhere \r\n \t \t\t");
				text.append(prmryKeyField[3]+"=#value#");
				e.setText(text.toString().replaceAll(",_@_", ""));
				*/
				
		}
		
	}
	
	public void addSelectByMap(String name) {
		root.addComment(" add method "+name );

		Element e = root.addElement("select");
	//	e.addAttribute("id", tableName + "." + name);
		e.addAttribute("id",name);
		e.addAttribute("resultMap", xmlNameAlias+"Map");
		e.addAttribute("parameterType", "map");
	//	e.addAttribute("method", name);
		
		/*
		 * select * 的生成
		 */
		
		StringBuffer text = new StringBuffer("\r\n \t\tselect * from " + tableName);
		e.setText(text.toString());
		/**
		 *使用select * 如果要指定定段，使用下面的代码
		 */
		/*
		StringBuffer text = new StringBuffer("\r\n \t\tselect ");
		for (Iterator<String[]> iterator = fields.iterator(); iterator
				.hasNext();) {
			String[] field = (String[]) iterator.next();
			text.append("\r\n\t\t\t\t" + tableName + "." + field[3] + ",");
		}
		text.append("_@_");
		text.append("\r\n\t\t\tfrom\r\n\t\t\t" + tableName);

		e.setText(text.toString().replaceAll(",_@_", ""));
		*/
		Element el = e.addElement("include");
		el.addAttribute("refid", tableName+"Condition");
		
	}

	public void addInsert(String name) {
		if(!initPrmry){
			initPrmry();
		}
		root.addComment(" add method "+name );
		Element e = root.addElement("insert");
		//	e.addAttribute("id", tableName + "." + name);
		e.addAttribute("id",name);
		e.addAttribute("parameterType", xmlName);
	//	e.addAttribute("method", name);
		if(seqName!= null && !"".equals(seqName) && prmryKeyField.size() > 0){
			if(prmryKeyField.size() > 1){
				e.addComment("more than one prmry key,can't match seq with pk");
			}else{
				Element selectKeyEle = e.addElement("selectKey");
				selectKeyEle.addAttribute("keyProperty",prmryKeyField.get(0).getJavaName());
				selectKeyEle.addAttribute("resultType","long");
				selectKeyEle.addAttribute("order","BEFORE");
				StringBuffer textSeq = new StringBuffer();
				textSeq.append("\r\n\t\t\tselect ");
				textSeq.append(seqName).append(".nextVal from dual");
				
				selectKeyEle.addText(textSeq.toString());
			}
			
		}
		
		
		StringBuffer text = new StringBuffer("\r\n  \t\tinsert into  "
				+ tableName + "(");
		for (ColumnInfo ci : columnInfoList) {
			text.append("\r\n\t\t\t\t" + ci.getColumnName() + ",");
		}
		text.append("_@_");
		text.append(") values(");
		for (ColumnInfo ci : columnInfoList) {
			//field[3]字段 field[5]主键 
			text.append("\r\n\t\t\t\t" + "#{" + ci.getJavaName() + ",jdbcType="+ci.getJdbcType()+"},");
		}
		text.append("_@_)");
		e.addText(text.toString().replaceAll(",_@_", ""));
	}
	public void addUpdate(String name) {
		root.addComment(" add method "+name );
		if(!initPrmry){
			initPrmry();
		}
		
		
		if(prmryKeyField.isEmpty()){
			root.addComment(" no prmry key, can not create update statement");
		}else if(!hasOtherCloumn){
			root.addComment(" nothing to update");
		}else{
			Element e = root.addElement("update");
			e.addAttribute("id",name);
			e.addAttribute("parameterType", xmlName);
//			e.addAttribute("method", name);
			StringBuffer text = new StringBuffer("\r\n  \t\t update   "
					+ tableName  + " set\n");
			for (ColumnInfo ci : columnInfoList) {
				if(!ci.isPk()){
					text.append("\t\t\t"+ci.getColumnName()+"=#{"+ci.getJavaName() + ",jdbcType="+ci.getJdbcType()+"},\r\n");
				}
		}
			e.setText(text.toString().replaceAll(",$", ""));
			
			text = new StringBuffer();
			int i = 0;
			for (ColumnInfo ci : prmryKeyField) {
				if(i==0){
					text.append("\t\t\twhere \r\n \t \t\t");
					text.append(ci.getColumnName()+"=#{"+ci.getJavaName()+"}");
				}else{
					text.append("\r\n\t\t\t and " + ci.getColumnName()+"=#{"+ci.getJavaName()+"}");
				}
				i++;
			}
			e.addText(text.toString());
		}
		
	}
	
	public void addUpdateNotNull(String name) {
		root.addComment(" add method "+name+ " only not null fields updated" );
		if(!initPrmry){
			initPrmry();
		}
		
		if(prmryKeyField.isEmpty()){
			root.addComment(" no prmry key, can not create update statement");
		}else if(!hasOtherCloumn){
			root.addComment(" nothing to update");
		}else{
			Element e = root.addElement("update");
			e.addAttribute("id",name);
			e.addAttribute("parameterType", xmlName);
//			e.addAttribute("method", name);
			StringBuffer text = new StringBuffer("\r\n  \t\t update   "
					+ tableName  );
			e.setText(text.toString().replaceAll(",_@_", ""));
			
			
			Element el = e.addElement("set");
			for (ColumnInfo ci : columnInfoList) {
				if(!ci.isPk()){
					Element ele = el.addElement("if");
					ele.addAttribute("test", ci.getJavaName() + " != null");
					ele.setText(" "+ci.getColumnName()+"=#{"+ci.getJavaName()+"},");
				}
				
			}
			
			text = new StringBuffer("");
			int i = 0;
			for (ColumnInfo ci : prmryKeyField) {
				if(i==0){
					text.append("\r\n\t\t\twhere \r\n \t \t\t");
					text.append(ci.getColumnName()+"=#{"+ci.getJavaName()+"}");
				}else{
					text.append("\r\n\t\t\t and " + ci.getColumnName()+"=#{"+ci.getJavaName()+"}");
				}
				i++;
			}
			e.addText(text.toString());
		}
		
	}
	//Condition
	
	public void addCondition() {
		root.addComment(" add Condition "+tableName );
		Element e = root.addElement("sql");
		e.addAttribute("id", tableName+"Condition");
		Element whereEle = e.addElement("where");
		for (ColumnInfo ci : columnInfoList) {
			Element ele = whereEle.addElement("if");
			ele.addAttribute("test", ci.getJavaName() + " != null");
			ele.setText(" and "+ci.getColumnName()+"=#{"+ci.getJavaName()+"}");
			
		}
	}
	
	//存 在全部删除的隐患 不再支持此方法
/*	public void addDelete(String name) {
		root.addComment(" add method "+name );
		if(!initPrmry){
			initPrmry();
		}
		
		if(prmryKeyField.isEmpty()){
			root.addComment(" no prmry key, can not create update statement");
		}else if(!hasOtherCloumn){
			root.addComment(" nothing to update");
		}else{
			Element e = root.addElement("delete");
//			e.addAttribute("method", name);
			//	e.addAttribute("id", tableName + "." + name);
			e.addAttribute("id",name);
			e.addAttribute("parameterType", xmlName);
			StringBuffer text = new StringBuffer("\r\n  \t\t delete   "
					+ tableName  );
			e.setText(text.toString().replaceAll(",_@_", ""));
			
			
			Element el = e.addElement("include");
			el.addAttribute("refid", tableName+"Condition");
		}
		
		
	}*/
	
	public void addDeleteById(String name) throws Exception{
		root.addComment(" delete method "+name );
		if(!initPrmry){
			initPrmry();
		}
		
		if(prmryKeyField.isEmpty()){
			root.addComment(" no prmry key, can not create deletebyid statement");
		}else if(prmryKeyField.size() >1){
			Element e = root.addElement("delete");
//			e.addAttribute("method", name);
			//	e.addAttribute("id", tableName + "." + name);
			e.addAttribute("id",name);
			e.addAttribute("parameterType", xmlName);
			StringBuffer text = new StringBuffer("\r\n \t\tdelete " + tableName);
			for(int i = 0; i < prmryKeyField.size(); i++){
				ColumnInfo ci = prmryKeyField.get(i);
				if(i == 0){
					text.append("\r\n\t\t\twhere ");
				}else{
					text.append("\r\n\t\t\tand ");
				}
				text.append(ci.getColumnName()+"=#{"+ci.getJavaName()+"}");
			}
			e.setText(text.toString());
		}else{
			Element e = root.addElement("delete");
//			e.addAttribute("method", name);
			//	e.addAttribute("id", tableName + "." + name);
			e.addAttribute("id",name);
			e.addAttribute("parameterType", transParameClass(prmryKeyField.get(0).getClassNameShort()));
			StringBuffer text = new StringBuffer("\r\n \t\tdelete " + tableName);
			text.append("\r\n  \t\twhere \r\n \t \t\t");
			text.append(prmryKeyField.get(0).getColumnName()+"=#{value}");
			e.setText(text.toString());
		}
		
	}
	
	private void createFile() throws IOException {
		String fileName = src+packge.replace(".", "/") + "/" + xmlName + ".xml";
		File pFile = new File(src+packge.replace(".", "/"));
		if (!pFile.isDirectory()) {
			pFile.mkdirs();
		}
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();

		} else {
			if(replace.equals("true"))file.delete();
			else throw new RuntimeException(fileName+" has allready being!");
		}

		OutputFormat format = new OutputFormat("\t", true);

		XMLWriter writer = new XMLWriter(new FileWriter(new File(fileName)),
				format);
		writer.write(doc);
		writer.close();
		System.out.println("create begin: "+fileName);

	}
	
	private void initPrmry(){
		prmryKeyField = columnInfoList.get(0).getPrmryKeys();
		initPrmry = true;
		hasOtherCloumn = false;
		for (ColumnInfo ci : columnInfoList) {
			if(!ci.isPk()){
				hasOtherCloumn = true;
				return;
			}
		}
	}
	
	/**
	 * 将类型转为ParameClass类型
	 * @param javaType
	 * @return
	 */
	private static String transParameClass(String javaType) throws Exception{
		if("Boolean".equals(javaType)){
			return "boolean";
		}else if("Byte".equals(javaType)){
			return "byte";
		}else if("Short".equals(javaType)){
			return "short";
		}else if("Integer".equals(javaType)){
			return "int";
		}else if("Long".equals(javaType)){
			return "long";
		}else if("Float".equals(javaType)){
			return "float";
		}else if("Double".equals(javaType)){
			return "double";
		}else if("String".equals(javaType)){
			return "string";
		}else if("Date".equals(javaType)){
			return "date";
		}else if("BigDecimal".equals(javaType)){
			return "decimal";
		}

		throw new Exception("未知数据类型"+ javaType);
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}
}
