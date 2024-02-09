package it.cnr.ilc.complitbuilder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

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

    //PREFIX
    public final static String TTLPREFIXES
            = """
              @prefix ontolex: <http://www.w3.org/ns/lemon/ontolex#> . 
              @prefix lex: <http://lexica/mylexicon#> . 
              @prefix lime: <http://www.w3.org/ns/lemon/lime#> . 
              @prefix lexinfo: <http://www.lexinfo.net/ontology/3.0/lexinfo#> . 
              @prefix dct: <http://purl.org/dc/terms/> . 
              @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . 
              @prefix vs: <http://www.w3.org/2003/06/sw-vocab-status/ns#> . 
              @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . 
              @prefix owl: <http://www.w3.org/2002/07/owl#> . 
              @prefix compl-it: <http://klab/lexicon/vocabulary/compl-it#> . 
              @prefix skos: <http://www.w3.org/2004/02/skos/core#> .
              @prefix vartrans: <http://www.w3.org/ns/lemon/vartrans#> """;
    
    //lexinfo
    public static final String LEXINFO_URL = "http://www.lexinfo.net/ontology/3.0/lexinfo#";
    public static final String LEXINFO_POS = "lexinfo:partOfSpeech";
    public static final String LSENSEEXAMPLE = "lexinfo:senseExample";

    //ontolex
    public static final String OCANONICAL_FORM = "ontolex:canonicalForm";
    public static final String OSENSE = "ontolex:sense";
    public static final String OOTHERFORM = "ontolex:otherForm";
    public static final String OFORM = "ontolex:Form";
    public static final String OLEXICAL_SENSE = "ontolex:LexicalSense";
    public static final String OWRITTEN_REP = "ontolex:writtenRep";
    
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
    public static final String COMPLIT_URL = "http://klab/lexicon/vocabulary/compl-it#";
    private static List<String> excludedCharacter = Arrays.asList("|", "%", "\u00b5", "`", "\u00b0", "\"");

    public static final String COMPL_IT_DEF = """
                                               compl-it:PolysemyAgentOfPersistentActivity-Profession rdfs:subPropertyOf  compl-it:polysemy ;
                                               rdfs:label "polysemy agent of persistent activity-profession"@en .
                                                  
                                                compl-it:adjectiveAdjective rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "adjective adjective"@en .
                                                
                                                compl-it:affectedBy  rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "affected by"@en .
                                               
                                                compl-it:affects  rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "affects"@en .
                                               
                                                compl-it:agentVerb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "agent verb"@en .
                                               
                                                compl-it:agentiveCause rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "agentive cause"@en .
                                               
                                                compl-it:agentiveExperience rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "agentive experience"@en .
                                               
                                                compl-it:agentiveProg rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "agentive prog"@en.
                                               
                                                compl-it:antonymComp rdfs:subPropertyOf compl-it:antonym  ;
                                                   rdfs:label "complementary antonym"@en .
                                               
                                                compl-it:antonymGrad rdfs:subPropertyOf compl-it:antonym ;
                                                   rdfs:label "gradable antonym"@en .
                                               
                                                compl-it:antonymMult rdfs:subPropertyOf compl-it:antonym ;
                                                   rdfs:label "multiple antonym"@en .
                                                   
                                               
                                                compl-it:beneficiaryVerb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "beneficiary verb"@en .
                                               
                                                compl-it:concerns rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "concerns"@en .
                                               
                                                compl-it:constitutiveActivity rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "constitutive activity"@en .
                                                   
                                               
                                                compl-it:contains rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "contains"@en .
                                               
                                                compl-it:createdby rdfs:subPropertyOf compl-it:artifactualAgentive ;
                                                   rdfs:label "created by"@en .
                                               
                                                compl-it:deadjectivalNoun rdfs:subPropertyOf compl-it:derivationa ;
                                                   rdfs:label "deadjectival noun"@en .
                                               
                                                compl-it:deadjectivalVerbAdjective rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "deadjectival verb adjective"@en .
                                               
                                                compl-it:denominalAdjective rdfs:subPropertyOf  compl-it:derivational ;
                                                   rdfs:label "denominal adjective"@en .
                                               
                                                compl-it:denominalVerbNoun rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "denominal verb noun"@en .
                                               
                                                compl-it:derivedFrom rdfs:subPropertyOf compl-it:artifactualAgentive ;
                                                   rdfs:label "derived From"@en .
                                               
                                                compl-it:deverbalAdjective rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "deverbal adjective"@en .
                                               
                                                compl-it:deverbalNounVerb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "deverbal noun verb"@en .
                                                   
                                                compl-it:eventVerb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "event verb"@en .
                                                  
                                                compl-it:feeling rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "feeling"@en .
                                               
                                                compl-it:hasAsColour rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "has as colour"@en .
                                               
                                                compl-it:hasAsEffect rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "has as effect"@en .
                                               
                                                compl-it:hasAsProperty rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "has as property"@en .
                                               
                                                compl-it:instrument rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "instrument"@en .
                                               
                                               
                                                compl-it:instrumentVerb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "instrument verb"@en .
                                                   
                                               
                                                compl-it:isA rdfs:subPropertyOf  compl-it:formal ;
                                                   owl:equivalentProperty lexinfo:hyponym ;
                                                   owl:inverseOf lexinfo:hypernym ;
                                                   rdfs:label "isA"@en .
                                               
                                                compl-it:isAFollowerOf rdfs:subPropertyOf compl-it:isAMemberOf ;
                                                   owl:equivalentProperty lexinfo:memberMeronym ;
                                                   owl:inverseOf compl-it:hasAsMember ;
                                                   rdfs:label "is a follower of"@en .
                                               
                                                compl-it:isIn rdfs:subPropertyOf compl-it:location  ;
                                                   rdfs:label "is in"@en .
                                               
                                                compl-it:isRegulatedBy rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "is regulated by"@en .
                                               
                                               
                                                compl-it:isTheAbilityOf rdfs:subPropertyOf compl-it:activity ;
                                                   rdfs:label "is the ability of"@en .
                                                   
                                               
                                                compl-it:isTheActivityOf rdfs:subPropertyOf compl-it:activity ;
                                                   rdfs:label "is the activity of"@en .
                                               
                                                compl-it:isTheHabitOf rdfs:subPropertyOf compl-it:activity ;
                                                   rdfs:label "is the habit of"@en .
                                               
                                                compl-it:kinship rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "kinship"@en .
                                               
                                                compl-it:livesIn rdfs:subPropertyOf compl-it:location ;
                                                   rdfs:label "lives in"@en .
                                               
                                                compl-it:madeOf rdfs:subPropertyOf compl-it:isAPartOf ;
                                                   rdfs:label "made of"@en .
                                               
                                                compl-it:measuredBy rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "measured by"@en .
                                                   
                                                compl-it:measures rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "measures"@en .
                                                   
                                                compl-it:metaphor rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "metaphor"@en .
                                                   
                                                compl-it:metonym rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "metonym"@en .
                                               
                                                compl-it:nominalization rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "nominalization"@en .
                                                   
                                                compl-it:nounNoun rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "noun noun"@en .
                                                  
                                                compl-it:nounProperNoun rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "noun proper noun"@en .
                                               
                                                compl-it:objectOfTheActivity rdfs:subPropertyOf compl-it:directTelic ;
                                                   rdfs:label "object Of the activity"@en .
                                                   
                                                compl-it:patientVerb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "patient verb"@en .
                                                    
                                                compl-it:polysemyAnimalFood rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy animal-food"@en .
                                               
                                                compl-it:polysemyAnimalMaterial rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy animal-material"@en .
                                                   
                                                compl-it:polysemyAreaHumanGroup rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy area-human group"@en .
                                               
                                                compl-it:polysemyArtifactualMaterialArtwork rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy artifactual material-artwork"@en .
                                                   
                                               
                                                compl-it:polysemyAspectualCauseAspectual rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy aspectual-cause aspectual"@en .
                                               
                                                compl-it:polysemyBuildingInstitution rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy building-institution"@en .
                                               
                                                compl-it:polysemyCauseActNonRelationalAct rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy cause act-non relational act"@en .
                                               
                                                compl-it:polysemyCauseChangeChange rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy cause change-change"@en .
                                               
                                                compl-it:polysemyChangeLocationCauseChangeLocation rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy change location-cause change location"@en .
                                               
                                                compl-it:polysemyChangeOfStateCauseChangeOfState rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy change of state-cause change of state"@en .
                                               
                                                compl-it:polysemyChangeOfValueCauseChangeOfValue rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy change of value-cause change of value"@en .
                                               
                                                compl-it:polysemyCognitiveEventExperienceEvent rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy cognitive event-experience event"@en .
                                               
                                                compl-it:polysemyConstitutiveChangeCauseConstitutiveChange rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy constitutive change-cause constitutive change"@en .
                                               
                                                compl-it:polysemyContainerAmount rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy container-amount"@en .
                                               
                                                compl-it:polysemyContainerQuantity rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy container-quantity"@en .
                                               
                                                compl-it:polysemyConventionSemioticArtifact rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy convention-semiotic artifact"@en .
                                               
                                                compl-it:polysemyDomainActivity rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy domain-activity"@en .
                                               
                                                compl-it:polysemyExperienceEventCauseExperienceEvent rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy experience event-cause experience event"@en .
                                               
                                                compl-it:polysemyHumanGroupBuilding rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy human group-building"@en .
                                               
                                                compl-it:polysemyHumanGroupGeopoliticalLocation rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy human group-geopolitical location"@en .
                                               
                                                compl-it:polysemyHumanGroupInstitution rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy human group-institution"@en .
                                               
                                                compl-it:polysemyInstitutionHumanGroup rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy institution-human group"@en .
                                               
                                                compl-it:polysemyLocationHumanGroup rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy location-human group"@en .
                                               
                                                compl-it:polysemyMoveCauseMotion rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy move-cause motion"@en .
                                               
                                                compl-it:polysemyMoveChangeOfLocation rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy move-change of location"@en .
                                               
                                                compl-it:polysemyNationalityStyle rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy nationality-style"@en .
                                               
                                                compl-it:polysemyNaturalSubstanceArtwork rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy natural substance-artwork"@en .
                                               
                                                compl-it:polysemyOpeningArtifact rdfs:subPropertyOf compl-it:polysemy ; 
                                                   rdfs:label "polysemy opening-artifact"@en .
                                               
                                                compl-it:polysemyPeopleLanguage rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy people-language"@en .
                                               
                                                compl-it:polysemyPhysicalCreationArtifact rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy physical creation-artifact"@en .
                                               
                                                compl-it:polysemyPlantArtifactualDrink rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy plant-artifactual drink"@en .
                                               
                                                compl-it:polysemyPlantFlavouring rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy plant-flavouring"@en .
                                               
                                                compl-it:polysemyPlantFlower rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy plant-flower"@en .
                                               
                                                compl-it:polysemyPlantFruit rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy plant-fruit"@en .
                                               
                                                compl-it:polysemyPlantSubstance rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy plant-substance"@en .
                                               
                                                compl-it:polysemyPlantVegetable rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy plant-vegetable"@en .
                                               
                                                compl-it:polysemyPurposeActDomain rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy purpose act-domain"@en .
                                               
                                                compl-it:polysemyRelationalChangeCauseRelationalChange rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy relational change-cause relational change"@en .
                                               
                                                compl-it:polysemySemioticArtifactInformation rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy semiotic artifact-information"@en .
                                               
                                                compl-it:polysemySoundActivityCauseSoundActivity rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy sound activity-cause sound activity"@en .
                                               
                                                compl-it:polysemyStativeLocationChangeOfLocation rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy stative location-change of location"@en .
                                               
                                                compl-it:polysemySubstanceColour rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy substance-colour"@en .
                                               
                                                compl-it:polysemyTemperatureBehaviour rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy temperature-behaviour"@en .
                                               
                                                compl-it:polysemyVegetalEntityColour rdfs:subPropertyOf compl-it:polysemy ;
                                                   rdfs:label "polysemy vegetal entity-colour"@en .
                                               
                                                compl-it:precedes rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "precedes"@en .
                                               
                                                compl-it:processVerb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "process verb"@en .
                                               
                                                compl-it:propertyOf  rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "property of"@en .
                                               
                                                compl-it:purpose rdfs:subPropertyOf compl-it:telic ;
                                                   rdfs:label "purpose"@en .
                                               
                                                compl-it:quantifies rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "quantifies"@en .
                                               
                                                compl-it:regulates rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "regulates"@en .
                                               
                                                compl-it:relatedTo rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "related to"@en .
                                               
                                                compl-it:relates rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "relates"@en .
                                               
                                                compl-it:resultingFrom rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "resulting from"@en .
                                                   
                                               
                                                compl-it:resultingState rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "resulting state"@en .
                                               
                                                compl-it:resultOf rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "result of"@en .
                                               
                                                compl-it:source rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "source"@en .
                                               
                                                compl-it:stateverb rdfs:subPropertyOf compl-it:derivational ;
                                                   rdfs:label "state verb"@en .
                                                  
                                                compl-it:successorOf rdfs:subPropertyOf  compl-it:property ;
                                                   rdfs:label "successor of"@en .
                                               
                                                compl-it:synonym rdfs:subPropertyOf vartrans:senseRel,owl:SymmetricProperty ;
                                                   owl:equivalentProperty lexinfo:approximateSynonym ;
                                                   rdfs:label "synonym"@en .
                                               
                                                compl-it:typicalLocation rdfs:subPropertyOf  compl-it:constitutive ;
                                                   rdfs:label "typical location"@en .
                                               
                                                compl-it:typicalOf rdfs:subPropertyOf  compl-it:property ;
                                                   rdfs:label "typical of"@en .
                                               
                                                compl-it:usedAgainst rdfs:subPropertyOf  compl-it:instrumental ;
                                                   rdfs:label "used against"@en .
                                               
                                                compl-it:usedAs rdfs:subPropertyOf compl-it:instrumental ;
                                                   rdfs:label "used as"@en .
                                               
                                                compl-it:usedBy rdfs:subPropertyOf  compl-it:instrumental ;
                                                   rdfs:label "used by"@en .   
                                               
                                                compl-it:usedFor rdfs:subPropertyOf  compl-it:instrumental ;
                                                   rdfs:label "used for"@en .
                                               
                                                compl-it:uses rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "uses"@en .
                                                   
                                                compl-it:causedBy rdfs:subPropertyOf  compl-it:agentive ;
                                                   owl:inverseOf  compl-it:causes ;
                                                   rdfs:label "caused by"@en .
                                                  
                                                compl-it:causes rdfs:subPropertyOf  compl-it:property ;
                                                   owl:inverseOf  compl-it:causedBy ;
                                                   rdfs:label "causes"@en .
                                                   
                                                compl-it:directTelic rdfs:subPropertyOf compl-it:telic ;
                                                   rdfs:label "direct telic"@en .
                                               
                                                compl-it:hasAsMember rdfs:subPropertyOf compl-it:constitutive ;
                                                   owl:equivalentProperty lexinfo:memberHolonym ;
                                                   owl:inverseOf  compl-it:isAMemberOf ;
                                                   rdfs:label "has as member"@en .
                                                  
                                                compl-it:hasAsPart rdfs:subPropertyOf compl-it:constitutive ;
                                                   owl:equivalentProperty lexinfo:partHolonym ;
                                                   owl:inverseOf  compl-it:isAPartOf ;
                                                   rdfs:label "has as part"@en .
                                               
                                                compl-it:producedBy rdfs:subPropertyOf compl-it:property ;
                                                   owl:inverseOf  compl-it:produces ;
                                                   rdfs:label "produced by"@en .
                                                  
                                                compl-it:produces rdfs:subPropertyOf compl-it:property ;
                                                   rdfs:label "produces"@en ;
                                                   owl:inverseOf  compl-it:producedBy .
                                                   
                                                compl-it:artifactualAgentive rdfs:subPropertyOf compl-it:agentive ;
                                                   rdfs:label "artifactual agentive"@en .
                                                   
                                               compl-it:formal rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "formal"@en .
                                                   
                                                compl-it:indirectTelic rdfs:subPropertyOf compl-it:telic ;
                                                   rdfs:label "indirect telic"@en .
                                               
                                                compl-it:isAMemberOf rdfs:subPropertyOf compl-it:constitutive ;
                                                   owl:equivalentProperty lexinfo:memberMeronym ;
                                                   owl:inverseOf compl-it:hasAMember ; 
                                                   rdfs:label "is a member of"@en .
                                               
                                                compl-it:isAPartOf rdfs:subPropertyOf compl-it:constitutive ;
                                                   owl:equivalentProperty lexinfo:partMeronym ;
                                                   owl:inverseOf compl-it:hasAsPart ;
                                                   rdfs:label "is a part of"@en .
                                                   
                                                compl-it:location rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "location"@en .
                                               
                                                compl-it:activity rdfs:subPropertyOf  compl-it:indirectTelic ;
                                                   rdfs:label "activity"@en .
                                                  
                                                compl-it:antonym rdfs:subPropertyOf compl-it:formal ;
                                                   owl:equivalentProperty lexinfo:antonym ;
                                                   rdfs:label "antonym"@en .
                                                   
                                                compl-it:telic rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "telic"@en .
                                                   
                                                compl-it:instrumental rdfs:subPropertyOf compl-it:indirectTelic ;
                                                   rdfs:label "instrumental"@en .
                                                   
                                                compl-it:agentive rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "agentive"@en .
                                                   
                                                compl-it:property rdfs:subPropertyOf compl-it:constitutive ;
                                                   rdfs:label "property"@en .
                                               
                                                compl-it:constitutive rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "constitutive"@en .
                                               
                                                compl-it:derivational rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "derivational"@en .
                                                  
                                                compl-it:polysemy rdfs:subPropertyOf vartrans:senseRel ;
                                                   rdfs:label "polysemy"@en                                                   
                                               """;
    public static String getTimestamp() {
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        return timestampFormat.format(tm);
    }

    static String normalize(String nextLine) {
        return nextLine.replaceAll("\u2019", "'");
    }

    static boolean checkRow(ConllRow cr) {
        boolean check = true;
        if (cr != null) {
            for (String ch : excludedCharacter) {
                if ((cr.getForma() != null && cr.getForma().contains(ch)) || (cr.getLemma() != null && cr.getLemma().contains(ch))) {
                    check = false;
                }
            }
        }
        return check;
    }
}
