package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.File;

import junit.framework.TestCase;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.sparql.impl.util.SparqlTurtleResultHandler;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SparqlQueryProcessorTest extends TestCase {

	private final String PROTOCOL = "file:/";
	private final String ITALIAN_OPERA_PATH = "c:/topicmaps/ontopia-5.1.3/apache-tomcat/webapps/omnigator/WEB-INF/topicmaps/ItalianOpera.ltm";
	private TopicMapIF tm;
	private QueryProcessorIF processor;

	@Before
	public void setUp() throws Exception {
		try {
			LTMTopicMapReader reader = new LTMTopicMapReader(new File(ITALIAN_OPERA_PATH));
			this.tm = reader.read();
		} catch (java.io.IOException e) {
			System.err.println("Error reading topic map: " + e);
		}
		SparqlQueryProcessorFactory factory = new SparqlQueryProcessorFactory();
		processor = factory.createQueryProcessor(tm, null, null);
	}

	@After
	public void tearDown() throws Exception {
		// TODO add
	}

	@Test
	public void testExecuteWernersWork() {
		String query = "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "SELECT *\r\n" + "WHERE {\r\n"
				+ "o:Friedrich_Ludwig_Zacharias_Werner o:Work ?name .}";

		try {
			QueryResultIF result = processor.execute(query);
			assertEquals("name", result.getColumnName(0));
			assertTrue(result.next());
			assertEquals(PROTOCOL + ITALIAN_OPERA_PATH + "#attila-src", result.getValue(0));
			assertFalse(result.next());

		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParseWernersWork() {
		String query = "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "SELECT *\r\n" + "WHERE {\r\n"
				+ "o:Friedrich_Ludwig_Zacharias_Werner o:Work ?name .}";
		try {
			ParsedQueryIF parsedQuery = processor.parse(query);
			String[] variables = parsedQuery.getAllVariables().toArray(new String[0]);
			assertEquals(1, variables.length);
			assertEquals("name", variables[0]);
		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteGraphQuery() {
		String query = "PREFIX foaf:   <http://xmlns.com/foaf/0.1/>\r\n"
				+ "PREFIX tm:    <http://psi.topicmaps.org/iso13250/model/>\r\n"
				+ "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#>\r\n"
				+ "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "\r\n"
				+ "CONSTRUCT { o:Puccini foaf:name ?name }\r\n"
				+ "WHERE  { o:Puccini tm:topic-name ?name }";
		String header = SparqlTurtleResultHandler.SERIALIZATION_FORMAT + " result: ";
		String expectedResult = "<pre>@prefix foaf: &lt;http://xmlns.com/foaf/0.1/&gt; .\n"
				+ "@prefix tm: &lt;http://psi.topicmaps.org/iso13250/model/&gt; .\n"
				+ "@prefix xsd: &lt;http://www.w3.org/2001/XMLSchema#&gt; .\n"
				+ "@prefix o: &lt;http://psi.ontopedia.net/&gt; .\n"
				+ "o:Puccini &lt;http://xmlns.com/foaf/0.1/name&gt; &quot;Giacomo Puccini&quot;^^xsd:string ,\n"
				+ "      &quot;Puccini&quot;^^xsd:string ,\n"
				+ "      &quot;Puccini, Giacomo&quot;^^xsd:string .\n" + "</pre>";
		try {
			QueryResultIF result = processor.execute(query);
			assertEquals(header, result.getColumnName(0));
			assertTrue(result.next());
			String realResult = (String) result.getValue(header);
			assertEquals(expectedResult, realResult);
			assertFalse(result.next());

		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}

}
