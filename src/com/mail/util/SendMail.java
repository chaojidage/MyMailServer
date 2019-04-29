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

	// 收件人邮箱（替换为自己知道的有效邮箱）
	public static String receiveMailAccount = "";// "990219@gqm.com";

	public static void send() throws Exception {
		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties(); // 参数配置
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", myEmailSMTPHost); // 发件人的邮箱的 SMTP
																// 服务器地址
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
		Session session = Session.getInstance(props);
		//session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log

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

		// 7. 关闭连接
		transport.close();
	}

	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
		// 1. 创建一封邮件
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
		message.setFrom(new InternetAddress(sendMail, "gqm", "UTF-8"));

		String[] to = receiveMail.split(" ");
		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		InternetAddress[] sendTo = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			sendTo[i] = new InternetAddress(to[i]);
		}
		
		message.setRecipients(MimeMessage.RecipientType.TO, sendTo);//new InternetAddress(receive[i], "guqimi", "UTF-8"));

		// 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
		message.setSubject(subject, "UTF-8");

		// 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
		message.setContent(content, "text/html;charset=UTF-8");

		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}

}
