package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import core.plugin.Loader;

public class Profile {
	
	private String name;
	private ArrayList<Parser> parserList = new ArrayList<Parser>();
	private ArrayList<Parser> activeParserList = new ArrayList<Parser>();
	
	private void initParser(Loader loader) {
		ArrayList<String> plugins = loader.getPluginList();
		try {
			for(String plugin : plugins) {
				Object test = loader.invoke(plugin, "getParser");
				if(test != null) {
					this.addParser((Parser)test, plugin);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initPlugins() throws IllegalAccessException, InstantiationException, IOException, Exception {
		Loader l = new Loader();
		l.load();
		initParser(l);
	}
	
	public void addParser(Parser p, String name) {
		p.setName(name);
		this.parserList.add(p);
	}
	
	public ArrayList<Parser> getParser() {
		return this.parserList;
	}
	
	/**
	 * Parser-search by Name, case-sensitive
	 * @param name
	 * @return 
	 */
	public Parser getParserByName(String name) {
		for(Parser p : this.parserList) {
			String pName = p.getName();
			if(pName.contains(name)) {
				return p;
			}
		}
		return null;
	}
	
	public void setParserName(Parser p, String name) {
		p.setName(name);
	}
	
	public Set<String> getParserParams(Parser p) {
		return p.getParamNames();
	}
	
	public void setParserParam(Parser p, String param, Object value) {
		p.setParam(param, value);
	}
	
	public void removeFromActive(Parser p) {
		this.activeParserList.remove(p);
	}
	
	public void addToActive(Parser p) {
		this.activeParserList.add(p);
	}
	public ArrayList<Parser> getActiveParser() {
		return this.activeParserList;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Test-method
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Profile p = new Profile();
		
		try {
			p.initPlugins();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Parser par : p.getParser()) {
			System.out.println(par.getName());
			for(String param : par.getParamNames()) {
				System.out.println("+ " + param);
			}
		}
		
		System.out.println("--------------REGEX-TEST--------------");
		String parse_test = "TESTstring";
		Parser par = p.getParserByName("RegEx");
		par.setParam("match", "(?i)test");
		par.setParam("replace", "REPLACED");
		
		for(String param : par.getParamNames()) {
			System.out.println(param + ": " + par.getParams().get(param) + " (" + par.getParams().get(param).getClass() + ")");
		}
		System.out.println(parse_test + ": " + par.parse(parse_test));
	}
}
