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