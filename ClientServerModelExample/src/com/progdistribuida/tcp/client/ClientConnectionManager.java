/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progdistribuida.tcp.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author user
 */
public class ClientConnectionManager extends Thread{
    
    Socket clientSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    
    boolean isListening=true;
    
    ClientConnectionManagerInterface caller;
    
    public ClientConnectionManager(ClientConnectionManagerInterface caller,
            Socket incomingSocket){
        this.caller=caller;
        this.clientSocket=incomingSocket;
        if(extractStreams()){
            this.start();
        }
    }
    
    public ClientConnectionManager(ClientConnectionManagerInterface caller,
            String ipAddress,int destionationPort){
        this.caller=caller;
        this.clientSocket=createClientSocket(ipAddress,destionationPort);
        if(extractStreams()){
            this.start();
        }
    }
    
    public Socket createClientSocket(String ipAddress,int destionationPort){
        try{
            Socket clientSocket=new Socket(ipAddress,destionationPort);            
            return clientSocket;
        }catch (Exception ex) {
            return null;
        }
    }
    
    public boolean extractStreams(){
        try{
            this.inputStream=new DataInputStream(clientSocket.getInputStream());
            this.outputStream=new DataOutputStream(clientSocket.getOutputStream());
            return true;
        }catch(Exception error){
            return false;
        }
    }
    
    @Override
    public void run(){
        try{
            int len;
            while((len=inputStream.readInt())>0 && this.isListening){
                byte[] bytes = new byte[len];
                inputStream.readFully(bytes);
                Object object=bytesToObject(bytes);
                this.caller.ObjectHasBeenReceived(object);
            }
        }catch(Exception error){
            System.err.println(error.getMessage());
        }
    }    

    private Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public void sendThisObjectToTheServerSide(Object object){
        try{
            byte[] bytes = objectToBytes(object);
            outputStream.writeInt(bytes.length); // write length of the message
            outputStream.write(bytes);
            outputStream.flush();
        }catch (Exception ex) {
            
        }
    }

    public static byte[] objectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public void closeThisConnection(){
        try{
            this.inputStream.close();
            this.outputStream.close();
            this.clientSocket.close();
        }catch (Exception ex) {
            
        }
        finally{
            this.isListening=false;
        }
    }
    
    
    
}
