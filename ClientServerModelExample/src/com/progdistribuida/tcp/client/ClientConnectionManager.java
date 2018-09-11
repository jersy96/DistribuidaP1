/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progdistribuida.tcp.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author user
 */
public class ClientConnectionManager extends Thread{
    
    Socket clientSocket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    
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
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
            return true;
        }catch(Exception error){
            return false;
        }
    }
    
    @Override
    public void run(){
        try{
            String textLine=null;
            while(((textLine=(String)this.inputStream.readObject())!=null)
                    &&(this.isListening)){
                this.caller.MessageHasBeenReceived(
                        clientSocket.getInetAddress()
                        .getHostAddress()+": "+
                                this.clientSocket.getPort()+": "+ 
                        textLine);
            }
        }catch(Exception error){
            System.err.println(error.getMessage());
        }
    }
    
    public void sendThisMessageToTheServerSide(String message){
        try{
            message+="\n";
            this.outputStream.writeObject(message);
            outputStream.flush();
        }catch (Exception ex) {
            
        }
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
