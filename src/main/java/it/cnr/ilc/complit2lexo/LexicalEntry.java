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
public class LexicalEntry extends Metadata {

    private static Logger logger = LoggerFactory.getLogger(LexicalEntry.class);

    private String status; //working, completato, revisionato

    private String label;// es. andare
    private String language; //es. it

    private String type = "http://www.w3.org/ns/lemon/ontolex#Word";
    //http://www.w3.org/ns/lemon/ontolex#MultiwordExpression nel caso di multiword

    private String pos; //es. "http://www.lexinfo.net/ontology/3.0/lexinfo#" + POS dal merge
    //Lemma

    private String author; //in fase di creazione coincide con il creatore

    private Form canonicalForm; //lemma

    /* The 'lexical form' property relates a lexical entry to 
     * one grammatical form variant of the lexical entry.
     */
    private List<Form> forms;

    private List<LexicalSense> senses;

    //Lexico Unit IDS
    private Map<String, List<AbstractLexicoUnit>> lexicoUnits;

    //denotes
    //evokes
    //
    //morphologicalPattern
    //otherForm
    public LexicalEntry() {

        super.creation = "";
        super.creator = "importer";
        super.lastUpdate = "";

    }

    public Map<String, List<AbstractLexicoUnit>> getLexicoUnits() {
        return lexicoUnits;
    }

    public void setLexicoUnits(Map<String, List<AbstractLexicoUnit>> lexicoUnits) {
        this.lexicoUnits = lexicoUnits;
    }

    public String getLexo_id() {
        return lexo_id;
    }

    public void setLexo_id(String lexo_id) {
        this.lexo_id = lexo_id;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
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

    public boolean containsForma(String forma, List<Trait> traits) {

        if (getForms() != null) {
            for (Form form : getForms()) {
                if (form.getRepresentation().equals(forma)) {
                    //compare trais
                    if (form.getTraits().containsAll(traits)) {

                        logger.warn("Entry already exists: {} {}", forma, traits.toString());
                        return true;
                    }
                }
            }
        }
        logger.debug("Not found: {} {}", forma, traits.toString());
        return false;
    }

    public void addLexicoUnits(Map<String, List<AbstractLexicoUnit>> units) {

        if (units != null) {
            for (String key : units.keySet()) {
                if (lexicoUnits == null) {
                    lexicoUnits = new  HashMap<>();
                }
                if (!lexicoUnits.containsKey(key)) {
                    lexicoUnits.put(key, units.get(key));
                } else {
                    for (AbstractLexicoUnit value : units.get(key)) {
                        if (!lexicoUnits.get(key).contains(value)) {
                            lexicoUnits.get(key).add(value);
                        }
                    }
                }
            }

        }
    }

}
