package mil.dtic.datafeed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class PrefixAllNodeNames2 {

    public static void main(String[] args) {
        try {
            // Load the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\ecms.xml"));

            // Prefix all node names
            prefixAllNodeNames(document, "prefix");

            // Save the modified XML document to a new file
            saveXmlDocument(document, "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\prefixed-ecms.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prefixAllNodeNames(Document document, String prefix) {
        // Get the root element
        Element rootElement = document.getDocumentElement();

        // Recursively add the prefix to all nodes in the document
        addPrefixToNodeNamesRecursive(document, rootElement, prefix);
    }

    private static void addPrefixToNodeNamesRecursive(Document document, Element element, String prefix) {
        // Get the namespace URI and prefix of the current element
        String namespaceURI = element.getNamespaceURI();
        //String namespaceURI = " ";
        String currentPrefix = element.getPrefix();

        // Build the new QName with the updated prefix
        String newPrefix = prefix.isEmpty() ? null : prefix;
//        String newName = element.getLocalName();
        String newName = element.getNodeName();
        String newQualifiedName = (newPrefix != null) ? (newPrefix + ":" + newName) : newName;

        // Rename the current element with the new QName and namespace
        document.renameNode(element, namespaceURI, newQualifiedName);

        // Get the child nodes of the current element
        NodeList childNodes = element.getChildNodes();

        // Process child nodes recursively
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (childNode instanceof Element) {
                // If the child node is an element, recursively add the prefix to its nodes
                addPrefixToNodeNamesRecursive(document, (Element) childNode, prefix);
            }
        }
    }

    private static void saveXmlDocument(Document document, String filePath) throws Exception {
        // Use a Transformer to save the modified document to a new file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}
