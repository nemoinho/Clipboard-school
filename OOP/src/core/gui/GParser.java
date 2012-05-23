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
	
	private Manager manager;
	private Container cp;
	private ArrayList<GParserItem> parserItems = new ArrayList<GParserItem>();
	// Ende Variablen

	public GParser(String title, Manager manager) {
		// Frame-Initialisierung
		super(title);
		this.manager = manager;
		/*
		 * addWindowListener(new WindowAdapter() { public void
		 * windowClosing(WindowEvent evt) { this.dispose(); } });
		 */
		int frameWidth = 427;
		int frameHeight = 245;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		cp = getContentPane();
		cp.setLayout(null);
		// Anfang Komponenten
		
		
		Vector<String> parserList = new Vector<String>();
		Profile profile = manager.getProfile();
		for(Parser p : profile.getParser()) {
			parserList.add(p.getName());
		}
		jList1 = new JList();
		jList1.setListData(parserList);
		jList1.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(arg0.getValueIsAdjusting()) {
					parserSelected();
				}
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
		
		
		Profile profile = manager.getProfile();
		
		for (String sp : selectedParser) {
			Parser p = profile.getParserByName(sp);
			profile.addToActive(p);
		}
		
		// sendParserToDatabase();
		this.dispose();
	}

	public void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}
	
	private void parserSelected() {
		Profile profile = manager.getProfile();
		// TODO: Fix manager / GUI to actually CHANGE the active Parser.
		System.out.println(profile);
		Parser p = profile.getParserByName((String)jList1.getSelectedValue());
		parserConfig(p);
	}
	
	private void parserConfig(Parser p) {
		if(this.parserItems != null) {
			for(GParserItem o : this.parserItems) {
				cp.remove(o.getLabel());
				cp.remove(o.getField());
				this.parserItems.remove(o);
			}
		}
		int i = 0;
		int x = 180;
		int y = 30;
		for(String param : p.getParamNames()) {
			GParserItem item = new GParserItem();
			JLabel label = new JLabel(param);
			label.setBounds(x, 5 + y * i++, 200, y);
			item.setLabel(label);
			
			cp.add(item.getLabel());
			
			item.setParser(p);
			Object value = p.getParams().get(param);
			if(value instanceof String) {
				JTextField configField = new JTextField((String)value);
				configField.setBounds(x, 5 + y * i++, 200, 30);
				configField.addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent arg0) {}
					@Override
					public void keyReleased(KeyEvent arg0) {
						paramChanged(arg0);
					}
					@Override
					public void keyPressed(KeyEvent arg0) {}
				});
				item.setField(configField);
				cp.add(item.getField());
				
				this.parserItems.add(item);
			}
			
		}
		cp.repaint();
	}
	
	private void paramChanged(KeyEvent e) {
		for(GParserItem item : this.parserItems) {
			if(item.getField().equals(e.getSource())) {
				item.getParser().setParam(item.getLabel().getText(), item.getField().getText());
			}
		}
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
