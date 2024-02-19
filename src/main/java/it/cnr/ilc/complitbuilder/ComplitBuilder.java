/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package it.cnr.ilc.complitbuilder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.core.util.StatusPrinter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import static java.util.Comparator.nullsLast;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Simone Marchi
 */
public class ComplitBuilder {

    private static Logger logger = LoggerFactory.getLogger(ComplitBuilder.class);

    public static void main(String[] args) throws Exception {
        //Per la stampa della configurazione di LogBack
/*
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
        System.err.println("CP: " + System.getProperty("java.class.path"));

        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();
        URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(loggerContext);
        System.out.println(mainURL);
        // or even
        logger.info("Logback used '{}' as the configuration file.", mainURL);
         */

 /* Test input file */
        //File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/test-importer.conll");
        //File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/abbagliare_v.conll");
        //File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/altoforno_n.conll");
        // File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/merge.conll");
        //File firstConnlFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/Lexico/polacco_noun.conll");
        //File firstConnlFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/Lexico/distaccare.conll");
        //File firstConnlFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/Lexico/a.conll");
        //File firstConnlFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/Lexico/fails.conll");
        //File firstConnlFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/Lexico/orfanoADJ.conll");
        //File firstConnlFile = new File("/home/simone/Nextcloud/PROGETTI/UD/sudorientale.conll");

        /* Release input file */
        File firstConnlFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/Lexico/lexico_lexinfo.conll");
        File secondConnlFile = new File("/home/simone/Nextcloud/PROGETTI/magic_src/formario_MAGIC.conll");
        File thirdConnlFile = new File("/home/simone/Nextcloud/PROGETTI/UD/UD_lexinfo.conll");

        //leggo il file di mapping delle relazioni semantiche
        HashMap<String, String> semRelHM = readSematicRelationMapping("resources/mappingSemanticRelations.tsv");

        /**
         * 1 - processConll legge il conll di input per la creazione delle
         * lexicalentries ogni lexical entry ha lemma, pos, una lista di forme,
         * una lista di identificativi di USEM.
         */
        Lexicon lexicon = new Lexicon("compl_it", "it", "importer");
        lexicon.addContributor("https://www.ilc.cnr.it/people/flavia-sciolette/");
        lexicon.addContributor("https://www.ilc.cnr.it/people/andrea-bellandi/");
        lexicon.addContributor("https://www.ilc.cnr.it/people/emiliano-giovannetti/");
        lexicon.addContributor("https://www.ilc.cnr.it/people/simone-marchi/");
        lexicon.setDescription("The CompL-it lexicon");

        //map per la memorizzazione delle LexicalEntry create (la key è la concatenazione di Lemma e Pos)
        HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries = new HashMap<>();
        lexicon.setLexicalEntries(lexicalEntries);
        processConll(lexicalEntries, firstConnlFile, "LexicO");
        System.err.println("Finished first file");
        processConll(lexicalEntries, secondConnlFile, "M-GLF");
        System.err.println("Finished second file");
        processConll(lexicalEntries, thirdConnlFile, "TB");
        System.err.println("Finished third file");

        //printLexicalEntries(lexicalEntries);
        System.err.println("lexicalentries: " + lexicon.getLexicalEntries().size());

        /**
         * 2 - sendLexicalEntriesToLexO memorizza in LexO le lexicalentry, in
         * particolare invoca il servizio di creazione e successivamente il
         * servizio di modifica per aggiungere lemma e pos.
         */
        // sendLexicalEntriesToLexO(lexicalEntries);
        //HashMap<String, List<String>> usemId2LexicalEntryId = extractUsemId(lexicalEntries);
        //sendLexicalSenseToLexO(usemId2LexicalEntryId);
        // System.exit(0);
        extractLexicalSensesInformation(lexicon.getLexicalEntries());
        //Creazione delle relazioni tra unità semantiche
        HashMap<String, List<SemRel>> listOfSematicRelations = createSemanticRelations(lexicon.getLexicalEntries(), semRelHM);

        TTLSerializer.serialize(lexicon, listOfSematicRelations, "/home/simone/complit.ttl");
    }

    public static HashMap<String, String> readSematicRelationMapping(String filename) throws Exception {
        InputStream inputStream = ComplitBuilder.class.getResourceAsStream("/mappingSemanticRelations.txt");

        HashMap<String, String> prefixMapping = new HashMap<>();
        prefixMapping.put("lexinfo#", "lexinfo:");
        prefixMapping.put("compl-it#", "compl-it:");

        HashMap<String, String> ret = new HashMap();
        try (BufferedReader br
                = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split("\t");
                if (prefixMapping.containsKey(row[1])) {
                    String value = prefixMapping.get(row[1]) + row[2];
                    ret.put(row[0], value);
                } else{
                    throw new Exception("Key not found " + row[1]);
                }
            }
        } catch (IOException ex) {
            System.err.format("Error reading file %s\n", filename);
            System.exit(-1);
        }
        return ret;
    }

    public static void processConll(HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries,
            File conll,
            String creator) throws Exception {

        Scanner scanner = new Scanner(conll);

        while (scanner.hasNextLine()) {
            try {
                ConllRow cr = new ConllRow(Utils.normalize(scanner.nextLine()));
                if (!Utils.checkRow(cr)) {
                    logger.warn(String.format("Salto la riga %s", cr.toString()));
                    continue; //salto i token contenenti la pipe
                }
                logger.debug("ConllRow: " + cr.toString());
                String lexicalEntryId = cr.getLexicalEntryId(); //lemma_pos

                //hashmap delle lexical entry che hanno la stessa chiave (lemma_pos): es. polacco_noun che ha 2 MUS 
                HashMap<String, LexicalEntry> lexicalEntriesByKeyHM = null;

                if (!lexicalEntries.containsKey(lexicalEntryId)) {
                    //crea la lexical entry
                    LexicalEntry le = new LexicalEntry(cr.calculateKey(), cr.getLemma(), "it", cr.getPos(), creator);//forzata la lingua a "it" che è il nodo root in LexO

                    lexicalEntriesByKeyHM = new HashMap();
                    lexicalEntriesByKeyHM.put(cr.calculateKey(), le); //la chiave è la musId oppure lemma_pos (nel caso la musId non ci sia)
                    lexicalEntries.put(lexicalEntryId, lexicalEntriesByKeyHM);
                    logger.debug(le.toString());
                } else {
                    lexicalEntriesByKeyHM = lexicalEntries.get(lexicalEntryId);
                }

                //for (LexicalEntry le : lexicalEntryByKeyList) {
                List<LexicalEntry> leList = new ArrayList<>();
                if (lexicalEntriesByKeyHM.containsKey(cr.getMusId())) {
                    //devo aggiungere la forma alla lista delle forme per quella mus
                    leList.add(lexicalEntriesByKeyHM.get(cr.getMusId()));
                } else if (cr.getMusId() != null) {
                    //mus che non è contenuta nella hashmap quindi va creata una nuova entry nella hashmap
                    LexicalEntry le = new LexicalEntry(cr.calculateKey(), cr.getLemma(), "it", cr.getPos(), creator);
                    lexicalEntriesByKeyHM.put(cr.calculateKey(), le);
                    leList.add(le);
                } else if (lexicalEntriesByKeyHM.containsKey(cr.getLexicalEntryId())) {
                    //forme da aggiungere alla LE indidivuata
                    leList.add(lexicalEntriesByKeyHM.get(cr.getLexicalEntryId()));
                } else {
                    //caso di info proveniente da MAGIC da fondere con quella già presente in LexicO
                    //////// TODO /////////// VERIFICARE APPROCCIO!!!
                    leList.addAll(lexicalEntriesByKeyHM.values());//devo aggiungere le letture morfologiche a tutte le LE già create
                    // throw new Exception("Non c'è una lista di entry per la chiave " + lexicalEntryId);
                }

                //se la riga letta ha una musId ed uguale a quella che sto considerando nel for
                //oppure se la riga letta NON ha una musId
                // => aggiungo la forma se non già presente
                if (cr.getForma() != null) {
                    for (LexicalEntry le : leList) {
                        if (!cr.getForma().equals("_")) { //se la forma è _ la devo saltare
                            Form form = le.searchForma(cr); //cerco la forma all'interno delle forme già create 
                            if (form == null) {
                                form = new Form(creator);
                                form.setRepresentation(cr.getForma());
                                form.setWrittenRep(cr.getForma());
                                form.setTraits(cr.getTraitsList());
                                //metto la PHU nella phoneticRepresentation per non perdere il link tra forma scritta e PHU
                                StringBuilder id = new StringBuilder();
                                if (cr.getMiscUnits() != null && cr.getMiscUnits(Utils.PHU) != null) {
                                    form.setPhoneticRep(cr.getMiscUnits(Utils.PHU).get(0).getId()); //assunzione: per ogni forma esiste una sola phu
                                    //id.append(form.getPhoneticRep());
                                }
                                id.append(cr.getForma());
                                //Creo l'id della forma
                                StringBuilder formId = id.append("_").append(le.getId()).append("_").append(le.getPos());
                                if (!"".equals(cr.getTraitsValueAsString())) { //se non ci sono tratti evito di aggiungere _ in fondo alla stringa
                                    formId.append("_").append(cr.getTraitsValueAsString());
                                }
                                form.setId(formId.toString());
                                //form.setId(id.append("_").append(le.getId()).append("_").append(le.getPos()).append("_").append(cr.getTraitsValueAsString()).toString());
                                le.addForm(form);
                            } else {
                                //la forma esiste già e i tratti sono compatibili => fondo i tratti (perché
                                form.addTraits(cr.getTraitsList());
                                form.addCreator(creator);
                            }
                            le.addCreator(creator);//aggiungo il creator alla Lexical Entry (che se già presente non sarà aggiunto)
                            form.appendProvenance(cr.getId());
                        }
                        le.addLexicoUnits(cr.getMiscUnits());
                    }
                }

            } catch (MalformedRowException e) {
                System.exit(-1);
            }

        }
        scanner.close();
    }

    private static HashMap<String, List<SemRel>> createSemanticRelations(Map<String, HashMap<String, LexicalEntry>> lexicalEntries, HashMap<String, String> semRelHM) throws SQLException, IOException, InterruptedException {

        //hashmap dove la chiave è la idUsem e il value è la lista delle usemrel collegate a idUsem
        HashMap<String, List<SemRel>> semRels = null;
        if (lexicalEntries != null) {
            semRels = new HashMap<>();
            for (String key : lexicalEntries.keySet()) {
                HashMap<String, LexicalEntry> leHM = lexicalEntries.get(key);
                for (Map.Entry<String, LexicalEntry> entry : leHM.entrySet()) {
                    //String key1 = entry.getKey();
                    LexicalEntry le = entry.getValue();

                    if (le.getLexicoUnits() != null) {
                        List<AbstractMiscUnit> usems = le.getLexicoUnits().get(Utils.USEM);
                        if (usems != null) {
                            for (AbstractMiscUnit item : usems) {
                                SemanticUnit sem = (SemanticUnit) item;
                                //recupero tutte le ennuple da usemrel dove sem è la sorgente
                                List<UsemRel> usemrels = SQLConnection.getRelByUsem(sem.getId());
                                if (usemrels != null) {
                                    for (UsemRel usemrel : usemrels) {
                                        String rel = semRelHM.get(usemrel.getRelation());
                                        logger.info(String.format("Relation with %s %s=(%s) %s", sem.getId(),
                                                usemrel.getRelation(), rel,
                                                usemrel.getIdUsemTarget()));
                                        SemRel sr = new SemRel(); //nuova sem rel
                                        sr.setSource(usemrel.getIdUsem());
                                        sr.setRelation(rel);
                                        sr.setTarget(usemrel.getIdUsemTarget());
                                        List<SemRel> listOfSemRel;
                                        if (!semRels.containsKey(sem.getId())) {
                                            listOfSemRel = new ArrayList<>();
                                            semRels.put(sem.getId(), listOfSemRel);
                                        }
                                        listOfSemRel = semRels.get(sem.getId());
                                        listOfSemRel.add(sr);
                                    }
                                } else {
                                    logger.info(String.format("No relation for %s", sem.getId()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return semRels;
    }

    private static void sendLexicalSenseToLexO(HashMap<String, List<String>> lexicalSenses) throws IOException, InterruptedException {
        if (lexicalSenses != null) {
            for (String key : lexicalSenses.keySet()) {
                List<String> leHM = lexicalSenses.get(key); //lexical entry a cui la usem è collegata
                if (leHM.size() > 1) {
                    System.err.println("USEM: " + key);
                    for (String entry : leHM) {
                        System.err.println("\t" + entry);
                    }
                }
                System.err.println("USEM: " + key);
                for (String entry : leHM) {
                    System.err.println("\t" + entry);
                }
            }
        }
    }

    private static void sendLexicalEntriesToLexO(Map<String, HashMap<String, LexicalEntry>> lexicalEntries) throws IOException, InterruptedException {
        if (lexicalEntries != null) {
            for (String key : lexicalEntries.keySet()) {
                HashMap<String, LexicalEntry> leHM = lexicalEntries.get(key);
                for (Map.Entry<String, LexicalEntry> entry : leHM.entrySet()) {
                    //String key1 = entry.getKey();
                    LexicalEntry le = entry.getValue();
                    logger.warn("Starting Analyze LE {}", key);
                    Instant start = Instant.now();

                    String leId = LexO.createLexicalEntry(le.getId()); //(null); //
                    if (leId != null) {
                        le.setId(leId);
                        LexO.setLexicalEntryLanguage(le.getCreator(), le.getId(), le.getLanguage());
                        LexO.setLexicalEntryLabel(le.getCreator(), le.getId(), le.getLabel());
                        LexO.setLexicalEntryPos(le.getCreator(), le.getId(), le.getPos());

                        if (le.getForms() != null) {
                            for (Form form : le.getForms()) {
                                String formId = LexO.createForm(le.getId(), form.getId()); //le.getId() è l'aggancio tra la forma da creare e la lexical entry alla quale appartiene
                                LexO.setFormWrittenRepr(le.getCreator(), formId, form.getWrittenRep());
                                for (Trait trait : form.getTraits()) {
                                    LexO.addMophologicTrait(le.getCreator(), formId, trait.getName(), trait.getValue());
                                    logger.debug("Trait: name {}, value {}", trait.getName(), trait.getValue());
                                }
                            }
                        }

                    }

                    Instant finish = Instant.now();
                    long timeElapsed = Duration.between(start, finish).toMillis();

                    logger.warn("End analyze LE {} in {}ms", key, timeElapsed);

                }
            }
        }
    }

    private static void extractLexicalSensesInformation(Map<String, HashMap<String, LexicalEntry>> lexicalEntries) throws SQLException {

        int i = 0;
        if (lexicalEntries != null) {
            for (String key : lexicalEntries.keySet()) {
                HashMap<String, LexicalEntry> leHM = lexicalEntries.get(key);
                for (Map.Entry<String, LexicalEntry> entry : leHM.entrySet()) {
                    //String key1 = entry.getKey();
                    LexicalEntry le = entry.getValue();

                    if (le.getLexicoUnits() != null && le.getLexicoUnits().get(Utils.USEM) != null) {
                        List<AbstractMiscUnit> usemList = le.getLexicoUnits().get(Utils.USEM);
                        for (AbstractMiscUnit usem : usemList) { //per ogni usem associata alla lexical entry
                            i++;
                            String usemId = usem.getId();
                            //cerco le info relative alla usemId nel DB
                            LexicalSense ls = SQLConnection.getUsemInformation(usemId);
                            le.addLexicalSenses(ls);
                        }
                    }
                }
            }
        }
    }

    private static void printPotentialVariants(LexicalEntry value) {
        List<Form> forms = value.getForms();

        if (forms != null) {
            if (forms.size() >= 2) {
                String id = value.getId();
                for (int i = 0; i < forms.size(); i++) {
                    Form a = forms.get(i);
                    for (int j = i + 1; j < forms.size(); j++) {
                        Form b = forms.get(j);
                        if (a.getTraits().containsAll(b.getTraits())
                                || b.getTraits().containsAll(a.getTraits())) {
                            //sono uguali
                            if (a.getWrittenRep().equals(b.getWrittenRep())) {
                                logger.error(String.format("Duplicate: %s\n%s%s\n", id, a, b));
                            } else {
                                System.err.format("Variant: %s\n%s%s\n", id, a, b);
                            }
                        }
                    }
                }
            }

        }
    }

    private static void printLexicalEntries(HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("lexicalEntries.txt"));
        List<String> lexicalEntriesByKey = new ArrayList<>(lexicalEntries.keySet());
        Collections.sort(lexicalEntriesByKey);
        for (Iterator<String> iterator = lexicalEntriesByKey.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            //System.err.println("next: " + next);
            for (Map.Entry<String, LexicalEntry> entry : lexicalEntries.get(next).entrySet()) {
                String key = entry.getKey();
                LexicalEntry value = entry.getValue();
                // writer.write(String.format("key: %s, Value: %s\n", key, value.toString()));
                writer.write(String.format("%s\n", value.toString()));
                //printPotentialVariants(value);
            }
        }
        writer.close();
    }

}
