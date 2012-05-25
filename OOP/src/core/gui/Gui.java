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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import core.Constants;
import core.Manager;
import core.Parser;

/**
 * @author nemoinho
 * @version 1.1
 */
public class Gui implements Runnable, ActionListener, Observer, ListSelectionListener{
	public static final String WINDOW_TITLE = "Clipboard";
	public static final String FILE_MENU_TITLE = "Datei";
	public static final String FILE_MENU_ITEM_OPEN = "Öffnen";
	public static final String FILE_MENU_ITEM_SAVE = "Speichern";
	public static final String FILE_MENU_ITEM_SAVE_AS = "Speichern unter...";
	public static final String HELP_MENU_TITLE = "Hilfe";
	public static final String HELP_MENU_ITEM_ABOUT = "Info";
	public static final String HELP_MENU_ITEM_HELP = "Hilfehandbuch";
	public static final String ENTRIES_LABEL_TEXT = "Clipboard History";
	public static final String DELETE_ENTRY_BUTTON_TEXT = "Del";
	public static final String CREATE_PROFILE_BUTTON_TEXT = "+";
	public static final String DELETE_PROFILE_BUTTON_TEXT = "-";
	public static final String PARSER_CONFIG_BUTTON_TEXT = "Parser";
	public static final String PARSED_LABEL_TEXT = "geparster Text";
	public static final int MAIN_WINDOW_INSET = 10;
	public static final int STANDARD_COMPONENT_INSET = 2;
	public static final int MAIN_LIST_WIDTH = 285;
	public static final int MAIN_LIST_HEIGHT = 225;
	
	private JFrame window = null;
	private JMenuBar mainMenu = null;
	private JMenu fileMenuJMenu = null;
	private JMenuItem fileMenuOpenJMenuItem = null;
	private JMenuItem fileMenuSaveJMenuItem = null;
	private JMenuItem fileMenuSaveAsJMenuItem = null;
	private JMenu helpMenuJMenu = null;
	private JMenuItem helpMenuAboutJMenuItem = null;
	private JMenuItem helpMenuHelpJMenuItem = null;
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
	private JFileChooser fileChooser = null;
	private GridBagLayout layout = null;
	private GridBagConstraints grid = null;
	private File fileToProfileSetup = null;
	
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
		}else if(evt.getSource() == fileMenuSaveJMenuItem){
			saveProfileConfiguration(fileToProfileSetup);
		}else if(evt.getSource() == fileMenuSaveAsJMenuItem){
			saveProfileConfiguration();
		}else if(evt.getSource() == fileMenuOpenJMenuItem){
			loadProfileConfiguration();
		}else if(evt.getSource() == helpMenuAboutJMenuItem){
			new GAbout(window);
		}else if(evt.getSource() instanceof JMenuItem){
			System.out.println(((JMenuItem)evt.getSource()).getText());
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		int idx = entriesJList.getSelectedIndex();
		if(idx >= 0){
			parsedJTextField.setText(manager.getEntries().get(idx).getModified());
		}
	}
	
	public void saveProfileConfiguration(File file) {
		if(file == null){
			saveProfileConfiguration();
		} else {
			try {
				Document doc = null;
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db;
				db = dbf.newDocumentBuilder();

				doc = db.newDocument();
				Element root = doc.createElement("ClipboardManager");
				for(String p : manager.getProfileSet()){
					Element profile = doc.createElement("profile");
					profile.setAttribute("name", p);
					for(Parser a : manager.getProfiles().get(p).getActiveParser()){
						Element activParser = doc.createElement("activeParser");
						activParser.setAttribute("name",a.getName());
						try {
							for(String l : a.getParamNames()){
								Element attr = doc.createElement("attribute");
								attr.setAttribute("name", l);
								Text attrVal = doc.createTextNode(a.getParams().get(l).toString());
								attr.appendChild(attrVal);
								activParser.appendChild(attr);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						profile.appendChild(activParser);
					}
					root.appendChild(profile);
				}
				doc.appendChild(root);
				doc.setXmlVersion("1.0");
				doc.setTextContent("text/xml");

				try {
					DOMSource domSource = new DOMSource(doc);
					StringWriter writer = new StringWriter();
					StreamResult result = new StreamResult(writer);
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer transformer = tf.newTransformer();
					transformer.transform(domSource, result);
					try {
						FileWriter fw = new FileWriter(file);
						fw.write(writer.toString());
						fw.flush();
						fw.close();
			            fileToProfileSetup = file;
			            fileMenuSaveJMenuItem.setEnabled(true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (TransformerException ex) {
					ex.printStackTrace();
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		StringBuffer fileContent = new StringBuffer();
	}
	
	
	public void saveProfileConfiguration() {
	    //Handle open button action.
        int returnVal = fileChooser.showSaveDialog(window);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            saveProfileConfiguration(file);
        }
	}
	
	public void loadProfileConfiguration(){

	    //Handle open button action.
        int returnVal = fileChooser.showOpenDialog(window);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
        } else {
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
		window.setJMenuBar(buildMainMenu());
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
		fileChooser = new JFileChooser();
//		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new ClipboardFileFilter());
	}
	
	private JMenuBar buildMainMenu() {
		mainMenu = new JMenuBar();

		fileMenuJMenu = new JMenu(FILE_MENU_TITLE); // TODO
		mainMenu.add(fileMenuJMenu);
//		fileMenuOpenJMenuItem = new JMenuItem(FILE_MENU_ITEM_OPEN);
//		fileMenuOpenJMenuItem.addActionListener(this);
//		fileMenuJMenu.add(fileMenuOpenJMenuItem);
		fileMenuSaveJMenuItem = new JMenuItem(FILE_MENU_ITEM_SAVE);
		fileMenuSaveJMenuItem.setEnabled(false);
		fileMenuSaveJMenuItem.addActionListener(this);
		fileMenuJMenu.add(fileMenuSaveJMenuItem);
		fileMenuSaveAsJMenuItem = new JMenuItem(FILE_MENU_ITEM_SAVE_AS);
		fileMenuSaveAsJMenuItem.addActionListener(this);
		fileMenuJMenu.add(fileMenuSaveAsJMenuItem);

		helpMenuJMenu = new JMenu(HELP_MENU_TITLE); // TODO
		mainMenu.add(helpMenuJMenu);
//		helpMenuHelpJMenuItem = new JMenuItem(HELP_MENU_ITEM_HELP);
//		helpMenuJMenu.add(helpMenuHelpJMenuItem);
		helpMenuAboutJMenuItem = new JMenuItem(HELP_MENU_ITEM_ABOUT);
		helpMenuAboutJMenuItem.addActionListener(this);
		helpMenuJMenu.add(helpMenuAboutJMenuItem);
		return mainMenu;
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
		Manager man  = new Manager();
		Gui test = new Gui(man);
		SwingUtilities.invokeLater(test);
	}

}
