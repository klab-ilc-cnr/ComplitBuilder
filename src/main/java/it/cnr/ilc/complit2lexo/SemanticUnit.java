/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

import lombok.ToString;

/**
 *
 * @author Simone Marchi
 */
@ToString
public class SemanticUnit extends AbstractLexicoUnit {

    private String id;

    public SemanticUnit(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;

        if (obj instanceof SemanticUnit) {
            SemanticUnit ptr = (SemanticUnit) obj;
            retVal = ptr.id.equals(this.id);
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 31 * (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
