/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Protocolo.Protocolo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guillermo y nacho
 */
//clase que contiene los manejadores de flujo asociados a un cliente
public class Cliente {
    
    public  PrintWriter out;
    public  BufferedReader in;
    public  Socket socket;
    private  boolean socketIniciado = false;
    public String nombre;

    
    public Cliente(String login) {
        nombre = login;
    }
    
    public void AbrirSocket(){
        
        if (!socketIniciado) {
            try {
            socket = new Socket("localhost", 6789);
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            socketIniciado = true;
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    BufferedReader getIn() {
       return in;
    }

    PrintWriter getOut() {
       return out;
    }
    
    public Socket getSocket(){
        return socket;
    
    }
 
}
