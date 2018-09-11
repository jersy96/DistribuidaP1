package com.progdistribuida.tcp.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author javier
 */
public class FileWrapper {
    private String fileName;
    private ByteArrayInputStream fileBytes;
    
    public FileWrapper(File file){
        fileName = file.getName();
        fileBytes = new ByteArrayInputStream(readBytesFromFile(file));
    }
    
    private byte[] readBytesFromFile(File file) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            bytesArray = new byte[(int) file.length()];
            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bytesArray;
    }

    public String getFileName() {
        return fileName;
    }

    public ByteArrayInputStream getFileBytes() {
        return fileBytes;
    }
}
