package it.cnr.ilc.complit2lexo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Simone Marchi
 */
public class Utils {

    public static final String MUS = "mus";
    public static final String PHU = "phu";
    public static final String USYN = "usyn";
    public static final String USEM = "usem";

    public static final String ENDROW = ";\n";

    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); //Conforme a quanto usato da LexO
    public static final String LEXICO = "LexicO";

    //Table usemRel
    public static final String IDUSEMTRG = "idUsemTarget";
    public static final String IDREL = "idRSem";

    //Table usem
    public static final String COMMENT = "comment";
    public static final String EXAMPLE = "exemple";
    public static final String DEFINITION = "definition";
    public static final String WORD = "ontolex:Word";

    //DCT
    public static final String DCTCREATED = "dct:created";
    public static final String DCTCREATOR = "dct:creator";
    public static final String DCTMODIFED = "dct:modified";
    public static final String DCTDESCRIPTION = "dct:description";
    public static final String DCTCONTRIBUTOR = "dct:contributor";
    public static final String DCTLANG = "dct:language";
    public static final String TERMSTATUS = "vs:term_status";
    public static final String STATUS_WORKING = "working";
    public static final String RDFSLABEL = "rdfs:label";
    public static final String ADJECTIVE = "adjective";
    public static final String NOUN = "noun";
    public static final String VERB = "verb";

    public static final String VERBMOOD = "verbFormMood";

    public static final String MASCULINE = "masculine";
    public static final String FEMININE = "feminine";
    public static final String PLURAL = "plural";
    public static final String SINGULAR = "singular";
    public static final String INFINITIVE = "infinitive";
    public static final String MOOD = "mood";

    //lexinfo
    public static final String LEXINFO_URL = "http://www.lexinfo.net/ontology/3.0/lexinfo#";
    public static final String LEXINFO_POS = "lexinfo:partOfSpeech";
    public static final String LSENSEEXAMPLE = "lexinfo:senseExample";

    //ontolex
    public static final String OCANONICAL_FORM = "ontolex:canonicalForm";
    public static final String OSENSE = "ontolex:sense";
    public static final String OOTHERFORM = "ontolex:otherForm";
    public static final String OFORM = "ontolex:form";
    public static final String OLEXICAL_SENSE = "ontolex:LexicalSense";

    //skos
    public static final String SDEFINITION = "skos:definition";

    public static final String TAB = "\t";

    //Generic Prefix
    public static final String PLEX = "lex:";
    public static final String PLEXINFO = "lexinfo:";
    
    //Lime
    public static final String LLANG = "lime:language";
    public static final String LLINGCAT = "lime:linguisticCatalog";
    public static final String LENTRY = "lime:entry";
    
    //CompL-it
    public static final String COMPLIT_URL = "http://klab/lexicon/vocabulary/complit#";
    
    public static String getTimestamp() {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        return timestampFormat.format(tm);
    }
}
