package mil.dtic.datafeed;

//In Java, you can use the `org.w3c.dom` package to work with XML documents. To rename all child nodes of a parent node, you can iterate through the child nodes and use the `renameNode` method. Here's an example Java code snippet demonstrating how to rename all child nodes of a parent node:
//
//```java
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class RenameChildNodesExample {
	
	private static Document document;

    public static void main(String[] args) {
        try {
            // Load the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("ecms.xml");

            // Get the parent node
            Element parentNode = document.getDocumentElement();

            // Rename all child nodes
            renameAllChildNodes(parentNode, null, "iacEcms:");

            // Print the modified XML document
            printXmlDocument(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renameAllChildNodes(Element parentNode, String namespaceURI, String newName) {
        // Get the list of child nodes
        NodeList childNodes = parentNode.getChildNodes();

        // Iterate through the child nodes and rename them
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (childNode instanceof Element) {
                // Cast the node to Element
                Element childElement = (Element) childNode;

                // Rename the child node
                document.renameNode(childElement, null, newName);
            }
        }
    }

    private static void printXmlDocument(Document document) throws TransformerException {
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
//```
//
//Please note that you need to replace `"path/to/your/xml/file.xml"` with the actual path to your XML file and `"newName"` with the desired new name for the child nodes.
//
//Keep in mind that the `renameNode` method has been deprecated in recent versions of Java, and it's recommended to use other alternatives if available in your specific use case.
