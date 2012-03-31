
// $Id: query-samples.js,v 1.24 2006/10/27 12:15:24 pepper Exp $

function insertExample(exName) {

  // ===== ItalianOpera.ltm =================================================

  if (exName == "exPucciniSparql") { // -------------------------------------------
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?OPERA\n' +
      'WHERE {\n' +
      '  o:Puccini o:Work ?OPERA .\n' +
      '# ?OPERA o:Composer o:Puccini .\n' +
      '}\n' +
      '\n' +
      '# The tolog query asking same thing as this SPARQL\n' +
      '# query would express the association between\n' +
      '# o:Puccini and variable ?OPERA with one association\n' +
      '# o:composed_by (please see the tolog examples).\n' +
      '\n' +
      '# In SPARQL query we have to use one of the role types\n' +
      '# as predicate to express such an association. So \n' +
      '# it might not be necessary to use both role types\n' +
      '# as predicates, but doing so we will improve the quality\n' +
      '# of results (e.g. removing false positive results).\n' +
      '# The drawback is it will increase query evaluation time.\n';
    } else if (exName == "exPucciniSortedSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?OPERA\n' +
      'WHERE {\n' +
      '  o:Puccini o:Work ?OPERA .\n' +
      '}\n' +
      'ORDER BY ASC(?OPERA)\n' +
      '\n' +
      '# Topics are sorted by name defined in \'sort\'\n' +
      '# scope or by plain topic name if the first is\n' +
      '# not available. Tolog follow this rule, but \n' +
      '# SPARQL queries always sort by plain name.\n';
    } else if (exName == "exShakespeareSparqlCorrect") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?WORK ?COMPOSER ?OPERA\n' +
      'WHERE {\n' +
      '  ?OPERA o:Composer ?COMPOSER .\n' +
      '  ?OPERA o:Source ?WORK .\n' +
      '  ?WORK o:Writer o:Shakespeare .\n' +
      '}\n' +
      'ORDER BY ASC(?COMPOSER)\n' +
      '\n' +
      '# This example illustrates how to interconnect\n' +
      '# multiple predicates. \n';
    } else if (exName == "exShakespeareSparqlBroken") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?WORK ?COMPOSER ?OPERA\n' +
      'WHERE {\n' +
      '  ?COMPOSER o:Work ?OPERA .\n' +
      '  ?WORK o:Result ?OPERA .\n' +
      '  o:Shakespeare o:Work ?WORK .\n' +
      '}\n' +
      'ORDER BY ASC(?COMPOSER)\n' +
      '\n' +
      '# Each tolog association can be translated to SPARQL\n' +
      '# in two ways - with first or second role type (or both).\n' +
      '# The previous query (\'Composers inspired by Shakespeare\n' +
      '# (Correct)\') was created by using the second role \n' +
      '# type as predicate in all three cases. This query was \n' +
      '# created by using the first role type as predicate.\n' +
      '\n' +
      '# The results of both queries are not the same. This\n' +
      '# query returns more results - some of them are correct\n' +
      '# but some of them are false positive. The reason why\n' +
      '# this query fails to return same results as the original\n' +
      '# tolog query is using o:Work role type in first predicate.\n' +
      '# Note that the previous SPARQL query used o:Composer instead.\n' +
      '\n' +
      '# The difference between o:Composer and o:Work is that o:Work\n' +
      '# is role type for many other associations while o:Composer\n' +
      '# is role type only for two association. One of the two \n' +
      '# association is our target association o:composed_by (see \n' +
      '# the tolog query). \n' +
      '\n' +
      '# o:Composer role is used in:\n' +
      '# o:completed_by, o:composed_by associations.\n' +
      '\n' +
      '# o:Work role is used in: \n' +
      '# o:completed_by, o:composed_by, o:appears_in, o:premiere, \n' +
      '# o:published_by, o:unfinished, o:written_by associations.\n';
    } else if (exName == "exShakespeareSparqlFixed") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?WORK ?COMPOSER ?OPERA\n' +
      'WHERE {\n' +
      '  ?COMPOSER o:Work ?OPERA .\n' +
      '# ?OPERA o:Composer ?COMPOSER .\n' +
      '# ?COMPOSER a o:Composer .\n' +
      '  ?WORK o:Result ?OPERA .\n' +
      '  o:Shakespeare o:Work ?WORK .\n' +
      '}\n' +
      'ORDER BY ASC(?COMPOSER)\n' +
      '\n' +
      '# This is last of three SPARQL queries that are based on \n' +
      '# one tolog query. The first query demonstrates correct\n' +
      '# results. The second demonstrates how widely used roles\n' +
      '# aren\'t suitable to be SPARQL predicates. And this query\n' +
      '# presents two ways how to remove as much false positive\n' +
      '# results as possible. Uncomment one of the following predicates\n' +
      '# to get correct results.\n' +
      '\n' +
      '# (1) ?OPERA o:Composer ?COMPOSER\n' +
      '# This one is obvious. Same predicate was used before to\n' +
      '# obtain correct results. The reason why o:Composer succeeded\n' +
      '# where o:Work failed is that o:Composer role is used only in\n' +
      '# two associations. See \'Composers inspired by Shakespeare (Broken)\'\n' +
      '# for details.\n' +
      '\n' +
      '# (2) ?COMPOSER a o:Composer\n' +
      '# The keyword \'a\' means rdf:type in SPARQL. In Topic Maps it\n' +
      '# connects topic with its topic type. It is not related to \n' +
      '# role types, except the case when topic types are used as role\n' +
      '# types. This way we can filter all topics in ?COMPOSER variable\n' +
      '# to contain only topics of o:Composer type.\n' +
      '# This predicate is strong enough to filter out all the o:Character, \n' +
      '# o:Publisher or even o:Theatre false positive topics displayed in\n' +
      '# results.\n' +
      '# WARNING: topics playing a role (e.g. o:Composer role) does not have to \n' +
      '# be of the same topic type (i.e. o:Composer type). Ignoring this\n' +
      '# may lead to incomplete results.\n' +
      '\n' +
      '# Note that both predicates above acts as filters. Even used\n' +
      '# together they might not be powerful enough to filter out\n' +
      '# all false positive results. On the other hand using all \'filters\'\n' +
      '# may be exaggerated and it will have negative impact on performance.\n';
    } else if (exName == "exBornDiedSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?PLACE ?PERSON\n' +
      'WHERE {\n' +
      '  ?PERSON o:Place ?PLACE .\n' +
      '  ?PLACE o:Person ?PERSON .\n' +
      '}\n' +
      'ORDER BY ASC(?PLACE) ASC(?PERSON)\n' +
      '\n' +
      '# The original tolog query is using o:born_in and o:died_in\n' +
      '# associations to find persons that were born and that died in\n' +
      '# the same place. Both associations are using the same two roles\n' +
      '# o:Place and o:Person. Since we can\'t use the associations\n' +
      '# itself in SPARQL query and both queries are using the same \n' +
      '# roles we can\'t get the same results as tolog query without\n' +
      '# help of magic predicates (those are not implemented at the time)\n' +
      '\n' +
      '# To avoid this situation, we would have to replace the o:Place in\n' +
      '# topic map itself with o:PlaceOfBirth and o:PlaceOfDeath for example.\n';
    } else if (exName == "exComposersSparql") {
      document.queryform.query.value =
     'PREFIX o: <http://psi.ontopedia.net/>\n' +
     'SELECT ?COMPOSER count(?OPERA)\n' +
     'WHERE {\n' +
     '  ?OPERA o:Composer ?COMPOSER .\n' +
     '}\n' +
     'ORDER BY DESC(?OPERA)\n' +
     '\n' +
     '# This query will fail to parse until Sesame\n' +
     '# implements parser that support count aggregation.\n' +
     '\n' +
     '# Remove the count function from SELECT and compare\n' +
     '# these result to tolog query edited in the same way. \n' +
     '# If the results are different from tolog results see\n' +
     '# \'Audio recordings\' for explanation.\n';
    } else if (exName == "exMeccaSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT DISTINCT ?CITY count(?OPERA)\n' +
      'WHERE {\n' +
      '  ?CITY a o:City .\n' +
      '  {\n' +
      '    ?OPERA o:Place ?CITY .\n' +
      '    ?CITY o:Work ?OPERA .\n' +
      '  }\n' +
      '  UNION\n' +
      '  {\n' +
      '    ?OPERA o:Place ?THEATRE .\n' +
      '    ?THEATRE o:Work ?OPERA .\n' +
      '    ?THEATRE o:Container ?CITY .\n' +
      '    ?CITY o:Containee ?THEATRE .\n' +
      '  }\n' +
      '}\n' +
      'ORDER BY DESC(?OPERA)\n' +
      '\n' +
      '# If query fails to parse, remove the count function from\n' +
      '# SELECT and compare these result to tolog query edited in\n' +
      '# the same way. \n' +
      '\n' +
      '# In tolog the syntax for OR operation is { A | B}. In SPARQL\n' +
      '# we use { A } UNION { B } syntax.\n' +
      '\n' +
      '# While results of tolog query are always distinct we have\n' +
      '# to use DISTINCT keyword for this SPARQL query to filter out\n' +
      '# duplicate rows.\n';
    } else if (exName == "exTheatresByPremiereSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT DISTINCT ?THEATRE count(?OPERA)\n' +
      'WHERE {\n' +
      '  ?THEATRE a o:Theatre .\n' +
      '  ?OPERA o:Place ?THEATRE .\n' +
      '  ?THEATRE o:Work ?OPERA .\n' +
      '}\n' +
      'ORDER BY DESC(?OPERA)\n' +
      '\n' +
      '# If query fails to parse, remove the count function from\n' +
      '# SELECT and compare these result to tolog query edited in\n' +
      '# the same way. \n';
    } else if (exName == "exOperasByPremiereDateSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?OPERA ?PREMIEREDATE\n' +
      'WHERE {\n' +
      '  ?OPERA a o:Opera .\n' +
      '  ?OPERA o:premiere_date ?PREMIEREDATE .\n' +
      '}\n' +
      'ORDER BY DESC(?PREMIEREDATE)\n' +
      'LIMIT 20\n' +
      '\n' +
      '# This example demonstrate matching topics with\n' +
      '# its occurences. The tolog use this syntax to\n' +
      '# match occurences:\n' +
      '# o:premiere_date($OPERA, $PREMIERE-DATE)\n' +
      '\n' +
      '# The unidirectional nature of occurences allow us\n' +
      '# to use o:premiere_date as predicate in SPARQL query.\n' +
      '# Its mandatory to put topic to subject and occurence\n' +
      '# to object of the SPARQL triple.\n' +
      '\n' +
      '# Note that tolog\'s variables may contain hyphen in\n' +
      '# its name: $PREMIERE-DATE but in SPARQL you can\'t: ?PREMIEREDATE.\n';
    } else if (exName == "exEnglishTitlesSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'PREFIX tm: <http://psi.topicmaps.org/iso13250/model/>\n' +
      'SELECT ?OPERA ?TITLE\n' +
      'WHERE {\n' +
      '  ?OPERA a o:Opera .\n' +
      '  ?OPERA tm:topic-name ?TITLE .\n' +
      '}\n' +
      'ORDER BY ASC(?OPERA)\n' +
      '\n' +
      '# The tolog counterpart to this query displays operas\n' +
      '# and their english title (if they have one).\n' +
      '\n' +
      '# The next table shows required predicates and SPARQL\n' +
      '# support for them:\n' +
      '#          instance-of     rdf:type OR a\n' +
      '#          topic-name      tm:topic-name\n' +
      '#          value           (alredy done by the predicate above)\n' +
      '#          scope           not supported in any way\n' +
      '# As you can see SPARQL query can list all titles in any language\n' +
      '# but can\'t sort them by scope or language tag. Also there is no\n' +
      '# need to map tolog\'s predicate value(A,B), because tm:topic-name\n' +
      '# already returns string form of the name.\n';
    } else if (exName == "exSuicidesSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?WORK ?SUICIDE\n' +
      'WHERE {\n' +
      '  ?SUICIDE o:Work ?WORK .\n' +
      '  ?SUICIDE o:Perpetrator ?SUICIDE .\n' +
      '  ?SUICIDE o:Victim ?SUICIDE .\n' +
      '}\n' +
      'ORDER BY ASC(?WORK)\n' +
      '\n' +
      '# This example is easy to express in SPARQL, because \n' +
      '# roles like o:Perpetrator and o:Victim are quite\n' +
      '# specific and won\'t be used in many associations.\n';
    } else if (exName == "exSettingsByCountrySparql") {
      document.queryform.query.value =
      '# The original tolog query use inference rule to find country\n' +
      '# where an opera takes place. Such a place can already be\n' +
      '# a o:Country or it may be o:City, o:Region or just non-specific topic\n' +
      '# type o:Place.\n' +
      '\n' +
      '# The inference rule is called recursively to unfold levels, for example:\n' +
      '# o:Palermo (o:City) is located in o:Sicilia (o:Region) is located in\n' +
      '# xtm:IT (o:Country). \n' +
      '\n' +
      '# On the contrary RDF does not rely on smart queries. Property such\n' +
      '# a o:located_in would rather be modeled as owl:TransitiveProperty. \n' +
      '\n' +
      '# Despite that SPARQL does not allow to define inference rules in\n' +
      '# query itself, we can replace every occurence of the rule with its\n' +
      '# definition and then translate the tolog query to SPARQL. However this\n' +
      '# is not possible for recursive inference rules where we don\'t know\n' +
      '# the maximal number of levels. And even if we know the number of levels\n' +
      '# it would be extremely laborious to disassemble the inference rule.\n';
    } else if (exName == "exNaryAriasSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT DISTINCT ?OPERA ?ARIA count(?CHARACTERS)\n' +
      'WHERE {\n' +
      '  ?ARIA o:Whole ?OPERA .\n' +
      '  ?OPERA o:Part ?ARIA .\n' +
      '  ?CHARACTERS o:Aria ?ARIA .\n' +
      '  ?ARIA o:Person ?CHARACTERS .\n' +
      '  ?CHARACTER2 o:Aria ?ARIA .\n' +
      '  ?ARIA o:Person ?CHARACTER2 .\n' +
      '  FILTER(?CHARACTERS != ?CHARACTER2)\n' +
      '}\n' +
      'ORDER BY DESC(?CHARACTERS) ASC(?OPERA)\n' +
      '\n' +
      '# If query fails to parse, remove the count function from\n' +
      '# SELECT and compare these result to tolog query edited in\n' +
      '# the same way. \n' +
      '\n' +
      '# This query introduces FILTER keyword.\n';
    } else if (exName == "exInspiredBySparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT DISTINCT ?WHO\n' +
      'WHERE {\n' +
      '  ?OPERA o:Composer o:Giuseppe_Verdi .\n' +
      '  ?OPERA o:Source ?WORK .\n' +
      '  ?WORK o:Writer ?WHO .\n' +
      '}\n' +
      'ORDER BY ASC(?WHO)\n' +
      '\n' +
      '# This is example of translating tolog query that is using\n' +
      '# inference rule to SPARQL. \n' +
      '# First step was to remove the inference rule. The result is:\n' +
      '\n' +
      '#using o for i"http://psi.ontopedia.net/"\n' +
      '#select $WHO from\n' +
      '# o:composed_by($OPERA : o:Work, o:Giuseppe_Verdi: o:Composer),\n' +
      '# o:based_on($OPERA : o:Result, $WORK : o:Source),\n' +
      '# o:written_by($WORK : o:Work, $WHO: o:Writer)\n' +
      '#order by $WHO?\n' +
      '\n' +
      '# Second step is to translate above tolog query to SPARQL. \n';
    } else if (exName == "exBibliographySparql") {
      document.queryform.query.value =
      '# PREFIX tms: <http://www.networkedplanet.com/tmsparql/>\n' +
      '#\n' +
      '# The tolog query depends on built-in predicate reifies(A, B).\n' +
      '# Such a association requires magic identifiers as defined by\n' +
      '# Networked Planet - tms:reifier.\n';
    } else if (exName == "exRecordingsSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT DISTINCT ?COMPOSER ?OPERA ?RECORDING\n' +
      'WHERE {\n' +
      '  ?OPERA o:audio_recording ?RECORDING .\n' +
      '  ?OPERA o:Composer ?COMPOSER .\n' +
      '  ?COMPOSER o:Work ?OPERA .\n' +
      '}\n' +
      'ORDER BY ?OPERA \n' +
      '\n' +
      '# This query returns the same results plus five false positives.\n' +
      '# Why? Because o:composed_by (see tolog query) uses the same\n' +
      '# role types as o:completed_by. SPARQL engine won\'t be able to\n' +
      '# distinguish between the two associations and it will add the \n' +
      '# composers who completed the opera to the result set along\n' +
      '# composers who actually composed the opera.\n' +
      '\n' +
      '# The operas with false positive results are Turandot with two\n' +
      '# false positive and  Nerone (Boito) with three false positive\n' +
      '# results.\n';
    } else if (exName == "exNoDramatisPersonaeSparql") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT DISTINCT ?COMPOSER ?OPERA\n' +
      'WHERE {\n' +
      '  ?OPERA a o:Opera .\n' +
      '  ?OPERA o:Composer ?COMPOSER .\n' +
      '  ?COMPOSER o:Work ?OPERA .\n' +
      '\n' +
      '  ?OPERAEXCLUDE a o:Opera .\n' +
      '  ?OPERAEXCLUDE o:Character ?CHAR .\n' +
      '  FILTER(?OPERA != ?OPERAEXCLUDE)\n' +
      '}\n' +
      'ORDER BY ?COMPOSER ?OPERA\n' +
      '\n' +
      '# This query illustrates how to translate not() from tolog\n' +
      '# to SPARQL. \n' +
      '# The SPARQL result contain all tolog results plus many false\n' +
      '# positive. The reason why is well described in \'Audio recordings\'\n' +
      '# example.\n';
    } else if (exName == "exNoVoiceTypeSparql") {
      document.queryform.query.value =
     'PREFIX o: <http://psi.ontopedia.net/>\n' +
     'SELECT DISTINCT ?OPERA ?CHARACTER\n' +
     'WHERE {\n' +
     '  ?OPERA a o:Opera .\n' +
     '  ?OPERA o:Character ?CHARACTER .\n' +
     '  ?CHARACTER o:Work ?OPERA .\n' +
     '\n' +
     '  ?CHARACTEREXCLUDE a o:Character .\n' +
     '  ?CHARACTEREXCLUDE o:Voice_type ?VOICE .\n' +
     '  FILTER(?CHARACTER != ?CHARACTEREXCLUDE)\n' +
     '}\n' +
     'ORDER BY ?OPERA ?CHARACTER\n' +
     '\n' +
     '# This query is bugged, the filter fails. See \'Operas with \n' +
     '# no dramatis personae\' for similar query.\n';
    } else if (exName == "exWerner") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  o:Friedrich_Ludwig_Zacharias_Werner o:Work ?name .\n' +
      '}\n';
    } else if (exName == "exWernerBirthday") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  o:Friedrich_Ludwig_Zacharias_Werner o:date_of_birth ?birthday .\n' +
      '}\n';
    } else if (exName == "exPucciniWorks") {
      document.queryform.query.value =
      'SELECT *\n' +
      'WHERE {\n' +
      '  <http://psi.ontopedia.net/Puccini> <http://psi.ontopedia.net/Work> ?Work .\n' +
      '}\n';
    } else if (exName == "exPucciniWorksWithBase") {
      document.queryform.query.value =
      'BASE <http://psi.ontopedia.net/>\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  <Puccini> <Work> ?Work .\n' +
      '}\n';
    } else if (exName == "exPucciniWorksPrefix") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  o:Puccini o:Work ?Work .\n' +
      '}\n';
    } else if (exName == "exPucciniWorksOrder") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  o:Puccini o:Work ?Work .\n' +
      '}\n' +
      'ORDER BY ?Work\n';
    } else if (exName == "exPucciniWorksItemID") {
      document.queryform.query.value =
      '# You probably need to edit the local: prefix\n' +
      'PREFIX local: <file:/C:/topicmaps/ontopia-5.1.3/apache-tomcat/webapps/omnigator/WEB-INF/topicmaps/ItalianOpera.ltm#>\n\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  local:puccini local:work ?Work .\n' +
      '}\n';
    } else if (exName == "exPucciniPlaces") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  o:Puccini o:Place ?Place .\n' +
      '}\n';
    } else if (exName == "exShakespeareBasedWorks") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT ?Composer ?Work ?Source\n' +
      'WHERE {\n' +
      '  ?Composer o:Work ?Work .\n' +
      '  ?Work o:Source ?Source .\n' +
      '  o:Shakespeare o:Work ?Source .\n' +
      '  ?Composer a o:Composer .\n' +
      '}\n';
    } else if (exName == "exShakespeareBasedWorksDistinct") {
      document.queryform.query.value =
      '# Source work is omitted from results to illustrate distinct. \n' +
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'SELECT DISTINCT ?Composer ?Work\n' +
      'WHERE {\n' +
      '  ?Composer o:Work ?Work .\n' +
      '  ?Work o:Source ?Source .\n' +
      '  o:Shakespeare o:Work ?Source .\n' +
      '  ?Composer a o:Composer .\n' +
      '}\n';
    } else if (exName == "exPucciniInstanceOf") {
      document.queryform.query.value =
      'PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n' +
      'PREFIX o: <http://psi.ontopedia.net/>\n\n' +
      'SELECT *\n' +
      'WHERE {\n' +
      '  o:Puccini rdf:type ?type .\n' +
      '}\n' +
      '#rdf:type can be replaced by "a" keyword\n';
    } else if (exName == "exConstructFoafAll") {
      document.queryform.query.value =
      'PREFIX local: <file:/C:/topicmaps/ontopia-5.1.3/apache-tomcat/webapps/omnigator/WEB-INF/topicmaps/ItalianOpera.ltm#>\n' +
      'PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n' +
      'PREFIX foaf:   <http://xmlns.com/foaf/0.1/>\n' +
      'PREFIX tm:    <http://psi.topicmaps.org/iso13250/model/>\n' +
      'PREFIX o: <http://psi.ontopedia.net/>\n\n' +
      'CONSTRUCT { ?x foaf:name ?name }\n' +
      'WHERE  { ?x tm:topic-name ?name }\n';
    } else if (exName == "exConstructFoafPuccini") {
      document.queryform.query.value =
      'PREFIX foaf:   <http://xmlns.com/foaf/0.1/>\n' +
      'PREFIX tm:    <http://psi.topicmaps.org/iso13250/model/>\n' +
      'PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>\n' +
      'PREFIX o: <http://psi.ontopedia.net/>\n\n' +
      'CONSTRUCT { o:Puccini foaf:name ?name }\n' +
      'WHERE  {\n' +
      '  o:Puccini tm:topic-name ?name .\n' +
      '}\n';
    } else if (exName == "exPucciniDescribe") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n' +
      'DESCRIBE o:Puccini\n';
    } else if (exName == "exPucciniDescribeWithWhere") {
      document.queryform.query.value =
      'PREFIX o: <http://psi.ontopedia.net/>\n\n'+
      'DESCRIBE ?x\n' +
      'WHERE {\n' +
      '  o:Puccini o:date_of_birth ?x . \n' +
      '}\n';
    }
}
