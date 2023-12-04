/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

import java.util.List;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Simone Marchi
 */
@ToString
public class Form extends Metadata {

    private String representation;
    @ToString.Exclude
    private String writtenRep;
    private String phoneticRep;

    private List<Trait> traits; //morfologia

    private StringBuilder provenance = new StringBuilder();

    public String getProvenance() {
        return provenance.toString();
    }

    public void appendProvenance(String provenance) {
        this.provenance.append(provenance);
    }

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

    public void addTraits(List<Trait> traits) {
        for (Trait trait : traits) {
            if (!this.traits.contains(trait)) {
                this.traits.add(trait);
            }
        }
    }

    public Form(String creator) {
        super.addCreator(creator);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("representation", representation)
                .appendToString(traits.toString())
                .append("provenance", provenance)
                .append("\n")
                .toString();
    }

    public String getTraitsAsString() {
        StringBuilder s = new StringBuilder();
        for (Trait trait : traits) {
            s.append(trait.getName()).append("=").append(trait.getValue()).append(";");
        }
        return s.toString();
    }
}
