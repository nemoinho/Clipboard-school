package core;

import java.util.Date;

public class Entry {
	private String original = null;
	private String modified = null;
	private Date creationTime = null;

	public Entry(String orig){
		creationTime = new Date();
		original = orig;
	}
}

