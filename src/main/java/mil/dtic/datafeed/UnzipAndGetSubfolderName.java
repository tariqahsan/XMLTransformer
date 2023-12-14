package mil.dtic.datafeed;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.*;

public class UnzipAndGetSubfolderName {

    public static void main(String[] args) {
    	String zipFilePath = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\IAC2019-12-03.zip";
        try {
            String subfolderName = unzipAndGetSubfolderName(zipFilePath);
            System.out.println("Subfolder name: " + subfolderName);
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
//                ZipEntry zipEntry = new ZipEntry(zipFilePath);
//                String fileName = zipEntry.getName();
                int lastIndexOfSlash = entryName.lastIndexOf("\\");
                if (lastIndexOfSlash != -1) {
                    entryName = entryName.substring(lastIndexOfSlash + 1);
                }
               // System.out.println(fileName); // prints "file.txt"
                // Check if the entry is a directory (subfolder)
                if (entry.isDirectory()) {
                    // Assuming the subfolder name is the first directory found
                    String subfolderName = entryName.split("/")[0];
                    return subfolderName;
                }
            }
        }

        throw new IOException("No subfolder found in the zip file.");
    }
}

