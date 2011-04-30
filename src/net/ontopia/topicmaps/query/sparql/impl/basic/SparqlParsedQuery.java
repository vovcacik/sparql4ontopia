package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;

import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.parser.ParsedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents parsed SPARQL query.
 * 
 * @author Vlastimil OvË·ËÌk
 * 
 */
public class SparqlParsedQuery implements ParsedQueryIF {

	final Logger logger = LoggerFactory.getLogger(SparqlParsedQuery.class);
	private String query;
	private ParsedQuery parsedQuery;
	private SparqlQueryProcessor processor;

	/**
	 * Constructor. It requires SparqlQueryProcessor and (String) query for executing the query.
	 * 
	 * @param processor
	 *            sparql query processor used to execute the query
	 * @param query
	 *            string of the query
	 * @param parsedQuery
	 *            parsed query
	 */
	public SparqlParsedQuery(SparqlQueryProcessor processor, String query, ParsedQuery parsedQuery) {
		this.processor = processor;
		this.query = query;
		this.parsedQuery = parsedQuery;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute() throws InvalidQueryException {
		return processor.execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(Map<String, ?> arguments) throws InvalidQueryException {
		logger.warn("Parameters from arguments parameter were not bind to query. The query: "
				+ query);
		return execute();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getAllVariables() {
		TupleExpr tuple = parsedQuery.getTupleExpr();
		return tuple.getBindingNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getCountedVariables() {
		return new LinkedList<String>();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getOrderBy() {
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getSelectedVariables() {
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOrderedAscending(String name) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return parsedQuery.toString();
	}
}
