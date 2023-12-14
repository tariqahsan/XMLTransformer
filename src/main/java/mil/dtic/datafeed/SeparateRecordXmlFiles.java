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

public class SeparateRecordXmlFiles {

    public static void main(String[] args) {
        try {
            // Load the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\ecms.xml"));

            // Process each 'Record' node under 'Records'
            processRecordNodes(document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processRecordNodes(Document document) throws Exception {
        // Get the root element
        Element rootElement = document.getDocumentElement();

        // Find 'Records' node
        NodeList recordsList = rootElement.getElementsByTagName("Records");
        if (recordsList.getLength() > 0) {
            Element recordsElement = (Element) recordsList.item(0);

            // Process each 'Record' node under 'Records'
            NodeList recordNodes = recordsElement.getElementsByTagName("Record");
            for (int i = 0; i < recordNodes.getLength(); i++) {
                Element recordElement = (Element) recordNodes.item(i);

                // Get the value of the 'AccessionNumber' node
                String accessionNumber = getAccessionNumber(recordElement);

                if (accessionNumber != null && !accessionNumber.isEmpty()) {
                    // Create a new document containing only the current 'Record' node and its descendants
                    Document newDocument = createNewDocument(recordElement);

                    // Save the new document to a separate file named after the 'AccessionNumber' value
                    saveXmlDocument(newDocument, accessionNumber + ".xml");
                }
            }
        }
    }

    private static String getAccessionNumber(Element recordElement) {
        // Find the 'AccessionNumber' node under the current 'Record' node
        NodeList accessionNumberList = recordElement.getElementsByTagName("AccessionNumber");
        if (accessionNumberList.getLength() > 0) {
            return accessionNumberList.item(0).getTextContent().trim();
        }
        return null;
    }

    private static Document createNewDocument(Element rootElement) throws Exception {
        // Create a new document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document newDocument = builder.newDocument();

        // Import the current 'Record' node and its descendants into the new document
        Node importedNode = newDocument.importNode(rootElement, true);
        newDocument.appendChild(importedNode);

        return newDocument;
    }

    private static void saveXmlDocument(Document document, String filePath) throws Exception {
        // Use a Transformer to save the document to a new file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}
