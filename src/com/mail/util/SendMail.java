package com.mail.util;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SendMail {

	public static String myEmailAccount = "";// "921015@gqm.com";//
												// "2662799389@qq.com";
	public static String myEmailPassword = "";// "921015";
	public static String content = "", subject = "";
	public static String myEmailSMTPHost = "localhost";

	// �ռ������䣨�滻Ϊ�Լ�֪������Ч���䣩
	public static String receiveMailAccount = "";// "990219@gqm.com";

	public static void send() throws Exception {
		// 1. ������������, ���������ʼ��������Ĳ�������
		Properties props = new Properties(); // ��������
		props.setProperty("mail.transport.protocol", "smtp"); // ʹ�õ�Э�飨JavaMail�淶Ҫ��
		props.setProperty("mail.smtp.host", myEmailSMTPHost); // �����˵������ SMTP
																// ��������ַ
		props.setProperty("mail.smtp.auth", "true"); // ��Ҫ������֤
		Session session = Session.getInstance(props);
		//session.setDebug(true); // ����Ϊdebugģʽ, ���Բ鿴��ϸ�ķ��� log

		MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);

		Transport transport = session.getTransport();

		try {
			transport.connect(myEmailAccount, myEmailPassword);
		} catch (Exception e) {
			System.out.println("com.mail.service.SendMail.main()");
			System.exit(0);
		}

		try {
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			System.out.println("com.mail.service.SendMail.main()");
			System.exit(0);
		}

		// 7. �ر�����
		transport.close();
	}

	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
		// 1. ����һ���ʼ�
		MimeMessage message = new MimeMessage(session);

		// 2. From: �����ˣ��ǳ��й�����ɣ����ⱻ�ʼ�����������Ϊ���ķ������������ʧ�ܣ����޸��ǳƣ�
		message.setFrom(new InternetAddress(sendMail, "gqm", "UTF-8"));

		String[] to = receiveMail.split(" ");
		// 3. To: �ռ��ˣ��������Ӷ���ռ��ˡ����͡����ͣ�
		InternetAddress[] sendTo = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			sendTo[i] = new InternetAddress(to[i]);
		}
		
		message.setRecipients(MimeMessage.RecipientType.TO, sendTo);//new InternetAddress(receive[i], "guqimi", "UTF-8"));

		// 4. Subject: �ʼ����⣨�����й�����ɣ����ⱻ�ʼ�����������Ϊ���ķ������������ʧ�ܣ����޸ı��⣩
		message.setSubject(subject, "UTF-8");

		// 5. Content: �ʼ����ģ�����ʹ��html��ǩ���������й�����ɣ����ⱻ�ʼ�����������Ϊ���ķ������������ʧ�ܣ����޸ķ������ݣ�
		message.setContent(content, "text/html;charset=UTF-8");

		// 6. ���÷���ʱ��
		message.setSentDate(new Date());

		// 7. ��������
		message.saveChanges();

		return message;
	}

}
