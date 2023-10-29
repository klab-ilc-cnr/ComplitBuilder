/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.List;
import lombok.ToString;

/**
 *
 * @author Simone Marchi
 */
@ToString
public class Form extends Metadata {

    private String representation;
    private String writtenRep;
    private String phoneticRep;
    
    List<Trait> traits; //morfologia
    
    @JsonGetter 
    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public String getWrittenRep() {
        return writtenRep;
    }

    public void setWrittenRep(String writtenRep) {
        this.writtenRep = writtenRep;
    }

    public String getPhoneticRep() {
        return phoneticRep;
    }

    public void setPhoneticRep(String phoneticRep) {
        this.phoneticRep = phoneticRep;
    }
  
    
    public List<Trait> getTraits() {
        return traits;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }
    
    
    
}
