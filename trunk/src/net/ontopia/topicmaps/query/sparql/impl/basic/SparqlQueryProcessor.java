package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.impl.tmapi2.MemoryTopicMapSystemImpl;
import net.ontopia.topicmaps.query.core.DeclarationContextIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedModificationStatementIF;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.impl.basic.QueryTracer;
import net.ontopia.topicmaps.query.sparql.impl.sesame.SparqlTupleResultHandler;
import net.ontopia.topicmaps.query.sparql.impl.sesame.SparqlTurtleResultHandler;

import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.Query;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.UnsupportedQueryLanguageException;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.QueryParserUtil;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailGraphQuery;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailTupleQuery;
import org.openrdf.rio.RDFHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.sesame.sail.tmapi.TmapiStore;

/**
 * This is SPARQL query processor which is used to parse and execute queries. You can get the instance with
 * {@link SparqlQueryProcessorFactory}.
 * 
 * @author Vlastimil OvË·ËÌk
 * 
 */
public class SparqlQueryProcessor implements QueryProcessorIF {
	private static final Logger LOGGER = LoggerFactory.getLogger(SparqlQueryProcessor.class);
	private TopicMap topicMap;
	private Repository repository;
	private String base;

	/**
	 * Constructor of {@link SparqlQueryProcessor}. Base URI address is unknown.
	 * 
	 * @param topicMap
	 *            topic map to be queried
	 */
	public SparqlQueryProcessor(final TopicMapIF topicMap) {
		this(topicMap, null);
	}

	/**
	 * Constructor of {@link SparqlQueryProcessor}.
	 * 
	 * @param topicMap
	 *            topic map to be queried
	 * @param base
	 *            known base URI of topic map
	 */

	public SparqlQueryProcessor(final TopicMapIF topicMap, final String base) {
		this.base = base;
		TopicMapSystem topicMapSystem = null;
		try {
			final TopicMapSystemFactory factory = TopicMapSystemFactory.newInstance();
			topicMapSystem = factory.newTopicMapSystem();
			// we need to get TMAPI this.topicMap from Ontopia (TopicMapIF) topicMap
			this.topicMap = ((MemoryTopicMapSystemImpl) topicMapSystem).createTopicMap(topicMap);
		} catch (FactoryConfigurationException e) {
			LOGGER.warn("Creating new TopicMapSystemFactory failed.", e);
		} catch (TMAPIException e) {
			LOGGER.warn("Creating new TopicMapSystem failed.", e);
		}

		// Nikunau OpenRDF Store implementation for topic map system
		final TmapiStore tmapiStore = new TmapiStore(topicMapSystem);
		// Repository is OpenRDF's main object for manipulation with data
		repository = new SailRepository(tmapiStore);
		try {
			repository.initialize();
		} catch (RepositoryException e) {
			LOGGER.warn("Sail repository initialization failed.", e);
		}
		LOGGER.info("SparqlQueryProcessor initialized. Base URL: {}.", this.base);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(final String query) throws InvalidQueryException {
		QueryResultIF queryResult = null;
		RepositoryConnection con = null;

		try {
			con = repository.getConnection();
			final Query queryRDF = con.prepareQuery(QueryLanguage.SPARQL, query, base);
			// if (base != null) {
			// // FIXME why Armin needed this code? it breaks funcionality.
			// // assure that only the graph base can be queried
			// final DatasetImpl dataSet = new DatasetImpl();
			// // create new dataSet with single base URI and pass it to the query q
			// dataSet.addDefaultGraph(con.getValueFactory().createURI(base));
			// q.setDataset(dataSet);
			// }
			if (queryRDF.getClass() == SailGraphQuery.class) {
				// Graph queries are used in CONSTRUCT sparql query form
				final GraphQuery graphQuery = (GraphQuery) queryRDF;
				final SparqlTurtleResultHandler handler = new SparqlTurtleResultHandler();
				// Ontopia's query tracer. It displays time needed to evaluate query in milliseconds.
				QueryTracer.startQuery();
				QueryTracer.trace("Length of evaluate() method in seconds", new String[0]);
				// this should mark entering ORDER BY statement in query but it marks very beginning of the query
				QueryTracer.enterOrderBy();
				graphQuery.evaluate(handler);
				queryResult = new SparqlGraphQueryResult(handler);
			} else if (queryRDF.getClass() == SailTupleQuery.class) {
				// Tuple queries are used in other sparql query forms
				final TupleQuery tupleQuery = (TupleQuery) queryRDF;
				QueryTracer.startQuery();
				QueryTracer.trace("Length of evaluate() method in seconds", new String[0]);
				QueryTracer.enterOrderBy();
				final SparqlTupleResultHandler handler = new SparqlTupleResultHandler();
				tupleQuery.evaluate(handler);
				queryResult = new SparqlTupleQueryResult(handler, topicMap);
			} else {
				throw new InvalidQueryException("Query is not GraphQuery or TupleQuery.");
			}
		} catch (RepositoryException e) {
			LOGGER.warn("While preparing query evaluation an error occured.", e);
		} catch (MalformedQueryException e) {
			LOGGER.warn("While preparing query evaluation an error occured.", e);
		} catch (QueryEvaluationException e) {
			LOGGER.warn("While evaluating query an error occured.", e);
		} catch (TupleQueryResultHandlerException e) {
			LOGGER.warn("While evaluating query an error occured.", e);
		} catch (RDFHandlerException e) {
			LOGGER.warn("While evaluating query an error occured.", e);
		} finally {
			QueryTracer.leaveOrderBy();
			QueryTracer.endQuery();
			if (con != null) {
				try {
					con.close();
				} catch (RepositoryException e) {
					LOGGER.warn("Connection could not be closed.", e);
				}
			}
		}
		return queryResult;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, DeclarationContextIF context) throws InvalidQueryException {
		LOGGER.warn("DeclarationContextIF parameter was not used in query. The query: " + query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arguments) throws InvalidQueryException {
		LOGGER.warn("Parameters from arguments parameter were not bind to query. The query: " + query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arguments, DeclarationContextIF context)
			throws InvalidQueryException {
		LOGGER.warn("DeclarationContextIF parameter was not used in query. "
				+ "Parameters from arguments parameter were not bind to query. The query: " + query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public void load(String ruleset) throws InvalidQueryException {
		LOGGER.warn("Ruleset was not loaded into the query processor");
	}

	/**
	 * {@inheritDoc}
	 */
	public void load(Reader ruleset) throws InvalidQueryException, IOException {
		LOGGER.warn("Ruleset was not loaded into the query processor");
	}

	/**
	 * {@inheritDoc}
	 */
	public ParsedQueryIF parse(final String query) throws InvalidQueryException {
		ParsedQuery parsedQuery;
		try {
			parsedQuery = QueryParserUtil.parseQuery(QueryLanguage.SPARQL, query, base);
			return new SparqlParsedQuery(this, query, parsedQuery);
		} catch (MalformedQueryException e) {
			LOGGER.warn("While parsing SPARQL query an exception occured.", e);
		} catch (UnsupportedQueryLanguageException e) {
			LOGGER.warn("While parsing SPARQL query an exception occured.", e);
		}
		throw new InvalidQueryException("Query could not be parsed. The query: " + query);
	}

	/**
	 * {@inheritDoc}
	 */
	public ParsedQueryIF parse(String query, DeclarationContextIF context) throws InvalidQueryException {
		LOGGER.warn("DeclarationContextIF parameter was not used in parsed query. The query: " + query);
		return parse(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public ParsedModificationStatementIF parseUpdate(String statement) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public ParsedModificationStatementIF parseUpdate(String statement, DeclarationContextIF context)
			throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public int update(String query) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public int update(String query, DeclarationContextIF context) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public int update(String query, Map<String, ?> arguments) throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public int update(String query, Map<String, ?> arguments, DeclarationContextIF context)
			throws InvalidQueryException {
		throw new UnsupportedOperationException();
	}

}
