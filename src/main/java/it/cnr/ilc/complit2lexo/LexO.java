/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package it.cnr.ilc.complit2lexo;

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

    private static final String baseurl = "http://licodemo.ilc.cnr.it:8080/LexO-backend-simone_test/service/";
    private static final String author = "author=importer";
    private static final String prefix = "prefix=lex";
    private static final String baseIRI = "baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23";

    public static void main(String[] args) throws IOException, InterruptedException {
        // get("http://licodemo.ilc.cnr.it:8080/LexO-backend-simone_test/service/lexicon/data/languages");

        //createLexicalEntry();
        //post();
    }
//https://mkyong.com/java/java-11-httpclient-examples/

    public static String createLexicalEntry() {
        return createEntry("lexicalEntry", author, prefix, baseIRI);
        //https://klab.ilc.cnr.it/maia-compl-it-be/maia/lexo/lexicon/creation/lexicalEntry?author=simone.marchi&prefix=lex&baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23
    }

    public static String createForm(String lexicalEntryId) {
        return createEntry("form", author, prefix, baseIRI, lexicalEntryId);
        //https://klab.ilc.cnr.it/maia-compl-it-be/maia/lexo/lexicon/creation/lexicalEntry?author=simone.marchi&prefix=lex&baseIRI=http%3A%2F%2Flexica%2Fmylexicon%23
    }

    private static String createEntry(String typeOfEntity, String author, String prefix, String baseIRI) {
        return createEntry(typeOfEntity, author, prefix, baseIRI, null);
    }

    private static String createEntry(String typeOfEntity, String author, String prefix, String baseIRI, String id) {
        String entryID = null;

        try {
            HttpResponse<String> response = get(baseurl + "lexicon/creation/" + typeOfEntity + "?"
                    + (id != null ? "lexicalEntryID=" + URLEncoder.encode(id, StandardCharsets.UTF_8.toString()) + "&" : "")
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
                    logger.error("Error creating {} with id {}", typeOfEntity, id);
                }
            }
        } catch (IOException | InterruptedException ex) {
            logger.error(ex.getLocalizedMessage());
        }

        return entryID;
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

    //https://klab.ilc.cnr.it/maia-demo-simone-be/maia/lexo/lexicon/update/lexicalEntry?user=simone&id=http%3A%2F%2Flexica%2Fmylexicon%23LexO_2023-11-0918_42_10_954
    public static void setLexicalEntryLabel(String user, String id, String label) throws IOException, InterruptedException {
//{"relation":"http://www.w3.org/2000/01/rdf-schema#label","value":"pippo"}
        updateLexicalEntry(user, id, "http://www.w3.org/2000/01/rdf-schema#label", label);
    }

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
    private static void updateRelationValueByEntityType(String user, String id,
            String entityType, String type, String relation, String value) throws IOException, InterruptedException {
        String uri = baseurl
                + "lexicon/update/" + entityType + "?"
                + "user=" + user
                + "&id=" + URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
        Map<String, String> data = new HashMap<>();
        if (type != null) {
            data.put("type", type);
        }
        data.put("relation", relation);
        data.put("value", value);
        data.put("currentValue", "");

        post(uri, data);
    }

    private static HttpResponse<String> get(String uri) throws IOException, InterruptedException {

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
        logger.warn("GET Execution time: {}", timeElapsed);
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
            logger.error("Error in POST {}", response.body());
        }
        logger.debug("response: " + response.statusCode());
        logger.debug("response: " + response.body());
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        logger.warn("POST Execution time: {}", timeElapsed);
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
