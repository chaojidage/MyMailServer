package com.mail.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mail.dao.AdminDao;
import com.mail.db.ConnectionFactory;
import com.mail.db.DB;

public class AdminDaoImpl implements AdminDao{

	@Override
	public boolean isAdmin(String name, String password) {
		boolean flag = false;
		String sql = "select * from tb_admin where name='"+name+"' and password='"+password+"';";
		DB connection = ConnectionFactory.getConnection();
		ResultSet rSet = connection.executeQuery(sql);
		try {
			while(rSet.next()){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public static AdminDao getInstanse(){
		return new AdminDaoImpl();
	}
}
