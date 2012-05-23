package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import core.plugin.Loader;

public class Profile {
	
	private String name;
	private ArrayList<Parser> parserList = new ArrayList<Parser>();
	private ArrayList<Parser> activeParserList = new ArrayList<Parser>();
	
	public Profile(){
		try {
			initPlugins();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * loads Parser-Objects
	 * @param loader
	 */
	private void initParser(Loader loader) {
		ArrayList<String> plugins = loader.getPluginList();
		try {
//			for(Plugin p : loader.getPlugins()){
//				if(p instanceof Parser){
//					this.addParser(p, p.getClass().getName());
//				}
//			}
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
	/**
	 * Loads Plugins, currently only Parser in use.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IOException
	 * @throws Exception
	 */
	public void initPlugins() throws IllegalAccessException, InstantiationException, IOException, Exception {
		Loader l = new Loader();
		l.load();
		initParser(l);
	}
	/**
	 * add a parser to the profile
	 * @param p
	 * @param name
	 */
	public void addParser(Parser p, String name) {
		p.setName(name);
		this.parserList.add(p);
	}
	/**
	 * get all available Parser-Objects
	 * @return
	 */
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
	/**
	 * set the name of a Parser
	 * @param p
	 * @param name
	 */
	public void setParserName(Parser p, String name) {
		p.setName(name);
	}
	/**
	 * get a Key-Set of parameters of a Parser
	 * @param p
	 * @return
	 */
	public Set<String> getParserParams(Parser p) {
		return p.getParamNames();
	}
	/**
	 * Set Parser parameter by key, value
	 * @param p
	 * @param param
	 * @param value
	 */
	public void setParserParam(Parser p, String param, Object value) {
		p.setParam(param, value);
	}
	/**
	 * deactivate a Parser
	 * @param p
	 */
	public void removeFromActive(Parser p) {
		this.activeParserList.remove(p);
	}
	/**
	 * activate a Parser
	 * @param p
	 */
	public void addToActive(Parser p) {
		this.activeParserList.add(p);
	}
	/**
	 * returns currently active Parser Objects
	 * @return
	 */
	public ArrayList<Parser> getActiveParser() {
		return this.activeParserList;
	}
	
	/**
	 * returns profile name
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets profile name
	 * @param name
	 */
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
