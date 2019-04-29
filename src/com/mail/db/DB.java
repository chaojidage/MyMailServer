package com.mail.db;

import java.sql.*;
public class DB {
	private String url = "jdbc:mysql://localhost:3306/db_mail?&useSSL=true";
	private String className = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "990219";
	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;
	public DB(){
		try{
			Class.forName(className).newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void createCon(){
		try{
			con = DriverManager.getConnection(url,userName,password);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void getStm(){
		createCon();
		try{
			stm = con.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean executeUpdate(String sql){
		boolean mark=false;
		try{
			getStm();
			int iCount = stm.executeUpdate(sql);
			if(iCount>0)
				mark=true;
			else
				mark=false;
		}catch(Exception e){
			e.printStackTrace();
		}
		return mark;
	}
	public ResultSet executeQuery(String sql){
		try{
			getStm();
			rs = stm.executeQuery(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public void close(){
		try{
			if(rs!=null)
				rs.close();
			if(stm!=null)
				stm.close();
			if(con!=null)
				con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

