/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocolo;

/**
 *
 * @author guillermo y nacho
 */

//Clase que implemente los mensajes usados en el protocolo del chat
public class Protocolo {
    
    
    public final static String mensajeOK = "OK";
    public final static String mensajeNOK = "NOK";
    public final static String codigoLogin = "enviaLogin";
    public final static String codigoPeticion = "peticionConversacion";
    
    public final static String codigoConversacionAceptada = "conversacionAceptada";
    public final static String codigoConversacionRechazada = "conversacionRechazada";
    public final static String codigoConversacion = "enviarMensaje";
    public final static String codigoEntrar = "entrarSala";
    public final static String codigoSalaAceptada ="entradaSalaAceptada";
    public final static String codigoConversacionSala = "enviarMensajeSala";
    public final static String codigoSalirSala = "salirSala";
    public final static String codigoUsuarioFuera = "usuarioFuera";
    public final static String codigoUsuarioDentro = "usuarioDentro";
    public final static String codigoAdios = "usuarioAdios";
    public final static String codigoCerrarSesion = "cerrarSesion";
    
    public static String mensajeLoginYPass(String login, String pass) {
        String mensaje = codigoLogin + "/" + login + "/" + pass;
        return mensaje;
    }
    
    public static String mensajePedirConversacion(String loginOrigen, String loginDestino) {
        String mensaje = codigoPeticion + "/" + loginOrigen + "/" + loginDestino;
        return mensaje;
    }
    
    public static String mensajeConversacionAceptada(String loginOrigen, String loginDestino) {
        String mensaje = codigoConversacionAceptada + "/" + loginOrigen + "/" + loginDestino;
        return mensaje;
    }
    
    public static String mensajeConversacionRechazada(String loginOrigen, String loginDestino) {
        String mensaje = codigoConversacionRechazada + "/" + loginOrigen + "/" + loginDestino;
        return mensaje;
    }
    
    public static String mensajeConversacion(String loginOrigen, String loginDestino, String texto) {
        String mensaje = codigoConversacion + "/" + loginOrigen + "/" + loginDestino + "/" + texto;
        return mensaje;
    }
    
    public static String mensajeEntrarSala(String login, String sala) {
        String mensaje = codigoEntrar + "/" + login + "/" + sala;
        return mensaje;
    }
    
    public static String mensajeEntradaSalaAceptada (String login, String sala) {
        String mensaje = codigoSalaAceptada + "/" + login + "/"  + sala;
        return mensaje;
    }
    
    public static String mensajeConversacionSala(String login, String sala, String texto) {
        String mensaje = codigoConversacionSala + "/" + login + "/" + sala + "/" + texto;
        return mensaje;
    }
    
    public static String mensajeSalirSala(String login, String sala) {
        String mensaje = codigoSalirSala + "/" + login + "/" + sala;
        return mensaje;
    }
    
    public static String mensajeUsuarioFuera(String login) {
        String mensaje = codigoUsuarioFuera + "/" + login;
        return mensaje;
    }
    
    public static String mensajeUsuarioDentro(String login) {
        String mensaje = codigoUsuarioDentro + "/" + login;
        return mensaje;
    }
    
    public static String mensajeUsuarioAdios(String loginOrigen, String loginDestino) {
        String mensaje = codigoAdios +"/" + loginOrigen + "/" + loginDestino;
        return mensaje;
    }
    
    public static String mensajeCerrarSesion(String login) {
        String mensaje = codigoCerrarSesion + "/" + login;
        return mensaje;
    }
}
