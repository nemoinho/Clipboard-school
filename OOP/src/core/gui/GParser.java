package core.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.Manager;
import core.Parser;
import core.Profile;

import java.util.*;

/**
 * 
 * Beschreibung
 * 
 * @version 1.0 vom 11.05.2012
 * @author
 */

public class GParser extends JFrame {
	// Anfang Variablen
	private ArrayList<String> selectedParser = new ArrayList<String>();
	private JList jList1;
	private JButton btnSave = new JButton();
	private JButton btnCancel = new JButton();

	// Ende Variablen

	public GParser(String title, Manager manager) {
		// Frame-Initialisierung
		super(title);
		/*
		 * addWindowListener(new WindowAdapter() { public void
		 * windowClosing(WindowEvent evt) { this.dispose(); } });
		 */
		int frameWidth = 377;
		int frameHeight = 245;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		Container cp = getContentPane();
		cp.setLayout(null);
		// Anfang Komponenten
		Profile profile = new Profile();
		
		
		Vector<String> parserList = new Vector<String>();
		
		for(Parser p : profile.getParser()) {
			parserList.add(p.getName());
		}
		jList1 = new JList();
		jList1.setListData(parserList);
		jList1.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				parserSelected();
			}
		});
		jList1.setBounds(16, 8, 137, 153);
		
		cp.add(jList1);
		btnSave.setBounds(16, 176, 59, 25);
		btnSave.setText("Save");
		cp.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnSaveActionPerformed(evt);
			}
		});

		btnCancel.setBounds(88, 176, 67, 25);
		btnCancel.setText("Cancel");
		cp.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		// Ende Komponenten

		setResizable(false);
		setVisible(true);
	}

	// Anfang Ereignisprozeduren
	public void btnSaveActionPerformed(ActionEvent evt) {
		int i;
		for (i = 0; i < jList1.getSelectedValues().length; i++) {
			selectedParser.add("" + jList1.getSelectedValues()[i]);
		}
		
		Profile profile = new Profile();
		//TODO Profile profile = manager.getProfile();
		
		for (String sp : selectedParser) {
			profile.addToActive(profile.getParserByName(sp));
		}
		
		// sendParserToDatabase();
		this.dispose();
	}

	public void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}
	
	private void parserSelected() {
		Profile profile = new Profile();
		//TODO Profile profile = manager.getProfile();
		Parser p = profile.getParserByName((String)jList1.getSelectedValue());
		System.out.println(p);
	}

	// Ende Ereignisprozeduren

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new GParser("parser", new Manager());
	}
}
