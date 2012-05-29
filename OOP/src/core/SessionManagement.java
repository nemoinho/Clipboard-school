package core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * !!!                              NOT READY                               !!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * @author nemoinho
 *
 */
public class SessionManagement {
	private Manager manager = null;
	
	public SessionManagement(Manager manager) {
		this.manager = manager;
	}
	
	public boolean saveProfileConfiguration(File file) {
		try {
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();

			doc = db.newDocument();
			Element root = doc.createElement("ClipboardManager");
			for(String p : manager.getProfileSet()){
				Element profile = doc.createElement("profile");
				profile.setAttribute("name", p);
				for(Parser a : manager.getProfiles().get(p).getActiveParser()){
					Element activParser = doc.createElement("activeParser");
					activParser.setAttribute("name",a.getName());
					try {
						for(String l : a.getParamNames()){
							Element attr = doc.createElement("attribute");
							attr.setAttribute("name", l);
							Text attrVal = doc.createTextNode(a.getParams().get(l).toString());
							attr.appendChild(attrVal);
							activParser.appendChild(attr);
						}
					} catch (Exception e) {
						// ignore
					}
					profile.appendChild(activParser);
				}
				root.appendChild(profile);
			}
			doc.appendChild(root);
			doc.setXmlVersion("1.0");
			doc.setTextContent("text/xml");

			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			FileWriter fw = new FileWriter(file);
			fw.write(writer.toString());
			fw.flush();
			fw.close();
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean loadProfileConfiguration(File file) {
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new FileReader(file));
			Document doc = db.parse(inStream);
			for(String p : manager.getProfileSet()){
//				manager.removeProfile(p);
			}
			NodeList profileList = doc.getElementsByTagName("profile");
			int end = profileList.getLength();
			for(int i=0;i<end;i++){
				Node profileNode = profileList.item(i);
				String profileName = profileNode.getAttributes().getNamedItem("name").getNodeValue();
				Profile profile = manager.getProfiles().get(profileName);
				manager.addProfile(profileName);
				NodeList parserList = profileNode.getChildNodes();
				int pEnd = parserList.getLength();
				for(int j=pEnd;j-->0;){
					Node parserNode = parserList.item(j);
					if(parserNode.getNodeType() == Node.ELEMENT_NODE){
//						Parser p = profile.getParserByName(parserNode.getAttributes().getNamedItem("name").getNodeValue());
//						profile.addToActive(p);
//						NodeList attributes = parserNode.getChildNodes();
//						int aEnd = attributes.getLength();
//						for(int k=aEnd;k-->0;){
//							Node attr = attributes.item(k);
//							if(attr.getNodeType() == Node.ELEMENT_NODE){
//								
//							}
//						}
					}
				}
			}
			return true;
		}catch(ParserConfigurationException e){
		}catch(SAXException e){
		}catch(IOException e){
		}
		return false;
	}
}
