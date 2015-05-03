/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author guillermo y nacho
 */

//Clase que contiene la información y variables necesarias de un usuario
public class Usuario {
    
    public String login;
    public String pass;
    public Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    
    public Usuario(String usuario, String contrasena){
        login = usuario;
        pass = contrasena;
    }
    
    public Usuario(String usuario, String contrasena, Socket s, BufferedReader input, PrintWriter output){
        login = usuario;
        pass = contrasena;
        socket = s;
        in = input;
        out = output;
    }
}
