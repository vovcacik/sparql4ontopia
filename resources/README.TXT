sparql4ontopia plugin
--------------------------------------------------------------------------------
This plugin enables SPARQL query language in Ontopia Omnigator. For translation
between Topic Maps and RDF it utilizes Nikunau project (see [1]) that is 
following mapping developed by Networked Planet (see [2]).



INSTALLATION INSTRUCTIONS
--------------------------------------------------------------------------------
The directory structure varies between Ontopia versions. Copy the files from 
this distribution (right side) to the Ontopia directories listed on the left 
side. %ONTOPIA_HOME% variable is folder where you have installed your Ontopia
instance. 

(1) Ontopia 5.1.0
.\sparql4ontopia.jar  ->    %ONTOPIA_HOME%\apache-tomcat\common\lib\
.\lib\*.*             ->    %ONTOPIA_HOME%\apache-tomcat\common\lib\
.\jsp\*.*   ->    %ONTOPIA_HOME%\apache-tomcat\webapps\omnigator\plugins\query\

(2) Ontopia 5.2.0
.\sparql4ontopia.jar  ->    %ONTOPIA_HOME%\apache-tomcat\lib\ontopia
.\lib\*.*             ->    %ONTOPIA_HOME%\apache-tomcat\lib\ontopia
.\jsp\*.*   ->    %ONTOPIA_HOME%\apache-tomcat\webapps\omnigator\plugins\query\



FIRST STEPS
--------------------------------------------------------------------------------
To see this plugin in action, please navigate to the Italian Opera topic map in
Omnigator and choose 'Query' option from top horizontal menu. Than in the 
'Query language' dropdown menu select 'Sparql' and try some of our predefined 
example queries.
You are strongly encourage to compare the SPARQL queries to their tolog
counterpart. 



PLUGIN STRUCTURE
--------------------------------------------------------------------------------
./sparql4ontopia.jar                                      (sparql4ontopia logic)
./jsp
     query-samples-sparql.js                            (SPARQL example queries)
     sparql.jsp                        (Ontopia Navigator framework integration)
./lib
     commons-lang.jar
     openrdf-sesame-2.3-pr1-onejar.jar                           (Sesame engine)
     sesame-sail-tmapi-0.1.3.jar                                       (Nikunau)



REFERENCES
--------------------------------------------------------------------------------
[1] http://code.google.com/p/sesametm/wiki/Nikunau
[2] http://www.networkedplanet.com/ontopic/2009/11/making_topic_maps_sparql.html
