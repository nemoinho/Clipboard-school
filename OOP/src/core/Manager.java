package core;

import java.util.Vector;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import core.Clipboard;
import core.Entry;
import core.Constants;

public class Manager extends Observable implements Observer {
	private Clipboard clipboard = null;
	private Vector<Entry> entries = new Vector<Entry>();
	private HashMap<String, String> profiles = new HashMap<String, String>();

	/**
	 * Constructor build a Managementprogramm
	 */
	public Manager(){
		initClipboard();
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
	public Vector getEntries(){
		return entries;
	}

	public void addProfile(int index){
	}

	public void removeProfile(int index){
	}

	public void setProfile(int index){
	}

	@Override
	public void update(Observable obs, Object o){
		if(o instanceof String){
			entries.add(new Entry((String)o)); 
			setChanged();
			notifyObservers(Constants.OBSERVE_ENTRY);
		} 
	}

	public static void main(String[] args){
		Manager manager = new Manager();
	}
}
