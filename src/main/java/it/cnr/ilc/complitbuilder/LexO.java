/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package it.cnr.ilc.complitbuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author simone
 */
public class LexO {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LexO.class);

    private static HttpClient client = null;

    //private static final String baseurl = "http://licodemo.ilc.cnr.it:8080/LexO-backend-simone_test/service/";
    private static final String BASEURL = "http://localhost:8080/LexO-backend-simone_test/service/";
    private static final String AUTHOR = "author=importer";
    private static final String PREFIX = "prefix=lex";
    private static final String BASEIRI = "baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23";

    public static void main(String[] args) throws IOException, InterruptedException {
        // get("http://licodemo.ilc.cnr.it:8080/LexO-backend-simone_test/service/lexicon/data/languages");

        //createLexicalEntry();
        //post();
    }
//https://mkyong.com/java/java-11-httpclient-examples/

    public static String createLexicalEntry(String id) {
        return createEntry("lexicalEntry", AUTHOR, PREFIX, BASEIRI, null, id);
        //https://klab.ilc.cnr.it/maia-compl-it-be/maia/lexo/lexicon/creation/lexicalEntry?author=simone.marchi&prefix=lex&baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23
    }

    public static String createLexicalSense(String lexicalEntryId, String senseId) {
        return createEntry("lexicalSense", AUTHOR, PREFIX, BASEIRI, lexicalEntryId, senseId);
        //https://klab.ilc.cnr.it/maia-compl-it-be/maia/lexo/lexicon/creation/lexicalEntry?author=simone.marchi&prefix=lex&baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23
    }

    /**
     * Creazione di una forma
     *
     * @param lexicalEntryId identificativo della lexical entry a cui la forma
     * va agganciata/riferita
     * @return
     */
    public static String createForm(String lexicalEntryId, String formId) {
        return createEntry("form", AUTHOR, PREFIX, BASEIRI, lexicalEntryId, formId);
        //https://klab.ilc.cnr.it/maia-compl-it-be/maia/lexo/lexicon/creation/lexicalEntry?author=simone.marchi&prefix=lex&baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23
    }

    private static String createEntry(String typeOfEntity, String author, String prefix, String baseIRI) {
        return createEntry(typeOfEntity, author, prefix, baseIRI, null, null);
    }

    private static String createEntry(String typeOfEntity, String author, String prefix, String baseIRI, String lexicalEntryId, String desiredId) {
        String entryID = null;

        try {
            HttpResponse<String> response = get(BASEURL + "lexicon/creation/" + typeOfEntity + "?"
                    + (lexicalEntryId != null ? "lexicalEntryID=" + URLEncoder.encode(lexicalEntryId, StandardCharsets.UTF_8.toString()) + "&" : "")
                    + (desiredId != null ? "desiredID=" + URLEncoder.encode(desiredId, StandardCharsets.UTF_8.toString()) + "&" : "")
                    + author + "&"
                    + prefix + "&"
                    + baseIRI); // + author=simone.marchi&prefix=lex&baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23");
            if (response != null) {
                if (response.statusCode() == 200) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(response.body());
                    JsonNode idNode = node.path(typeOfEntity);
                    entryID = idNode.asText();
                } else {
                    logger.error("Error creating {}: {}", typeOfEntity, response.body());
                }
            }
        } catch (IOException | InterruptedException ex) {
            logger.error(ex.getLocalizedMessage());
        }
        return entryID;
    }

    //POST /maia/lexo/lexicon/update/linguisticRelation?id=http%3A%2F%2Frut%2Fsomali%2Fferrandi%23le_2ewUDaqL65fx6vdqJow4wa_noun_sense1 
    public static void createSemanticRelation(String user, String subject, String relation, String object) throws IOException, InterruptedException {
        /*{"type":"senseRel",
        "relation":"http://www.lexinfo.net/ontology/3.0/lexinfo#approximate",
        "currentValue":"",
        "value":"http://rut/somali/ferrandi#le_2ewUDaqL65fx6vdqJow4wa_noun_sense1"}
         */
        String type = "senseRel";
        String entityType = "linguisticRelation";

        updateRelationValueByEntityType(user, subject, entityType, type, relation, object);

    }

    //https://klab.ilc.cnr.it/maia-demo-simone-be/maia/lexo/lexicon/update/form?id=http%3A%2F%2Flexica%2Fmylexicon%23LexO_2023-11-0919_40_06_586&user=simone
    public static void setFormWrittenRepr(String user, String formId, String label) throws IOException, InterruptedException {
//{"relation":"http://www.w3.org/ns/lemon/ontolex#writtenRep","value":"laforma"}
        updateForm(user, formId, "http://www.w3.org/ns/lemon/ontolex#writtenRep", label);
    }

    //https://klab.ilc.cnr.it/maia-demo-simone-be/maia/lexo/lexicon/update/linguisticRelation?id=http%3A%2F%2Flexica%2Fmylexicon%23LexO_2023-11-1012_03_34_465
    //{"type":"morphology","relation":"http://www.lexinfo.net/ontology/3.0/lexinfo#number","value":"http://www.lexinfo.net/ontology/3.0/lexinfo#plural","currentValue":""}
    public static void addMophologicTrait(String user, String id,
            String relation, String value) throws IOException, InterruptedException {

        String entityType = "linguisticRelation";
        String type = "morphology";
        String lexinfoRelation = "http://www.lexinfo.net/ontology/3.0/lexinfo#" + relation;
        String lexinfoValue = "http://www.lexinfo.net/ontology/3.0/lexinfo#" + value;

        updateRelationValueByEntityType(user, id, entityType, type, lexinfoRelation, lexinfoValue);
    }

    // /lexicon/update/lexicalEntry?user=simone&id=http%3A%2F%2Flexica%2Fmylexicon%23LexO_2023-11-0918_42_10_954
    public static void setLexicalEntryLabel(String user, String id, String label) throws IOException, InterruptedException {
//{"relation":"http://www.w3.org/2000/01/rdf-schema#label","value":"pippo"}
        updateLexicalEntry(user, id, "http://www.w3.org/2000/01/rdf-schema#label", label);
    }

    // /lexicon/update/lexicalEntry?user=simone.marchi&id=http%3A%2F%2Flexica%2Fmylexicon%23LexO_2023-11-2317_11_58_433
    public static void setLexicalEntryLanguage(String user, String id, String lang) throws IOException, InterruptedException {
        //{"relation":"http://www.w3.org/ns/lemon/lime#entry","value":"it"}
        updateLexicalEntry(user, id, "http://www.w3.org/ns/lemon/lime#entry", lang);
    }

    // /lexicon/update/form
    private static void updateForm(String user, String id, String relation, String value) throws IOException, InterruptedException {

        updateRelationValueByEntityType(user, id, "form", null, relation, value);
    }

    // /lexicon/update/lexicalEntry
    private static void updateLexicalEntry(String user, String id, String relation, String value) throws IOException, InterruptedException {

        updateRelationValueByEntityType(user, id, "lexicalEntry", null, relation, value);
    }


    /* {"type":"morphology",
          "relation":"http://www.lexinfo.net/ontology/3.0/lexinfo#partOfSpeech",
          "value":"http://www.lexinfo.net/ontology/3.0/lexinfo#affixedPersonalPronoun",
          "currentValue":""}*/
    //https://klab.ilc.cnr.it/maia-demo-simone-be/maia/lexo/lexicon/update/linguisticRelation?id=http://lexica/mylexicon#LexO_2023-11-0914_46_05_04
    public static void setLexicalEntryPos(String user, String id, String pos) throws IOException, InterruptedException {

        String lexinfoPos = "http://www.lexinfo.net/ontology/3.0/lexinfo#" + pos;
        String relation = "http://www.lexinfo.net/ontology/3.0/lexinfo#partOfSpeech";
        String type = "morphology";
        String entityType = "linguisticRelation";

        updateRelationValueByEntityType(user, id, entityType, type, relation, lexinfoPos);
    }

    // /lexicon/update/
    private static void updateRelationValueByEntityType(String user, String subject,
            String entityType, String type, String relation, String object) throws IOException, InterruptedException {
        String uri = BASEURL
                + "lexicon/update/" + entityType + "?"
                + "user=" + user
                + "&id=" + URLEncoder.encode(subject, StandardCharsets.UTF_8.toString());
        Map<String, String> data = new HashMap<>();
        if (type != null) {
            data.put("type", type);
        }
        data.put("relation", relation);
        data.put("value", object);
        data.put("currentValue", "");

        post(uri, data);
    }

    private static HttpResponse<String> get(String uri) throws IOException, InterruptedException {

        logger.info("GET uri: " + uri);
        Instant start = Instant.now();
        if (client == null) {
            client = HttpClient.newHttpClient();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET() // GET is default
                .setHeader("accept", "application/json")
                .build();

        logger.info("GET request: " + request.headers());

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            //logger.error("Error in GET for url {} with data: {} => {}", uri, data, response.body());
            logger.error("Error in GET {}", response.body());
        }
        logger.info("GET response: " + response.statusCode());
        logger.debug("GET response: " + response.body());
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        logger.warn("GET Execution time: {}ms", timeElapsed);
        return response;
    }

    private static HttpResponse<String> post(String uri, Map<String, String> data) throws IOException, InterruptedException {
        Instant start = Instant.now();

        if (client == null) {
            client = HttpClient.newHttpClient();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(map2Json(data))
                .setHeader("Content-Type", "application/json") // add request header
                //.header("Content-Type", "application/json")
                .build();

        logger.debug("request: " + request.headers());

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            //logger.error("Error in POST for url {} with data: {} => {}", uri, data, response.body());
            logger.error("Error in POST: {}", response.body());
        }
        logger.debug("response: " + response.statusCode());
        logger.debug("response: " + response.body());
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        logger.warn("POST Execution time: {}ms", timeElapsed);
        return response;
    }

    private static String map2Params(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return builder.toString();
    }

    private static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {

        return HttpRequest.BodyPublishers.ofString(map2Params(data));
    }

    private static HttpRequest.BodyPublisher map2Json(Map<String, String> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);
        return HttpRequest.BodyPublishers.ofString(jsonData);
    }
}
