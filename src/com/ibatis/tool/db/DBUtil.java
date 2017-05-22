package com.ibatis.tool.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.LinkedList;
import java.util.List;

public class DBUtil {


		private static Connection con;

		/**
		 * 加载驱动
		 * 
		 * 使用的是 SQL2005 的驱动程序因此务必将2005的驱动架包引入或置于项目根目录下
		 * 
		 * @return
		 */

		public static void getConnection(String driverClassName, String url,
				String username, String password) {
			try {
				Class.forName(driverClassName);
				con = DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 关闭连接的方法
		 * 
		 * @param rs
		 * @param pstm
		 * @param con
		 */
		public static void closeConnection() {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 获得表头
		 * 
		 * @param tableName
		 * @return [类全名,字段名,字段java类,注释,主键]
		 */
		public static List<ColumnInfo>getMetaData(String tableName) throws Exception{
			List<ColumnInfo> list = new ArrayList<ColumnInfo>();
			ResultSet rs = null;
				try {
				
				 DatabaseMetaData dmd = con.getMetaData();
				 rs = dmd.getPrimaryKeys(con.getCatalog(),null,tableName.toUpperCase());
				 List<ColumnInfo> pkList = new ArrayList<ColumnInfo>();
				 List<String> pkListStr = new ArrayList<String>();
				 while(rs.next()){
					 String columnName = rs.getString("COLUMN_NAME");
					 pkListStr.add(columnName);
				 }
				 
				 rs = dmd.getColumns(con.getCatalog(),null,tableName.toUpperCase(),null);
				 while (rs.next()){
					 String columnName = rs.getString("COLUMN_NAME");
					 String type = rs.getString("TYPE_NAME");
					 int scale = rs.getInt("DECIMAL_DIGITS");
					 //String comment = rs.getString("REMARKS");
					 String comment = getColComment(tableName,columnName);
					 if(comment != null){
						 comment = comment.replaceAll("\n|\r"," ");
					 }
					 int size = rs.getInt("COLUMN_SIZE");
					 String[] typeClass = transType(type,size,scale);
					 String classNameShort = typeClass[0];
					 String className = typeClass[1];
					 String jdbcType = typeClass[2];
					 ColumnInfo c = new ColumnInfo();
					 c.setColumnName(columnName);
					 c.setComment(comment);
					 c.setClassName(className);
					 c.setClassNameShort(classNameShort);
					 c.setJavaName(formatFiledString(columnName.toLowerCase()));
					 c.setJdbcType(jdbcType);
					 c.setPrmryKeys(pkList);
					 if(pkListStr.contains(columnName)){
						 c.setPk(true);
						 pkList.add(c);
					 }
					 //list.add(new String[]{className,name,classNameShort,comment,prmryKey,jdbcType});
					 list.add(c);
					 
				 }
				 
//				 pst = con.prepareStatement("select * from "
//						+ tableName + " where 1=2");
//				 ResultSetMetaData rsd=pst.executeQuery().getMetaData();
//				 for (int i = 0; i < rsd.getColumnCount(); i++) {
//					 String columnName = rsd.getColumnName(i+1);//列名
//					 String className = rsd.getColumnClassName(i+1);//类 如java.lang.string
//					 int scale  = rsd.getScale(i+1);//小数点
//					 String classNameShort = className.substring(className.lastIndexOf(".")+1);
//					 //格式转化
//					 if("BigDecimal".equals(classNameShort)){
//						 if(scale == 0){
//							 classNameShort = "Long";
//							 className = "java.lang.Long";
//						 }
//					 }else if("Timestamp".equals(classNameShort)){
//						 classNameShort = "Date";
//						 className = "java.util.Date";
//					 }
//					
//					
//					//int precision = rsd.getPrecision(i+1);//宽度
//					list.add(new String[]{className,columnName,classNameShort});
//					
//				 }
				return list;
			}finally{
				try {
					//closeConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		}
		/**
		 * 取得当前用户下的所有表
		 * @return
		 */
		public static List<String> getAllTable(){
			PreparedStatement pst=null;
			ResultSet rs =null;
			try {
				String sql = "select table_name from user_all_tables";

				 pst = con.prepareStatement(sql);
				 rs = pst.executeQuery();
				
				List<String> list = new ArrayList<String>();
				while(rs.next()){
				list.add(rs.getString(1));
				}
				return list;
			} catch (SQLException e) {
				throw new RuntimeException("can't select all tables ");
			}finally{
				try {
					pst.close();pst=null;
					rs.close();rs=null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		/**
		 * 创建序列
		 * @param sequenceName
		 */
		public static void createSequence(String sequenceName)
		{
			PreparedStatement pst=null;
			 String sql = "create sequence "+sequenceName+" minvalue 1 maxvalue 9999999999999999 increment by 1 start with 1 cache 10 noorder nocycle";   
			 try {
			  pst = con.prepareStatement(sql);   
			  pst.execute(); 
			 } catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException("can't create sequenceName " + sequenceName);
				} finally{
					try {
						pst.close();pst=null;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		}
		/**
		 * 取得表的描述
		 * @param owner
		 * @param tableName
		 * @return
		 */
		public static String getTableDesc(String owner, String tableName) {
			PreparedStatement pst=null;
			ResultSet rs =null;
			try {
				String sql = "SELECT nvl(comments,'" + tableName.toUpperCase()
						+ "')   FROM Dba_Tab_Comments" + "  where upper(owner)='"
						+ owner.toUpperCase() + "' and upper(table_name)='"
						+ tableName.toUpperCase() + "'";

				 pst = con.prepareStatement(sql);
				 rs = pst.executeQuery();
				 if(rs.next())
				return rs.getString(1);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("can't select comments " + tableName);
			} finally{
				try {
					pst.close();pst=null;
					rs.close();rs=null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			return null;
		}
		/**
		 * 取得字段的描述
		 * @param tableName
		 * @param colName
		 * @return
		 */
		private static String getColComment(String tableName, String colName) {
			PreparedStatement pst=null;
			ResultSet rs =null;
			try {
				String sql = "select   comments from   user_col_comments   where  upper(table_name)='"
						+ tableName.toUpperCase() + "'  and  upper(column_name)='"+colName.toUpperCase()+"' ";
				 pst = con.prepareStatement(sql);
				 rs = pst.executeQuery();
				if(rs.next())
				return rs.getString(1);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("can't select comments " + tableName);
			}finally{
				try {
					pst.close();pst=null;
					rs.close();rs=null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			return null;
		}
		
		/**
		 * 
		 * @param typeName
		 * @param scale 小数点，非数字形不需要
		 * @return
		 */
		public static String[] transType(String typeName,int length,int scale) throws Exception{
			String[] result = new String[3];
			if("VARCHAR2".equals(typeName) || "CHAR".equals(typeName) || "CLOB".equals(typeName)
				|| "NVARCHAR2".equals(typeName)){
				result[0] = "String";
				result[1] = "java.lang.String";
				if("VARCHAR2".equals(typeName) || "NVARCHAR2".equals(typeName)){
					result[2] = "VARCHAR";
				}else if("CHAR".equals(typeName)){
					result[2] = "CHAR";
				}else if("CLOB".equals(typeName)){
					result[2] = "CLOB";
				}else{
					result[2] = "VARCHAR";
				}
				result[1] = "java.lang.String";
			}else if("NUMBER".equals(typeName)){
				if(scale <= 0){
					if(length > 5){
						result[0] = "Long";
						result[1] = "java.lang.Long";
						result[2] = "BIGINT";
					}else{
						result[0] = "Integer";
						result[1] = "java.lang.Integer";
						result[2] = "INTEGER";
					}
					
				}else{
					result[0] = "BigDecimal";
					result[1] = "java.math.BigDecimal";
					result[2] = "DECIMAL";
				}
				
			}else if("DATE".equals(typeName)){
				result[0] = "Date";
				result[1] = "java.util.Date";
				result[2] = "TIMESTAMP";
			}else if(typeName.startsWith("TIMESTAMP")){
				result[0] = "Timestamp";
				result[1] = "java.sql.Timestamp";
				result[2] = "TIMESTAMP";
			}
			else{
				throw new Exception("未配置的数据类型:"+typeName+"请在DBUtil.transType中添加.");
			}
			return result;
		}
		
		/**
		 * 格式化字段名称 下划线转驼峰
		 * @param filed
		 * @return
		 */
		private static String formatFiledString(String filed){
			String [] fileds = filed.split("_");
			String retFiled = "";
			int i = 0;
			for(String f : fileds){
				if(i == 0){
					f = f.substring(0, 1).toLowerCase() + f.substring(1, f.length());
				}else{
					f = f.substring(0, 1).toUpperCase() + f.substring(1, f.length());
				}
				retFiled = retFiled + f;
				i++;
			}
			return retFiled;
		}

		
		public static void main(String[] args) {

		}
	}