/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Protocolo.Protocolo;
import Servidor.Usuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guillermo y nacho
 */
public class Servidor {
    
    //Base de datos de usuarios registrados
    static public Map usuarios;
    
    //Socket y puerto de acceso al servicio
    static ServerSocket socketServicio;
    static int puerto;
    
    Map salas;
    static SalaConversacion salaGeneral;
    HebraServicio hebraS;
       
    
    public Servidor() {
        
        //Iniciamos el mapa de salas
        salas = new HashMap<String, SalaConversacion>();
        //Creamos la sala general (la única que hay por defecto) y la guardamos en el mapa de salas
        salaGeneral = new SalaConversacion("salaGeneral");
        salas.put(salaGeneral.nombreSala, salaGeneral);
        
    }
    
    int iniciarServicio(int port) {
        int error = 0;
        
        //Creamos la base de datos de usuarios
        usuarios = new HashMap<String, Usuario>();
        
        usuarios.put("Nacho", new Usuario("Nacho", "pass"));
        usuarios.put("Guille", new Usuario("Guille", "pass"));
        usuarios.put("Juanjo", new Usuario("Juanjo", "pass"));
        usuarios.put("Sara", new Usuario("Sara", "pass"));
        usuarios.put("Jose", new Usuario("Jose", "pass"));
        usuarios.put("JuanAntonio", new Usuario("JuanAntonio", "pass"));
        //Abrimos el puerto especificado para acceso al servicio
        try {
            
            System.out.print("Abriendo socket de escucha en puerto "+port+"...");
            socketServicio=new ServerSocket(port);
            System.out.println(" ok.");
            //Iniciamos la hebra que atiende las peticiones iniciales de conexión
            hebraS = new HebraServicio(this);
            hebraS.start();
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
            error=1;
        }
        
        return error;
        
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        puerto = 6789;
        Servidor server = new Servidor();
        server.iniciarServicio(puerto);
    }

}
