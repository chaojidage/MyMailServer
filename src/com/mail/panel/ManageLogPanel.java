package com.mail.panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import com.mail.util.MyFont;

public class ManageLogPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static Vector<String> columnNames;
	public static Vector<Vector<String>> logMessages;
	public static JTable logTable;
	JLabel label;

	public ManageLogPanel() {
		this.setLayout(null);
		init();
		
		label = new JLabel("用户操作日志");
		label.setFont(MyFont.biaoti);
		label.setBounds(280, 0, 200, 50);
		this.add(label);

		logTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				System.out.println(logTable.getSelectedRow() + "," + logTable.getSelectedColumn());
			}
		});

		JScrollPane scrollPane = new JScrollPane(logTable);
		scrollPane.setBounds(50, 50, 680, 400);
		this.add(scrollPane);
		this.add(new JLabel("ManageUserPanel"));
		this.setSize(600, 600);
	}
	
	public void init(){
		columnNames = new Vector<>();
		columnNames.add("用户");
		columnNames.add("操作");
		columnNames.add("时间");
		
		logMessages = new Vector<>();
		logTable = new JTable(logMessages, columnNames);
		logTable.setEnabled(false);
		
		DefaultTableCellRenderer r   = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);   
		logTable.setDefaultRenderer(Object.class, r);
		
	}
	
	public static void main(String[] args) {
		JFrame jFrame = new JFrame("userList");
		jFrame.add(new ManageLogPanel());

		jFrame.setSize(800, 600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		
	}
}
