package core.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import core.Constants;
import core.Manager;

/**
 * 
 * Beschreibung
 * 
 * @version 1.0 vom 22.03.2012
 * @author
 */

public class Gui extends JFrame implements Observer {
	// Anfang Attribute
	private Manager manager;
	
	private GProfile dialog;
	private JList entries;
	private JCheckBox actParser = new JCheckBox();
	private JButton Del = new JButton();
	private JButton jButton1 = new JButton();
	private JTextField parsed = new JTextField();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private String[] jcProfileData = { "Profile 1" };
	private JComboBox jcProfile = new JComboBox(jcProfileData);
	private JButton jbPlus = new JButton();
	private JButton jbMinus = new JButton();

	// Ende Attribute

	public Gui(String title) {
		// Frame-Initialisierung
		super(title);
		this.manager = new Manager();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		int frameWidth = 239;
		int frameHeight = 508;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		Container cp = getContentPane();
		cp.setLayout(null);
		// Anfang Komponenten
		entries = new JList(); // TODO: manager.getEntries();
		entries.setBounds(16, 32, 185, 225);
		cp.add(entries);
		
		actParser.setBounds(16, 368, 17, 25);
		actParser.setText("");
		cp.add(actParser);
		Del.setBounds(168, 264, 45, 25);
		Del.setText("Del");
		Del.setMargin(new Insets(2, 2, 2, 2));
		Del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Del_ActionPerformed(evt);
			}
		});
		cp.add(Del);
		jButton1.setBounds(40, 368, 99, 25);
		jButton1.setText("Parser");
		jButton1.setMargin(new Insets(2, 2, 2, 2));
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				new GParser("Parser", manager);
			}
		});
		cp.add(jButton1);
		parsed.setBounds(16, 432, 185, 24);
		parsed.setText("Text");
		cp.add(parsed);
		jLabel1.setBounds(16, 8, 114, 16);
		jLabel1.setText("Clipboard History");
		jLabel1.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
		cp.add(jLabel1);
		jLabel2.setBounds(16, 408, 76, 16);
		jLabel2.setText("parsed text");
		jLabel2.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
		cp.add(jLabel2);
		jcProfile.setBounds(16, 328, 121, 24);
		jcProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = jcProfile.getSelectedIndex();
				//TODO manager.setProfile(index);
			}
		});
		cp.add(jcProfile);
		jbPlus.setBounds(144, 328, 27, 25);
		jbPlus.setText("+");
		jbPlus.setMargin(new Insets(2, 2, 2, 2));
		jbPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jbPlus_ActionPerformed(evt);
			}
		});
		cp.add(jbPlus);
		jbMinus.setBounds(176, 328, 27, 25);
		jbMinus.setText("-");
		jbMinus.setMargin(new Insets(2, 2, 2, 2));
		jbMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jbMinus_ActionPerformed(evt);
			}
		});
		cp.add(jbMinus);
		
		// Ende Komponenten

		setResizable(false);
		setVisible(true);
	}

	// Fügt ein neues Profil hinzu
	public void addNewProfile(String n) {
		int index = jcProfile.getItemCount();
		jcProfile.insertItemAt(n, index);
		//TODO manager.addProfile(n);
		//TODO manager.setProfile(index);
		jcProfile.setSelectedIndex(index);
	}

	// Anfang Methoden
	public void Del_ActionPerformed(ActionEvent evt) {
		
	}

	public void jbMinus_ActionPerformed(ActionEvent evt) {
		if(jcProfile.getItemCount() >= 2) {
			int index = jcProfile.getSelectedIndex();
			jcProfile.removeItemAt(index);
			//TODO manager.removeProfile(index);
		}
	}

	public void jbPlus_ActionPerformed(ActionEvent evt) {
		dialog = new GProfile(this, "New parser", true);
		addNewProfile(dialog.getInput());
	}

	// Ende Methoden

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Gui("Clipboard");
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// arg0 : egal
		// arg1 : (String) case der aufgetreten ist.
		String _case = (String) arg1;
		
		if(_case == Constants.OBSERVE_ENTRY) {
			this.entries.removeAll();
			//TODO this.entries.setListData(manager.getEntries());
		}
	}
}
