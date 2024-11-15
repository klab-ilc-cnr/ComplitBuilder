/*
 */
package it.cnr.ilc.complitbuilder;

import lombok.ToString;

/**
 *
 * @author Simone Marchi
 */
@ToString
public class MorphologicalUnit extends AbstractMiscUnit {

    //idMus, naming, pos, ginp
    private String id;

    public MorphologicalUnit(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;

        if (obj instanceof MorphologicalUnit) {
            MorphologicalUnit ptr = (MorphologicalUnit) obj;
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
