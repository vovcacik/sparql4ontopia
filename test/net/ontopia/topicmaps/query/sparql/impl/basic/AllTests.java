package net.ontopia.topicmaps.query.sparql.impl.basic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

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
