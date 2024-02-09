/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.ilc.complitbuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Simone Marchi
 */
public class TTLSerializer {
/**
 * Lexicon in Turtle Format document:
 * https://docs.google.com/document/d/1llr5fe9Eq4aFF9cqH_TBpdBrh8Xg8ZkqWFFME9Z2IEk/edit#bookmark=id.z34b7lrmoi4d
 */
    /**
     * Characters to be escaped with backslashes
     */
    private static Pattern escapedCharacters = Pattern.compile("([\"'’])");

    public static void serialize(Lexicon lexicon,
            HashMap<String, List<SemRel>> sematicRelationsHM,
            String outputFilename) throws IOException, Exception {

        if (lexicon != null) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));

            HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries = lexicon.getLexicalEntries();
            if (lexicalEntries != null) {
                StringBuilder sb = new StringBuilder();

                //Prefixes        
                sb.append(Utils.TTLPREFIXES).append(" .\n");
                sb.append(Utils.COMPL_IT_DEF).append(" .\n");
                sb.append(lexiconToTurtleString(lexicon));
                for (String key : lexicalEntries.keySet()) {
                    HashMap<String, LexicalEntry> leHM = lexicalEntries.get(key);
                    for (Map.Entry<String, LexicalEntry> entry : leHM.entrySet()) {
                        //String key1 = entry.getKey();
                        LexicalEntry le = entry.getValue();
                        sb.append(Utils.TAB).append(Utils.LENTRY).append(" ").append(Utils.PLEX).append(escape(le.getId())).append(Utils.ENDROW);
                    }
                }
                sb.delete(sb.lastIndexOf(";"), sb.length()).append(" .\n");
                for (String key : lexicalEntries.keySet()) {
                    HashMap<String, LexicalEntry> leHM = lexicalEntries.get(key);
                    for (Map.Entry<String, LexicalEntry> entry : leHM.entrySet()) {
                        //String key1 = entry.getKey();
                        LexicalEntry le = entry.getValue();
                        le.calculateCanonicalForm();
                        sb.append(lexicalEntryToTurtleString(le));
                    }
                }
                for (String key : sematicRelationsHM.keySet()) {
                    List<SemRel> semRels = sematicRelationsHM.get(key);
                    for (SemRel sr : semRels) {
                        sb.append(semRelAsTurtle(sr));
                    }

                }
                //sb.delete(sb.lastIndexOf(";"), sb.length()).append(" .\n");

                // System.err.println(sb.toString());
                writer.write(sb.toString());
                writer.close();
            }
        }
    }

    private static String lexiconToTurtleString(Lexicon lex) {
        StringBuilder sb = new StringBuilder();

        sb.append(Utils.PLEX).append(escape(lex.getId())).append(" a ").append("lime:Lexicon").append(Utils.ENDROW);
        sb.append(printDCT(lex.getCreated(), lex.getCreator(), lex.getModified(), Utils.TAB));
        sb.append(Utils.TAB).append(Utils.DCTDESCRIPTION).append(" \"").append(lex.getDescription()).append("\"").append(Utils.ENDROW);
        sb.append(printContributors(lex, Utils.TAB));
        sb.append(Utils.TAB).append(Utils.LLANG).append(" ").append("\"").append(lex.getLanguage()).append("\"").append(Utils.ENDROW);
        sb.append(Utils.TAB).append(Utils.DCTLANG).append(" ").append("\"").append(lex.getLanguage()).append("\"").append(Utils.ENDROW);
        sb.append(Utils.TAB).append(Utils.LLINGCAT).append(" ").append("<").append(Utils.LEXINFO_URL).append(">").append(Utils.ENDROW);
        //sb.append(Utils.TAB).append(Utils.LLINGCAT).append(" ").append("<").append(Utils.COMPLIT_URL).append(">").append(Utils.ENDROW);
        return sb.toString();
    }

    private static String lexicalEntryToTurtleString(LexicalEntry le) {
        StringBuilder sb = new StringBuilder();

        //Lexical Entry
        sb.append(lexicalEntryAsTurtle(le));

        //Form Canonical
        sb.append(canonicalFormAsTurtle(le));

        //Other Forms
        sb.append(otherFormsAsTurtle(le));

        //Lexical Senses
        sb.append(lexicalSensesToTurtle(le));

        return sb.toString();
    }

    private static String lexicalEntryAsTurtle(LexicalEntry le) {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.PLEX).append(escape(le.getId())).append(" a ").append(Utils.WORD).append(Utils.ENDROW);
        sb.append(metaDCT(le, Utils.TAB));
        sb.append(Utils.TAB).append(Utils.TERMSTATUS).append(" \"").append(Utils.STATUS_WORKING).append("\"").append(Utils.ENDROW);
        sb.append(Utils.TAB).append(Utils.RDFSLABEL).append(" \"").append(le.getLabel()).append("\"@").append(le.getLanguage()).append(Utils.ENDROW);
        sb.append(Utils.TAB).append(Utils.LEXINFO_POS).append(" ").append(Utils.PLEXINFO).append(le.getPos()).append(Utils.ENDROW);
        sb.append(lexicalSensesIndexToTurtle(le.getLexicalSenses(), Utils.TAB));
        if (le.getCanonicalForm() != null) {
            sb.append(Utils.TAB).append(Utils.OCANONICAL_FORM).append(" ").append(Utils.PLEX).append(escape(le.getCanonicalForm().getId())).append(Utils.ENDROW);
        }
        sb.append(otherFormsIndexToTurtle(le, Utils.TAB));
        sb.delete(sb.lastIndexOf(";"), sb.length()).append(" .\n");

        return sb.toString();

    }

    private static String lexicalSensesIndexToTurtle(List<LexicalSense> lexicalSenses, String rowPrefix) {
        StringBuilder sb = new StringBuilder();
        if (lexicalSenses != null) {
            String prefix = (rowPrefix != null ? rowPrefix : "");
            for (LexicalSense ls : lexicalSenses) {
                sb.append(prefix).append(Utils.OSENSE).append(" ").append(Utils.PLEX).append(escape(ls.getId())).append(Utils.ENDROW);
            }
        }
        return sb.toString();
    }

    private static String otherFormsIndexToTurtle(LexicalEntry le, String rowPrefix) {
        StringBuilder sb = new StringBuilder();
        if (le != null) {
            if (le.getForms() != null) {
                String prefix = (rowPrefix != null ? rowPrefix : "");

                for (Form form : le.getForms()) {
                    if (!form.equals(le.getCanonicalForm())) {
                        sb.append(prefix).append(Utils.OOTHERFORM).append(" ").append(Utils.PLEX).append(escape(form.getId())).append(Utils.ENDROW);
                    }
                }
            }
        }
        return sb.toString();
    }

    private static String canonicalFormAsTurtle(LexicalEntry le) {
        StringBuilder sb = new StringBuilder();
        if (le.getCanonicalForm() != null) {
            Form cf = le.getCanonicalForm();
            sb.append(Utils.PLEX).append(escape(cf.getId())).append(" a ").append(Utils.OFORM).append(Utils.ENDROW);
            sb.append(metaDCT(cf, Utils.TAB));
            sb.append(traitsAsTurtle(cf.getTraits(), Utils.TAB));
            sb.append(Utils.TAB).append(Utils.OWRITTEN_REP).append(" \"").append(cf.getWrittenRep()).append("\"@").append(le.getLanguage()).append(Utils.ENDROW);
            sb.delete(sb.lastIndexOf(";"), sb.length()).append(" .\n");
        }

        return sb.toString();
    }

    private static String traitsAsTurtle(List<Trait> traits, String rowPrefix) {
        StringBuilder s = new StringBuilder();
        String prefix = (rowPrefix != null ? rowPrefix : "");
        for (Trait trait : traits) {
            s.append(prefix).append(Utils.PLEXINFO).append(trait.getName()).append(" ").append(Utils.PLEXINFO).append(trait.getValue()).append(";\n");
        }
        return s.toString();
    }

    private static String otherFormsAsTurtle(LexicalEntry le) {
        StringBuilder sb = new StringBuilder();
        if (le != null) {
            if (le.getForms() != null) {
                for (Form form : le.getForms()) {
                    if (!form.equals(le.getCanonicalForm())) {
                        sb.append(Utils.PLEX).append(escape(form.getId())).append(" a ").append(Utils.OFORM).append(Utils.ENDROW);
                        sb.append(metaDCT(form, Utils.TAB));
                        sb.append(traitsAsTurtle(form.getTraits(), Utils.TAB));
                        sb.append(Utils.TAB).append(Utils.OWRITTEN_REP).append(" \"").append(form.getWrittenRep()).append("\"@").append(le.getLanguage()).append(Utils.ENDROW);
                        sb.delete(sb.lastIndexOf(";"), sb.length()).append(" .\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    private static String lexicalSensesToTurtle(LexicalEntry le) {
        StringBuilder sb = new StringBuilder();
        if (le != null) {
            if (le.getLexicalSenses() != null) {
                for (LexicalSense ls : le.getLexicalSenses()) {
                    sb.append(Utils.PLEX).append(escape(ls.getId())).append(" a ").append(Utils.OLEXICAL_SENSE).append(Utils.ENDROW);
                    sb.append(metaDCT(le, Utils.TAB));
                    if (ls.getDefinition() != null) {
                        sb.append(Utils.TAB).append(Utils.SDEFINITION).append(" \"").append(escape(ls.getDefinition())).append("\"").append(Utils.ENDROW);
                    }
                    if (ls.getExample() != null) {
                        sb.append(Utils.TAB).append(Utils.LSENSEEXAMPLE).append(" \"").append(ls.getExample().replaceAll("\"", "\\\\\"")).append("\"").append(Utils.ENDROW);
                    }
                    sb.delete(sb.lastIndexOf(";"), sb.length()).append(" .\n");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Escape special character @see escapedCharacters
     * @param s
     * @return 
     */
    private static String escape(String s) {
        return escapedCharacters.matcher(s).replaceAll("\\\\$1");
    }

    private static String metaDCT(Metadata meta) {
        return metaDCT(meta, null);
    }

    private static String metaDCT(Metadata meta, String rowPrefix) {
        StringBuilder sb = new StringBuilder();
        if (meta != null) {
            sb.append(printDCT(meta.getCreated(), meta.getCreator(), meta.getModified(), rowPrefix));
        }
        return sb.toString();
    }

    private static String printDCT(String created, String creator, String modifier, String rowPrefix) {
        StringBuilder sb = new StringBuilder();
        String prefix = (rowPrefix != null ? rowPrefix : "");
        sb.append(prefix).append(Utils.DCTCREATED).append(" \"").append(created).append("\"").append(Utils.ENDROW);
        sb.append(prefix).append(Utils.DCTCREATOR).append(" \"").append(creator).append("\"").append(Utils.ENDROW);
        sb.append(prefix).append(Utils.DCTMODIFED).append(" \"").append(modifier).append("\"").append(Utils.ENDROW);
        return sb.toString();
    }

    private static String printContributors(Lexicon lex, String rowPrefix) {
        StringBuilder sb = new StringBuilder();
        String prefix = (rowPrefix != null ? rowPrefix : "");
        for (String contributor : lex.getContributors()) {
            sb.append(prefix).append(Utils.DCTCONTRIBUTOR).append(" ").append("<").append(contributor).append(">").append(Utils.ENDROW);
        }

        return sb.toString();
    }

    private static String semRelAsTurtle(SemRel sr) {
        StringBuilder sb = new StringBuilder();
        if (sr != null) {
            sb.append(Utils.PLEX).append(sr.getSource()).append(" ");
            sb.append(sr.getRelation()).append(" ");
            sb.append(Utils.PLEX).append(sr.getTarget()).append(Utils.ENDROW);
            sb.delete(sb.lastIndexOf(";"), sb.length()).append(" .\n");
        }
        return sb.toString();

    }
}
