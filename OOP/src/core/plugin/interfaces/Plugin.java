/**
 *
 * Interface for the plugins
 *
 * @version 1.0 vom 11.05.2012
 * @author Tobias Schult
 */
package core.plugin.interfaces;

import core.exceptions.PluginInternalException;

public interface Plugin {
	/**
	 * this method is needed to load the Plugin
	 * 
	 * @throws PluginInternalException
	 * @param loader
	 *            the pluginloader class witch loads the plugin
	 */
	public void loadPlugin(PluginLoader loader) throws Exception;

	/**
	 * this method is needed to start the plugin
	 * 
	 * @throws PluginInternalException
	 */
	public void startPlugin() throws Exception;

	/**
	 * this method is needed to stop the plugin
	 * 
	 * @throws PluginInternalException
	 */
	public void stopPlugin() throws Exception;
}
