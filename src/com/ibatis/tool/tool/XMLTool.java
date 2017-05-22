package com.ibatis.tool.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public  class XMLTool {
 private static Document doc;
 public static void load(String path){
	 
	 try {
		XMLTool.doc=new SAXReader().read(XMLTool.class.getResourceAsStream(path));
		
	} catch (DocumentException e) {
		e.printStackTrace();
		throw new RuntimeException("获取xml文件失败:"+path);
	}
	 
 }
 public static String getElementAttribute(String xPath,String attribute){

	 return ((Element)doc.selectSingleNode(xPath)).attributeValue(attribute);
 }
 public static String getElementText(String xPath){
	 return((Element)doc.selectSingleNode(xPath)).getText();

 }
 @SuppressWarnings("unchecked")
public static List<String[]> getElementTexts(String xPath){
	 List<Element> nodeList=doc.selectNodes(xPath);
	 List<String[]> list = new ArrayList<String[]>();
	 for (Iterator<Element> iterator = nodeList.iterator(); iterator.hasNext();) {
		Element el = (Element) iterator.next();
		String seqName = el.attributeValue("seq");
		list.add(new String[]{el.getText(),seqName});
	//	System.out.println(el.getText());
	}
	 return list;
 } 
 public static void main(String[] args) {
	try{
//		 XMLTool.load("/config.xml");
//		 XMLTool.getElementTexts("/config/tables/table");
		
	}catch(Exception e){
		 System.out.println(e.getClass());
	}
	
 }
}
