package com.mail.dao;

import java.util.List;

import com.mail.entity.User;

public interface UserDao {
	boolean isUser(String email,String password);
	void save(User user);
	int getMaxId();
	String getUserEmail(String username);
	List<String> getFriendList(String email);
	void addFriend(String femail,String memail,String name);
	User getUser(String email);
	List<User> getUserList();
	void deleteFriend(String memail,String femail);
}
