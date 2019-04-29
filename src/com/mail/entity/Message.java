package com.mail.entity;

import java.util.ArrayList;
import java.util.List;


public class Message {
	private int id;
	private String from;
	private List<String> to = new ArrayList<>();
	private String date;
	private String subject;
	private StringBuilder content = new StringBuilder();
	private String fromname = "";
	private List<String> toname = new ArrayList<>();
	private String encoding;
	
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getFromname() {
		return fromname;
	}

	public void setFromname(String fromname) {
		this.fromname = fromname;
	}

	public List<String> getToname() {
		return toname;
	}

	public void addToname(String toname) {
		this.toname.add(toname);
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public void setContent(StringBuilder content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void addTo(String to) {
		this.to.add(to);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content.toString();
	}

	public void setContent(String content) {
		this.content.append(content);
	}

}
