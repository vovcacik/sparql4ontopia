package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.turtle.TurtleUtil;

public class SparqlTurtleResultHandler implements RDFHandler, OntopiaResultHandler<List<String[]>> {

	private static final String INDENT = "   ";
	public static final String SERIALIZATION_FORMAT = "Turtle";
	private StringBuilder builder;
	private boolean ended;
	private Resource lastSubject;
	private Object lastPredicate;
	private Map<String, String> namespaces;
	private List<String[]> rows;
	private List<String> columnNames;

	public SparqlTurtleResultHandler() {
		builder = new StringBuilder();
		ended = false;
		lastSubject = null;
		lastPredicate = null;
		namespaces = new LinkedHashMap<String, String>();
		columnNames = new ArrayList<String>();
		rows = new ArrayList<String[]>();
	}

	public void endRDF() throws RDFHandlerException {
		closeLastSubject();

		columnNames.add(SERIALIZATION_FORMAT + " result: ");
		String[] row = new String[] { builder.toString() };
		rows.add(row);
	}

	public void handleComment(String comment) throws RDFHandlerException {
		if (comment.indexOf("\r") != -1 || comment.indexOf("\n") != -1) {
			// multi-line comment
			StringTokenizer tokenizer = new StringTokenizer(comment, "\r\n");
			while (tokenizer.hasMoreTokens()) {
				builder.append("# " + tokenizer.nextToken() + "\n");
			}
		} else {
			// single-line comment
			builder.append("# " + comment + "\n");
		}
	}

	public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
		// TODO mohou se namespace duplikovat?
		if (!namespaces.containsKey(uri)) {
			if (TurtleUtil.isLegalPrefix(prefix) || !namespaces.containsValue(prefix)) {
				namespaces.put(uri, prefix);
			} else {
				prefix = "ns";
				int var = 1;
				while (namespaces.containsValue(prefix + var)) {
					var++;
				}
				prefix += var;
				namespaces.put(uri, prefix);
			}
		}
		builder.append("@prefix " + prefix + ": <" + uri + "> .\n");
		// TODO encodeuristring(uri)

	}

	public void handleStatement(Statement st) throws RDFHandlerException {
		// FIXME Resource c = st.getContext();
		Resource s = st.getSubject();
		URI p = st.getPredicate();
		Value o = st.getObject();

		p.getLocalName();

		if (s.equals(lastSubject)) {
			if (p.equals(lastPredicate)) {
				// comma,
				builder.append(",\n" + INDENT + INDENT);
				builder.append(writeObject(o));
			} else {
				// semicolon;
				builder.append(";\n" + INDENT);
				builder.append(writePredicate(p));
				builder.append(writeObject(o));
			}
		} else {
			closeLastSubject();
			// start new subject
			builder.append(writeSubject(s));
			builder.append(writePredicate(p));
			builder.append(writeObject(o));
		}

	}

	private void closeLastSubject() {
		if (lastSubject != null) {
			builder.append(".\n");
			lastSubject = null;
			lastPredicate = null;
		}

	}

	private String writeObject(Value o) {
		return getValue(o);
	}

	private String getValue(Value value) {
		if (value instanceof Resource) {
			return getResource((Resource) value);
		} else if (value instanceof Literal) {
			return getLiteral((Literal) value);
		} else {
			throw new RuntimeException(
					"Error while building TURTLE document: Provided Value parameter is not instance of Resource nor yet Literal.");
		}
	}

	private String getLiteral(Literal literal) {
		String label = literal.getLabel();
		String result = "";

		if (label.indexOf('\n') > 0 || label.indexOf('\r') > 0 || label.indexOf('\t') > 0) {
			result += "\"\"\"";
			result += TurtleUtil.encodeLongString(label);
			result += "\"\"\"";
		} else {
			result += "\"";
			result += TurtleUtil.encodeString(label);
			result += "\"";
		}

		if (literal.getDatatype() != null) {
			result += "^^" + getURI(literal.getDatatype());
		} else if (literal.getLanguage() != null) {
			result += "@" + literal.getLanguage() + " ";
		}
		return result;
	}

	private String writePredicate(URI p) {
		lastPredicate = p;
		return "<" + p.stringValue() + "> ";
	}

	private String writeSubject(Resource s) {
		if (lastSubject == null) {
			lastSubject = s;
			return getResource(s);
		} else {
			throw new RuntimeException(
					"Error while building TURTLE document: Previous subject statement has not been closed.");
		}
	}

	private String getResource(Resource resource) {
		if (resource instanceof URI) {
			return getURI((URI) resource);
		} else if (resource instanceof BNode) {
			return getBNode((BNode) resource);
		} else {
			throw new RuntimeException(
					"Error while building TURTLE document: Provided Resource parameter is not instance of URI nor yet BNode.");
		}
	}

	private String getBNode(BNode bNode) {
		return "_:" + bNode.getID() + " ";
	}

	private String getURI(URI uri) {
		String uriString = uri.toString();
		String prefix = null;

		int index = TurtleUtil.findURISplitIndex(uriString);
		if (index > 0) {
			String ns = uriString.substring(0, index);
			prefix = namespaces.get(ns);
		}

		if (prefix == null) {
			return "<" + TurtleUtil.encodeURIString(uriString) + "> ";
		} else {
			return prefix + ":" + uriString.substring(index) + " ";
		}
	}

	public void startRDF() throws RDFHandlerException {
		// TODO co sem?
	}

	public void close() {
		columnNames = null;
		rows = null;
		// TODO Auto-generated method stub

	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public List<String[]> getRows() {
		// TODO tahle metoda by mìla mít interface
		// TODO check complete
		return rows;
	}

}
