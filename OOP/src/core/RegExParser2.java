package core;

import java.util.HashMap;
import java.util.Set;

import core.plugin.interfaces.Plugin;
import core.plugin.interfaces.PluginLoader;

public class RegExParser2 implements Parser, Plugin {

	private HashMap<String, Object> params;
	private String name;
	
	public RegExParser2() {
		params = new HashMap<String, Object>();
		params.put("match", "");
		params.put("replace", "");
		name = "RegEx Parser";
	}

	/**
	 * parse input using the match & replace param
	 */
	@Override
	public String parse(String input) {
		try {
			return input.replaceAll((String)params.get("match"), (String)params.get("replace"));
		}
		catch(Exception e) {}
		return input;
	}

	public Set<String> getParamNames() {
		return params.keySet();
	}
	
	public HashMap<String, Object> getParams() {
		return this.params;
	}
	
	public void setParam(String key, Object value) {
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
		return new RegExParser2();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDescription() {
		return "This Parser provides basic RegEx replacement, it uses the String.replaceAll() method.";
	}

	public static void main(String[] args) {
		Parser p = new RegExParser2();
		p.setParam("match", "-");
		System.out.println(p.getName());
		System.out.println(p.parse("--------t-e-s-t---"));
	}
}
