package apartment;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Xml_Parser_Code {

	private static String address; 
	
	public static String getAddress() {
		return address;
	}

	public static void setAddress(String address) {
		Xml_Parser_Code.address = address;
	}

	public String parseXml(String xmlData) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlData)));
            doc.getDocumentElement().normalize();

            NodeList rows = doc.getElementsByTagName("row");
            for (int i = 0; i < rows.getLength(); i++) {
                Node node = rows.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    setAddress(element.getElementsByTagName("sido_cd").item(0).getTextContent()
                    		+element.getElementsByTagName("sgg_cd").item(0).getTextContent());
                    System.out.println("---------------------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getAddress();
    }


}
