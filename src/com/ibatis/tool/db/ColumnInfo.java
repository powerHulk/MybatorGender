package com.ibatis.tool.db;

import java.util.List;

public class ColumnInfo {
	private String className;//完整类型 java.lang.String
	private String classNameShort;//简名 String
	private String columnName;//字段名 USER_NAME
	private String javaName;//javaname userName
	private String comment;//注释
	private String jdbcType;//JDBC类型 VARCHAR
	private boolean isPk = false;

	private List<ColumnInfo> prmryKeys;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getClassNameShort() {
		return classNameShort;
	}
	public void setClassNameShort(String classNameShort) {
		this.classNameShort = classNameShort;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public List<ColumnInfo> getPrmryKeys() {
		return prmryKeys;
	}
	public void setPrmryKeys(List<ColumnInfo> prmryKeys) {
		this.prmryKeys = prmryKeys;
	}
	
	public String getJavaName() {
		return javaName;
	}
	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}
	
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof ColumnInfo){
			ColumnInfo co = (ColumnInfo)o;
			return this.columnName != null && this.columnName.equals(co.getColumnName());
		}
		return false;
	}
}
