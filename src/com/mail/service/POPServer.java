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
	
	//����������
	public static void openServer() {
		server = new POPServer();
		server.start();
	}
	
	//�رշ�����
	public static void closeServer() {
		server.close();
		System.out.println("pop�������ر�");
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
			System.out.println("pop��������");
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
 * �ͻ��˴����߳�
 * @author ������
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

	// �û���¼
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

	// ��¼�ɹ������봦��״̬
	public void deal() throws IOException {
		String input;
		while (true) {
			input = read();

			if (input.toLowerCase().startsWith("stat")) {
				// �����е��ʼ��������ʼ�ռ�õ��ֽڴ�С��
				System.out.println(input);
				stat();
			} else if (input.toLowerCase().startsWith("uidl")) {
				// �������ڲ�ѯĳ���ʼ���Ψһ��־��������msg#��ʾ�ʼ�����ţ���һ����1��ʼ��ŵ�����
			} else if (input.toLowerCase().startsWith("list")) {
				// list ���������г������е��ʼ���Ϣ������ msg#��һ����ѡ��������ʾ�ʼ�����š�����ָ������ʱ��
				// POP3�������г����������е��ʼ���Ϣ����ָ������msg#ʱ��POP3������ֻ������Ŷ�Ӧ���ʼ���Ϣ��
				String[] strings = input.split(" ");
				list(Integer.parseInt(strings[1]));
			} else if (input.toLowerCase().startsWith("retr")) {
				// retr �������ڻ�ȡĳ���ʼ������ݣ����� msg#��ʾ�ʼ������
				System.out.println(input);
				String[] strings = input.split(" ");
				retr(Integer.parseInt(strings[1]));
			} else if (input.toLowerCase().startsWith("top")) {
				// top �������ڻ�ȡĳ���ʼ����ʼ�ͷ���ʼ����е�ǰn�����ݣ�����msg#��ʾ�ʼ�����ţ�����n��ʾҪ�����ʼ���ǰ�������ݡ�
				System.out.println(input);
				String[] strings = input.split(" ");
				top(Integer.parseInt(strings[1]));
			} else if (input.toLowerCase().startsWith("noop")) {
				// NOOP ���� server ����һ���϶�����Ӧ
				System.out.println(input);
				write("+OK");
			} else if (input.toLowerCase().startsWith("quit")) {
				update();
				return;
			}
		}
	}

	// ������������ڡ�����״̬��ô�����롰���¡�״̬��ɾ���κα��Ϊɾ�����ʼ������ط�����֤��״̬��
	public void update() {
		//
		return;
	}

	// �����е��ʼ��������ʼ�ռ�õ��ֽڴ�С��
	public void stat() throws IOException {
		write("+OK " + list.size() + " " + list.toString().getBytes().length);
	}

	// uidl�������ڲ�ѯĳ���ʼ���Ψһ��־��������msg#��ʾ�ʼ�����ţ���һ����1��ʼ��ŵ�����
	public void uidl() {

	}

	// list ���������г������е��ʼ���Ϣ������ msg#��һ����ѡ��������ʾ�ʼ�����š�����ָ������ʱ��
	// POP3�������г����������е��ʼ���Ϣ����ָ������msg#ʱ��POP3������ֻ������Ŷ�Ӧ���ʼ���Ϣ��
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

	// retr �������ڻ�ȡĳ���ʼ������ݣ����� msg#��ʾ�ʼ������
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

	// top �������ڻ�ȡĳ���ʼ����ʼ�ͷ���ʼ����е�ǰn�����ݣ�����msg#��ʾ�ʼ�����ţ�����n��ʾҪ�����ʼ���ǰ�������ݡ�
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
