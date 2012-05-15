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

	public Manager(){
		initClipboard();
		addManagerToObservers();
	}

	public void initClipboard(){
		clipboard = new Clipboard();
		Thread thread = new Thread(clipboard);
		thread.start();
	}

	public void addManagerToObservers(){
		clipboard.addObserver(this);
	}

	public void update(Observable obs, Object o){
		if(o instanceof String){
			entries.add(new Entry((String)o));
		} 
	}

	public static void main(String[] args){
		Manager manager = new Manager();
	}
}
