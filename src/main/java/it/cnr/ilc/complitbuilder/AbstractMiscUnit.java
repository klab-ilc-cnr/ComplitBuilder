/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

/**
 *
 * @author Simone Marchi
 */
public abstract class AbstractMiscUnit {

    public abstract String getId();

    @Override
    public abstract boolean equals(Object obj);
    
    @Override
    public abstract int hashCode();

}