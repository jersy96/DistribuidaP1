/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progdistribuida.tcp.server;

import com.progdistribuida.tcp.client.ClientConnectionManager;
import com.progdistribuida.tcp.client.ClientConnectionManagerInterface;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author user
 */
public class ServerManager extends Thread implements ClientConnectionManagerInterface{
    
    boolean isServerAcceptingConnections=true;
    ServerSocket serverSocket;
    
    int portNumber;
    Vector<ClientConnectionManager> listOfClients=new Vector();
    
    int maxNumberOfClientsPerIp=10;
    int maxNumberOfDiffIps=10;
    Hashtable<String,Integer> connectionsPerIp=new Hashtable<>();

    public boolean isIsServerAcceptingConnections() {
        return isServerAcceptingConnections;
    }

    public void setIsServerAcceptingConnections(boolean isServerAcceptingConnections) {
        this.isServerAcceptingConnections = isServerAcceptingConnections;
    }
    
    public ServerManager(int portNumberParameter){
        this.portNumber=portNumberParameter;
        initializeServerSocket();
    }
    
    public void initializeServerSocket(){
        try{    
            this.serverSocket=new ServerSocket(portNumber);            
            this.start();
        }catch(Exception error){
            System.err.println(error.getMessage());
        }   
    }
    
    @Override
    public void run(){
        try{
            while(this.isServerAcceptingConnections){
                if(this.serverSocket!=null){
                    Socket clientConnection=this.serverSocket.accept();
                    if(applySocketFilter(clientConnection)){
                        processIncomingClient(clientConnection);
                    }else{
                        closeSocketBecauseFilter(clientConnection);
                    }
                }
            }
        }catch (Exception ex) {
            
        }
    }

    private void processIncomingClient(Socket clientConnection) {
        this.listOfClients.add(new ClientConnectionManager(this,clientConnection));
    }

    @Override
    public void MessageHasBeenReceived(String message) {
        System.out.println(message);
        if(message.indexOf("killall")!=-1){
            this.killAllConnections();
        }
        sendThisMessgeToAll(message);
    }
    
    public void sendThisMessgeToAll(String message){
        for(ClientConnectionManager current:listOfClients){
            current.sendThisMessageToTheServerSide(message);
        }
    }

    private boolean applySocketFilter(Socket clientConnection) {
        try{
            InetAddress inetAddress= clientConnection.getInetAddress();
            if(!connectionsPerIp.containsKey(inetAddress.getHostName())){
                connectionsPerIp.put(inetAddress.getHostName(), 1);
            }else{
                int currentNumberOfConnections=connectionsPerIp.
                        get(inetAddress.getHostName());
                if(currentNumberOfConnections+1>maxNumberOfClientsPerIp){
                    return false;
                }
                connectionsPerIp.replace(inetAddress.getHostName(), 
                        currentNumberOfConnections+1);
            }
            return true;
        }catch (Exception ex) {
            return false;
        }
    }

    /***
     * 
     * @param clientConnection This represents the client connection
     */
    private void closeSocketBecauseFilter(Socket clientConnection) {
        try{
            clientConnection.close();
        }catch (Exception ex) {
            
        }
    }
    
    
    public void killAllConnections(){
        for(ClientConnectionManager current:this.listOfClients){
            if(current!=null){
                current.closeThisConnection();
            }
        }
        this.connectionsPerIp.clear();
    }
    
}
