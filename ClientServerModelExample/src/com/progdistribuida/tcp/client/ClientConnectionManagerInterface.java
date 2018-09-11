/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progdistribuida.tcp.client;

/**
 *
 * @author asaad
 */
public interface ClientConnectionManagerInterface<T> {
    
    public void ObjectHasBeenReceived(T object);
    
}
