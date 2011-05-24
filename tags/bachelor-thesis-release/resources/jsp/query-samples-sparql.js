
// $Id: query-samples.js,v 1.24 2006/10/27 12:15:24 pepper Exp $

function insertExample(exName) {

  // ===== ItalianOpera.ltm =================================================

  if (exName == "exWerner") { // -------------------------------------------
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
