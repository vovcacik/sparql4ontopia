package net.ontopia.topicmaps.query.sparql.impl.basic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	// Edit the path to match your environment
	static String ITALIAN_OPERA_PATH = "c:/Program Files/Eclipse/workspace/sparql4ontopia/resources/ItalianOpera.ltm";
	static String ITALIAN_OPERA_BASE = "http://psi.ontopedia.net/";

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for net.ontopia.topicmaps.query.sparql.impl.basic");
		// $JUnit-BEGIN$
		suite.addTestSuite(SparqlQueryProcessorFactoryTest.class);
		suite.addTestSuite(SparqlQueryProcessorTest.class);
		suite.addTestSuite(SparqlParsedQueryTest.class);
		// $JUnit-END$
		return suite;
	}
}
