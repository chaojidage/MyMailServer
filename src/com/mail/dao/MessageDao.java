package com.mail.dao;

import java.util.List;

import com.mail.entity.Message;

public interface MessageDao {
	void save(Message message);
	List<Message> getMessageList(String email);
	int getMaxId();
}
