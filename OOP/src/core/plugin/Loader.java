package core.plugin;

/**
 *
 * implementation of the plugin
 *
 * @version 1.0 vom 11.05.2012
 * @author Tobias Schult
 */
import java.util.Vector;
import java.util.Enumeration;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FilenameFilter;
import java.io.BufferedReader;
import java.net.URLClassLoader;
import java.net.URL;
import java.util.jar.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

import core.plugin.interfaces.Plugin;
import core.plugin.interfaces.PluginLoader;


public class Loader implements PluginLoader {
	private Vector<Plugin> plugins = null;
	private PluginFilenameFilter pluginFilenameFilter = null;

	/**
	 * SETTINGS
	 */
	private final String PLUGIN_FOLDER = "plugins";
	private final String PLUGIN_FILE = "Plugin.rsf";

	/**
	 * constructor of the loader
	 */
	public Loader() {
		pluginFilenameFilter = new PluginFilenameFilter();
		plugins = new Vector<Plugin>();
	}

	public void load() throws IOException, IllegalAccessException,
			InstantiationException, Exception {
		File pluginDir = new File(PLUGIN_FOLDER);
		File[] pluginJars = pluginDir.listFiles(pluginFilenameFilter);
		for (File pluginJar : pluginJars) {
			String name = "FAIL";
			JarFile jarFile = new JarFile(pluginJar);
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries
					.hasMoreElements();) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(PLUGIN_FILE))
					name = (new BufferedReader(new InputStreamReader(
							jarFile.getInputStream(entry)))).readLine();
			}
			jarFile.close();
			if (name.equals("FAIL"))
				continue;
			URLClassLoader urlCL = new URLClassLoader(new URL[] { pluginJar
					.toURI().toURL() });
			Class<?> pluginClass = urlCL.loadClass(name);
			Plugin plugin = (Plugin) pluginClass.newInstance();
			plugin.loadPlugin(this);
			plugins.add(plugin);
		}
		for (Plugin plugin : plugins) {
			plugin.startPlugin();
		}
	}

	public Object invoke(String pluginName, String methodName, Object... args)
			throws Exception {
		for (Plugin plugin : plugins) {
			Class<?> pluginClass = plugin.getClass();
			if (pluginClass.getName().equals(pluginName)) {
				for (Method method : pluginClass.getDeclaredMethods()) {
					if (method.getName().equals(methodName))
						return method.invoke(plugin, args);
				}
			}
		}
		return null;
	}

	public void shutdown() /*throws ShutdownException*/ {
		for (Plugin plugin : plugins) {
			try {
				plugin.stopPlugin();
			} catch (Exception e) {
				/*throw new ShutdownException();*/
			}
		}
	}

	public ArrayList<String> getPluginList() {
		ArrayList<String> pluginList = new ArrayList<String>();
		for (Plugin plugin : plugins) {
			Class<?> pluginClass = plugin.getClass();
			pluginList.add(pluginClass.getName());
		}
		return pluginList;
	}

	public Vector<Plugin> getPlugins(){
		return plugins;
	}

	/**
	 * the reload method for the pluginloader
	 */
	public void reload() {
		try{
			this.shutdown();
			this.load();
		}/* catch (ShutdownException e) {
			e.printStackTrace();
		}*/ catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * a internal class for the filenamefilter
	 */
	static final class PluginFilenameFilter implements FilenameFilter {
		public boolean accept(File path, String name) {
			if ((name.toLowerCase().endsWith(".jar"))
					&& (!name.equals("Launcher.jar")))
				return true;
			else
				return false;
		}
	}
}
