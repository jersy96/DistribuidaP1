/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.webservice1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.soap.MTOM;

/**
 *
 * @author javier
 */
@WebService(serviceName = "WebService1")
@MTOM(enabled = true)
public class WebService1 {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "createFile")
    public boolean createFile(@WebParam(name = "fileName") String fileName){
        try {
            String path = "/home/javier/Desktop/test/server/"+fileName;
            File file = new File(path);
            file.createNewFile();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    @WebMethod(operationName = "appendToFile")
    public boolean appendToFile(
            @WebParam(name = "fileName") String fileName,
            @WebParam(name = "bytes") byte[] bytes){
        try {
            String path = "/home/javier/Desktop/test/server/"+fileName;
            FileOutputStream fos = new FileOutputStream(path, true);
            fos.write(bytes);
            fos.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    @WebMethod(operationName = "getFileSize")
    public int getFileSize(@WebParam(name = "fileName") String fileName){
        String path = "/home/javier/Desktop/test/server/"+fileName;
        File file = new File(path);
        return readBytesFromFile(file).length;
    }
    
    @WebMethod(operationName = "getFilePartition")
    public byte[] getFilePartition(
            @WebParam(name = "fileName") String fileName,
            @WebParam(name = "start") int start,
            @WebParam(name = "end") int end){
        String path = "/home/javier/Desktop/test/server/"+fileName;
        File file = new File(path);
        byte[] fileBytes = readBytesFromFile(file);
        return Arrays.copyOfRange(fileBytes, start, end);
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
}
