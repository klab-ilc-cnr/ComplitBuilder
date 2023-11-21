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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Simone Marchi
 */
@ToString(callSuper = true)
public class LexicalEntry extends Metadata {

    private static Logger logger = LoggerFactory.getLogger(LexicalEntry.class);
    @ToString.Exclude
    private String status; //working, completato, revisionato

    private String label;// es. andare

    @ToString.Exclude
    private String language; //es. it

    @ToString.Exclude
    private String type = "http://www.w3.org/ns/lemon/ontolex#Word";
    //http://www.w3.org/ns/lemon/ontolex#MultiwordExpression nel caso di multiword

    private String pos; //es. "http://www.lexinfo.net/ontology/3.0/lexinfo#" + POS dal merge
    //Lemma

    @ToString.Exclude
    private String author; //in fase di creazione coincide con il creatore

    @ToString.Exclude
    private Form canonicalForm; //lemma

    /* The 'lexical form' property relates a lexical entry to 
     * one grammatical form variant of the lexical entry.
     */
    private List<Form> forms;

    private List<LexicalSense> senses;

    //Lexico Unit IDS
    private Map<String, List<AbstractMiscUnit>> lexicoUnits;

    //denotes
    //evokes
    //
    //morphologicalPattern
    //otherForm
    public LexicalEntry() {

        super.setCreation("");
        super.setCreator("importer");
        super.setLastUpdate("");

    }

    public LexicalEntry(String id, String label, String language, String pos) {
        super.setCreation("");
        super.setCreator("importer");
        super.setLastUpdate("");

        super.setId(id);
        this.label = label;
        this.language = language;
        this.pos = pos;
    }

    public Map<String, List<AbstractMiscUnit>> getLexicoUnits() {
        return lexicoUnits;
    }

    public void setLexicoUnits(Map<String, List<AbstractMiscUnit>> lexicoUnits) {
        this.lexicoUnits = lexicoUnits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Form getCanonicalForm() {
        return canonicalForm;
    }

    public void setCanonicalForm(Form canonicalForm) {
        this.canonicalForm = canonicalForm;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void addForm(Form form) {
        if (this.forms == null) {
            this.forms = new ArrayList<>();
        }
        this.forms.add(form);
    }

    public List<LexicalSense> getSenses() {
        return senses;
    }

    public void setSenses(List<LexicalSense> senses) {
        this.senses = senses;
    }

    public Form searchForma(ConllRow row) {
        Form ret = null;
        if (row != null) {
            String forma = row.getForma();
            List<Trait> traits = row.getTraitsList();

            List<AbstractMiscUnit> phus = row.getMiscUnits(Utils.PHU);
            String phuId = null;
            if (phus != null) {
                phuId = ((PhonologicalUnit) phus.get(0)).getId(); //la phu se esiste è unica per ogni riga del conll
            }
            if (getForms() != null) {
                for (Form form : getForms()) {
                    if (form.getRepresentation().equals(forma)) {
                        //compare traits
                        if (form.getTraits().containsAll(traits) || traits.containsAll(form.getTraits())) {
                            List<Trait> broaderTraits = form.getTraits().containsAll(traits) ? form.getTraits() : traits;
                            if (phuId != null && form.getPhoneticRep() != null && phuId.equals(form.getPhoneticRep())) {
                                logger.warn("Entry already exists: {} {}", forma, broaderTraits);
                                return form;
                            } else if (phuId == null || form.getPhoneticRep() == null) {
                                return form;
                            }
                        }
                    }
                }
            }
            logger.debug("Not found: {} {}", forma, traits.toString());
        }
        return ret;
    }

    public void addLexicoUnits(Map<String, List<AbstractMiscUnit>> units) {

        if (units != null) {
            for (String key : units.keySet()) {
                if (lexicoUnits == null) {
                    lexicoUnits = new HashMap<>();
                }
                if (!lexicoUnits.containsKey(key)) {
                    lexicoUnits.put(key, units.get(key));
                } else {
                    for (AbstractMiscUnit value : units.get(key)) {
                        if (!lexicoUnits.get(key).contains(value)) {
                            lexicoUnits.get(key).add(value);
                        }
                    }
                }
            }

        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("label", this.label)
                .append("pos", this.pos)
                .append("id", this.getId())
                .append("\n")
                .appendToString((this.getForms()!=null)?this.getForms().toString():"AAAAAAAHHHHHHHHH")
                .toString();
    }
}
