/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Servidor.Usuario;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author guillermo y nacho
 */

//Clase que define una sala de conversación más de dos personas pueden hablar a la vez
public class SalaConversacion {
    
    public String nombreSala;
    //Mapa de los usuarios que hay en una sala
    public Map usuariosEnSala;
    
    public SalaConversacion(String nombre) {
        nombreSala = nombre;
        usuariosEnSala = new HashMap<String, Usuario>();
        
    }
    
    
}
