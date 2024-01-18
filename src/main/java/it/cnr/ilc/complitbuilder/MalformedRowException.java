/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

/**
 *
 * @author Simone Marchi
 */
public class MalformedRowException extends Exception {

    public MalformedRowException(String string) {
    }

    public MalformedRowException(Exception e) {
        super(e);
    }
    
}
