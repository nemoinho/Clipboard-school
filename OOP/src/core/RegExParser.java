package core;

import java.util.HashMap;
import java.util.Set;

import core.plugin.interfaces.Plugin;
import core.plugin.interfaces.PluginLoader;

public class RegExParser implements Parser, Plugin {
	
	private HashMap<String, String> params;
	private String name;
	
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
	
	public HashMap<String, String> getParams() {
		return this.params;
	}
	
	public void setParam(String key, String value) {
		params.remove(key);
		params.put(key, value);
	}

	@Override
	public void startPlugin() throws Exception {}
	@Override
	public void stopPlugin() throws Exception {}
	@Override
	public void loadPlugin(PluginLoader loader) throws Exception {}
	
	public Parser getParser() {
		return this;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
