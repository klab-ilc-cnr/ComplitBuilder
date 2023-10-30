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

    public String getLexo_id() {
        return lexo_id;
    }

    public void setLexo_id(String lexo_id) {
        this.lexo_id = lexo_id;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
}
