/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Simone Marchi
 */
public class Lexicon extends Metadata {

    private String description;
    private List<String> contributors;
    private String language;
    
    HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries;

    public HashMap<String, HashMap<String, LexicalEntry>> getLexicalEntries() {
        return lexicalEntries;
    }

    public void setLexicalEntries(HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries) {
        this.lexicalEntries = lexicalEntries;
    }
    
    public Lexicon(String id,  String language, String creator) {
        super.setId(id);
        super.addCreator(creator);
        this.language = language;
    }
    
    public void addContributor(String contributor) {
        if(this.contributors == null){
            contributors = new ArrayList<>();
        }
        contributors.add(contributor);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getContributors() {
        return contributors;
    }
    
    
}
