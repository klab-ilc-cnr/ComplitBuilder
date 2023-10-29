/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complit2lexo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Simone Marchi
 */
@ToString
public class ConllRow {

    private static Logger logger = LoggerFactory.getLogger(Complit2LexO.class);

    private String id; //0
    private String forma;//1
    private String lemma;//2
    private String pos;//3
    private String xpos;//4
    private List<Trait> traitsList;//5
    private Map<String, List<AbstractLexicoUnit>> lexicoUnits;//9

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getXpos() {
        return xpos;
    }

    public void setXpos(String xpos) {
        this.xpos = xpos;
    }

    public Map<String, List<AbstractLexicoUnit>> getLexicoUnits() {
        return lexicoUnits;
    }
    
    public  List<AbstractLexicoUnit> getLexicoUnits(String type){
        if(type != null) {
            return lexicoUnits.get(type);
        }
        return null;
    }

    private void addTrais(String traits) throws Exception {
        if (traits != null) {
            if (this.traitsList == null) {
                this.traitsList = new ArrayList<>();
                if (!traits.contains("_")) {
                    for (String trait : traits.split("\\|")) {
                        this.traitsList.add(new Trait(trait));
                    }
                }
            }
        }
    }

    public List<Trait> getTraitsList() {
        return traitsList;
    }

    private void readMisc(String field) throws Exception {
        if (field != null) {
            if (!field.contains("_")) {
                if (lexicoUnits == null)  {
                    lexicoUnits = new HashMap<>();
                }
                for (String trait : field.split("\\|")) {
                    String[] couple = trait.split("=");
                    String type = couple[0];
                    String ids = couple[1];
                    if (!lexicoUnits.containsKey(type)) {
                        lexicoUnits.put(type, new ArrayList<>());
                    }
                    for (String _id : ids.split(",")) {
                        AbstractLexicoUnit am = LexicoUnitFactory.createUnit(type, _id);
                        lexicoUnits.get(type).add(am);
                    }
//                    this.traitsList.add(new Trait(trait));
                }
            }
        }

   }

    public ConllRow(String row) throws Exception {
        if (row != null) {
            String[] fields = row.split("\t");
            this.setId(fields[0]);
            this.setForma(fields[1]);
            this.setLemma(fields[2]);
            this.setPos(fields[3]);
            try {
                this.addTrais(fields[5]);
            } catch (MalformedRowException e) {
                logger.error("Error in row: {}", e.getLocalizedMessage());
                throw new MalformedRowException(e);
            }
            this.readMisc(fields[9]);
        }
    }

}
