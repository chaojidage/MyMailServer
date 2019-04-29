package com.mail.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.mail.dao.UserDao;
import com.mail.daoImpl.UserDaoImpl;
import com.mail.entity.User;
import com.mail.util.LogUtil;

public class UserServer extends Thread {
	ServerSocket serverSocket;
	public static UserServer server;
	public static List<User> loginUserList = Collections.synchronizedList(new ArrayList<>());

	public UserServer() {

	}

	public static void openServer() {
		server = new UserServer();
		server.start();
	}

	@SuppressWarnings("deprecation")
	public static void closeServer() {
		try {
			server.serverSocket.close();
			server.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("UserServer����");
			serverSocket = new ServerSocket(8001);
			Socket socket;
			while (true) {
				socket = serverSocket.accept();
				new SystemServerThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UserServer.openServer();
	}
}

class SystemServerThread extends Thread {
	BufferedInputStream inputStream;
	BufferedOutputStream outputStream;
	Socket socket;
	User user;
	String email;
	String password;

	public SystemServerThread(Socket socket) {
		this.socket = socket;
		try {
			inputStream = new BufferedInputStream(socket.getInputStream());
			outputStream = new BufferedOutputStream(socket.getOutputStream());
			System.out.println("system server ready");
			// write("system server ready");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		String input;
		while (true) {
			try {
				input = read();
				System.out.println(input);
				if (input.toLowerCase().startsWith("register")) {
					register(input);
				} else if (input.toLowerCase().startsWith("login")) {
					login(input);
				} else if (input.equals("getFriendList")) {
					getFriendList();
				} else if (input.startsWith("addFriend")) {
					addFriend(input);
				}else if (input.toLowerCase().startsWith("online")) {
					online(input);
				}else if (input.startsWith("deleteFriend")) {
					deleteFriend(input);
				}else if (input.startsWith("close")) {
					socket.close();
					this.stop();
				}
			} catch (IOException e) {
				e.printStackTrace();
				this.stop();
			}
		}

	}
	
	//ɾ������
	public void deleteFriend(String input){
		String[] strings = input.split(" ");
		UserDaoImpl.getInstanse().deleteFriend(email, strings[1]);
	}
	
	//�û�����
	public void online(String input) throws IOException {
		String[] strings = input.split(" ");
		user = new User();
		user.setEmail(strings[1]);
		email = strings[1];
		recordLog(user.getEmail(), "�û�����");
	}
	
	// �û�ע��
	public void register(String input) throws IOException {
		String[] strings = input.split(" ");
		email = strings[1];
		password = strings[2];
		System.out.println(email + "  " + password);
		user = new User();
		user.setName(email);
		user.setPasswoed(password);
		user.setEmail(email+"@gqm.com");
		recordLog(email+"@gqm.com", "�û�ע��");
		// UserServer.loginUserList.add(user);
		UserDaoImpl.getInstanse().save(user);
	}

	// �û���¼
	public void login(String input) throws IOException {
		String[] strings = input.split(" ");
		System.out.println(input);
		email = strings[1];
		password = strings[2];
		System.out.println(email + "  " + password);
		UserDao userDao = UserDaoImpl.getInstanse();
		if (userDao.isUser(email, password)) {
			recordLog(email, "�û���¼");
			user = userDao.getUser(email);
			// UserServer.loginUserList.add(user);
			write("ok");
		} else {
			write("error");
		}
	}

	// ��ȡ����
	public void getFriendList() throws IOException {
		UserDao userDao = UserDaoImpl.getInstanse();
		List<String> list = userDao.getFriendList(email);
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("");
		for (int i = 0; i < list.size(); i++)
			sBuilder.append(list.get(i)).append(" ");
		list = null;
		System.out.println(sBuilder.toString());
		write(sBuilder.toString());
		sBuilder = null;
	}

	// ��Ӻ���
	public void addFriend(String input) throws IOException {
		UserDao userDao = UserDaoImpl.getInstanse();
		String[] strings = input.split(" ");
		String friendemail = strings[2];
		String friendname = strings[1];
		System.out.println(friendemail+" "+friendname);
		userDao.addFriend(friendemail, user.getEmail(),friendname);
	}

	@SuppressWarnings("deprecation")
	public String read() throws IOException {
		int ch = -1;
		StringBuilder sb = new StringBuilder();
		while (true) {
			try {
				ch = inputStream.read();
				if (ch == 10) {
					break;
				}
				if (ch == 13)
					continue;
				sb.append((char) ch);
			} catch (Exception e) {
				this.stop();
			}
		}

		String input = sb.toString();
		sb = null;
		return input;
	}

	public void write(String string) throws IOException {
		outputStream.flush();
		outputStream.write((string + ((char) (13)) + ((char) (10))).getBytes());
		outputStream.flush();
	}

	public void recordLog(String email, String log) {
		LogUtil.addLog(email, log);
	}

}
