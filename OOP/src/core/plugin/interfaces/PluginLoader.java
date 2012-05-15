package core.plugin.bytehawks.interfaces;

/**
 *
 * Interface for the  PluginLoader
 *
 * @version 1.0 vom 11.05.2012
 * @author Tobias Schult
 */
import java.util.List;

public interface PluginLoader {
	/**
	 * this method is needed to invoke the method of the selected plugin
	 * 
	 * @throws Exception
	 * @param pluginName
	 *            the name of the target plugin
	 * @param pluginMethod
	 *            the method that shoud be invoked
	 * @return returns the Object of the plugin
	 */
	public Object invoke(String pluginName, String pluginMethod, Object... args)
			throws Exception;

	/**
	 * the load method is the constructor of the plugin
	 */
	public void load();

	/**
	 * the shutdown method is a finalze method
	 */
	public void shutdown();

	/**
	 * shoud return a ptringlist of the plugins
	 */
	public List<String> getPluginList();
}
