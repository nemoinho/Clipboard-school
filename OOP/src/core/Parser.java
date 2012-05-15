package core;

import java.util.Set;

public interface Parser {
	/**
	 * Changes given input
	 * @param input
	 * @return
	 */
	public String parse(String input);
	public Set<String> getParamNames();
	public void setParam(String key, String value);
	public Parser getParser();
}
