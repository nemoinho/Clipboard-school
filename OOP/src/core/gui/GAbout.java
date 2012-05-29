package core.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.net.URI;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GAbout {
	
	private JDialog aboutDialog = null;
	private JLabel aboutHeadline = null;
	private JLabel aboutText = null;
	private JLabel aboutText2 = null;
	private JLabel aboutAuthors = null;
	private JLabel aboutAuthors2 = null;
	private JLabel aboutLink = null;
	private JButton aboutExit = null;
	private GridBagLayout layout = null;
	private GridBagConstraints grid = null;

	public GAbout(JFrame owner) {
		buildAboutDialog(owner);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - aboutDialog.getSize().width) / 2;
		int y = (d.height - aboutDialog.getSize().height) / 2;
		aboutDialog.setLocation(x, y);
		aboutDialog.setVisible(true);
		aboutDialog.setResizable(false);
	}
	
	public void buildAboutDialog(JFrame owner) {
		aboutDialog = new JDialog(owner);
		layout = new GridBagLayout();
		grid = new GridBagConstraints();
		aboutDialog.setLayout(layout);
		grid.fill = GridBagConstraints.BOTH;
		
		aboutHeadline = new JLabel("Clipboard Manager 1.0");
		Font headingFont = aboutHeadline.getFont();
		aboutHeadline.setFont(new Font(headingFont.getName(), headingFont.getStyle(), headingFont.getSize()*2));
		aboutHeadline.setHorizontalAlignment(JLabel.CENTER);
		addComponent(0, 0, 1, 1, aboutHeadline, new Insets(Gui.MAIN_WINDOW_INSET,
				Gui.MAIN_WINDOW_INSET, Gui.MAIN_WINDOW_INSET*2, Gui.MAIN_WINDOW_INSET));
		
		aboutText = new JLabel("Diese Software wurde in Java geschrieben und ist auf");
		aboutText.setHorizontalAlignment(JLabel.CENTER);
		addComponent(0, 1, 1, 1, aboutText, new Insets(Gui.STANDARD_COMPONENT_INSET,
				Gui.MAIN_WINDOW_INSET, Gui.STANDARD_COMPONENT_INSET, Gui.MAIN_WINDOW_INSET));
		
		aboutText2 = new JLabel("vielen Betriebssystemen ohne Installation lauffähig.");
		aboutText2.setHorizontalAlignment(JLabel.CENTER);
		addComponent(0, 2, 1, 1, aboutText2, new Insets(Gui.STANDARD_COMPONENT_INSET,
				Gui.MAIN_WINDOW_INSET, Gui.MAIN_WINDOW_INSET*2, Gui.MAIN_WINDOW_INSET));
		
		aboutAuthors = new JLabel("© Felix Nehrke, Simon Sperling");
		aboutAuthors.setHorizontalAlignment(JLabel.CENTER);
		addComponent(0, 3, 1, 1, aboutAuthors, new Insets(Gui.STANDARD_COMPONENT_INSET,
				Gui.MAIN_WINDOW_INSET, Gui.STANDARD_COMPONENT_INSET, Gui.MAIN_WINDOW_INSET));
		
		aboutAuthors2 = new JLabel("Tobias Schult, Wojciech Rydzewski");
		aboutAuthors2.setHorizontalAlignment(JLabel.CENTER);
		addComponent(0, 4, 1, 1, aboutAuthors2, new Insets(Gui.STANDARD_COMPONENT_INSET,
				Gui.MAIN_WINDOW_INSET, Gui.MAIN_WINDOW_INSET*2, Gui.MAIN_WINDOW_INSET));
		
		aboutLink = new JLabel("https://github.com/nemoinho/Clipboard-school");
		aboutLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		aboutLink.setHorizontalAlignment(JLabel.CENTER);
		aboutLink.setForeground(Color.blue);
		HashMap<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute,Integer>();
		fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		aboutLink.setFont(aboutAuthors2.getFont().deriveFont(fontAttributes));
		aboutLink.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
                    Desktop.getDesktop().browse(new URI(((JLabel)arg0.getSource()).getText()));
                } catch (Exception e) {
                	// ignore
                }	
			}
		});
		addComponent(0, 5, 1, 1, aboutLink, new Insets(Gui.STANDARD_COMPONENT_INSET,
				Gui.MAIN_WINDOW_INSET, Gui.MAIN_WINDOW_INSET*2, Gui.MAIN_WINDOW_INSET));
		
		aboutExit = new JButton("Schließen");
		aboutExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutDialog.dispose();
			}
		});
		addComponent(0, 6, 1, 1, aboutExit, new Insets(Gui.STANDARD_COMPONENT_INSET,
				Gui.MAIN_WINDOW_INSET, Gui.MAIN_WINDOW_INSET, Gui.MAIN_WINDOW_INSET));
		aboutDialog.pack();
	}

	/**
	 * This method add a component to the layout and the window
	 * 
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param jComponent
	 * @param insets
	 */
	private void addComponent(int gridx, int gridy, int gridwidth,
			int gridheight, JComponent jComponent, Insets insets) {
		addComponent(gridx, gridy, gridwidth, gridheight, jComponent, insets, 0);
	}

	/**
	 * This method add a component to the layout and the window
	 * 
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param jComponent
	 * @param insets
	 * @param minWidth
	 */
	private void addComponent(int gridx, int gridy, int gridwidth,
			int gridheight, JComponent jComponent, Insets insets, int minWidth) {
		// In dieser Methode werden die Abstände zueinander, die Position und
		// der Spaltenberecih definiert. Anschließend wird das Element an das
		// Layout gebunden und in das Fenster gehängt.
		grid.ipadx = minWidth;
		grid.insets = insets;
		grid.gridx = gridx;
		grid.gridy = gridy;
		grid.gridwidth = gridwidth;
		grid.gridheight = gridheight;
		layout.setConstraints(jComponent, this.grid);
		aboutDialog.add(jComponent);
	}
}
