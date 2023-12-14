package mil.dtic.datafeed;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class UnzipAndProcessXML2 {

    public static void main(String[] args) {
    	
    	String zipFilePath = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\IAC2019-12-03.zip";
        String targetPDFDirectory = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\pdf";
        String targetXMLDirectory = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\xml";

        try {
            // Unzip the folder and get the subfolder name
            String subfolderName = unzipAndGetSubfolderName(zipFilePath);

            // Copy PDF files to a separate directory
            copyPDFFiles(subfolderName, targetPDFDirectory);

            // Process XML files
            String xmlFilePath = targetPDFDirectory + File.separator + subfolderName + File.separator + "your.xml";
            processXMLFiles(xmlFilePath, targetXMLDirectory);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String unzipAndGetSubfolderName(String zipFilePath) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();

                // Check if the entry is a directory (subfolder)
                if (entry.isDirectory()) {
                    // Assuming the subfolder name is the first directory found
                    return entryName.split("/")[0];
                }
            }
        }

        throw new IOException("No subfolder found in the zip file.");
    }

    private static void copyPDFFiles(String subfolderName, String targetPDFDirectory) throws IOException {
        String sourcePDFDirectory = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\pdf" + subfolderName;
        File sourceDirectory = new File(sourcePDFDirectory);

        if (sourceDirectory.isDirectory()) {
            File[] pdfFiles = sourceDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
            if (pdfFiles != null) {
                for (File pdfFile : pdfFiles) {
                    String targetPath = targetPDFDirectory + File.separator + pdfFile.getName();
                    Files.copy(pdfFile.toPath(), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } else {
            throw new IOException("Source directory not found: " + sourcePDFDirectory);
        }
    }

    private static void processXMLFiles(String xmlFilePath, String targetXMLDirectory) {
        try {
            // Load the XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(xmlFilePath));

            // Process each 'Record' node under 'Records'
            processRecordNodes(document, targetXMLDirectory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processRecordNodes(Document document, String targetXMLDirectory) throws Exception {
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
                    String targetFilePath = targetXMLDirectory + File.separator + accessionNumber + ".xml";
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

