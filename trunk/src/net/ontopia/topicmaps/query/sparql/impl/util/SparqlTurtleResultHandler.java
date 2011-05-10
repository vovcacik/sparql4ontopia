package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.ontopia.topicmaps.query.sparql.impl.basic.OntopiaResultHandler;

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
	private Boolean running;
	private boolean done;

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
		running = false;
		done = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void startRDF() throws RDFHandlerException {
		if (running || done) {
			throw new RuntimeException("Obtaining results has been already started.");
		}
		running = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleComment(final String comment) throws RDFHandlerException {
		closeLastSubject();
		if (comment.indexOf('\r') != -1 || comment.indexOf('\n') != -1) {
			// multi-line comment
			final StringTokenizer tokenizer = new StringTokenizer(comment, "\r\n");
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
	public void handleNamespace(final String prefix, final String uri) throws RDFHandlerException {
		final String encodedURI = TurtleUtil.encodeURIString(uri);
		if (!namespaces.containsKey(uri)) {
			if (TurtleUtil.isLegalPrefix(prefix) && !namespaces.containsValue(prefix)) {
				namespaces.put(uri, prefix);
				builder.append("@prefix " + prefix + ": <" + encodedURI + "> .\n");
			} else {
				String newPrefix = "ns";
				int var = 1;
				while (namespaces.containsValue(newPrefix + var)) {
					var++;
				}
				newPrefix += var;
				namespaces.put(uri, newPrefix);
				builder.append("@prefix " + newPrefix + ": <" + encodedURI + "> .\n");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleStatement(final Statement statement) throws RDFHandlerException {
		final Resource subject = statement.getSubject();
		final URI predicate = statement.getPredicate();
		final Value object = statement.getObject();
	
		predicate.getLocalName();
	
		if (subject.equals(lastSubject)) {
			// we will use comma and semicolon to make turtle document more readable while we still have same subject
			if (predicate.equals(lastPredicate)) {
				// comma,
				builder.append(",\n" + INDENT + INDENT);
				builder.append(getObject(object));
			} else {
				// semicolon;
				builder.append(";\n" + INDENT);
				builder.append(getPredicate(predicate));
				builder.append(getObject(object));
			}
		} else {
			// starting new subject
			closeLastSubject();
			builder.append(getSubject(subject));
			builder.append(getPredicate(predicate));
			builder.append(getObject(object));
		}
	
	}

	/**
	 * {@inheritDoc}
	 */
	public void endRDF() throws RDFHandlerException {
		if (!running) {
			throw new RuntimeException("Cannot endRDF: Obtaining results has not been started");
		}
		closeLastSubject();

		columnNames.add(SERIALIZATION_FORMAT + " result: ");
		final String[] row = new String[] { builder.toString() };
		rows.add(row);

		builder = null;
		namespaces = null;

		running = false;
		done = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getColumnNames() {
		if (!done) {
			throw new RuntimeException("Result column names were not collected yet.");
		}
		return columnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String[]> getRows() {
		if (!done) {
			throw new RuntimeException("Result rows were not collected yet.");
		}
		return rows;
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		columnNames = null;
		rows = null;
	}

	/**
	 * Method returns string representation of <i>RDF subject</i> in Turtle notation.
	 * 
	 * @param subject
	 *            the subject to be serialized
	 * @return RDF subject in Turtle notation
	 */
	private String getSubject(final Resource subject) {
		if (lastSubject == null) {
			lastSubject = subject;
			return getResource(subject);
		} else {
			throw new RuntimeException(
					"Error while building TURTLE document: Previous subject statement has not been closed.");
		}
	}

	/**
	 * Method returns string representation of <i>RDF predicate</i> in Turtle notation.
	 * 
	 * @param predicate
	 *            the predicate to be serialized
	 * @return RDF predicate in Turtle notation
	 */
	private String getPredicate(final URI predicate) {
		lastPredicate = predicate;
		return getURI(predicate);
	}

	/**
	 * Method returns string representation of <i>RDF object</i> in Turtle notation.
	 * 
	 * @param object
	 *            the object to be serialized
	 * @return RDF object in Turtle notation
	 */
	private String getObject(final Value object) {
		return getValue(object);
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
	private String getValue(final Value value) {
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
	private String getResource(final Resource resource) {
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
	private String getURI(final URI uri) {
		final String uriString = uri.toString();
		final String[] uriSplitted = splitByPrefix(uriString);

		if (uriSplitted.length == 0) {
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
	private String getBNode(final BNode bNode) {
		return "_:" + bNode.getID() + " ";
	}

	/**
	 * Returns provided <i>Literal</i> in Turtle notation.
	 * <p>
	 * Long strings are encoded as <i>"""Turtle long strings"""</i>. Returned string consist of literal's label (the
	 * value) and optionally <i>^^datatype</i> or <i>@lang</i> suffix (not both).
	 * 
	 * @param literal
	 *            a literal to be encoded to Turtle notation.
	 * @return string representation of provided literal.
	 */
	private String getLiteral(final Literal literal) {
		final String label = literal.getLabel();
		final StringBuilder result = new StringBuilder();
	
		if (label.indexOf('\n') > 0 || label.indexOf('\r') > 0 || label.indexOf('\t') > 0) {
			result.append("\"\"\"");
			result.append(TurtleUtil.encodeLongString(label));
			result.append("\"\"\"");
		} else {
			result.append("\"");
			result.append(TurtleUtil.encodeString(label));
			result.append("\"");
		}
	
		if (literal.getDatatype() != null) {
			result.append("^^" + getURI(literal.getDatatype()));
		} else if (literal.getLanguage() != null) {
			result.append("@" + literal.getLanguage() + " ");
		}
		return result.toString();
	}

	/**
	 * Provided URI address splits on two parts - namespace and path. Namespace is replaced by prefix.
	 * 
	 * @param uri
	 *            URI to be split
	 * @return String array containing <i>two</i> strings - existing prefix and path. Otherwise empty String array.
	 */
	private String[] splitByPrefix(final String uri) {
		String prefix = null;
		String path = null;
		final int index = TurtleUtil.findURISplitIndex(uri);
		if (index > 0) {
			final String namespace = uri.substring(0, index);
			prefix = namespaces.get(namespace);
			path = uri.substring(index);
		}
		if (prefix == null || path == null) {
			return new String[0];
		} else {
			return new String[] { prefix, path };
		}
	}

}
