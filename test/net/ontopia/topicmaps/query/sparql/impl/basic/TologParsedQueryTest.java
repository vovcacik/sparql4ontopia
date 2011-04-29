package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.File;
import java.util.Collection;

import junit.framework.TestCase;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.impl.utils.TologQueryProcessorFactory;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TologParsedQueryTest extends TestCase {

	private final String PROTOCOL = "file:/";
	private final String ITALIAN_OPERA_PATH = "c:/topicmaps/ontopia-5.1.3/apache-tomcat/webapps/omnigator/WEB-INF/topicmaps/ItalianOpera.ltm";
	private final String QUERY = "using o for i\"http://psi.ontopedia.net/\"\r\n"
			+ " o:written_by($name : o:Work, o:Friedrich_Ludwig_Zacharias_Werner : o:Writer)?\r\n";
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
		TologQueryProcessorFactory factory = new TologQueryProcessorFactory();
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
		try {
			QueryResultIF result = pq.execute();
			assertEquals("name", result.getColumnName(0));
			assertTrue(result.next());
			TopicIF t = (TopicIF) result.getValue(0);
			// String id = t.getObjectId();
			Collection<LocatorIF> itemIDs = t.getItemIdentifiers();
			LocatorIF[] itemIDsArray = itemIDs.toArray(new LocatorIF[0]);
			assertEquals(1, itemIDsArray.length);
			// Collection<LocatorIF> subIdent = t.getSubjectIdentifiers();
			// Collection<LocatorIF> subLoc = t.getSubjectLocators();
			assertEquals(PROTOCOL + ITALIAN_OPERA_PATH + "#attila-src", itemIDsArray[0]
					.getExternalForm());
			assertFalse(result.next());

		} catch (InvalidQueryException e) {
			e.printStackTrace();
		}
	}
}
