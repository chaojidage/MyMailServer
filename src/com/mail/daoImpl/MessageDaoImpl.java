package com.mail.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mail.dao.MessageDao;
import com.mail.db.ConnectionFactory;
import com.mail.db.DB;
import com.mail.entity.Message;

public class MessageDaoImpl implements MessageDao{

	@Override
	public void save(Message message) {
		System.out.println("±£´æÓÊ¼þ");
		System.out.println(message.getFrom());
		System.out.println(message.getDate());
		System.out.println(message.getTo());
		System.out.println(message.getSubject());
		System.out.println(message.getContent());
		message.setId(getMaxId()+1);
		List<String> list = message.getTo();
		List<String> list2 = message.getToname();
		for(int i=0;i<list.size();i++){
			String sql = "insert into tb_email values("+message.getId()
						+",'"+message.getFromname()+"','"+message.getFrom()
						+"','"+list.get(i)+"','"+list2.get(i)+"','"+message.getDate()
						+"','"+message.getSubject()+"','"+message.getContent()+"','"+message.getEncoding()+"');";
			DB connection = ConnectionFactory.getConnection();
			connection.executeUpdate(sql);
		}

	}

	@Override
	public List<Message> getMessageList(String email) {
		List<Message> list = new ArrayList<>();
		String sql = "select * from tb_email where email_to = '<"+email+">';";
		DB connection = ConnectionFactory.getConnection();
		ResultSet rSet = connection.executeQuery(sql);
		try {
			while(rSet.next()){
				Message message = new Message();
				message.setId(rSet.getInt(1));
				message.setFrom(rSet.getString(3));
				message.setFromname(rSet.getString(2));
				message.addTo(email);
				message.setDate(rSet.getString(6));
				message.setSubject(rSet.getString(7));
				message.setContent(rSet.getString(8));
				message.setEncoding(rSet.getString(9));
				list.add(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getMaxId() {
		int id = 0;
		String sql = "select MAX(email_id) from tb_email;";
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
	
	public static MessageDao getInstanse(){
		return new MessageDaoImpl();
	}
	
}
