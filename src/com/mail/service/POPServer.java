package com.mail.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.mail.dao.MessageDao;
import com.mail.dao.UserDao;
import com.mail.daoImpl.MessageDaoImpl;
import com.mail.daoImpl.UserDaoImpl;
import com.mail.entity.Message;

public class POPServer extends Thread {
	ServerSocket serverSocket;
	public static POPServer server;

	private POPServer() {

	}
	
	//启动服务器
	public static void openServer() {
		server = new POPServer();
		server.start();
	}
	
	//关闭服务器
	public static void closeServer() {
		server.close();
		System.out.println("pop服务器关闭");
	}

	@SuppressWarnings("deprecation")
	public void close() {
		try {
			serverSocket.close();
			server.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(995);
			System.out.println("pop服务启动");
			while (true) {
				Socket socket = serverSocket.accept();
				new POPServerThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		POPServer.openServer();
	}
}

/**
 * 客户端代理线程
 * @author 郭清明
 *
 */
class POPServerThread extends Thread {
	Socket socket;
	List<Message> list;
	boolean flag = true;
	BufferedInputStream inputStream;
	BufferedOutputStream outputStream;
	String email = null, password;

	public POPServerThread(Socket socket) {
		this.socket = socket;
		try {
			inputStream = new BufferedInputStream(socket.getInputStream());
			outputStream = new BufferedOutputStream(socket.getOutputStream());
			write("+OK Welcome to gqm Mail Pop3 Server");
			System.out.println("+OK Welcome to gqm Mail Pop3 Server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String input;
		while (flag) {
			try {
				input = read();

				if (input.toLowerCase().startsWith("capa")) {
					System.out.println(input);
					write("-ERR Unknown command");
				} else if (input.toLowerCase().startsWith("user")) {
					System.out.println(input);
					String[] strings = input.split(" ");
					email = strings[1];
					write("+OK core mail");
				} else if (input.toLowerCase().startsWith("pass")) {
					authLogin(input);

				} else {

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	// 用户登录
	public void authLogin(String input) throws IOException {
		System.out.println(input);
		String[] strings = input.split(" ");
		password = strings[1];
		UserDao userDao = UserDaoImpl.getInstanse();
		if (userDao.isUser(email, password)) {
			MessageDao messageDao = MessageDaoImpl.getInstanse();
			list = messageDao.getMessageList(email);
			write("+OK 654 message(s)");
			deal();
			socket.close();
			flag = false;
		} else {
			write("-ERR login faile");
			flag = false;
		}
	}

	// 登录成功，进入处理状态
	public void deal() throws IOException {
		String input;
		while (true) {
			input = read();

			if (input.toLowerCase().startsWith("stat")) {
				// 邮箱中的邮件数量和邮件占用的字节大小等
				System.out.println(input);
				stat();
			} else if (input.toLowerCase().startsWith("uidl")) {
				// 命令用于查询某封邮件的唯一标志符，参数msg#表示邮件的序号，是一个从1开始编号的数字
			} else if (input.toLowerCase().startsWith("list")) {
				// list 命令用于列出邮箱中的邮件信息，参数 msg#是一个可选参数，表示邮件的序号。当不指定参数时，
				// POP3服务器列出邮箱中所有的邮件信息；当指定参数msg#时，POP3服务器只返回序号对应的邮件信息。
				String[] strings = input.split(" ");
				list(Integer.parseInt(strings[1]));
			} else if (input.toLowerCase().startsWith("retr")) {
				// retr 命令用于获取某封邮件的内容，参数 msg#表示邮件的序号
				System.out.println(input);
				String[] strings = input.split(" ");
				retr(Integer.parseInt(strings[1]));
			} else if (input.toLowerCase().startsWith("top")) {
				// top 命令用于获取某封邮件的邮件头和邮件体中的前n行内容，参数msg#表示邮件的序号，参数n表示要返回邮件的前几行内容。
				System.out.println(input);
				String[] strings = input.split(" ");
				top(Integer.parseInt(strings[1]));
			} else if (input.toLowerCase().startsWith("noop")) {
				// NOOP 处理 server 返回一个肯定的响应
				System.out.println(input);
				write("+OK");
			} else if (input.toLowerCase().startsWith("quit")) {
				update();
				return;
			}
		}
	}

	// 如果服务器处于“处理”状态，么将进入“更新”状态以删除任何标记为删除的邮件，并重返“认证”状态。
	public void update() {
		//
		return;
	}

	// 邮箱中的邮件数量和邮件占用的字节大小等
	public void stat() throws IOException {
		write("+OK " + list.size() + " " + list.toString().getBytes().length);
	}

	// uidl命令用于查询某封邮件的唯一标志符，参数msg#表示邮件的序号，是一个从1开始编号的数字
	public void uidl() {

	}

	// list 命令用于列出邮箱中的邮件信息，参数 msg#是一个可选参数，表示邮件的序号。当不指定参数时，
	// POP3服务器列出邮箱中所有的邮件信息；当指定参数msg#时，POP3服务器只返回序号对应的邮件信息。
	public void list(int n) throws IOException {
		if (n <= list.size() && n > 0) {
			Message message = list.get(n - 1);
			write("+OK " + n + " " + message.toString().getBytes().length);
		} else {
			write("-ERR");
			socket.close();
			flag = false;
		}
	}

	// retr 命令用于获取某封邮件的内容，参数 msg#表示邮件的序号
	public void retr(int n) throws IOException {
		if (n <= list.size() && n > 0) {
			write("+OK");
			Message message = list.get(n - 1);
			write("MIME-Version: 1.0");
			write("Content-Type: text/html;charset=UTF-8");
			write(message.getEncoding());
			write("");
			write(message.getContent());
			write(".");
		} else {
			write("-ERR");
			socket.close();
			flag = false;
		}
	}

	// top 命令用于获取某封邮件的邮件头和邮件体中的前n行内容，参数msg#表示邮件的序号，参数n表示要返回邮件的前几行内容。
	public void top(int n) throws IOException {
		if (n <= list.size() && n > 0) {
			Message message = list.get(n - 1);
			write("+OK");
			write("From:" + message.getFrom());
			write("To:" + message.getTo().get(0));
			write(message.getDate());
			write(message.getSubject());
			write("Content-Type: text/html;charset=UTF-8");
			write(message.getEncoding());
			write("");
			write(message.getContent());
			write(".");
		} else {
			write("-ERR");
			socket.close();
			flag = false;
		}
	}

	public String read() throws IOException {
		int ch;
		StringBuilder sb = new StringBuilder();
		while (true) {
			ch = inputStream.read();
			if (ch == 10) {
				break;
			}
			if (ch == 13)
				continue;
			sb.append((char) ch);
		}

		String input = sb.toString();
		return input;
	}

	public void write(String string) throws IOException {
		outputStream.flush();
		outputStream.write((string + ((char) (13)) + ((char) (10))).getBytes());
		outputStream.flush();
	}
}
