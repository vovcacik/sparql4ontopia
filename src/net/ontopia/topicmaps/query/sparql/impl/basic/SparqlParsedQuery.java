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

public class SparqlParsedQuery implements ParsedQueryIF {

	final Logger logger = LoggerFactory.getLogger(SparqlParsedQuery.class);
	private String query;
	private ParsedQuery parsedQuery;
	private SparqlQueryProcessor processor;

	public SparqlParsedQuery(SparqlQueryProcessor processor, String query, ParsedQuery parsedQuery) {
		this.processor = processor;
		this.query = query;
		this.parsedQuery = parsedQuery;
	}

	public QueryResultIF execute() throws InvalidQueryException {
		return processor.execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(Map<String, ?> arguments) throws InvalidQueryException {
		// FIXME implement this
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
	// FIXME co tahle metoda dìlá? counted variables?
	public Collection<String> getCountedVariables() {
		// TupleExpr tuple = pq.getTupleExpr();
		// return tuple.getAssuredBindingNames();
		// TODO odpovídají assured binding names = counted variables?
		return new LinkedList<String>();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getOrderBy() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	public List<String> getSelectedVariables() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	public boolean isOrderedAscending(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return parsedQuery.toString();
	}
}
