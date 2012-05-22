package core.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 
 * Beschreibung
 * 
 * @version 1.0 vom 11.05.2012
 * @author
 */

public class GProfile extends JDialog {
	// Anfang Variablen
	private String name = "Profile";
	private boolean saved = false;
	private JTextField jtProfileName = new JTextField();
	private JLabel jLabel1 = new JLabel();
	private JButton btnSave = new JButton();

	// Ende Variablen

	public GProfile(JFrame owner, String title, boolean modal) {
		// Dialog-Initialisierung
		super(owner, title, modal);
		/*
		 * addWindowListener(new WindowAdapter() { public void
		 * windowClosing(WindowEvent evt) { System.exit(0); } });
		 */
		int frameWidth = 171;
		int frameHeight = 127;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		Container cp = getContentPane();
		cp.setLayout(null);
		// Anfang Komponenten

		jtProfileName.setBounds(16, 32, 121, 24);
		jtProfileName.setText("");
		cp.add(jtProfileName);
		jLabel1.setBounds(16, 8, 75, 16);
		jLabel1.setText("Profilename:");
		jLabel1.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
		cp.add(jLabel1);
		btnSave.setBounds(16, 64, 123, 25);
		btnSave.setText("Save");
		cp.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// sendProfilesToDatabase();
				GProfile.this.saved = true;
				GProfile.this.name = jtProfileName.getText();
				GProfile.this.dispose();
			}
		});

		// Ende Komponenten

		setResizable(false);
		setVisible(true);
	}

	public boolean getSaved() {
		return this.saved;
	}

	public String getInput() {
		return this.name;
	}

	// Ende Ereignisprozeduren
}
