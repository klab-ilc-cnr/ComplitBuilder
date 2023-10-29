/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package it.cnr.ilc.complit2lexo;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
        //LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        //StatusPrinter.print(lc);
        //System.err.println("CP: " + System.getProperty("java.class.path"));
        processConll();
    }

    public static void test() {
        String sqlSelectAllPersons = """
                                     select distinct forms.forma as forma, 
                                           forms.naming as lemma, 
                                           forms.pos as pos, 
                                           forms.morphFeat as feats, 
                                           forms.phu as phu,
                                           forms.mus as mus, 
                                           COALESCE(GROUP_CONCAT(distinct(syntax.usyn)),'_') as syntax,
                                           COALESCE(GROUP_CONCAT(distinct(senses.sense)),'_') as sense
                                     from 
                                        (select m.idMus as mus, p.naming as forma, m.naming  , mp.pos , mp.morphFeat, p.idPhu as phu
                                           from mus m, musphu mp, phu p
                                           where mp.idMus = m.idMus 
                                           and mp.idPhu = p.idPhu and m.naming not in (
                                              select naming from lexfeature l
                                              )) as forms 
                                     left join
                                        (select syn.idUms as mus, uu.idUsem as sense
                                           from usyns syn, usynusem uu, usem sem
                                           where uu.idUsyn = syn.idUsyn 
                                           and uu.idUsem = sem.idUsem) as senses
                                     on forms.mus = senses.mus
                                     left join 
                                        (select syn.idUsyn as usyn, syn.idUms as mus
                                           from usyns syn, mus m
                                           where syn.idUms = m.idMus) as syntax
                                     on forms.mus = syntax.mus
                                     where forms.pos <> "NP"
                                     GROUP by forms.mus, forms.forma, forms.naming, forms.pos, forms.morphFeat, forms.phu""";

        String connectionUrl = "jdbc:mysql://localhost:3306/LexicO?serverTimezone=UTC";

        try (
                Connection conn = DriverManager.getConnection(connectionUrl, "root", "root"); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String forma = rs.getString("forma");
                String lemma = rs.getString("lemma");
                String pos = rs.getString("pos");
                String sense = rs.getString("sense");

                System.err.println("forma " + forma + " lemma " + lemma + " pos " + pos + " sense " + sense);
                // do something with the extracted data...
            }
        } catch (SQLException e) {
            // handle the exception
            e.printStackTrace();
        }
    }

    public static void processConll() throws Exception {

        Map<String, LexicalEntry> lexicalEntries = new HashMap<>();

        try {
            Scanner scanner = new Scanner(new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/test-importer.conll"));
            //Scanner scanner = new Scanner(new File("/home/simone/Nextcloud/PROGETTI/FormarioItalex/merge.conll"));

            while (scanner.hasNextLine()) {
                try {
                    ConllRow cr = new ConllRow(scanner.nextLine());
                    logger.debug("ConllRow: " + cr.toString());
                    String key = cr.getLemma() + "_" + cr.getPos();
                    LexicalEntry le = null;
                    if (!lexicalEntries.containsKey(key)) {
                        //crea la lexical entry
                        le = new LexicalEntry();
                        le.setLabel(cr.getLemma());
                        le.setPos(cr.getPos());
                        lexicalEntries.put(key, le);

                        logger.debug(le.toString());
                    } else {
                        le = lexicalEntries.get(key);
                    }

                    if (cr.getForma() != null) {
                        if (!le.containsForma(cr.getForma(), cr.getTraitsList())) {
                            Form newForm = new Form();
                            newForm.setRepresentation(cr.getForma());
                            newForm.setTraits(cr.getTraitsList());
                            //metto la PHU nella phoneticRepresentation per non perdere il link tra forma scritta e PHU
                            if (cr.getLexicoUnits() != null && cr.getLexicoUnits(Utils.PHU) != null ) {
                                newForm.setPhoneticRep(cr.getLexicoUnits(Utils.PHU).get(0).getId()); //assunzione: per ogni forma esiste una sola phu
                            }
                            le.addForm(newForm);
                        }
                    }
                    le.addLexicoUnits(cr.getLexicoUnits());
                } catch (MalformedRowException e) {
                    System.exit(-1);
                }

            }

            scanner.close();
//            ObjectMapper mapper = new ObjectMapper();
            for (Map.Entry<String, LexicalEntry> entry : lexicalEntries.entrySet()) {
                String key = entry.getKey();
                LexicalEntry value = entry.getValue();
//                logger.info(mapper.writeValueAsString(value));
                logger.info(value.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        logger.info("End.");
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
