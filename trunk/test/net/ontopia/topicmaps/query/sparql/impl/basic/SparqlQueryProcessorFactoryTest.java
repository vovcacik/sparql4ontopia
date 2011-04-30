package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.File;

import junit.framework.TestCase;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SparqlQueryProcessorFactoryTest extends TestCase {

	private TopicMapIF tm;
	private SparqlQueryProcessorFactory factory;

	@Before
	public void setUp() throws Exception {
		try {
			LTMTopicMapReader reader = new LTMTopicMapReader(new File(AllTests.ITALIAN_OPERA_PATH));
			this.tm = reader.read();
		} catch (java.io.IOException e) {
			System.err.println("Error reading topic map: " + e);
		}
		factory = new SparqlQueryProcessorFactory();
	}

	@After
	public void tearDown() throws Exception {
		factory = null;
		tm = null;
	}

	@Test
	public void testGetQueryLanguage() {
		assertEquals("SPARQL", factory.getQueryLanguage());
	}
}
