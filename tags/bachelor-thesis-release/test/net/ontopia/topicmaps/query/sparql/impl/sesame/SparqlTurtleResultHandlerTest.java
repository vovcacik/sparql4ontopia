package net.ontopia.topicmaps.query.sparql.impl.sesame;

import java.util.ArrayList;

import junit.framework.TestCase;

import net.ontopia.topicmaps.query.sparql.impl.sesame.SparqlTurtleResultHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;

public class SparqlTurtleResultHandlerTest extends TestCase {

	private SparqlTurtleResultHandler handler;
	private ArrayList<String> columnNames;
	private ArrayList<String[]> rows;
	private StatementImpl statement1_111;
	private StatementImpl statement2_111;
	private StatementImpl statement2_222;
	private StatementImpl statement3_11L;
	private StatementImpl statement3_12L;
	private StatementImpl statement3_12U;
	private StatementImpl statement3_124;

	@Before
	public void setUp() throws Exception {
		String document =
				"# prefixes:\n" +
				"@prefix one: <http://one.org/> .\n" +
				"@prefix two: <http://two.org/> .\n" +
				"@prefix three: <http://three.org/> .\n" +
				"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
				"# statement11:\n" +
				"one:subject1 one:predicate1 one:object1 .\n" +
				"# comment behind statement\n" +
				"# statement21 and 22:\n" +
				"_:1 two:predicate1 two:object1 .\n" +
				"two:subject2 two:predicate2 _:2 .\n" +
				"# statement3*:\n" +
				"three:subject1 three:predicate1 \"label\";\n" +
				"   three:predicate2 \"amigo\"@es ,\n" +
				"      \"http://three.org/anyURI_object\"^^xsd:anyURI ,\n" +
				"      three:object4 .\n" +
				"# multi\n" +
				"# multi\n" +
				"# multi line comment\n";

		columnNames = new ArrayList<String>();
		rows = new ArrayList<String[]>();

		columnNames.add("Turtle result: ");
		rows.add(new String[] { document });
		
		
		handler = new SparqlTurtleResultHandler();

		statement1_111 = new StatementImpl(
				new URIImpl("http://one.org/subject1"),
				new URIImpl("http://one.org/predicate1"),
				new URIImpl("http://one.org/object1")
				);
		statement2_111 = new StatementImpl(
				new BNodeImpl("1"),
				new URIImpl("http://two.org/predicate1"),
				new URIImpl("http://two.org/object1")
				);
		statement2_222 = new StatementImpl(
				new URIImpl("http://two.org/subject2"),
				new URIImpl("http://two.org/predicate2"),
				new BNodeImpl("2")
				);
		statement3_11L = new StatementImpl(
				new URIImpl("http://three.org/subject1"),
				new URIImpl("http://three.org/predicate1"),
				new LiteralImpl("label")
				);
		statement3_12L = new StatementImpl(
				new URIImpl("http://three.org/subject1"),
				new URIImpl("http://three.org/predicate2"),
				new LiteralImpl("amigo", "es")
				);
		statement3_12U = new StatementImpl(
				new URIImpl("http://three.org/subject1"),
				new URIImpl("http://three.org/predicate2"),
				new LiteralImpl("http://three.org/anyURI_object",
				new URIImpl("http://www.w3.org/2001/XMLSchema#anyURI"))
				);
		statement3_124 = new StatementImpl(
				new URIImpl("http://three.org/subject1"),
				new URIImpl("http://three.org/predicate2"),
				new URIImpl("http://three.org/object4")
				);
	}

	@After
	public void tearDown() throws Exception {
		handler.close();
		columnNames = null;
		rows = null;
	}

	@Test
	public void testSimpleTurtleDocument() {
		try {
			handler.startRDF();

			handler.handleComment("prefixes:");
			handler.handleNamespace("one", "http://one.org/");
			handler.handleNamespace("two", "http://two.org/");
			handler.handleNamespace("three", "http://three.org/");
			handler.handleNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");

			handler.handleComment("statement11:");
			handler.handleStatement(statement1_111);
			handler.handleComment("comment behind statement");

			handler.handleComment("statement21 and 22:");
			handler.handleStatement(statement2_111);
			handler.handleStatement(statement2_222);

			handler.handleComment("statement3*:");
			handler.handleStatement(statement3_11L);
			handler.handleStatement(statement3_12L);
			handler.handleStatement(statement3_12U);
			handler.handleStatement(statement3_124);

			handler.handleComment("multi\nmulti\nmulti line comment");
			handler.endRDF();

			assertEquals(columnNames, handler.getColumnNames());
			assertEquals(rows.get(0)[0], handler.getRows().get(0)[0]);

			handler.close();

			// FIXME test special cases

		} catch (RDFHandlerException e) {
			e.printStackTrace();
		}
	}
}
