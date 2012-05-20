package core;

import java.util.ArrayList;

import core.plugin.Loader;

public class Profile {
	private ArrayList<Parser> parserList = new ArrayList<Parser>();
	
	private void initParser(Loader loader) {
		ArrayList<String> plugins = loader.getPluginList();
		System.out.println(plugins);
		try {
			for(String plugin : plugins) {
				Object test = loader.invoke(plugin, "getParser");
				System.out.println(test);
				if(test != null) {
					this.addParser((Parser)test);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		System.out.println(this.parserList);
		Parser parser = new RegExParser();
		for(String key : parser.getParamNames()) {
			System.out.println(key);
		}
		parser.setParam("match", "([0-9])");
		parser.setParam("replace", "$1$1");
		
		System.out.println(parser.parse("test0,12, 8 test"));
		*/
	}
	
	public void initPlugins() {
		Loader l = new Loader();
		try {
			l.load();
		} catch (Exception e) {
			// TODO: handle exception
		}
		ArrayList<String> plugins = l.getPluginList();
		
		initParser(l);
	}
	
	private void addParser(Parser p) {
		this.parserList.add(p);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Profile p = new Profile();
		//p.initParser();
		p.initPlugins();
	}
}
