/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progdistribuida.tcp.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author user
 */
public class ClientConnectionManager extends Thread{
    
    Socket clientSocket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    
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
            this.bufferedReader=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            return true;
        }catch(Exception error){
            return false;
        }
    }
    
    @Override
    public void run(){
        try{
            String textLine=null;
            while(((textLine=this.bufferedReader.readLine())!=null)
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
            this.bufferedWriter.write(message, 0, message.length());
            bufferedWriter.flush();
        }catch (Exception ex) {
            
        }
    }
    
    public void closeThisConnection(){
        try{
            this.bufferedWriter.close();
            this.bufferedReader.close();
            this.clientSocket.close();
        }catch (Exception ex) {
            
        }
        finally{
            this.isListening=false;
        }
    }
    
    
    
}
