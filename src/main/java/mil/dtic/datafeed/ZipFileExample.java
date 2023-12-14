package mil.dtic.datafeed;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileExample {

    public static void main(String[] args) throws IOException {
    	String zipFileWithPath = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\IAC2019-12-03.zip";
        // Get the path to the zip file
        Path zipFilePath = Paths.get(zipFileWithPath);

        // Check if the zip file exists
        if (!Files.exists(zipFilePath)) {
            System.out.println("The zip file does not exist.");
            return;
        }

        // Create a ZipFile object
        ZipFile zipFile = new ZipFile(zipFilePath.toFile());
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        // Iterate over the zip file entries
        while (entries.hasMoreElements()) {
//        for (ZipEntry entry : entries) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();
            // Check if the entry is a directory
            if (entry.isDirectory()) {
                System.out.println("The zip file has folders.");
                break;
            }
        }

        // Close the ZipFile object
        zipFile.close();
    }
}
