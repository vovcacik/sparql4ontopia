<%
  String tmid = request.getParameter("tm");
%>

<p>
To search the current topic map, please enter your <b>SPARQL</b> query
in the box on the right, or select a query from this list of example
queries:</p>
<p>
<select name="codeexample"
 onChange='javascript:insertExample(this.options[this.selectedIndex].value)'
 tabindex='1'>
<%
  if (tmid.equals("ItalianOpera.ltm")) {
%>
    <option value="">Example queries:</option>
    <option value="exPucciniSparql">Puccini's operas</option>
    <option value="exPucciniSortedSparql">Puccini's operas (sorted)</option>
    <option value="exShakespeareSparqlCorrect">Composers inspired by Shakespeare (Correct)</option>
    <option value="exShakespeareSparqlBroken">Composers inspired by Shakespeare (Broken)</option>
    <option value="exShakespeareSparqlFixed">Composers inspired by Shakespeare (Fixed)</option>
    <option value="exBornDiedSparql">Born and died in the same place</option>
    <option value="exComposersSparql">Most prolific composers</option>
    <option value="exMeccaSparql">Cities with the most premieres</option>
    <option value="exTheatresByPremiereSparql">Theatres with the most premieres</option>
    <option value="exOperasByPremiereDateSparql">Operas by premiere date (paged)</option>
    <option value="exEnglishTitlesSparql">Operas that have English titles</option>
    <option value="exSuicidesSparql">Suicides (incomplete data)</option>
    <option value="exSettingsByCountrySparql">Settings of operas by country</option>
    <option value="exNaryAriasSparql">Arias sung by more than one person</option>
    <option value="exInspiredBySparql">"Inspired by" as inference rule</option>
    <option value="exBibliographySparql">Subtle bibliography query</option>
    <option value="exRecordingsSparql">Audio recordings</option>
    <option value="exNoDramatisPersonaeSparql">Operas with no dramatis personae</option>
    <option value="exNoVoiceTypeSparql">Operas with missing voice types</option>
    <option value="">-------------------------------------</option>
    <option value="exWerner">Werner's works</option>
    <option value="exWernerBirthday">Werner's birthday</option>
    <option value="exPucciniWorks">Works composed by Puccini (no prefix)</option>
    <option value="exPucciniWorksWithBase">Works composed by Puccini (with base URI)</option>
    <option value="exPucciniWorksPrefix">Works composed by Puccini (with prefix)</option>
    <option value="exPucciniWorksOrder">Works composed by Puccini (ORDER BY Work)</option>
    <option value="exPucciniWorksItemID">Works composed by Puccini (item identifiers)</option>
    <option value="exPucciniPlaces">Places associated with Puccini</option>
    <option value="exShakespeareBasedWorks">Composers inspired by Shakespeare.</option>
    <option value="exShakespeareBasedWorksDistinct">Composers inspired by Shakespeare (DISTINCT).</option>
    <option value="exPucciniInstanceOf">Puccini is instance-of what type?</option>
    <option value="exConstructFoafAll">Construct foaf:name from topic-name for everything.</option>
    <option value="exConstructFoafPuccini">Construct foaf:name from topic-name for Puccini.</option>
    <option value="exPucciniDescribe">Describe Puccini.</option>
    <option value="exPucciniDescribeWithWhere">Describe Puccini's birthday.</option>
<%
  } else {
%>
    <option value="">Example queries:</option>
<%
  }
%>
</select>
</p>