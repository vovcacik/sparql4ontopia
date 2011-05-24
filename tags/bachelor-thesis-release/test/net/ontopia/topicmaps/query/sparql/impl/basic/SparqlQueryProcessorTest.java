package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.File;

import junit.framework.TestCase;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.sparql.impl.sesame.SparqlTurtleResultHandler;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SparqlQueryProcessorTest extends TestCase {

	private TopicMapIF tm;
	private QueryProcessorIF processor;
	private QueryProcessorIF processorWithBase;

	@Before
	public void setUp() throws Exception {
		try {
			LTMTopicMapReader reader = new LTMTopicMapReader(new File(AllTests.ITALIAN_OPERA_PATH));
			this.tm = reader.read();
		} catch (java.io.IOException e) {
			System.err.println("Error reading topic map: " + e);
		}
		LocatorIF locator = new URILocator(AllTests.ITALIAN_OPERA_BASE);
		SparqlQueryProcessorFactory factory = new SparqlQueryProcessorFactory();

		processor = factory.createQueryProcessor(tm, null, null);
		processorWithBase = factory.createQueryProcessor(tm, locator, null);
	}

	@After
	public void tearDown() throws Exception {
		processor = null;
		processorWithBase = null;
		tm = null;
	}

    @Test
    public void testExecuteWernersWork() {
        String query = "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "SELECT *\r\n" + "WHERE {\r\n"
                + "o:Friedrich_Ludwig_Zacharias_Werner o:Work ?name .}";
        TMObjectIF expectedTopic = tm.getObjectById("5874"); // werner's play Attila id
        try {
            QueryResultIF result = processor.execute(query);
            assertEquals("name", result.getColumnName(0));
            assertEquals("name", result.getColumnNames()[0]);
            assertTrue(result.next());

            TMObjectIF realTopic = (TMObjectIF) result.getValue(0);
            assertEquals(expectedTopic, realTopic);

            realTopic = (TMObjectIF) result.getValues()[0];
            assertEquals(expectedTopic, realTopic);

            Object[] realValues = new Object[result.getWidth()];
            assertEquals(realTopic, result.getValues(realValues)[0]);

            assertFalse(result.next());
            result.close();

        } catch (InvalidQueryException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExecutePuccinisPlaces() {
        String query =
                "PREFIX o: <http://psi.ontopedia.net/>\r\n" +
                        "SELECT *\r\n" +
                        "WHERE {\r\n" +
                        "o:Puccini o:Place ?Place .\r\n" +
                        "}";
        // second match:
        TMObjectIF expectedTopic = tm.getObjectById("3541"); // Puccini's place of death - Brussels id
        try {
            QueryResultIF result = processor.execute(query);
            assertEquals("Place", result.getColumnName(0));
            assertEquals("Place", result.getColumnNames()[0]);
            // first match
            assertTrue(result.next());
            TMObjectIF realTopic = (TMObjectIF) result.getValue(0);
            assertNotNull(realTopic);
            // second match
            assertTrue(result.next());
            realTopic = (TMObjectIF) result.getValue(0);
            assertEquals(expectedTopic, realTopic);
            realTopic = (TMObjectIF) result.getValues()[0];
            assertEquals(expectedTopic, realTopic);

            Object[] realValues = new Object[result.getWidth()];
            assertEquals(realTopic, result.getValues(realValues)[0]);

            assertFalse(result.next());
            result.close();

        } catch (InvalidQueryException e) {
            e.printStackTrace();
        }
    }

	@Test
	public void testExecuteWernersBirthday() {
		String query = "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "SELECT *\r\n" + "WHERE {\r\n"
				+ "o:Friedrich_Ludwig_Zacharias_Werner o:date_of_birth ?birth .\r\n" + "}";
		try {
			QueryResultIF result = processor.execute(query);
			assertEquals("birth", result.getColumnName(0));
			assertTrue(result.next());
			assertEquals("1768-11-18", result.getValue(0));
			assertFalse(result.next());
			result.close();
		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteWernersBirthdayWithBaseIRI() {
		String query = "SELECT *\r\n" + "WHERE {\r\n"
				+ "<Friedrich_Ludwig_Zacharias_Werner> <date_of_birth> ?birth .}";
		try {
			QueryResultIF result = processorWithBase.execute(query);
			assertEquals("birth", result.getColumnName(0));
			assertTrue(result.next());
			assertEquals("1768-11-18", result.getValue(0));
			assertFalse(result.next());
			result.close();
		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteWernersBirthdayWithBaseIRIInQuery() {
		String query = "BASE <http://psi.ontopedia.net/>\r\n" + "SELECT *\r\n" + "WHERE {\r\n"
				+ "<Friedrich_Ludwig_Zacharias_Werner> <date_of_birth> ?birth .}";
		try {
			QueryResultIF result = processorWithBase.execute(query);
			assertEquals("birth", result.getColumnName(0));
			assertTrue(result.next());
			assertEquals("1768-11-18", result.getValue(0));
			assertFalse(result.next());
			result.close();
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
				+ "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "\r\n" + "CONSTRUCT { o:Puccini foaf:name ?name }\r\n"
				+ "WHERE  { o:Puccini tm:topic-name ?name }";
		String header = SparqlTurtleResultHandler.SERIALIZATION_FORMAT + " result: ";
		// order of predicates seems random
		String expectedResult1 = "<pre>@prefix foaf: &lt;http://xmlns.com/foaf/0.1/&gt; .\n"
				+ "@prefix tm: &lt;http://psi.topicmaps.org/iso13250/model/&gt; .\n"
				+ "@prefix xsd: &lt;http://www.w3.org/2001/XMLSchema#&gt; .\n"
				+ "@prefix o: &lt;http://psi.ontopedia.net/&gt; .\n"
				+ "o:Puccini foaf:name &quot;Giacomo Puccini&quot;^^xsd:string ,\n"
				+ "      &quot;Puccini, Giacomo&quot;^^xsd:string ,\n" + "      &quot;Puccini&quot;^^xsd:string .\n"
				+ "</pre>";
		String expectedResult2 = "<pre>@prefix foaf: &lt;http://xmlns.com/foaf/0.1/&gt; .\n"
				+ "@prefix tm: &lt;http://psi.topicmaps.org/iso13250/model/&gt; .\n"
				+ "@prefix xsd: &lt;http://www.w3.org/2001/XMLSchema#&gt; .\n"
				+ "@prefix o: &lt;http://psi.ontopedia.net/&gt; .\n"
				+ "o:Puccini foaf:name &quot;Giacomo Puccini&quot;^^xsd:string ,\n"
				+ "      &quot;Puccini&quot;^^xsd:string ,\n" + "      &quot;Puccini, Giacomo&quot;^^xsd:string .\n"
				+ "</pre>";
		try {
			QueryResultIF result = processor.execute(query);
			assertEquals(header, result.getColumnName(0));
			assertTrue(result.next());
			String realResult = (String) result.getValue(header);
			assertTrue(((realResult.equals(expectedResult1)) || (realResult.equals(expectedResult2))));
			assertFalse(result.next());
			result.close();
		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}
}
