package net.ontopia.topicmaps.query.sparql.impl.basic;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SparqlQueryProcessorFactoryTest extends TestCase {

	private SparqlQueryProcessorFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new SparqlQueryProcessorFactory();
	}

	@After
	public void tearDown() throws Exception {
		factory = null;
	}

	@Test
	public void testGetQueryLanguage() {
		assertEquals("SPARQL", factory.getQueryLanguage());
	}
}
