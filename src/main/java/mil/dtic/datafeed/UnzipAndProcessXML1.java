package mil.dtic.datafeed;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class UnzipAndProcessXML1 {

    public static void main(String[] args) {
        String zipFilePath = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\IAC2019-12-03.zip";
        String targetPDFDirectory = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\pdf";
        String targetXMLDirectory = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\xml";

        try {
            // Unzip the folder
            unzipFolder(zipFilePath, targetPDFDirectory, targetXMLDirectory);

            // Process XML files
            processXMLFiles(targetXMLDirectory);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void unzipFolder(String zipFilePath, String targetPDFDirectory, String targetXMLDirectory) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                String targetPathPdf = targetPDFDirectory + File.separator + entryName;
                String targetPathXml = targetXMLDirectory + File.separator + entryName;

                // If the entry is a PDF file, copy it to the target PDF directory
                if (entryName.toLowerCase().endsWith(".pdf")) {
                    copyEntry(zipInputStream, targetPathPdf);
                }

                // If the entry is an XML file, copy it to the target XML directory
                if (entryName.toLowerCase().endsWith(".xml")) {
                    copyEntry(zipInputStream, targetPathXml);
                }
            }
        }
    }

    private static void copyEntry(InputStream inputStream, String targetPath) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(targetPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    private static void processXMLFiles(String targetXMLDirectory) {
        File[] xmlFiles = new File(targetXMLDirectory + "20191203_f1e21db4-98f6-408d-a384-06638802de1a").listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        if (xmlFiles != null) {
            for (File xmlFile : xmlFiles) {
                try {
                    // Load the XML document
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(xmlFile);

                    // Process each 'Record' node under 'Records'
                    processRecordNodes(document, targetXMLDirectory);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void processRecordNodes(Document document, String xmlDirectory) throws Exception {
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
                    String targetFilePath = xmlDirectory + "/files/" + accessionNumber + ".xml";
                    saveXmlDocument(newDocument, targetFilePath);
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

