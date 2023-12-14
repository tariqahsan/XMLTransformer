package mil.dtic.datafeed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RenameAllNodeNames1 {

	private static Document document;

	public static void main(String[] args) {
		try {
			// Load the XML document
			//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//            DocumentBuilder builder = factory.newDocumentBuilder();
			//            Document document = builder.parse("C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\ecms.xml");
			String filePath = "EF003062.xml";
			//String filePath = "ecms.xml";
			//String filePath = args[0];
			File xmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
			// Rename all node names
			renameAllNodeNames(document, "newName");

			// Print the modified XML document
			//printXmlDocument(document);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void renameAllNodeNames(Document document, String newName) {
		// Get the root element
		Element rootElement = document.getDocumentElement();

		// Recursively rename all nodes in the document
		renameNodeNamesRecursive(rootElement, newName);
	}

	private static void renameNodeNamesRecursive(Element element, String newName) {
		// Rename the current element
		document.renameNode(element, null, newName);

		// Get the child nodes of the current element
		NodeList childNodes = element.getChildNodes();

		// Process child nodes recursively
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);

			if (childNode instanceof Element) {
				// If the child node is an element, recursively rename its nodes
				renameNodeNamesRecursive((Element) childNode, newName);
			}
		}
	}

	private static void printXmlDocument(Document document) {
		// Code to print the modified XML document
		// (This can vary based on your specific requirements)
	}
}
