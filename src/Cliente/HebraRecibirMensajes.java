/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import Protocolo.Protocolo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guillermo y nacho
 */
//Hebra que se encarga de escuchar y procesar los mensajes que recibe un cliente
public class HebraRecibirMensajes extends Thread {

    BufferedReader in;
    PrintWriter out;
    Socket socket;
    boolean salir = false;
    VentanaAutenticar v;
    Cliente cliente;
    
    public VentanaConversacionSala vConversacionSala;
    
    public Map conversaciones;

    HebraRecibirMensajes(Cliente comunicacion, VentanaAutenticar ventana) {
        in = comunicacion.getIn();
        out = comunicacion.getOut();
        socket = comunicacion.getSocket();
        v = ventana;
        cliente = comunicacion;
        conversaciones = new HashMap<String, Conversacion>();
    }

    public String getCabecera(String mensaje) {
        String cabecera = mensaje.split("/")[0];
        return cabecera;
    }

    @Override
    public void run() {
        String peticion;

        while (!salir) {

            try {
                peticion = in.readLine();

                if (peticion != null) {
                    System.out.println("Recibido del servidor mensaje " + peticion);
                    
                    //Recepción de mensaje de petición de conversación privada
                    if (getCabecera(peticion).equals(Protocolo.codigoPeticion)) {
                        String loginOrigen = peticion.split("/")[1];
                        String loginDestino = peticion.split("/")[2];
                        
                        new VentanaPeticionConversacion(loginOrigen, loginDestino, cliente, v.hebra).setVisible(true);
                    }
                    //Recepción de mensaje de petición privada aceptada
                    if (getCabecera(peticion).equals(Protocolo.codigoConversacionAceptada)) {
                        String loginOrigen = peticion.split("/")[1];
                        String loginDestino = peticion.split("/")[2];
                        System.out.println(loginDestino + " ha aceptado tu petición.");

                        Conversacion conv = new Conversacion(new VentanaConversacion(loginOrigen, loginDestino, cliente));
                        conv.vConversacion.setVisible(true);
                        conversaciones.put(loginOrigen+loginDestino, conv);
                    }
                    //Recepción de mensaje de petición privada rechazada
                    if(getCabecera(peticion).equals(Protocolo.codigoConversacionRechazada)) {
                        String loginDestino = peticion.split("/")[2];
                        System.out.println(loginDestino + " ha rechazado tu peticion, ja, ja.");
                    }
                    //Recepción de mensaje en una conversación privada iniciada
                    if(getCabecera(peticion).equals(Protocolo.codigoConversacion)) {
                       
                        if (peticion.split("/").length == 4) {
                            String usuario = peticion.split("/")[1];
                            String destino = peticion.split("/")[2];
                            String texto = peticion.split("/")[3];

                            Conversacion conver = (Conversacion) conversaciones.get(destino+usuario);
                            
                            conver.vConversacion.pegarTexto(usuario+": "+texto+"\n");
                        }
                        
                    }
                    //Recepción de mensaje de entrada en sala aceptada
                    if(getCabecera(peticion).equals(Protocolo.codigoSalaAceptada)) {
                        String nombreSala = peticion.split("/")[2];
                        String login = peticion.split("/")[1];
                        System.out.println("Has entrado en la sala "+nombreSala);
                        //Para permitir más salas en el futuro habría que cambiar lo siguiente, pues no se puede
                        //usar el mismo objeto vConversacionSala para salas distintas
                        vConversacionSala = new VentanaConversacionSala(login, nombreSala,cliente);
                        vConversacionSala.setVisible(true);
                    }
                    //Recepción de mensaje en una conversación en sala iniciada
                    if(getCabecera(peticion).equals(Protocolo.codigoConversacionSala)) {
                        String sala = peticion.split("/")[2];
                        String login = peticion.split("/")[1];
                        String texto = peticion.split("/")[3];
                        vConversacionSala.pegarTexto(login+": "+texto+"\n");
                        System.out.println(texto);
                        
                    }
                    
                    //Recepción del mensaje de un usuario fuera de la sala
                    if(getCabecera(peticion).equals(Protocolo.codigoUsuarioFuera)) {
                        String usuario = peticion.split("/")[1];
                        vConversacionSala.pegarTexto("-----El usuario "+usuario+" ha abandonado la sala-----\n");
                    }
                    
                    //Recepcion del mensaje de que un usuario ha entrado en una sala
                    if(getCabecera(peticion).equals(Protocolo.codigoUsuarioDentro)) {
                        String usuario = peticion.split("/")[1];
                        vConversacionSala.pegarTexto("-----El usuario "+usuario+" ha entrado en la sala-----\n");
                    }
                    
                    //Recepcion de mensaje de abandono de conversacion privada
                    if(getCabecera(peticion).equals(Protocolo.codigoAdios)) {
                        String loginOrigen = peticion.split("/")[1];
                        String loginDestino = peticion.split("/")[2];
                        Conversacion conver = (Conversacion) conversaciones.get(loginDestino+loginOrigen);
                        conver.vConversacion.pegarTexto("-----El usuario "+loginOrigen+" ha abandonado la conversación-----\n");
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(HebraRecibirMensajes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
