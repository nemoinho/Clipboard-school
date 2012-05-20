package core;

import java.util.HashMap;
import java.util.Set;

public interface Parser {
	/**
	 * Changes given input
	 * @param input
	 * @return
	 */
	
	public String parse(String input);
	public HashMap<String, Object> getParams();
	public Set<String> getParamNames();
	public void setParam(String key, Object value);
	
	/**
	 * 
	 * @return Parser instance
	 */
	public Parser getParser();
	public String getName();
	public void setName(String name);
	public String getDescription();
}
