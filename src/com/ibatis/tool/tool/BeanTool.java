package com.ibatis.tool.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ibatis.tool.db.ColumnInfo;
/**
 * 
 * @author Administrator
 *
 */
public class BeanTool {
	private Set<String> importClaess;
	private List<ColumnInfo> fields;
	private String beanName;
	private String beanPkg;
	private String desc;
	private String src="src/";
	private OutputStreamWriter  bw;
	private String replace="true";
	
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public Set<String> getImportClaess() {
		return importClaess;
	}
	public void setImportClaess(Set<String> importClaess) {
		this.importClaess = importClaess;
	}

	public List<ColumnInfo> getFields() {
		return fields;
	}
	public void setFields(List<ColumnInfo> fields) {
		this.fields = fields;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getBeanPkg() {
		return beanPkg;
	}
	public void setBeanPkg(String beanPkg) {
		this.beanPkg = beanPkg;
	}
	public void save(){
		try {
		createFile();
		setpackage();
		setImport();
		startClass();
		setField();
		setMethod();
		endClass();
		} catch (IOException e) {
			throw new RuntimeException(beanPkg+"."+beanName+".java create faild");
		}
	}
	private void setpackage() throws IOException{
		bw.write("package "+this.beanPkg+";\r\n\r\n");
	}
	@SuppressWarnings({ "unused", "unchecked" })
	private void setImport()throws IOException{
		for (Iterator iterator = importClaess.iterator(); iterator.hasNext();) {
			String importClass = (String) iterator.next();
			if(!importClass.startsWith("java.lang")){
				bw.write("import "+importClass+";\r\n");
			}
		}
	}
	private void startClass()throws IOException{
		bw.write("\r\n");
		bw.write("/**\r\n");
		//bw.write(" *\r\n");
		bw.write(" * "+this.desc.replaceAll("\n", "<p>")+"\r\n");
		bw.write(" * @author "+System.getProperty("user.name")+"\r\n");
		//bw.write(" * <p><b>create by IBatisTool " + new Date() +" \r\n");
		bw.write(" */\r\n");
		bw.write("public class "+this.beanName+" { \r\n");
	}
	
	
	private void setField()throws IOException{
		for (ColumnInfo ci : fields) {
			//setFieldAnnotation(field[2]);//edit by maxy 去掉注释
			//bw.write("    private "+ci.getClassName+" "+fieldName+";\r\n");
			String str = "\tprivate "+ci.getClassNameShort()+" "+ci.getJavaName()+";";
			if(ci.getComment() != null && !"".equals(ci.getComment())){
				str += " //" + ci.getComment();
			}
			str += "\r\n";
			bw.write(str);
			}
		bw.write("\r\n");
	}
	@SuppressWarnings("unused")
	private void setFieldAnnotation(String field) throws IOException{
		bw.write("\r\n");
		bw.write("    /**\r\n");
		if(field!=null) 
			bw.write("     *  "+field.replaceAll("\n", "<p>")+"\r\n");
		bw.write("     */\r\n");
	}
	@SuppressWarnings({ "unchecked", "unused" })
	private void setParamAnnotation(ArrayList<String[]> params) throws IOException{
		
		bw.write("\r\n");
		bw.write("    /**\r\n");
		for (ColumnInfo ci : fields) {
			if(ci.getClassNameShort()!=null) 
			bw.write("     * @param "+ci.getClassName()+" "+ci.getClassNameShort()+"\r\n");
		}
		bw.write("     */\r\n");
	}
	@SuppressWarnings("unused")
	private void setReturnAnnotation(String desc) throws IOException{
		String des="";
		if(desc!=null) 
			des=desc.replaceAll("\n", "<p>");
		bw.write("\r\n");
		bw.write("    /**\r\n");
		bw.write("     * "+des+"\r\n");
		bw.write("     * \r\n");
		bw.write("     * @return "+des+"\r\n");
		bw.write("     */\r\n");
	}
	@SuppressWarnings("unchecked")
	private void setMethod()throws IOException{
		
		/** 构造方法，要的添上
		ArrayList<String[]> li = new ArrayList<String[]>();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			String[] field = (String[]) iterator.next();
			li.add(new String[]{field[1].toLowerCase(),field[2],ci.getClassName});
			//										
		}
		bw.write("\tpublic   "+this.beanName+"(){\r\n\r\n\r\n\t}");
		//setParamAnnotation(li);
		bw.write("\tpublic   "+this.beanName+"(");
		StringBuffer params= new StringBuffer("_@_");
		for (Iterator iterator = li.iterator(); iterator.hasNext();) {
			String[] field = (String[]) iterator.next();
			params.append(","+field[2]+" "+formatFiledString(ci.getClassName));
			//params.append(","+field[2]+" "+ci.getClassName);//edit by maxy
		}
		bw.write(params.toString().replaceAll("_@_,", ""));
		bw.write("){\r\n");
		for (Iterator iterator = li.iterator(); iterator.hasNext();) {
			String[] field = (String[]) iterator.next();
			bw.write("\t\t\tthis."+formatFiledString(ci.getClassName)+"="+formatFiledString(ci.getClassName)+";\r\n");
		}
		bw.write("\t}\r\n");
		**/
		for (ColumnInfo ci : fields) {
			String name = ci.getJavaName();
			if(name.length() > 1 && name.substring(1,2).matches("[A-Z]")){
				// cField1 这种格式  --> setcField 第一个字母不变
			}else{
				name = name.substring(0,1).toUpperCase()+name.substring(1);
			}
			String setMethodName="set"+name;
			String getMethodName="get"+name;	
			ArrayList<String[]> list = new ArrayList<String[]>();
			list.add(new String[]{ci.getJavaName(),ci.getComment()});
			//setParamAnnotation(list);
			bw.write("    public void "+setMethodName+"("+ci.getClassNameShort()+" "+ci.getJavaName()+"){\r\n");
			//bw.write("    public  void "+setMethodName+"("+ci.getClassName+" "+fieldName+"){\r\n");////edit by maxy
			bw.write("    	this."+ci.getJavaName()+" = "+ci.getJavaName()+";\r\n    }\r\n");
			//setReturnAnnotation(field[2]);
			bw.write("    public "+ci.getClassNameShort()+" "+getMethodName+"(){\r\n");
			//bw.write("    public  "+ci.getClassName+" "+getMethodName+"(){\r\n");//edit by maxy
			bw.write("    	return	this."+ci.getJavaName()+";\r\n    }\r\n");
			
			}
		bw.write("\r\n");
		
	}
	private void endClass()throws IOException{
		bw.write("}");
		bw.flush();
	}
	private void createFile()throws IOException{
		
		String fileName=src+beanPkg.replace(".", "/")+"/"+beanName+".java";
		File pFile = new File(src+beanPkg.replace(".", "/"));
		if(!pFile.isDirectory()){
			pFile.mkdirs();
		}	
		File file = new File(fileName);
		if(!file.exists()){
				file.createNewFile();
			
		}else
		{
			if(replace.equals("true"))file.delete();
			else throw new RuntimeException(fileName+" has allready being!");
		}
		 bw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
		 System.out.println("create begin: "+fileName);
	}
	
	
	public static void main(String[] args) {
		BeanTool tool = new BeanTool();
		tool.setBeanName("test"+"bean");
		tool.setBeanPkg("com.test");
		tool.save();
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
}
