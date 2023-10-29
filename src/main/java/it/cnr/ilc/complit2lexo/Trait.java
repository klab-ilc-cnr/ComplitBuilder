/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;

/**
 *
 * @author Simone Marchi
 */
@ToString
public class Trait {

    //coppia chiave-valore per un tratto
    @JsonKey
    String name;
    
    @JsonValue
    String value;

    public Trait() {
    }

    public Trait(String conllTrait) throws Exception {
        if ((conllTrait != null) && (conllTrait.contains("="))) {
            String[] t = conllTrait.split("=");
            this.name = t[0];
            this.value = t[1];
        } else {
            throw new MalformedRowException("This is NOT conll trait: " + conllTrait);
        }

    }

    /*
     * per implementare un deep compare per il metodo containsAll
     */
    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;

        if (obj instanceof Trait) {
            Trait ptr = (Trait) obj;
            retVal = ptr.name.equals(this.name) && ptr.value.equals(this.value);
        }

        return retVal;
    }

    /*
     * per implementare un deep compare per il metodo containsAll
     */
    @Override
    public int hashCode() {
        int hash = 31 * 31 * (this.name != null ? this.name.hashCode() : 0) + 31 * (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
