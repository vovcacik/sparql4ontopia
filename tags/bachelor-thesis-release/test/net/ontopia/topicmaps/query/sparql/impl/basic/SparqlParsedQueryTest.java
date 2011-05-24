package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.File;
import java.util.Collection;

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

	private final String QUERY = "PREFIX o: <http://psi.ontopedia.net/>\r\n" + "SELECT *\r\n" + "WHERE {\r\n"
			+ "o:Friedrich_Ludwig_Zacharias_Werner o:Work ?name .}";
	private final String PARSED_QUERY = "Projection\r\n"
			+ "   ProjectionElemList\r\n"
			+ "      ProjectionElem \"name\"\r\n"
			+ "   StatementPattern\r\n"
			+ "      Var (name=-const-1, value=http://psi.ontopedia.net/Friedrich_Ludwig_Zacharias_Werner, anonymous)\r\n"
			+ "      Var (name=-const-2, value=http://psi.ontopedia.net/Work, anonymous)\r\n"
			+ "      Var (name=name)\r\n";
	private TopicMapIF tm;
	private QueryProcessorIF processor;
	private ParsedQueryIF pq;

	@Before
	public void setUp() throws Exception {
		try {
			LTMTopicMapReader reader = new LTMTopicMapReader(new File(AllTests.ITALIAN_OPERA_PATH));
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
		tm = null;
		processor = null;
		pq = null;
	}

	@Test
	public void testGetAllVariables() {
		String[] vars = pq.getAllVariables().toArray(new String[0]);
		assertEquals(1, vars.length);
		assertEquals("name", vars[0]);
	}

	@Test
	public void testGetCountedVariables() {
		Collection<String> vars = pq.getCountedVariables();
		assertEquals(0, vars.size());
	}

	@Test
	public void testExecuteWernersWork() {
		TMObjectIF expectedTopic = tm.getObjectById("5874"); // werner's play Attila id
		try {
			QueryResultIF result = pq.execute();
			assertEquals("name", result.getColumnName(0));
			assertTrue(result.next());
			TMObjectIF realTopic = (TMObjectIF) result.getValue(0);
			assertEquals(expectedTopic, realTopic);
			assertFalse(result.next());

		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testToString() {
		assertEquals(PARSED_QUERY, pq.toString());
	}
}
