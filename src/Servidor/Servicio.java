/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Protocolo.Protocolo;
import static Servidor.Servidor.salaGeneral;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guillermo y nacho
 */

//Hebra donde se procesan todas las acciones del usuario (auntenticacion, envío de mensajes...)
 public class Servicio extends Thread {
        
        // Socket utilizado para ofrecer el servicio al cliente que efectuó la solicitud
        // de conexión recibida en el socket de la clase "Servidor"
        Socket socket;
        
        // Manejadores de los flujos de recepcion y envio.
        PrintWriter out;
        BufferedReader in;
        Servidor server;
        // referencia de la base de datos de usuarios comu'n.'
        Map usuarios;
        //Base de datos de salas de conversacion
        Map salas;

        
        // Constructor de la clase:
        public Servicio(Socket s, Map u, Servidor server){
            
            usuarios=u;
            socket=s;
            this.server = server;
            salas = server.salas;
            
            // Obtenemos los flujos de escritura y lectura:
            try {
            
                out= new PrintWriter(socket.getOutputStream(), true);
                in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                
                
            } catch(IOException e){
                // Se acabo'
                System.err.println("Error en la hebra "+this.currentThread().getName());
            }
        }
        
        //Hebra principal del servicio
        public void run() {
            boolean salir = false;
            int estado = 0;
            String cabeceraMensaje;
            
            
            do{
                try {
                    String peticion = in.readLine();
                    System.out.println("Peticion de un usuario");
                    
                    switch (estado) {
                        
                        case 0: //Inicio
                            cabeceraMensaje = peticion.split("/")[0];
                            String login = peticion.split("/")[1];
                            String contrasena = peticion.split("/")[2];
                            
                            if(cabeceraMensaje.equals(Protocolo.codigoLogin)){

                                Usuario usuario = (Usuario) usuarios.get(login);
                                
                                if(usuario==null) { //El usuario introducido no existe
                                    out.println(Protocolo.mensajeNOK);
                                }
                                else { //el usuario introducido existe
                                    if (usuario.pass.equals(contrasena)) { //EL usuario y contraseña es correcto
                                        String respuesta = Protocolo.mensajeOK + "/" + login;
                                        out.println(respuesta);

                                        estado = 1; //Estado Cliente Autenticado
                                        //Asocio al usuario autenticado su socket
                                        usuarios.put(login, new Usuario("Nacho", "pass", socket, in, out));

                                        System.out.println("Cliente " + login + " autenticado");
                                    }
                                    if (!usuario.pass.equals(contrasena)) { //El usuario es correcto pero la pass no
                                        out.println(Protocolo.mensajeNOK);
                                        estado = 0;
                                    }
                                }
                            }
                            
                            
                            
                        break;
                        
                        case 1: //ClienteAutenticado
                            cabeceraMensaje = peticion.split("/")[0];
                            
                            
                            if(cabeceraMensaje.equals(Protocolo.codigoPeticion)) {
                                String usuarioOrigen = peticion.split("/")[1];
                                String usuarioDestino = peticion.split("/")[2];
                                //Obtenemos el usuario a quien va dirigida la petición
                                Usuario userDestino = (Usuario) usuarios.get(usuarioDestino);
                                //Encaminamos la petición a dicho usuario si está conectado
                                if(userDestino.socket!=null) {
                                    userDestino.out.println(peticion);
                                }
                                
                                System.out.println("Peticion de mensaje privado de " + usuarioOrigen + " a " + usuarioDestino);
                            }
                            
                            if(cabeceraMensaje.equals(Protocolo.codigoConversacionAceptada)){
                                String loginOrigen = peticion.split("/")[1];
                                //Obtenemos el usuario a quien va dirigido el mensaje
                                Usuario userDestino = (Usuario) usuarios.get(loginOrigen);
                                //Encaminamos el mensaje a dicho usuario
                                userDestino.out.println(peticion);
                                System.out.println(peticion);
                                
                            }
                            
                            if(cabeceraMensaje.equals(Protocolo.codigoConversacionRechazada)) {
                                String loginOrigen = peticion.split("/")[1];
                                Usuario userDestino = (Usuario) usuarios.get(loginOrigen);
                                
                                userDestino.out.println(peticion);
                            }
                            
                            if(cabeceraMensaje.equals(Protocolo.codigoConversacion)) {
                                
                                String loginDestino = peticion.split("/")[2];
                                
                                Usuario userDestino = (Usuario) usuarios.get(loginDestino);
                                userDestino.out.println(peticion);
                            }
                            
                            //Si un usuario pide entrar en sala
                            //Añadirlo al mapa de usuarios de la sala pedida
                            if(cabeceraMensaje.equals(Protocolo.codigoEntrar)) {
                                String sala = peticion.split("/")[2];
                                if (salas.containsKey(sala)) { //Si la sala existe
                                    
                                    String loginUsuario = peticion.split("/")[1];
                                    Usuario usuario =  (Usuario) usuarios.get(loginUsuario);
                                    
                                    //Si el usuario está en la sala no se le añade
                                    if (!salaGeneral.usuariosEnSala.containsKey(loginUsuario)) {
                                        System.out.println(loginUsuario);
                                        salaGeneral.usuariosEnSala.put(loginUsuario, usuario);
                                    
                                        usuario.out.println(Protocolo.mensajeEntradaSalaAceptada(loginUsuario,sala));
                                        System.out.println("Usuario "+loginUsuario+"ha entrado en salaGeneral");
                                        
                                        //Se informa al resto de usuarios, si hay, que alguien ha entrado en la sala
                                        String mensajeSalida = Protocolo.mensajeUsuarioDentro(loginUsuario);
                                        if (salaGeneral.usuariosEnSala.size() >= 1) { //Si no hay usuarios en la sala no tiene sentido informar
                                            //Guarda un set de todos los usuarios en sala
                                            Set<Map.Entry<String, Usuario>> entrySet = salaGeneral.usuariosEnSala.entrySet();
                                            //Envia el mensaje de salida a todos
                                            for (Map.Entry<String, Usuario> entry : entrySet) {
                                                Usuario user = entry.getValue();
                                                user.out.println(mensajeSalida);
                                            }
                                        }
                                    }
                                }
                            }
                            
                            if(cabeceraMensaje.equals(Protocolo.codigoConversacionSala)) {
                                String sala = peticion.split("/")[2];
                                
                                //Identificar que sala es
                                SalaConversacion salaDestino = (SalaConversacion) salas.get(sala);
                                System.out.println(salaDestino.usuariosEnSala.size());
                                //Enviar mensaje a cada usuario de la salaDestino
                                //Guarda un set de todos los usuarios en sala
                                Set<Map.Entry<String, Usuario>> entrySet = salaDestino.usuariosEnSala.entrySet();
                                //Por cada usuario en el set, reenvia el mensaje
                                for(Map.Entry<String, Usuario> entry : entrySet) {
                                    Usuario usuario = entry.getValue();
                                    usuario.out.println(peticion);
                                    System.out.println(peticion);
                                }
                            }
                            //Si el usuario cierra al ventana de la sala, se le saca de la sala.
                            if(cabeceraMensaje.equals(Protocolo.codigoSalirSala)) {
                                String usuario = peticion.split("/")[1];
                                String sala = peticion.split("/")[2];
                                //Se identifica en qué sala estaba
                                SalaConversacion s = (SalaConversacion) salas.get(sala);
                                //Se le saca de esa sala
                                s.usuariosEnSala.remove(usuario);
                                System.out.println("El usuario " + usuario + " ha salido de la sala "+sala);
                                //Informar al resto de que ha salido un usuario
                                String mensajeSalida = Protocolo.mensajeUsuarioFuera(usuario);
                                if(s.usuariosEnSala.size()>=1) { //Si no hay usuarios en la sala no tiene sentido informar
                                    //Guarda un set de todos los usuarios en sala
                                    Set<Map.Entry<String, Usuario>> entrySet = s.usuariosEnSala.entrySet();
                                    //Envia el mensaje de salida a todos
                                    for(Map.Entry<String, Usuario> entry : entrySet) {
                                    Usuario user = entry.getValue();
                                    user.out.println(mensajeSalida);
                                    }
                                }
                                
                            }
                            
                            if(cabeceraMensaje.equals(Protocolo.codigoAdios)) {
                                String loginDestino = peticion.split("/")[2];
                                
                                Usuario userDestino = (Usuario) usuarios.get(loginDestino);
                                userDestino.out.println(peticion);
                            }
                            //Si cierra sesion
                            if(cabeceraMensaje.equals(Protocolo.codigoCerrarSesion)) {
                                String usuario = peticion.split("/")[1];
                                //Cerramos el socket del usuario que se va
                                Usuario user = (Usuario) usuarios.get(usuario);
                                user.socket.close();
                                
                                
                                //salimos de la hebra
                                salir = true;
                                estado = 0;
                                socket.close();
                                System.out.println("El usuario " + usuario + " ha cerrado sesión");
                            }
                            
                        break;
                        
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }while(!salir);
            
            
        }
        
    }
