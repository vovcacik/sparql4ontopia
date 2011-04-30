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

/**
 * This class receives namespaces, RDF statements and comments in process of evaluating <i>SPARQL</i>
 * <code>CONSTRUCT</code> query form.
 * <p>
 * In the first phase the RDF statements are collected via push methods (handle* methods). Result document is created on
 * the go, and its statements are written compactly.
 * <p>
 * In the second phase the result document can be obtain via get methods.
 * <p>
 * Handler implements two interfaces. One for OpenRDF side ({@link RDFHandler}) and one for Ontopia side (
 * {@link OntopiaResultHandler})
 * 
 * @author Vlastimil OvË·ËÌk
 * 
 */
public class SparqlTurtleResultHandler implements RDFHandler, OntopiaResultHandler<List<String[]>> {

	private static final String INDENT = "   ";
	public static final String SERIALIZATION_FORMAT = "Turtle";
	private StringBuilder builder;
	private Resource lastSubject;
	private Object lastPredicate;
	private Map<String, String> namespaces;
	private List<String[]> rows;
	private List<String> columnNames;

	/**
	 * Constructs new instance.
	 */
	public SparqlTurtleResultHandler() {
		builder = new StringBuilder();
		lastSubject = null;
		lastPredicate = null;
		namespaces = new LinkedHashMap<String, String>();
		columnNames = new ArrayList<String>();
		rows = new ArrayList<String[]>();
	}

	/**
	 * {@inheritDoc}
	 */
	public void startRDF() throws RDFHandlerException {
		// TODO implement this
	}

	/**
	 * {@inheritDoc}
	 */
	public void endRDF() throws RDFHandlerException {
		closeLastSubject();
	
		columnNames.add(SERIALIZATION_FORMAT + " result: ");
		String[] row = new String[] { builder.toString() };
		rows.add(row);

		builder = null;
		namespaces = null;
		// TODO see startrdf()
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		columnNames = null;
		rows = null;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
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
			uri = TurtleUtil.encodeURIString(uri);
			builder.append("@prefix " + prefix + ": <" + uri + "> .\n");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleStatement(Statement st) throws RDFHandlerException {
		Resource s = st.getSubject();
		URI p = st.getPredicate();
		Value o = st.getObject();
	
		p.getLocalName();
	
		if (s.equals(lastSubject)) {
			// we will use comma and semicolon to make turtle document more readable while we still have same subject
			if (p.equals(lastPredicate)) {
				// comma,
				builder.append(",\n" + INDENT + INDENT);
				builder.append(getObject(o));
			} else {
				// semicolon;
				builder.append(";\n" + INDENT);
				builder.append(getPredicate(p));
				builder.append(getObject(o));
			}
		} else {
			// starting new subject
			closeLastSubject();
			builder.append(getSubject(s));
			builder.append(getPredicate(p));
			builder.append(getObject(o));
		}
	
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String[]> getRows() {
		// TODO check if gathering resutls is completed
		return rows;
	}

	/**
	 * Method returns string representation of <i>RDF subject</i> in Turtle notation.
	 * 
	 * @param s
	 *            the subject to be serialized
	 * @return RDF subject in Turtle notation
	 */
	private String getSubject(Resource s) {
		if (lastSubject == null) {
			lastSubject = s;
			return getResource(s);
		} else {
			throw new RuntimeException(
					"Error while building TURTLE document: Previous subject statement has not been closed.");
		}
	}

	/**
	 * Method returns string representation of <i>RDF predicate</i> in Turtle notation.
	 * 
	 * @param p
	 *            the predicate to be serialized
	 * @return RDF predicate in Turtle notation
	 */
	private String getPredicate(URI p) {
		lastPredicate = p;
		return getURI(p);
	}

	/**
	 * Method returns string representation of <i>RDF object</i> in Turtle notation.
	 * 
	 * @param o
	 *            the object to be serialized
	 * @return RDF object in Turtle notation
	 */
	private String getObject(Value o) {
		return getValue(o);
	}

	/**
	 * Closes last written <i>RDF subject</i> if any.
	 */
	private void closeLastSubject() {
		if (lastSubject != null) {
			builder.append(".\n");
			lastSubject = null;
			lastPredicate = null;
		}
	}

	/**
	 * Returns provided <i>value</i> in Turtle notation.
	 * <p>
	 * <i>Value</i> has to be instance of {@link org.openrdf.model.Resource} or {@link org.openrdf.model.Literal}
	 * 
	 * @param value
	 *            the value to be serialized
	 * @return value in Turtle notation
	 */
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

	/**
	 * Returns provided <i>resource</i> in Turtle notation.
	 * <p>
	 * <i>Resource</i> has to be instance of {@link org.openrdf.model.URI} or {@link org.openrdf.model.BNode}
	 * 
	 * @param resource
	 *            the resource to be serialized
	 * @return resource in Turtle notation
	 */
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

	/**
	 * Returns provided <i>URI</i> in Turtle notation.
	 * <p>
	 * Method tries to short the <i>URI</i> with prefix.
	 * 
	 * @param uri
	 *            the <i>URI</i> to be serialized
	 * @return URI in Turtle notation. If there is no matching prefix method returns full URI.
	 */
	private String getURI(URI uri) {
		String uriString = uri.toString();
		String[] uriSplitted = splitByPrefix(uriString);

		if (uriSplitted == null) {
			return "<" + TurtleUtil.encodeURIString(uriString) + "> ";
		} else {
			return uriSplitted[0] + ":" + uriSplitted[1] + " ";
		}
	}

	/**
	 * Returns <i>blank node (bNode)</i> in Turtle notation.
	 * 
	 * @param bNode
	 *            the blank node to be serialized
	 * @return blank node in Turtle notation
	 */
	private String getBNode(BNode bNode) {
		return "_:" + bNode.getID() + " ";
	}

	/**
	 * Returns provided <i>Literal</i> in Turtle notation.
	 * <p>
	 * Long strings are encoded as <i>"""Turtle long strings"""</i>. Returned string consist of literal's label (the
	 * value) and optionally <i>^^datatype</i> or <i>@lang</i> suffix (not both).
	 * 
	 * @param literal
	 * @return
	 */
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

	/**
	 * Provided URI address splits on two parts - namespace and path. Namespace is replaced by prefix.
	 * 
	 * @param uri
	 *            URI to be split
	 * @return String array containing <i>two</i> strings - existing prefix and path. Otherwise null.
	 */
	private String[] splitByPrefix(String uri) {
		int index = TurtleUtil.findURISplitIndex(uri);
		if (index > 0) {
			String ns = uri.substring(0, index);
			String prefix = namespaces.get(ns);
			String path = uri.substring(index);
	
			if (prefix != null) {
				return new String[] { prefix, path };
			}
		}
		return null;
	}

}
