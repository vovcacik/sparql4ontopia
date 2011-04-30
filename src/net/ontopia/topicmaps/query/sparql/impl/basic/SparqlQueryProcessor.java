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
import net.ontopia.topicmaps.query.sparql.impl.util.SparqlTupleResultHandler;
import net.ontopia.topicmaps.query.sparql.impl.util.SparqlTurtleResultHandler;

import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.Query;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.UnsupportedQueryLanguageException;
import org.openrdf.query.impl.DatasetImpl;
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
	private final Logger logger = LoggerFactory.getLogger(SparqlQueryProcessor.class);
	private TopicMapSystem topicMapSystem;
	private TopicMap topicMap;
	private Repository repository;
	private String base;

	/**
	 * Constructor of {@link SparqlQueryProcessor}. Base URI address is unknown.
	 * 
	 * @param topicMap
	 *            topic map to be queried
	 */
	public SparqlQueryProcessor(TopicMapIF topicMap) {
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

	public SparqlQueryProcessor(TopicMapIF topicMap, String base) {
		this.base = base;
		try {
			TopicMapSystemFactory factory = TopicMapSystemFactory.newInstance();
			topicMapSystem = factory.newTopicMapSystem();
			// we need to get TMAPI this.topicMap from Ontopia (TopicMapIF) topicMap
			this.topicMap = ((MemoryTopicMapSystemImpl) topicMapSystem).createTopicMap(topicMap);
		} catch (FactoryConfigurationException e) {
			e.printStackTrace();
		} catch (TMAPIException e) {
			e.printStackTrace();
		}

		// Nikunau OpenRDF Store implementation for topic map system
		TmapiStore tmapiStore = new TmapiStore(topicMapSystem);
		// Repository is OpenRDF's main object for manipulation with data
		repository = new SailRepository(tmapiStore);
		try {
			repository.initialize();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		logger.info("SPARQL query processor initialized.");
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query) throws InvalidQueryException {
		Query q = null;
		RepositoryConnection con = null;

		try {
			con = repository.getConnection();
			q = con.prepareQuery(QueryLanguage.SPARQL, query, base);
			if (base != null) {
				// assure that only the graph base can be queried
				DatasetImpl dataSet = new DatasetImpl();
				// create new dataSet with single base URI and pass it to the query q
				dataSet.addDefaultGraph(con.getValueFactory().createURI(base));
				q.setDataset(dataSet);
			}
			if (q.getClass() == SailGraphQuery.class) {
				// Graph queries are used in CONSTRUCT sparql query form
				GraphQuery gq = null;
				gq = (GraphQuery) q;
				SparqlTurtleResultHandler handler = new SparqlTurtleResultHandler();
				// Ontopia's query tracer. It displays time needed to evaluate query in milliseconds.
				QueryTracer.startQuery();
				QueryTracer.trace("Length of evaluate() method in seconds", new String[0]);
				// this should mark entering ORDER BY statement in query but it marks very beginning of the query
				QueryTracer.enterOrderBy();
				gq.evaluate(handler);
				return new SparqlGraphQueryResult(handler);
			} else if (q.getClass() == SailTupleQuery.class) {
				// Tuple queries are used in other sparql query forms
				TupleQuery tq = null;
				tq = (TupleQuery) q;
				QueryTracer.startQuery();
				QueryTracer.trace("Length of evaluate() method in seconds", new String[0]);
				QueryTracer.enterOrderBy();
				SparqlTupleResultHandler handler = new SparqlTupleResultHandler();
				tq.evaluate(handler);
				return new SparqlTupleQueryResult(handler, topicMap);
			} else {
				throw new InvalidQueryException("Query is not GraphQuery or TupleQuery.");
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (TupleQueryResultHandlerException e) {
			e.printStackTrace();
		} catch (RDFHandlerException e) {
			e.printStackTrace();
		} finally {
			QueryTracer.leaveOrderBy();
			QueryTracer.endQuery();
			if (con != null) {
				try {
					con.close();
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, DeclarationContextIF context) throws InvalidQueryException {
		logger.warn("DeclarationContextIF parameter was not used in query. The query: " + query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arguments) throws InvalidQueryException {
		logger.warn("Parameters from arguments parameter were not bind to query. The query: " + query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arguments, DeclarationContextIF context)
			throws InvalidQueryException {
		logger.warn("DeclarationContextIF parameter was not used in query. "
				+ "Parameters from arguments parameter were not bind to query. The query: " + query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public void load(String ruleset) throws InvalidQueryException {
		logger.warn("Ruleset was not loaded into the query processor");
	}

	/**
	 * {@inheritDoc}
	 */
	public void load(Reader ruleset) throws InvalidQueryException, IOException {
		logger.warn("Ruleset was not loaded into the query processor");
	}

	/**
	 * {@inheritDoc}
	 */
	public ParsedQueryIF parse(String query) throws InvalidQueryException {
		ParsedQuery parsedQuery;
		try {
			parsedQuery = QueryParserUtil.parseQuery(QueryLanguage.SPARQL, query, base);
			return new SparqlParsedQuery(this, query, parsedQuery);
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (UnsupportedQueryLanguageException e) {
			e.printStackTrace();
		}
		throw new InvalidQueryException("Query could not be parsed. The query: " + query);
	}

	/**
	 * {@inheritDoc}
	 */
	public ParsedQueryIF parse(String query, DeclarationContextIF context) throws InvalidQueryException {
		logger.warn("DeclarationContextIF parameter was not used in parsed query. The query: " + query);
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
