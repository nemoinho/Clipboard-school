package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import core.Clipboard;
import core.Entry;

public class Manager implements Observer {
	private Clipboard clipboard = null;
	private ArrayList<Entry> entries = new ArrayList<Entry>();
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

	@Override
	public void update(Observable obs, Object o){
		if(o instanceof String){
			entries.add(new Entry((String)o));
		} 
	}

	public static void main(String[] args){
		Manager manager = new Manager();
	}
}
