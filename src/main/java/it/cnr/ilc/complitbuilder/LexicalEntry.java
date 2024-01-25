/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

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

    private String label;// Lemma, es. andare

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

    private List<LexicalSense> lexicalSenses;

    //Lexico Unit IDS
    private Map<String, List<AbstractMiscUnit>> lexicoUnits;

    //denotes
    //evokes
    //
    //morphologicalPattern
    //otherForm
    public LexicalEntry(String id, String label, String language, String pos, String creator) {
        super.setId(id);
        super.addCreator(creator);
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

    public List<LexicalSense> getLexicalSenses() {
        return lexicalSenses;
    }

    public void addLexicalSenses(LexicalSense ls) {
        if (this.lexicalSenses == null) {
            this.lexicalSenses = new ArrayList<>();
        }
        this.lexicalSenses.add(ls);
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
                    if (form.getWrittenRep().equals(forma)) {
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
                .appendToString((this.getForms() != null) ? this.getForms().toString() : "No form!")
                .toString();
    }

    private Form findFormByGenderNumber(String gender, String number) throws Exception {
        Form selectedForm = null;
        if (forms != null) {
            for (Form form : forms) {
                boolean genderFound = false;
                boolean numberFound = false;
                for (Trait trait : form.getTraits()) {
                    if (trait.getName().equals("gender") && trait.getValue().equals(gender)) {
                        genderFound = true;
                    } else if (trait.getName().equals("number") && trait.getValue().equals(number)) {
                        numberFound = true;
                    }
                }
                if (genderFound && numberFound) {
                    selectedForm = form;
                    break;
                } else if (selectedForm == null) {
                    if (genderFound || numberFound) {
                    selectedForm = form;
                    } else if (form.getTraits() == null) {
                        selectedForm = form;
                        logger.error(String.format("Forma senza tratti! la uso come canonicalForm in mancanza di altro %s", selectedForm));
                    }
                } else {
                    //logger.error(String.format("selectedForm already present %s vs %s", selectedForm, form));
                    //throw new Exception();
                }
            }

        }

        return selectedForm;
    }

    private Form findFormByInfinitive() {
        if (forms != null) {
            for (Form form : forms) {
                for (Trait trait : form.getTraits()) {
                    if (trait.getName().equals(Utils.VERBMOOD) && trait.getValue().equals(Utils.INFINITIVE)) {
                        return form;
                    }
                }
            }
        }
        return null;
    }

    public void calculateCanonicalForm() throws Exception {
        //elezione della canonical form
        //se NOUN/ADJ => 1. maschile singolare, 2. femminile singolare, 3. maschile plurale, 4. femminile plurale
        //se VERB => 1. infinito (e se non fosse presente??? ne prendo uno a caso ad esempio la prima forma nella lista?)
        if (forms != null) {
            if ((pos.equals(Utils.NOUN) || pos.equals(Utils.ADJECTIVE))) {
                canonicalForm = findFormByGenderNumber(Utils.MASCULINE, Utils.SINGULAR);
                if (canonicalForm == null) {
                    canonicalForm = findFormByGenderNumber(Utils.FEMININE, Utils.SINGULAR);
                }
                if (canonicalForm == null) {
                    canonicalForm = findFormByGenderNumber(Utils.MASCULINE, Utils.PLURAL);
                }
                if (canonicalForm == null) {
                    canonicalForm = findFormByGenderNumber(Utils.FEMININE, Utils.PLURAL);
                }
            } else if (pos.equals(Utils.VERB)) {
                canonicalForm = findFormByInfinitive();
            } else { //altre pos prendo la prima forma
                canonicalForm = forms.get(0);
            }
        }
        if (canonicalForm == null) {
            logger.warn("No canonical form for " + this.getId());
        }

    }

}
