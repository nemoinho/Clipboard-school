package core;

import java.util.HashMap;
import java.util.Set;

import core.plugin.interfaces.Plugin;
import core.plugin.interfaces.PluginLoader;



public class RegExParser implements Parser, Plugin {

	private HashMap<String, String> params;
	
	public RegExParser() {
		params = new HashMap<String, String>();
		params.put("match", "");
		params.put("replace", "");
	}
	
	@Override
	public String parse(String input) {
		try {
			return input.replaceAll(params.get("match"), params.get("replace"));
		}
		catch(Exception e) {}
		return input;
	}

	public Set<String> getParamNames() {
		return params.keySet();
	}
	
	public void setParam(String key, String value) {
		params.remove(key);
		params.put(key, value);
	}
	
	public static void main(String[] args) {
		
	}

	@Override
	public void startPlugin() throws Exception {
		//
		
	}

	@Override
	public void stopPlugin() throws Exception {
		// Nothing for now..
		
	}

	@Override
	public void loadPlugin(PluginLoader loader) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public Parser getParser() {
		return this;
	}
}
