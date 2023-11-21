/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package it.cnr.ilc.complit2lexo;

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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
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
public class Complit2LexO {

    private static Logger logger = LoggerFactory.getLogger(Complit2LexO.class);

    public static void main(String[] args) throws Exception {
        //Per la stampa della configurazione di LogBack

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
        System.err.println("CP: " + System.getProperty("java.class.path"));

        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();
        URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(loggerContext);
        System.out.println(mainURL);
        // or even
        logger.info("Logback used '{}' as the configuration file.", mainURL);
        //File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/test-importer.conll");
        //File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/abbagliare_v.conll");
        //File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/altoforno_n.conll");
        // File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/merge.conll");

        // File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/Lexico/polacco_noun.conll");
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
        //map per la memorizzazione delle LexicalEntry create (la key è la concatenazione di Lemma e Pos)
        HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries = new HashMap<>();
        processConll(lexicalEntries, firstConnlFile);
        System.err.println("Finished first file");
        processConll(lexicalEntries, secondConnlFile);
        System.err.println("Finished second file");
        processConll(lexicalEntries, thirdConnlFile);
        System.err.println("Finished third file");
        //      Map<String, String> usemId2LexicalEntryId = extractUsemId(lexicalEntries);

        //Creazione delle relazioni tra unità semantiche
        //       createSemanticRelations(lexicalEntries, semRelHM);
        //System.err.println("lexicalentries: " + lexicalEntries);
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
                printPotentialVariants(value);
            }
        }
        writer.close();

        System.err.println("lexicalentries: " + lexicalEntries.size());
        System.exit(0);
        /**
         * 2 - sendLexicalEntriesToLexO memorizza in LexO le lexicalentry, in
         * particolare invoca il servizio di creazione e successivamente il
         * servizio di modifica per aggiungere lemma e pos.
         */
        //sendLexicalEntriesToLexO(lexicalEntries);

        //Map<String, String> semRelId2LexOId = null; //sendSemanticRelationToLexO(lexicalEntries);
        //calculateSemanticRelations(lexicalEntries, semRelId2LexOId);
    }

    public static HashMap<String, String> readSematicRelationMapping(String filename) {
        InputStream inputStream = Complit2LexO.class.getResourceAsStream("/mappingSemanticRelations.txt");

        HashMap<String, String> ret = new HashMap();
        try (BufferedReader br
                = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split("\t");
                ret.put(row[0], row[1] + row[2]);
            }
        } catch (IOException ex) {
            System.err.format("Error reading file %s\n", filename);
            System.exit(-1);
        }
        return ret;
    }

    public static HashMap<String, HashMap<String, LexicalEntry>> processConll(HashMap<String, HashMap<String, LexicalEntry>> lexicalEntries, File conll) throws Exception {

        //map per il recupero dell'ID generato da LexO di ogni USEM inserita
        Map<String, String> usemId2LexOId = new HashMap<>();

        Scanner scanner = new Scanner(conll);

        while (scanner.hasNextLine()) {
            try {
                ConllRow cr = new ConllRow(scanner.nextLine());
                logger.debug("ConllRow: " + cr.toString());
                String lexicalEntryId = cr.getLexicalEntryId(); //lemma_pos

                //hashmap delle lexical entry che hanno la stessa chiave (lemma_pos): es. polacco_noun che ha 2 MUS 
                HashMap<String, LexicalEntry> lexicalEntriesByKeyHM = null;

                if (!lexicalEntries.containsKey(lexicalEntryId)) {
                    //crea la lexical entry
                    LexicalEntry le = new LexicalEntry(cr.calculateKey(), cr.getLemma(), "it", cr.getPos());//forzata la lingua a "it" che è il nodo root in LexO

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
                    LexicalEntry le = new LexicalEntry(cr.calculateKey(), cr.getLemma(), "it", cr.getPos());
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
                    if (!cr.getForma().equals("_")) { //se la forma è _ la devo saltare
                        for (LexicalEntry le : leList) {
                            Form form = le.searchForma(cr); //cerco la forma all'interno delle forme già create 
                            if (form == null) {
                                form = new Form();
                                form.setRepresentation(cr.getForma());
                                form.setTraits(cr.getTraitsList());
                                //metto la PHU nella phoneticRepresentation per non perdere il link tra forma scritta e PHU
                                if (cr.getMiscUnits() != null && cr.getMiscUnits(Utils.PHU) != null) {
                                    form.setPhoneticRep(cr.getMiscUnits(Utils.PHU).get(0).getId()); //assunzione: per ogni forma esiste una sola phu
                                }
                                le.addForm(form);
                            } else {
                                //la forma esiste già e i tratti sono compatibili => fondo i tratti (perché
                                form.addTraits(cr.getTraitsList());
                            }
                            form.appendProvenance(cr.getId());
                            le.addLexicoUnits(cr.getMiscUnits());
                        }
                    }
                }

            } catch (MalformedRowException e) {
                System.exit(-1);
            }

        }
        scanner.close();
        return lexicalEntries;

    }

    /**
     * Da richiamare dopo che tutte le lexical entries sono state costruite
     *
     * @param lexicalEntries
     * @param usemId2LexOId
     */
    public static void calculateSemanticRelations(Map<String, LexicalEntry> lexicalEntries,
            Map<String, String> usemId2LexOId) {

//            ObjectMapper mapper = new ObjectMapper();
        for (Map.Entry<String, LexicalEntry> entry : lexicalEntries.entrySet()) {
            //String key = entry.getKey();
            LexicalEntry le = entry.getValue();
            if (le.getLexicoUnits() != null && le.getLexicoUnits().get(Utils.USEM) != null) {
                for (AbstractMiscUnit semUnit : le.getLexicoUnits().get(Utils.USEM)) {
                    String idUsem = ((SemanticUnit) semUnit).getId();
                    List<UsemRel> rels = SQLConnection.getRelByUsem(idUsem);
                    if (rels != null) {
                        logger.info(rels.toString());
                        for (UsemRel rel : rels) {
                            String usemSrc = usemId2LexOId.get(rel.getIdUsem());
                            String usemTrg = usemId2LexOId.get(rel.getIdUsemTarget());
                            String usemRel = usemId2LexOId.get(rel.getRelation());
                            //call lexo services
                        }
                    }
                }
            }
//                logger.info(mapper.writeValueAsString(value));
            //logger.info(le.toString());
        }

        logger.info("End.");
    }

    private static void sendLexicalEntriesToLexO(Map<String, LexicalEntry> lexicalEntries) throws IOException, InterruptedException {
        if (lexicalEntries != null) {
            for (Map.Entry<String, LexicalEntry> entry : lexicalEntries.entrySet()) {
                String key = entry.getKey();
                logger.warn("Starting Analyze LE {}", key);
                Instant start = Instant.now();

                LexicalEntry le = entry.getValue();
                String leId = LexO.createLexicalEntry();
                if (leId != null) {
                    le.setId(leId);
                    LexO.setLexicalEntryLanguage(le.getCreator(), le.getId(), le.getLanguage());
                    LexO.setLexicalEntryLabel(le.getCreator(), le.getId(), le.getLabel());
                    LexO.setLexicalEntryPos(le.getCreator(), le.getId(), le.getPos());

                    for (Form form : le.getForms()) {

                        String formId = LexO.createForm(leId);
                        LexO.setFormWrittenRepr(le.getCreator(), formId, form.getRepresentation());
                        for (Trait trait : form.getTraits()) {
                            LexO.addMophologicTrait(le.getCreator(), formId, trait.getName(), trait.getValue());
                            logger.debug("Trait: name {}, value {}", trait.getName(), trait.getValue());
                        }
                    }

                }
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toMillis();

                logger.warn("End analyze LE {} in {}ms", key, timeElapsed);

            }
        }
    }

    private static Map<String, String> extractUsemId(Map<String, List<LexicalEntry>> lexicalEntries) {

        int i = 0;
        Map<String, String> usem2lexicalEntryId = new HashMap<>();
        if (lexicalEntries != null) {
            for (String key : lexicalEntries.keySet()) {
                for (LexicalEntry le : lexicalEntries.get(key)) {

                    if (le.getLexicoUnits() != null && le.getLexicoUnits().get(Utils.USEM) != null) {
                        List<AbstractMiscUnit> usem = le.getLexicoUnits().get(Utils.USEM);
                        for (AbstractMiscUnit abstractLexicoUnit : usem) {
                            i++;
                            String usemId = abstractLexicoUnit.getId();
                            if (!usem2lexicalEntryId.containsKey(usemId)) {
                                if (le.getId() == null) {
                                    logger.warn("lexical entry id is {} for usemId {}", le.getId(), usemId);
                                } else {
                                    usem2lexicalEntryId.put(usemId, le.getId());
                                }
                            } else {
                                logger.error("Duplicate value for usem {}", usemId);
                            }
                        }
                    }
                }
            }
        }
        logger.warn("i is {}", i);
        return usem2lexicalEntryId;
    }

    private static void createSemanticRelations(Map<String, List<LexicalEntry>> lexicalEntries, HashMap<String, String> semRelHM) {

        for (String key : lexicalEntries.keySet()) {
            for (LexicalEntry le : lexicalEntries.get(key)) {

                if (le.getLexicoUnits() != null) {
                    List<AbstractMiscUnit> usems = le.getLexicoUnits().get(Utils.USEM);
                    if (usems != null) {
                        for (AbstractMiscUnit item : usems) {
                            SemanticUnit sem = (SemanticUnit) item;
                            //recupero tutte le ennuple da usemrel dove sem è la sorgente
                            List<UsemRel> usemrels = SQLConnection.getRelByUsem(sem.getId());
                            if (usemrels != null) {
                                for (UsemRel usemrel : usemrels) {
                                    System.err.format("Relation with %s %s %s\n", sem.getId(), usemrel.getRelation(), usemrel.getIdUsemTarget());
                                }
                            }
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
                            if (a.getRepresentation().equals(b.getRepresentation())) {
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
}


/*
Creazione di una nuova lexical entry
https://klab.ilc.cnr.it/maia-compl-it-be/LexO-backend-maia/service/lexicon/creation/lexicalEntry?author=simone.marchi&prefix=lex&baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23

Recupero di una lexical entry
https://klab.ilc.cnr.it/maia-compl-it-be/LexO-backend-maia/service/lexicon/data/elements?id=http%3A%2F%2Flexica%2Fmylexicon%23LexO_2023-10-2511_23_45_492
Response:
{
	"type": "OntoLex-Lemon model",
	"elements": [
		{
			"label": "form",
			"count": 0,
			"hasChildren": false
		},
		{
			"label": "sense",
			"count": 0,
			"hasChildren": false
		},
		{
			"label": "frame",
			"count": 0,
			"hasChildren": false
		},
		{
			"label": "lexicalConcept",
			"count": 0,
			"hasChildren": false
		},
		{
			"label": "concept",
			"count": 0,
			"hasChildren": false
		},
		{
			"label": "subterm",
			"count": 0,
			"hasChildren": false
		},
		{
			"label": "constituent",
			"count": 0,
			"hasChildren": false
		},
		{
			"label": "etymology",
			"count": 0,
			"hasChildren": false
		}
	]
}
 */
