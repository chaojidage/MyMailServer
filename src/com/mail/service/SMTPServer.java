package com.mail.service;

/**
 * outputStream.write(("123456"+((char)(13))+((char)(10))).getBytes());
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.mail.daoImpl.MessageDaoImpl;
import com.mail.daoImpl.UserDaoImpl;
import com.mail.entity.Message;
import com.mail.util.ParserBase64;

public class SMTPServer extends Thread {
	public static SMTPServer server;
	ServerSocket serverSocket;
	Socket socket;
	boolean flag = true;

	private SMTPServer() {

	}
	
	//�򿪷�����
	public static void openServer() {
		server = new SMTPServer();
		server.start();
	}
	
	//�رշ�����
	public static void closeServer() {
		server.close();
		System.out.println("smtp�������ر�");
	}

	@SuppressWarnings("deprecation")
	public void close() {
		try {
			server.serverSocket.close();
			server.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �������߳�
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(25);
			System.out.println("smtp��������");
			while (flag) {
				socket = serverSocket.accept();
				new SMTPServerThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SMTPServer.openServer();
	}
}

/**
 * �ͻ��˴����߳�
 * @author zsd
 *
 */
class SMTPServerThread extends Thread {

	Socket socket;
	String email, password;
	BufferedInputStream inputStream;
	BufferedOutputStream outputStream;
	boolean flag1 = true;

	public SMTPServerThread(Socket socket) {
		this.socket = socket;
		try {
			inputStream = new BufferedInputStream(socket.getInputStream());
			outputStream = new BufferedOutputStream(socket.getOutputStream());
			write("220 server ready");
			System.out.println("220 server ready");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �ͻ��˴����߳�
	@Override
	public void run() {
		while (flag1) {
			try {
				String input = read();
				// ���к�
				if (input.toLowerCase().startsWith("ehlo")) {
					EHLOHandler();
				} else if (input.toLowerCase().equals("quit")) {
					// ��������
					System.out.println(input);
					write("221 Bye");
					this.socket.close();
					flag1 = false;
				} else {
					this.socket.close();
					flag1 = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ���к�
	public void EHLOHandler() throws Exception {
		write("250-smtp.gqm.com");
		write("250-smtp.gqm.com");
		write("250-AUTH LOGIN PLAIN");
		write("250-MAILCOMPRESS");
		write("250 8BITMIME");
		String input = read();

		if (input.toLowerCase().equals("auth login")) {
			authLogin();
		} else {
			this.socket.close();
			flag1 = false;
		}
	}

	// �û���֤
	public void authLogin() throws Exception {
		String input;
		write("334 " + "VXNlcm5hbWU6");// email:
		input = read();
		System.out.println("Username:" + input);
		email = input;

		write("334 " + "UGFzc3dvcmQ6");// Password:
		input = read();
		System.out.println("Password:" + input);
		password = input;

		email = ParserBase64.parserBase64(email);
		password = ParserBase64.parserBase64(password);
		System.out.println(email + "  " + password);
		if (UserDaoImpl.getInstanse().isUser(email, password)) {
			// �û�����������ȷ
			write("235 Authentication successful");
			fromTo();
		} else {
			write("550 Authentication faile");
			this.socket.close();
			flag1 = false;
		}
	}

	// �������������
	public void fromTo() throws IOException {
		String input;
		input = read();
		if (input.toLowerCase().startsWith("mail from")) {
			System.out.println(input);
			write("250 Ok");
			input = read();
			while (input.toLowerCase().startsWith("rcpt to")) {
				System.out.println(input);
				write("250 Ok");
				input = read();
			}
			if (input.toLowerCase().equals("data")) {
				data();
			}
		} else {
			write("550 commend faile");
			this.socket.close();
			flag1 = false;
		}

	}

	// ��д�ʼ�
	public void data() throws IOException {
		write("354 End data with <CR><LF>.<CR><LF>");
		// �����ʼ���Ϣ
		Message message = new Message();
		boolean flag = true;
		while (flag) {
			String input = read();
			if (input.toLowerCase().startsWith("from")) {
				// ������
				// System.out.println(input);
				String[] strings = input.split(" ");
				message.setFrom(strings[2]);
				message.setFromname(strings[1]);
			} else if (input.toLowerCase().startsWith("to")) {
				// �ռ���
				// System.out.println(input);
				String[] strings = input.split(" ");
				
				//����
				if(input.contains(",")){
					for(int i=1;i<strings.length;i++){
						message.addTo("<"+strings[i].split(",")[0]+">");
						message.addToname("");
					}
				}else{
					message.addTo(strings[2]);
					message.addToname(strings[1]);
				}
			} else if (input.toLowerCase().startsWith("subject")) {
				// ����
				System.out.println(input);
				message.setSubject(input);
			} else if (input.toLowerCase().startsWith("date")) {
				// ����
				// System.out.println(input);
				message.setDate(input);
			} else if (input.length() == 0) {
				content(message);
				return;
			} else if (input.startsWith("Content-Transfer-Encoding")) {
				// Content-Transfer-Encoding
				System.out.println(input);
				message.setEncoding(input);
			} else {
				// write("550 commend faile");
				// this.socket.close();
				// flag1 = false;
				
				if(input.startsWith("	")){
					input = input.substring(1, input.length());
					String[] strings = input.split(" ");
					for (String string : strings) {
						message.addTo("<"+string.split(",")[0]+">");
						message.addToname("");
					}
				}
				System.out.println("123 " + input);
			}
		}
	}

	// ��������
	public void content(Message message) throws IOException {
		String input;
		while (true) {
			input = read();
			// ���Ľ���
			if (input.equals(".")) {
				write("250 Ok: queued as");
				MessageDaoImpl.getInstanse().save(message);
				break;
			}
			// ����
			System.out.println("����:" + input);
			message.setContent(input);
		}
	}

	// ��ȡ����
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

	// д����
	public void write(String string) throws IOException {
		outputStream.flush();
		outputStream.write((string + ((char) (13)) + ((char) (10))).getBytes());
		outputStream.flush();
	}
}
