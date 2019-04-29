package com.mail.db;

public class ConnectionFactory {
	private static DB connection = null;
	private ConnectionFactory(){
		
	}
	public static DB getConnection(){
		if(connection==null){
			connection = new DB();
			return connection;
		}else{
			return connection;
		}
	}
}
