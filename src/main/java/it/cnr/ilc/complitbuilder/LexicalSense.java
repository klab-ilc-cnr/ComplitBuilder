/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

/**
 *
 * @author Simone Marchi
 */
public class LexicalSense extends Metadata{
    
    private String definition;
    private String example;
    private String comment;
    
        //TODO
    //List<Relation> relations;
    //Direct o Indirect

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
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
