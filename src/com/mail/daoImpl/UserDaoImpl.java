package com.mail.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mail.dao.UserDao;
import com.mail.db.ConnectionFactory;
import com.mail.db.DB;
import com.mail.entity.User;

public class UserDaoImpl implements UserDao{

	@Override
	public boolean isUser(String email, String password) {
		boolean flag = false;
		String sql = "select * from tb_user where email='"+email+"' and password='"+password+"';";
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
	
	public static UserDao getInstanse(){
		return new UserDaoImpl();
	}

	@Override
	public void save(User user) {
		String sql = "insert into tb_user values("+(getMaxId()+1)+",'"
					+user.getName()+"','"+user.getPasswoed()+"','"+user.getEmail()+"');";
		DB connection = ConnectionFactory.getConnection();
		connection.executeUpdate(sql);
	}
	
	public int getMaxId(){
		int id = 0;
		String sql = "select MAX(userid) from tb_user;";
		DB connection = ConnectionFactory.getConnection();
		ResultSet rSet = connection.executeQuery(sql);
		try {
			while(rSet.next()){
				id = rSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}

	@Override
	public String getUserEmail(String username) {
		String email = "";
		String sql = "select * from tb_user where username='"+username+"';";
		DB connection = ConnectionFactory.getConnection();
		ResultSet rSet = connection.executeQuery(sql);	
		try { 
			while(rSet.next()){
				email = rSet.getString(4);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return email;
	}
	
	/**
	 * 获取好友的列表
	 */
	@Override
	public List<String> getFriendList(String email) {
		List<String> list = new ArrayList<>();
		String sql = "select * from tb_friend where memail = '"+email+"';";
		DB connection = ConnectionFactory.getConnection();
		ResultSet rSet = connection.executeQuery(sql);
		try {
			while(rSet.next()){
				list.add(rSet.getString(2)+" "+rSet.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 添加好友
	 */
	@Override
	public void addFriend(String femail,String memail,String name) {
		String sql = "insert into tb_friend values('"+memail+"','"+femail+"','"+name+"');";
		DB connection = ConnectionFactory.getConnection();
		connection.executeUpdate(sql);
	}

	@Override
	public User getUser(String email) {
		User user = null;
		String sql = "select * from tb_user where email = '"+email+"';";
		DB connection = ConnectionFactory.getConnection();
		ResultSet rSet = connection.executeQuery(sql);
		try {
			while(rSet.next()){
				user = new User();
				user.setUserid(rSet.getInt(1));
				user.setEmail(email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<User> getUserList() {
		ArrayList<User> list = new ArrayList<>();
		String sql = "select * from tb_user;";
		DB connection = ConnectionFactory.getConnection();
		ResultSet rSet = connection.executeQuery(sql);
		try{
			while(rSet.next()){
				User user = new User();
				user.setUserid(rSet.getInt(1));
				user.setEmail(rSet.getString(4));
				list.add(user);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 删除好友
	 * memail:我的email
	 * friendemail:好友的email
	 */
	@Override
	public void deleteFriend(String memail, String femail) {
		String sql = "delete from tb_friend where memail='"+memail+"' and friendemail='"+femail+"';";
		DB connection = ConnectionFactory.getConnection();
		connection.executeUpdate(sql);
	}
}
