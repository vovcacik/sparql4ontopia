package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.IOException;
import java.io.StringBufferInputStream;

import net.ontopia.topicmaps.query.sparql.impl.util.TMConnector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

public class TMConnectorTest {

	private final String BASE_IRI = "http://www.ex.com/tm";
	private TMConnector tmConnector;
	private TopicMap tm;
	private TopicMapSystem tms;

	@Before
	public void setUp() throws Exception {
		tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		tm = tms.createTopicMap(BASE_IRI);

		tmConnector = new TMConnector(tms);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddRDF() {
		String update = "<http://www.es.org/s> <http://www.es.org/p> 0 . ";
		try {
			tmConnector.addRDF(BASE_IRI, RDFFormat.N3, new StringBufferInputStream(update));
		} catch (RDFParseException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Locator loc = tms.createLocator("http://www.es.org/p");
		Topic topic = tm.getTopicBySubjectIdentifier(loc);
		topic.getId();
	}

	// @Test
	// public void test(){
	// RDF.TYPE;
	// }
}
