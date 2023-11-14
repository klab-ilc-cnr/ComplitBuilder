/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

/**
 *
 * @author Simone Marchi
 */
public class LexicalSense extends Metadata{
    
    String definition;
    
    //TODO
    //List<Relation> relations;
    //Direct o Indirect
    
    
    //isLexicalizedSenseOf //OBJprop => LexicalConcept
    //LexicalEntry isSenseOf;
    //Rsource usage; // => resource

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }  
}
