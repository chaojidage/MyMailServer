package com.mail.util;

import java.util.Vector;

import javax.swing.JTable;

import com.mail.panel.ManageLogPanel;

public class LogUtil {
	
	public static void addLog(String user,String operation){
		JTable logTable = ManageLogPanel.logTable;
		Vector<Vector<String>> logMessages = ManageLogPanel.logMessages;
		
		Vector<String> v = new Vector<>();
		v.addElement(user);
		v.addElement(operation);
		v.addElement(TimeUtil.getNormalTime());
		logMessages.addElement(v);
		logTable.updateUI();
	}
	
}
