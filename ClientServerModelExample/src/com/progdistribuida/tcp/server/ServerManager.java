/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progdistribuida.tcp.server;

import com.progdistribuida.tcp.client.ClientConnectionManager;
import com.progdistribuida.tcp.client.ClientConnectionManagerInterface;
import com.progdistribuida.tcp.client.FileWrapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.ws.soap.MTOMFeature;

/**
 *
 * @author user
 */
public class ServerManager<T> extends Thread implements ClientConnectionManagerInterface<T>{
    
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
    public void ObjectHasBeenReceived(T object) {
        FileWrapper fw = (FileWrapper)object;
        org.me.webservice1.WebService1_Service service = new org.me.webservice1.WebService1_Service();
        org.me.webservice1.WebService1 port = service.getWebService1Port(new MTOMFeature(true));
        System.out.println("El file llego despues del soap");
        int lengt = fw.getFileBytes().length;
        byte[] Array = fw.getFileBytes();
        byte[] page = new byte[1024];  
        int cont = 0;
        int pages = 0;
        int lengthArray = Array.length;
        for (byte b : Array) {
            page[cont] = b;
            cont++; 
            
            //System.out.println(""+cont);
            
            if (cont == 1023){
                System.out.println("Entro en el if");
                if(pages == 59){
                    System.out.println("esta en la ultima pag");
                    System.out.println("webservice says: "+port.sendFile(fw.getFileName(), page, pages,1));
                    pages = 0;
                    //page = null;
                    System.out.println("El file llego a la ultima pagina");
                }else{
                    System.out.println("webservice says: "+port.sendFile(fw.getFileName(), page, pages,0));
                    
                   
                   //page = null;
                    System.out.println("El file se esta enviando la pagina "+pages);
                    pages ++;
                }
                 cont = 0;
            }
            
            
            
            
        }
        
    }
    
    public void sendThisMessgeToAll(Object object){
        for(ClientConnectionManager current:listOfClients){
            current.sendThisObjectToTheServerSide(object);
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
