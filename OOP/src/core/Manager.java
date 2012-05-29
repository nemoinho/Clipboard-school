package core;

import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import core.Clipboard;
import core.Entry;
import core.Constants;
import core.gui.Gui;

public class Manager extends Observable implements Observer {
	private Clipboard clipboard = null;
	private Vector<Entry> entries = new Vector<Entry>();
	private HashMap<String, Profile> profiles = new HashMap<String, Profile>();
	private String activeProfile = null;
	private boolean allowCopyFiles = true;
	private boolean allowCopyImages = true;
	private ArrayList<DataFlavor> fileFlavors = new ArrayList<DataFlavor>();

	/**
	 * Constructor build a Managementprogramm
	 */
	public Manager(){
		profiles.put("Default", new Profile());
		setProfile("Default");
		initClipboard();
		setFileFlavors();
		setAllowCopyFiles(true);
		setAllowCopyImages(true);
		addManagerToObservers();
	}

	/**
	 * initlialize the Clipboard-wrapper and starts it in another Thread
	 */
	public void initClipboard(){
		clipboard = new Clipboard();
		Thread thread = new Thread(clipboard);
		thread.start();
	}

	/**
	 * adds the Manager to the List of Observers of the Clipboard
	 */
	public void addManagerToObservers(){
		clipboard.addObserver(this);
	}

	/**
	 * gets the list of entries for the gui, or other observers
	 */
	public Vector<Entry> getEntries(){
		return entries;
	}
	
	public boolean removeEntry(int index) {
		if(entries.size() > index){
			entries.remove(index);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Profile> getProfiles() {
		return profiles;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getProfileSet() {
		return profiles.keySet();
	}
	
	/**
	 * 
	 * @param profileName
	 * @return
	 */
	public boolean addProfile(String profileName){
		
		if(!profiles.containsKey(profileName)){
			Profile p = new Profile();
			p.setName(profileName);
			profiles.put(profileName, p);
			return true;
		}
		return false;
	}

	public void removeProfile(String profileName){
		if(profiles.get(profileName) != null){
			profiles.remove(profileName);
		}
	}

	public void setProfile(String profileName){
		activeProfile = profileName;
	}
	
	public Profile getProfile() {
		return this.profiles.get(activeProfile);
	}
	
	public void setAllowCopyFiles(boolean val) {
		allowCopyFiles = val;
		setFlavors();
	}
	
	public void setAllowCopyImages(boolean val) {
		allowCopyImages = val;
		setFlavors();
	}
	
	public void setFlavors(){
		if(allowCopyFiles){ // Files können kopiert werden
			clipboard.setDefinitlyExcludedDataFlavors(fileFlavors);
		}else{ // Dateipfade werden im Manager behandelt!
			clipboard.setDefinitlyExcludedDataFlavors(new ArrayList<DataFlavor>());
		}
		if(allowCopyImages){ // Bilder kopieren (true) oder Pfad archivieren (false)
			clipboard.addDefinitlyExcludedDataFlavors(DataFlavor.imageFlavor);
		}
	}
	
	public void setFileFlavors(){
		try {
			fileFlavors.add(DataFlavor.javaFileListFlavor);
			fileFlavors.add(new DataFlavor( "application/x-java-file-list" ));
			fileFlavors.add(new DataFlavor( "x-special/gnome-copied-files" ));
			fileFlavors.add(new DataFlavor("text/uri-list"));
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public void update(Observable obs, Object o){
		if(o instanceof String && (entries.size() == 0 || !entries.lastElement().getOriginal().equals((String)o))){
			Entry ent = new Entry((String)o);
			String entry = (String)o;
			if(activeProfile != null){
				ArrayList<Parser> par = profiles.get(activeProfile).getActiveParser();
				for(Parser p : par){
					entry = p.parse(entry);
				}
				ent.setModified(entry);
			}
			entries.add(ent);
			if(entries.size() >= 1){
				setChanged();
				notifyObservers(Constants.OBSERVE_ENTRY);
			}
			clipboard.setEntry(entry);
		}
	}

	public static void main(String[] args){
		Manager man  = new Manager();
		SessionManagement session = new SessionManagement(man);
		Gui test = new Gui(man);
		test.setSessionManager(session);
		SwingUtilities.invokeLater(test);
	}
}
