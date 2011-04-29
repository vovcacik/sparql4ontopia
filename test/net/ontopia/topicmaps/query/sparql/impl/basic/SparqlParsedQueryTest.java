package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.File;

import junit.framework.TestCase;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SparqlParsedQueryTest extends TestCase {

	private final String PROTOCOL = "file:/";
	private final String ITALIAN_OPERA_PATH = "c:/topicmaps/ontopia-5.1.3/apache-tomcat/webapps/omnigator/WEB-INF/topicmaps/ItalianOpera.ltm";
	private final String QUERY = "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "SELECT *\r\n" + "WHERE {\r\n"
			+ "o:Friedrich_Ludwig_Zacharias_Werner o:Work ?name .}";
	private TopicMapIF tm;
	private QueryProcessorIF processor;
	private ParsedQueryIF pq;

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
		pq = processor.parse(QUERY);
	}

	@After
	public void tearDown() throws Exception {
		// TODO add
	}

	@Test
	public void testGetAllVariables() {
		String[] vars = pq.getAllVariables().toArray(new String[0]);
		assertEquals(1, vars.length);
		assertEquals("name", vars[0]);
	}

	@Test
	public void testGetCountedVariables() {
		String[] vars = pq.getCountedVariables().toArray(new String[0]);
		assertEquals(0, vars.length);
		// assertEquals("", vars[0]);
		// TODO doplnit query o counted variable abych to mohl testnout
	}

	@Test
	public void testExecuteWernersWork() {
		TMObjectIF expectedTopic = tm.getObjectById("5874"); // werner's play Attila id
		try {
			QueryResultIF result = pq.execute();
			assertEquals("name", result.getColumnName(0));
			// TODO result by mìl vracet instance topic a ne jen ident...
			assertTrue(result.next());
			TMObjectIF realTopic = (TMObjectIF) result.getValue(0);
			// assertEquals(PROTOCOL + ITALIAN_OPERA_PATH + "#attila-src", result.getValue(0));
			assertEquals(expectedTopic, realTopic);
			assertFalse(result.next());

		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testToString() {
		assertEquals(true, pq.toString().length() > 0);
	}
}
