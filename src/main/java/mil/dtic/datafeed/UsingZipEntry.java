package mil.dtic.datafeed;

import java.util.zip.ZipEntry;

public class UsingZipEntry {
    public static void main(String[] args) {
    	String zipFilePath = "C:\\Users\\Tariq Ahsan\\Downloads\\XMLTransformer\\IAC2019-12-03.zip";
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        String fileName = zipEntry.getName();
        int lastIndexOfSlash = fileName.lastIndexOf("\\");
        if (lastIndexOfSlash != -1) {
            fileName = fileName.substring(lastIndexOfSlash + 1);
        }
        System.out.println(fileName); // prints "file.txt"
    }
}