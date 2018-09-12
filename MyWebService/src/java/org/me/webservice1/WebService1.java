/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.webservice1;

import java.io.FileOutputStream;
import java.io.IOException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author javier
 */
@WebService(serviceName = "WebService1")
public class WebService1 {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "sendFile")
    public String sendFile(
            @WebParam(name = "fileName") String fileName,
            @WebParam(name = "bytes") byte[] bytes){
        try {
            String path = "/home/javier/Desktop/test/server/"+fileName;
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(bytes);
            return path;
        } catch (IOException ex) {
            return "ERROR";
        }
    }
}
