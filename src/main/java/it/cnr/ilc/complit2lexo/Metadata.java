/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

/**
 *
 * @author Simone Marchi
 */
public abstract class Metadata {
    
    String lexo_id; //id generato dai metodi di creazione invocati su LexO
    
    //data creazione = timestamp
    String creation;
    //creator = "bot"
    String creator;
    //last update = timestamp
    String lastUpdate;
    
}
