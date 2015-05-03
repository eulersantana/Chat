/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import static Servidor.Servidor.socketServicio;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author guillermo y nacho
 */

//Hebra que esucucha las peticiones iniciales
//Cuando recibe una, crea otra hebra donde atiende al cliente y le autentica
public class HebraServicio extends Thread {
    
    Servidor server;
    
    public HebraServicio(Servidor server){
        this.server = server;
    }
    
    @Override
    public void run(){
    
        do {
              try{
                //Acepta una conexión
                Socket s=Servidor.socketServicio.accept();
                System.out.println("Creando hebra de servicio ...");
                //Crea una hebra para el usuario donde le autenticará
                Servicio servicio =new Servicio(s,Servidor.usuarios,server);
                servicio.start();
               //server.addHebraCliente(s,pepe)
               
                
            }catch(IOException e){
                System.err.print("Error");
            } 
            
        } while(true);
    
    }
    
}
