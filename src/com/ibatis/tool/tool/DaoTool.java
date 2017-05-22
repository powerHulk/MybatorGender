package com.ibatis.tool.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DaoTool {
	private String daoName;
	private String daoPkg;
	private Set<String> importClaess;
	private Set<String> iimportClaess;
	private String implName;
	private String implPkg;
	private Map<String, String[]> resultMap;
	private List<String[]> methodList;
	private String xmlPath;
	private OutputStreamWriter bw;
	private OutputStreamWriter ibw;
	private Document doc;
	private String src="src/";
	private String replace;
	@SuppressWarnings("unchecked")
	public void init() {
		try {
			File f = new File(xmlPath);
			doc = new SAXReader().read(f);
			resultMap = new HashMap<String, String[]>();
			methodList = new ArrayList<String[]>();

			importClaess = new HashSet<String>();
			iimportClaess = new HashSet<String>();
			String reMpXp = "/sqlMap/resultMap";
			List<Element> list = doc.selectNodes(reMpXp);
			for (Element element : list) {

				resultMap.put(element.attributeValue("id"), new String[] {

				element.attributeValue("class") });
				importClaess.add(element.attributeValue("class"));
				iimportClaess.add(element.attributeValue("class"));
			}
			
			importClaess.add("java.util.List");
			iimportClaess.add("java.util.List");
			iimportClaess.add("com.huawei.aimi.util.AiMiDaoSupport");
			iimportClaess.add("org.springframework.stereotype.Repository");
			iimportClaess.add( this.daoPkg+"."+this.daoName);
			getMethod("insert");
			getMethod("update");
			getMethod("delete");
			getMethod("select");

			// String meMpXp="//@";

		} catch (DocumentException e) {
			new RuntimeException("can't read xml :" + xmlPath);
		}
	}

	@SuppressWarnings("unchecked")
	private void getMethod(String name) {
		List<Element> list = doc.selectNodes("/sqlMap/" + name);
		for (Element element : list) {
			methodList.add(new String[] { name, element.attributeValue("id"),
					element.attributeValue("parameterClass"),
					element.attributeValue("resultMap") });
		}

	}

	@SuppressWarnings("unchecked")
	private void setMethod() throws IOException {
		for (Iterator iterator = this.methodList.iterator(); iterator.hasNext();) {
			String[] method = (String[]) iterator.next();
			createMethod(method);
		}
	}

	@SuppressWarnings("unchecked")
	private void setParamAnnotation(List<String[]> params) throws IOException {

		bw.write("\r\n");
		bw.write("    /**\r\n");
		for (Iterator iterator = params.iterator(); iterator.hasNext();) {
			String[] field = (String[]) iterator.next();
			field[1] = field[1].replaceAll("\n", "<p>");
			bw.write("     * @param " + field[0] + " " + field[1] + "\r\n");
			bw.write("     * @return  \r\n");
		}
		bw.write("     */");

		ibw.write("\r\n");
		ibw.write("    /**\r\n");
		for (Iterator iterator = params.iterator(); iterator.hasNext();) {
			String[] field = (String[]) iterator.next();
			field[1] = field[1].replaceAll("\n", "<p>");
			ibw.write("     * @param " + field[0] + " " + field[1] + "\r\n");
			ibw.write("     * @return  \r\n");
		}
		ibw.write("     */");
	}

	public void createMethod(String[] method) throws IOException {
		String pramClassName = null;
		String paramName = "";
		String pramClass = method[2];
		String resultClassName=null;
		String methodName=method[1];
		List<String[]> list = new ArrayList<String[]>();

		if (pramClass != null) {
			// System.out.println(pramClass);
			pramClassName = pramClass.substring(pramClass.lastIndexOf(".") + 1);
			paramName = pramClassName.substring(0, 1).toLowerCase()
					+ pramClassName.substring(1);
			
			list.add(new String[] { paramName, paramName });
		}

		String resultClass = null;

		if (method[3]!=null&&!method[3].equals("java.")) {
			resultClass = ((String[]) resultMap.get(method[3]))[0];
			 resultClassName = resultClass.substring(pramClass
						.lastIndexOf(".") + 1);
		}else
		{
			if(method[0].equals("insert"))resultClassName="Long";
			else resultClassName="int";
		}
		
		

		setParamAnnotation(list);

		if (method[0].equals("select")) {

			bw.write("\r\n\tpublic List<"
					+ resultClassName
					+ "> "
					+ methodName
					+ "("
					+ (pramClass == null ? ""
							: (pramClassName + " " + paramName)) + ");\r\n");
			ibw.write("\r\n\tpublic List<"
					+ resultClassName
					+ "> "
					+ methodName
					+ "("
					+ (pramClass == null ? ""
							: (pramClassName + " " + paramName)) + "){\r\n");
			ibw.write("\r\n\t\t return (List<"+resultClassName+">)getSqlMapClientTemplate().queryForList(\r\n");
			ibw.write("\t\t "+"\""+method[1]+"\","+paramName+");");
		
			ibw.write("\r\n\t}\r\n");
		} else {
			bw.write("\r\n\tpublic  "
					+ resultClassName
					+ " "
					+ methodName
					+ "("
					+ (pramClass == null ? ""
							: (pramClassName + " " + paramName)) + ");\r\n");
			ibw.write("\r\n\tpublic "
					+ resultClassName
					+ " "
					+ methodName
					+ "("
					+ (pramClass == null ? ""
							: (pramClassName + " " + paramName)) + "){\r\n");
			
			if(method[0].equals("insert"))
				ibw.write("\r\n\t\t return ("+resultClassName+")getSqlMapClientTemplate().insert(\r\n");
			if(method[0].equals("update"))
				ibw.write("\r\n\t\t return getSqlMapClientTemplate().update(\r\n");
			if(method[0].equals("delete"))
				ibw.write("\r\n\t\t return getSqlMapClientTemplate().delete(\r\n");
			
			ibw.write("\t\t "+"\""+method[1]+"\","+paramName+");");
			
			ibw.write("\r\n\t}\r\n");
		}
	}

	public static void main(String[] args) {
		// com/test/xml/TUser.xml
		DaoTool dt = new DaoTool();
		dt.setDaoName("TestDao");
		dt.setImplName("TestImpl");
		dt.setDaoPkg("com.test.dao");
		dt.setImplPkg("com.test.Impl");
		dt.setXmlPath("./com/test/xml/TUser.xml");
		dt.save();

	}

	public void save() {
		try {
			init();
			createFile();
			setpackage();
			setImport();
			startClass();
			setMethod();
			endClass();
		} catch (IOException e) {
			throw new RuntimeException(daoPkg + "." + daoName
					+ ".java create faild");
		}
	}

	private void setpackage() throws IOException {
		bw.write("package " + this.daoPkg + ";\r\n\r\n");
		ibw.write("package " + this.implPkg + ";\r\n\r\n");
	}

	@SuppressWarnings("unchecked")
	private void setImport() throws IOException {
		for (Iterator iterator = importClaess.iterator(); iterator.hasNext();) {
			String importClass = (String) iterator.next();
			if (!importClass.startsWith("java.lang")) {
				bw.write("import " + importClass + ";\r\n");
			}
		}
		for (Iterator iterator = iimportClaess.iterator(); iterator.hasNext();) {
			String importClass = (String) iterator.next();
			if (!importClass.startsWith("java.lang")) {
				ibw.write("import " + importClass + ";\r\n");
			}
		}
	}

	private void startClass() throws IOException {
		bw.write("\r\n");
		bw.write("/**\r\n");
		bw.write(" *\r\n");
		bw.write(" * @author " + System.getProperty("user.name") + "\r\n");
		bw.write(" * <p><b>create by IBatisTool " + new Date() + " \r\n");
		bw.write(" */\r\n");
		bw.write("public interface " + this.daoName  + " { \r\n");
		ibw.write("\r\n");
		ibw.write("/**\r\n");
		ibw.write(" *\r\n");
		ibw.write(" * @author " + System.getProperty("user.name") + "\r\n");
		ibw.write(" * <p><b>create by IBatisTool" + new Date() + " \r\n");
		ibw.write(" */\r\n");
		ibw.write(" @Repository(\"" + daoName + "\")\r\n");
		ibw.write("public class " + this.implName + " extends AiMiDaoSupport "+ " implements "
				+ this.daoName+"  { \r\n");

	}

	private void endClass() throws IOException {
		bw.write("}");
		bw.flush();
		ibw.write("}");
		ibw.flush();
	}

	private void createFile() throws IOException {
		String fileName = src+daoPkg.replace(".", "/") + "/" + daoName + ".java";

		String ifileName = src+implPkg.replace(".", "/") + "/" + implName + ".java";

		File pFile = new File(src+daoPkg.replace(".", "/"));
		if (!pFile.isDirectory()) {
			pFile.mkdirs();
		}
		File ipFile = new File(src+implPkg.replace(".", "/"));
		if (!ipFile.isDirectory()) {
			ipFile.mkdirs();
		}

		File file = new File(fileName);
		File ifile = new File(ifileName);
		if (!file.exists()) {
			file.createNewFile();

		} else {
			if(replace.equals("true"))file.delete();
			else throw new RuntimeException(fileName+" has allready being!");
		}

		if (!ifile.exists()) {
			ifile.createNewFile();

		} else {
			if(replace.equals("true"))ifile.delete();
			else throw new RuntimeException(ifileName+" has allready being!");
		}

		 bw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
		 ibw = new OutputStreamWriter(new FileOutputStream(ifile),"UTF-8");
		
		System.out.println("create begin: "+fileName);
		System.out.println("create begin: "+ifileName);
	}

	public String getDaoName() {
		return daoName;
	}

	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}

	public String getDaoPkg() {
		return daoPkg;
	}

	public void setDaoPkg(String daoPkg) {
		this.daoPkg = daoPkg;
	}

	public String getImplName() {
		return implName;
	}

	public void setImplName(String implName) {
		this.implName = implName;
	}

	public String getImplPkg() {
		return implPkg;
	}

	public void setImplPkg(String implPkg) {
		this.implPkg = implPkg;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

}