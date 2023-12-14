package mil.dtic.datafeed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;

public class SeparateXmlFiles {

    public static void main(String[] args) {
        try {
            // Load the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\ecms.xml"));

            // Process each node and create separate XML files
            processNodes(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processNodes(Document document) throws Exception {
        // Get the root element
        Element rootElement = document.getDocumentElement();

        // Process each child node
        NodeList childNodes = rootElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                Element childElement = (Element) childNodes.item(i);

                // Create a new document containing only the current node and its descendants
                Document newDocument = createNewDocument(childElement);

                // Save the new document to a separate file
                saveXmlDocument(newDocument, childElement.getNodeName() + ".xml");
            }
        }
    }

    private static Document createNewDocument(Element rootElement) throws Exception {
        // Create a new document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document newDocument = builder.newDocument();

        // Import the current node and its descendants into the new document
        Node importedNode = newDocument.importNode(rootElement, true);
        newDocument.appendChild(importedNode);

        return newDocument;
    }

    private static void saveXmlDocument(Document document, String filePath) throws Exception {
        // Use a Transformer to save the document to a new file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        System.out.println(filePath);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}

