package mil.dtic.datafeed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class RenameAllNodeNames2 {

    public static void main(String[] args) {
        try {
            // Load the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\ecms.xml"));

            // Rename all node names
            renameAllNodeNames(document, "newName");
            System.out.println("done");

            // Print the modified XML document
            printXmlDocument(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renameAllNodeNames(Document document, String newName) throws XPathExpressionException {
        // Create an XPath object
        XPath xpath = XPathFactory.newInstance().newXPath();

        // Select all elements in the document
        XPathExpression expression = xpath.compile("//*/self::*");
        NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

        // Rename each element
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            System.out.println(element.getNodeName());
            newName = "iacEcms:" + element.getNodeName();
            document.renameNode(element, null, newName);
        }
    }

    private static void printXmlDocument(Document document) throws TransformerException {
    	//write the updated document to file or console
		document.getDocumentElement().normalize();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File("modified-ecms.xml"));
		//StreamResult result = new StreamResult(new File(args[1]));
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, result);
		System.out.println("XML file updated successfully");
    }
}

