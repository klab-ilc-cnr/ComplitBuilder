/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Simone Marchi
 */
@ToString(includeFieldNames = false)
public class Trait {

    //coppia chiave-valore per un tratto
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Trait() {
    }

    public Trait(String conllTrait) throws Exception {
        if ((conllTrait != null) && (conllTrait.contains("="))) {
            String[] t = conllTrait.split("=");
            this.name = t[0].split(":")[1];
            this.value = t[1].split(":")[1];
            //workaround per i verbFormMood

            if (this.name.equals("mood")) {//&& !this.value.matches("(imperative|indicative|subjuctive)")) {
                this.name = "verbFormMood";
            }
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("name", name)
                .append("value", value)
                .toString();
    }
}
