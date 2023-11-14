/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package it.cnr.ilc.complit2lexo;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.core.util.StatusPrinter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
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
        File conllFile = new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/merge.conll");

        /**
         * 1 - processConll legge il conll di input per la creazione delle
         * lexicalentries ogni lexical entry ha lemma, pos, una lista di forme,
         * una lista di identificativi di USEM.
         */
        Map<String, LexicalEntry> lexicalEntries = processConll(conllFile);
        Map<String, String> usemId2LexicalEntryId = extractUsemId(lexicalEntries);

        //System.err.println("lexicalentries: " + lexicalEntries);
        System.err.println("lexicalentries: " + lexicalEntries.size());
        /**
         * 2 - sendLexicalEntriesToLexO memorizza in LexO le lexicalentry, in
         * particolare invoca il servizio di creazione e successivamente il
         * servizio di modifica per aggiungere lemma e pos.
         */
        sendLexicalEntriesToLexO(lexicalEntries);

        //Map<String, String> semRelId2LexOId = null; //sendSemanticRelationToLexO(lexicalEntries);
        //calculateSemanticRelations(lexicalEntries, semRelId2LexOId);
    }

    public static Map<String, LexicalEntry> processConll(File conll) throws Exception {

        //map per la memorizzazione delle LexicalEntry create (la key è la concatenazione di Lemma e Pos)
        Map<String, LexicalEntry> lexicalEntries = new HashMap<>();

        //map per il recupero dell'ID generato da LexO di ogni USEM inserita
        Map<String, String> usemId2LexOId = new HashMap<>();

        Scanner scanner = new Scanner(conll);

        while (scanner.hasNextLine()) {
            try {
                ConllRow cr = new ConllRow(scanner.nextLine());
                logger.debug("ConllRow: " + cr.toString());
                String key = cr.getLemma() + "_" + cr.getPos(); //cr.getMusId(); //l'id è la MUS se esiste oppure lemma+pos 
                LexicalEntry le = null;
                if (!lexicalEntries.containsKey(key)) {
                    //crea la lexical entry
                    le = new LexicalEntry();
                    le.setLabel(cr.getLemma());
                    le.setPos(cr.getPos());
                    le.setLanguage("it"); //forzata la lingua a "it" che è il nodo root in LexO
                    lexicalEntries.put(key, le);
                    logger.debug(le.toString());
                } else {
                    le = lexicalEntries.get(key);
                }
                if (cr.getMusId() != null) {
                    le.setId(cr.getMusId());
                }

                if (cr.getForma() != null) {
                    if (!le.containsForma(cr.getForma(), cr.getTraitsList())) {
                        Form newForm = new Form();
                        newForm.setRepresentation(cr.getForma());
                        newForm.setTraits(cr.getTraitsList());
                        //metto la PHU nella phoneticRepresentation per non perdere il link tra forma scritta e PHU
                        if (cr.getMiscUnits() != null && cr.getMiscUnits(Utils.PHU) != null) {
                            newForm.setPhoneticRep(cr.getMiscUnits(Utils.PHU).get(0).getId()); //assunzione: per ogni forma esiste una sola phu
                        }
                        le.addForm(newForm);
                    }
                }
                le.addLexicoUnits(cr.getMiscUnits());

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

    private static Map<String, String> extractUsemId(Map<String, LexicalEntry> lexicalEntries) {

        int i = 0;
        Map<String, String> usem2lexicalEntryId = new HashMap<>();
        if (lexicalEntries != null) {
            for (LexicalEntry le : lexicalEntries.values()) {
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
        logger.warn("i is {}", i);
        return usem2lexicalEntryId;
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
