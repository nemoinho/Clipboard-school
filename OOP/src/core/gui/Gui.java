/**
 * 
 */
package core.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.Constants;
import core.Manager;

/**
 * @author nemoinho
 * @version 1.1
 */
public class Gui implements Runnable, ActionListener, Observer, ListSelectionListener{
	private static final String WINDOW_TITLE = "Clipboard";
	private static final String ENTRIES_LABEL_TEXT = "Clipboard History";
	private static final String DELETE_ENTRY_BUTTON_TEXT = "Del";
	private static final String CREATE_PROFILE_BUTTON_TEXT = "+";
	private static final String DELETE_PROFILE_BUTTON_TEXT = "-";
	private static final String PARSER_CONFIG_BUTTON_TEXT = "Parser";
	private static final String PARSED_LABEL_TEXT = "geparster Text";
	private static final int MAIN_WINDOW_INSET = 10;
	private static final int STANDARD_COMPONENT_INSET = 2;
	private static final int MAIN_LIST_WIDTH = 285;
	private static final int MAIN_LIST_HEIGHT = 225;
	
	private JFrame window = null;
	private JLabel entriesJLabel = null;
	private JList entriesJList = null;
	private JScrollPane entriesJScrollPane = null;
	private JButton deleteEntryJButton = null;
	private JComboBox profileJComboBox = null;
	private JButton createProfileJButton = null;
	private JButton deleteProfileJButton = null;
	private JCheckBox actParserJCheckBox = null;
	private JButton parserConfigJButton = null;
	private JLabel parsedJLabel = null;
	private JTextField parsedJTextField = null;
	private GridBagLayout layout = null;
	private GridBagConstraints grid = null;
	
	private Manager manager = null;
	private GProfile dialog = null;

	/**
	 * Will initialize the Gui
	 */
	public Gui() {
		this(null);
	}
	
	/**
	 * Will initialize the Gui
	 * @param manager
	 */
	public Gui(Manager manager) {
		this.manager = manager;
		if(manager != null){
			manager.addObserver(this);
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		buildWindow(WINDOW_TITLE);
		deleteEntryJButton.addActionListener(this);
		createProfileJButton.addActionListener(this);
		deleteProfileJButton.addActionListener(this);
		parserConfigJButton.addActionListener(this);
		actParserJCheckBox.addActionListener(this);
		profileJComboBox.addActionListener(this);
		entriesJList.addListSelectionListener(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// arg0 : egal
		// arg1 : (String) case der aufgetreten ist.
		String _case = (String) arg1;
		
		if(_case == Constants.OBSERVE_ENTRY && entriesJList != null) {
			DefaultListModel listModel = (DefaultListModel)entriesJList.getModel();
			listModel.addElement(manager.getEntries().lastElement().getOriginal());
			parsedJTextField.setText(manager.getEntries().lastElement().getModified());
			deleteEntryJButton.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == deleteEntryJButton){
			deleteEntry();
		}else if(evt.getSource() == createProfileJButton){
			createProfile();
		}else if(evt.getSource() == deleteProfileJButton){
			deleteProfile();
		}else if(evt.getSource() == parserConfigJButton){
			configureParser();
		}else if(evt.getSource() == actParserJCheckBox){
			manager.setProfile(actParserJCheckBox.isSelected() ? profileJComboBox.getSelectedItem().toString() : null);
		}else if(evt.getSource() == profileJComboBox){
			setCurrentProfile();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		int idx = entriesJList.getSelectedIndex();
		if(idx >= 0){
			parsedJTextField.setText(manager.getEntries().get(idx).getModified());
		}
	}
	
	/**
	 * deletes an clipboard entry from history
	 */
	private void deleteEntry(){
		DefaultListModel listModel = (DefaultListModel)entriesJList.getModel();
		int[] idx = entriesJList.getSelectedIndices();
		for(int i = idx.length; i-->0 ;){
			if(manager.removeEntry(idx[i])){
				listModel.remove(idx[i]);
			}
		}
		if(listModel.size() <= 0){
			deleteEntryJButton.setEnabled(false);
			parsedJTextField.setText("");
		}else{
			entriesJList.setSelectedIndex(entriesJList.getLastVisibleIndex());
		}
	}
	
	/**
	 * creates a new profile 
	 */
	private void createProfile(){
		dialog = new GProfile(window, "New parser", true);
		String name = dialog.getInput();
		if(manager.addProfile(name)){
			int index = profileJComboBox.getItemCount();
			profileJComboBox.insertItemAt(dialog.getInput(), index);
			//TODO manager.setProfile(index);
			profileJComboBox.setSelectedIndex(index);
		}
	}
	
	/**
	 * deletes an existing profile
	 */
	private void deleteProfile(){
		if(profileJComboBox.getItemCount() >= 2) {
			String name = profileJComboBox.getSelectedItem().toString();
			int index = profileJComboBox.getSelectedIndex();
			profileJComboBox.removeItemAt(index);
			manager.removeProfile(name);
		}
	}
	
	/**
	 * Will update the profile, which process the entries
	 */
	private void setCurrentProfile(){
		manager.setProfile(profileJComboBox.getSelectedItem().toString());
	}
	
	/**
	 * confiure the parser-settings for the current profile
	 */
	private void configureParser(){
		new GParser("Parser", manager);
	}
	
	// Ab hier Fenster bauen

	/**
	 * build the window with all its components
	 * @param windowTitle
	 */
	private void buildWindow(String windowTitle) {
		initializeComponents(windowTitle);
		addAllComponents();
		Set<String> prof = manager.getProfileSet();
		int i = 0;
		for(String name : prof){
			profileJComboBox.insertItemAt(name, i++);
		}
		actParserJCheckBox.setSelected(true);
		profileJComboBox.setSelectedIndex(0);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - window.getSize().width) / 2;
		int y = (d.height - window.getSize().height) / 2;
		window.setLocation(x, y);
		window.setVisible(true);
	}
	
	/**
	 * initialize all components for the window
	 * @param windowTitle
	 */
	private void initializeComponents(String windowTitle) {
		window = new JFrame(windowTitle);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		entriesJLabel = new JLabel(ENTRIES_LABEL_TEXT);
	    DefaultListModel model = new DefaultListModel();
		entriesJList = new JList(model);
		entriesJScrollPane = new JScrollPane(entriesJList);
		entriesJList.setVisible(true);
		deleteEntryJButton = new JButton(DELETE_ENTRY_BUTTON_TEXT);
		deleteEntryJButton.setEnabled(false);
		profileJComboBox = new JComboBox();
		createProfileJButton = new JButton(CREATE_PROFILE_BUTTON_TEXT);
		deleteProfileJButton = new JButton(DELETE_PROFILE_BUTTON_TEXT);
		actParserJCheckBox = new JCheckBox();
		parserConfigJButton = new JButton(PARSER_CONFIG_BUTTON_TEXT);
		parsedJLabel = new JLabel(PARSED_LABEL_TEXT);
		parsedJTextField = new JTextField();
		parsedJTextField.setEditable(false);
	}
	
	/**
	 * adds all components the the layout and the window
	 */
	private void addAllComponents() {
		// layout initialisieren
		layout = new GridBagLayout();
		grid = new GridBagConstraints();
		window.setLayout(layout);
		grid.fill = GridBagConstraints.BOTH;
		
		int deleteButtonSpacer = 20;
		int configSpacer = 15;
		
		// Alle komponenten einfügen
		addComponent(0, 0, 4, 1, entriesJLabel, new Insets(MAIN_WINDOW_INSET,
				MAIN_WINDOW_INSET, STANDARD_COMPONENT_INSET, MAIN_WINDOW_INSET));
		grid.weighty = 1;
		addComponent(0, 1, 4, 1, entriesJScrollPane, new Insets(STANDARD_COMPONENT_INSET,
				MAIN_WINDOW_INSET, STANDARD_COMPONENT_INSET, MAIN_WINDOW_INSET));
		entriesJScrollPane.setPreferredSize(new Dimension(MAIN_LIST_WIDTH, MAIN_LIST_HEIGHT));
		grid.weighty = .0;
		addComponent(2, 2, 2, 1, deleteEntryJButton, new Insets(
				STANDARD_COMPONENT_INSET, STANDARD_COMPONENT_INSET, deleteButtonSpacer,
				MAIN_WINDOW_INSET));
		addComponent(0, 3, 2, 1, profileJComboBox, new Insets(
				MAIN_WINDOW_INSET, MAIN_WINDOW_INSET, MAIN_WINDOW_INSET,
				STANDARD_COMPONENT_INSET));
		profileJComboBox.setPreferredSize(new Dimension(99, profileJComboBox.getHeight()));
		addComponent(2, 3, 1, 1, createProfileJButton, new Insets(
				MAIN_WINDOW_INSET, STANDARD_COMPONENT_INSET, MAIN_WINDOW_INSET,
				STANDARD_COMPONENT_INSET), configSpacer);
		addComponent(3, 3, 1, 1, deleteProfileJButton, new Insets(
				MAIN_WINDOW_INSET, STANDARD_COMPONENT_INSET, MAIN_WINDOW_INSET,
				MAIN_WINDOW_INSET), configSpacer);
		grid.weightx = .0;
		addComponent(0, 4, 1, 1, actParserJCheckBox, new Insets(
				MAIN_WINDOW_INSET, MAIN_WINDOW_INSET, MAIN_WINDOW_INSET,
				STANDARD_COMPONENT_INSET));
		grid.weightx = 1;
		addComponent(1, 4, 1, 1, parserConfigJButton, new Insets(
				MAIN_WINDOW_INSET, STANDARD_COMPONENT_INSET, MAIN_WINDOW_INSET,
				STANDARD_COMPONENT_INSET));
		addComponent(0, 5, 4, 1, parsedJLabel, new Insets(STANDARD_COMPONENT_INSET,
				MAIN_WINDOW_INSET, STANDARD_COMPONENT_INSET, MAIN_WINDOW_INSET));
		addComponent(0, 6, 4, 1, parsedJTextField, new Insets(STANDARD_COMPONENT_INSET,
				MAIN_WINDOW_INSET, MAIN_WINDOW_INSET, MAIN_WINDOW_INSET));
		
		// Alles anzeigen
		window.pack();
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
		window.add(jComponent);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Manager man  = new Manager();
		Gui test = new Gui(man);
		SwingUtilities.invokeLater(test);
	}

}
