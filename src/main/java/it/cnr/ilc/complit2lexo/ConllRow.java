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

    private String id; //0 - provenance
    private String forma;//1
    private String lemma;//2
    private String pos;//3
    private String xpos;//4
    private List<Trait> traitsList;//5
    private Map<String, List<AbstractMiscUnit>> miscUnits;//9 MISC field
    private String musId;

    public String getMusId() {
        return musId;
    }

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
        if (pos != null) {
            //pos=lexinfo:partOfSpeech=lexinfo:adjective
            try {
                this.pos = pos.split("=")[1].split(":")[1];
            } catch (NullPointerException e) {
                logger.error("Error reading pos: " + pos);
            }
        }
    }

    public String getXpos() {
        return xpos;
    }

    public void setXpos(String xpos) {
        this.xpos = xpos;
    }

    public Map<String, List<AbstractMiscUnit>> getMiscUnits() {
        return miscUnits;
    }

    public List<AbstractMiscUnit> getMiscUnits(String type) {
        if (type != null) {
            if (miscUnits != null) {
                return miscUnits.get(type);
            }
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

    public String getTraitsAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < traitsList.size(); i++) {
            Trait trait = traitsList.get(i);
            sb.append(trait.getName()).append("_").append(trait.getValue());
            if(i < traitsList.size() - 1) {
                sb.append("-");
            }
        }

        return sb.toString();
    }
    
    public String getTraitsValueAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < traitsList.size(); i++) {
            Trait trait = traitsList.get(i);
            sb.append(trait.getValue());
            if(i < traitsList.size() - 1) {
                sb.append("-");
            }
        }

        return sb.toString();
    }
    
    private void readMisc(String field) throws Exception {
        if (field != null) {
            if (!field.contains("_")) { //se MISC non e' vuoto
                if (miscUnits == null) {
                    miscUnits = new HashMap<>();
                }
                /* Esempio
                phu=PHUabbagli|mus=MUSabbagliareVERB|usyn=SYNUabbagliareV,SYNUabbagliareV2,SYNUabbagliareV3
                |usem=USem63007abbagliare,USem63008abbagliare,USem68024abbagliare,USem79709abbagliare,USem79710abbagliare
                 */
                for (String trait : field.split("\\|")) {
                    String[] couple = trait.split("=");
                    String type = couple[0];
                    String ids = couple[1];
                    if (!miscUnits.containsKey(type)) {
                        miscUnits.put(type, new ArrayList<>());
                    }
                    for (String _id : ids.split(",")) {
                        AbstractMiscUnit am = LexicoUnitFactory.createUnit(type, _id);
                        miscUnits.get(type).add(am);
                    }
//                    this.traitsList.add(new Trait(trait));
                }

            }
        }

    }

    private void calculateId() {
        //se c'è la MUS => la metto come id della lexical entry
        if (miscUnits != null && miscUnits.get(Utils.MUS) != null) {
            this.musId = miscUnits.get(Utils.MUS).get(0).getId(); //se c'è è una sola
        }
    }

    public ConllRow(String row) throws Exception {
        if (row != null) {
            String[] fields = row.split("\t");
            this.setId(fields[0]); //provenance
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
            calculateId();
        }
    }

    public String getLexicalEntryId() {
        //l'id è la MUS se esiste oppure lemma+pos 
        return getLemma() + "_" + getPos();
    }

    public String calculateKey() {
        //l'id è la MUS se esiste oppure lemma+pos 
        String lexicalEntryId = null;

        if (getMusId() != null) {
            lexicalEntryId = getMusId();
        } else {
            lexicalEntryId = getLemma() + "_" + getPos();
        }
        return lexicalEntryId;
    }

}
