package mil.dtic.datafeed;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AddNamespaceExample {

    public static void main(String[] args) {
        try {
            // Load existing XML document
            File inputFile = new File("C:\\Users\\Tariq Ahsan\\Desktop\\Training\\Selenium\\Workspace\\XMLTransformer12102023-Works\\AD1000282.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            // Add a namespace declaration to the root element
            Element rootElement = document.getDocumentElement();
            addNamespaceDeclaration(rootElement, "prefix", "URI");

            // Add a namespace declaration to the start tag of an element
            Element targetElement = findElementById(document, "Record");
            if (targetElement != null) {
                addNamespaceDeclaration(targetElement, "prefix", "URI");
            }

            // Save the modified document
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(new File("C:\\Users\\Tariq Ahsan\\Desktop\\Training\\Selenium\\Workspace\\XMLTransformer12102023-Works\\namespace-AD1000282.xml")));
            System.out.println("Namespace declarations added successfully.");

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addNamespaceDeclaration(Element element, String prefix, String uri) {
        if (element != null) {
            element.setAttribute("xmlns:" + prefix, uri);
        }
    }

    private static Element findElementById(Document document, String id) {
        NodeList elements = document.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            if (id.equals(element.getAttribute("id"))) {
                return element;
            }
        }
        return null;
    }
}

