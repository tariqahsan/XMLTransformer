package mil.dtic.datafeed;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SpringBootApplication
public class XmlTransformerApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(XmlTransformerApplication.class, args);
		
		// Access command line arguments here
		//String filePath = "EF003062.xml";
		String filePath = "ecms.xml";
		//String filePath = args[0];
		File xmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
	
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			System.out.println(doc.getDocumentURI());
			//update attribute value
			//updateAttributeValue(doc); // executing this one only for the test

			//update Element value
			//updateElementValue(doc);
			
			// Rename all node names
			renameAllNodeNames(doc, "iacEcms:");

			//delete element
			//deleteElement(doc);

			//add new element
			//addElement(doc);

			//write the updated document to file or console
			doc.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("modified-ecms.xml"));
			//StreamResult result = new StreamResult(new File(args[1]));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			System.out.println("XML file updated successfully");

		} catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
			e1.printStackTrace();
		}

	}

	private static void deleteElement(Document doc) {

		NodeList ureds = doc.getElementsByTagName("meta:Metadata");
		Element ured = null;
		//Iterate
		for(int i=0; i<ureds.getLength();i++){
			ured = (Element) ureds.item(i);
			Node genderNode = ured.getElementsByTagName("meta:DODFundingSources").item(0);
			ured.removeChild(genderNode);
		}

	}

	private static void updateElementValue(Document doc) {

		NodeList ureds = doc.getElementsByTagName("meta:Metadata");
		Element ured = null;

		//Iterate
		for(int i=0; i<ureds.getLength();i++){
			ured = (Element) ureds.item(i);
			Node name = ured.getElementsByTagName("meta:Punctuated").item(0).getFirstChild();
			String dodFundingsources = ured.getElementsByTagName("meta:Punctuated").item(0).getFirstChild().getNodeValue();
			Node ingestionDateNode = ured.getElementsByTagName("meta:IngestionDate").item(0).getFirstChild();
			String ingestionDate = ured.getElementsByTagName("meta:IngestionDate").item(0).getFirstChild().getNodeValue();
			System.out.println(dodFundingsources + " " + ingestionDate);
			//name.setNodeValue(name.getNodeValue().toUpperCase());
		}
//		for(int i=0; i<iacs.getLength();i++){
//			iac = (Element) iacs.item(i);
//			System.out.println("iac -> " + iac);
//			System.out.println("node name: " + iac.getNodeName() + " node value: " + iac.getNodeValue());
//			Node name = iac.getElementsByTagName("meta:DODFundingSources").item(0).getFirstChild();
//			System.out.println(name.getLocalName());
//			name.setNodeValue(name.getNodeValue().toUpperCase());
//		}
	}

	private static void updateAttributeValue(Document doc) {

		NodeList ureds = doc.getElementsByTagName("meta:Metadata");
		Element ured = null;

		for(int i=0; i<ureds.getLength();i++){
			ured = (Element) ureds.item(i);

			//System.out.println(ured.getTextContent());
			Node ingestionDateNode = ured.getElementsByTagName("meta:IngestionDate").item(0).getFirstChild();
			String ingestionDate = ured.getElementsByTagName("meta:IngestionDate").item(0).getFirstChild().getNodeValue();
			System.out.println(ingestionDate);
			// Just a one liner regex. There are better way to check date validation
			boolean isValidFormat = ingestionDate.matches("([0-9]{4})-([0-9]{2})/([0-9]{2})");
			// Check if the date value is invalid then correct it
			if(!isValidFormat) {

				// Parse the original date string
				LocalDateTime dateTime = LocalDateTime.parse(ingestionDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

				// Format the modified date string
				String validIngestionDate = dateTime.format(DateTimeFormatter.ISO_DATE);

				// Print the modified date string
				System.out.println(validIngestionDate);
				ingestionDateNode.setNodeValue(validIngestionDate);
			}
			System.out.println(isValidFormat);
		}
	}
	
	private static void renameAllNodeNames(Document document, String newName) {
        // Get the root element
        Element rootElement = document.getDocumentElement();

        // Recursively rename all nodes in the document
        renameNodeNamesRecursive(document, rootElement, newName);
    }
	
	private static void renameNodeNamesRecursive(Document document, Element element, String newName) {
        // Rename the current element
        document.renameNode(element, null, newName);

        // Get the child nodes of the current element
        NodeList childNodes = element.getChildNodes();

        // Process child nodes recursively
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (childNode instanceof Element) {
                // If the child node is an element, recursively rename its nodes
                renameNodeNamesRecursive(document, (Element) childNode, newName);
            }
        }
    }

    private static void printXmlDocument(Document document) {
        // Code to print the modified XML document
        // (This can vary based on your specific requirements)
    }

}
