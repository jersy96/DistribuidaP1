/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.webservice1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
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
    List<byte[]> arrayBytes = new ArrayList<byte[]>();
    
    byte[] file;
    int contador = 0;
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    @WebMethod(operationName = "saveArray")
    public void saveArray(
            @WebParam(name = "page") byte[] page,
            @WebParam(name = "id") int id,
            @WebParam(name = "flag") int flag){
            arrayBytes.add(id, page);    
            byte[] temp;
          if(flag == 1){
              
              
//              for(int i = 0; i < arrayBytes.size(); i++ ){
//                  file = arrayBytes.get(i-1);
//              }
            for(int i = 0; i < arrayBytes.size(); i++ ){
                temp = arrayBytes.get(i);
                for(int j = 0; j < temp.length; j++){
                    file[contador]  = temp[j];
                    contador ++;
             }
            }
            
          }
    }
//    public void saveArray(byte[] page, int id, int flag){
//        arrayBytes.add(id, page);
//        
//        if(flag == 1){
//        for(int i = 0; i < arrayBytes.size(); i++ ){
//            file = arrayBytes.get(i);
////            for(int j = 0; j < arrayBytes.get(i).length; j++){
////                
////            }
//        }
//        }        
//    }
            
    @WebMethod(operationName = "sendFile")
    public String sendFile(
            @WebParam(name = "fileName") String fileName,
            @WebParam(name = "bytes") byte[] bytes,
            @WebParam(name = "id") int id,
            @WebParam(name = "flag") int flag){
        
         String path = "C:\\Users\\t-bon\\Desktop\\"+fileName;
       
         try {
            saveArray(bytes, id, flag);
            if(flag == 1){
                System.out.println("llego casi al final");
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(bytes);
            }
            
            return path;
        }catch (IOException ex) {
                return "ERROR";
            }
    }
}
