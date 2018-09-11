/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progdistribuida.run;

import com.progdistribuida.tcp.server.ServerManager;

/**
 *
 * @author user
 */
public class ClientServerModelExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ServerManager(9019);
    }
    
}
