package core;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Observable;

public class Clipboard extends Observable implements Runnable {

	private String entry = null;

	/**
	 * Empty Constructor
	 */
	public Clipboard() {
	}

	@Override
	public void run() {
		for(;;){
			String clip = getClipboard();
			if(clip != null && !clip.equals(getEntry())){
				setEntry(clip);
				setChanged();
				notifyObservers(clip);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) { // ignore 
			} catch (Exception e) {
				// etwas Spaß bei der Fehlermeldung, falls mal wirklich was passiert
				System.out.println("Erwarteter Fehler beim Warten auf die Anwendung.");
			}
		}
	}

	/**
	 * Get the current content from the clipboard
	 * @return String clipboard-content
	 */
	public static String getClipboard() {
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
			if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String text = (String)t.getTransferData(DataFlavor.stringFlavor);
				return text;
			}
		} catch (UnsupportedFlavorException e) { // ignore 
		} catch (IOException e) { // ignore 
		}
		return null;
	}

	/**
	 * Sets a new String to the clipboard
	 * @param String new content for clipboard
	 */
	public static void setClipboard(String str) {
		StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}	

	/**
	 * Returns the newest read clipboard entry
	 * @return String clipboard entry
	 */
	public String getEntry() {
		return entry;
	}

	/**
	 * Sets a completely new clipboard entry
	 * @param String clipboard entry
	 */
	public void setEntry(String entry) {
		this.entry = entry;
	}
}

