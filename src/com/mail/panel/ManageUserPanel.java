package com.mail.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.mail.dao.UserDao;
import com.mail.daoImpl.UserDaoImpl;
import com.mail.entity.User;
import com.mail.util.MyFont;

//浏览用户信息以及删除用户，群发邮件
public class ManageUserPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	JTable userTable;
	JLabel label;

	public ManageUserPanel() {
		this.setLayout(null);

		label = new JLabel("用户信息");
		label.setFont(MyFont.biaoti);
		label.setBounds(280, 0, 120, 50);
		this.add(label);

		UserDao userDao = UserDaoImpl.getInstanse();
		List<User> list = userDao.getUserList();
		Object[][] data = new Object[list.size()][2];
		String[] columnNames = { "id", "email" };

		for (int i = 0; i < list.size(); i++) {
			data[i][0] = list.get(i).getUserid();
			data[i][1] = list.get(i).getEmail();
		}

		userTable = new JTable(data, columnNames);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		userTable.setDefaultRenderer(Object.class, r);
		userTable.setEnabled(false);
		
		userTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(userTable.getSelectedRow() + "," + userTable.getSelectedColumn());
			}
		});

		JScrollPane scrollPane = new JScrollPane(userTable);
		scrollPane.setBounds(50, 50, 680, 400);
		this.add(scrollPane);
		this.add(new JLabel("ManageUserPanel"));
		this.setSize(600, 600);
	}

	public static void main(String[] args) {
		JFrame jFrame = new JFrame("userList");
		jFrame.add(new ManageUserPanel());

		jFrame.setSize(800, 600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
