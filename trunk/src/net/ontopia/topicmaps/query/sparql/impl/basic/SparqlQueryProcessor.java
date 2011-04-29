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
import net.ontopia.topicmaps.query.sparql.impl.util.SPARQLResultHandler;
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
 * This is SPARQL query processor which is used to execute queries.
 * 
 * @author Vlastimil Ovèáèík
 * 
 */
public class SparqlQueryProcessor implements QueryProcessorIF {
	// TODO docs
	private final Logger logger = LoggerFactory.getLogger(SparqlQueryProcessor.class);
	private TopicMapSystem topicMapSystem;
	private TopicMap topicMap;
	private Repository repository;
	private String baseIRI;

	/**
	 * Constructor of {@link SparqlQueryProcessor}.TODO doc
	 * 
	 * @param topicMap
	 *            topic map to be queried
	 */

	public SparqlQueryProcessor(TopicMapIF topicMap) {
		try {
			TopicMapSystemFactory factory = TopicMapSystemFactory.newInstance();
			topicMapSystem = factory.newTopicMapSystem();
			this.topicMap = ((MemoryTopicMapSystemImpl) topicMapSystem).createTopicMap(topicMap);
			baseIRI = this.topicMap.getLocator().toExternalForm();
		} catch (FactoryConfigurationException e) {
			e.printStackTrace();
		} catch (TMAPIException e) {
			e.printStackTrace();
		}

		TmapiStore tmapiStore = new TmapiStore(topicMapSystem);
		// TODO nastavení tmapiStore??
		repository = new SailRepository(tmapiStore);
		try {
			repository.initialize();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}

		logger.info("SPARQL query processor initialized.");
	}

	/**
	 * TODO doc
	 */
	@SuppressWarnings("null")
	public QueryResultIF execute(String query) throws InvalidQueryException {
		Query q = null;
		RepositoryConnection con = null;

		try {
			con = repository.getConnection();
			q = con.prepareQuery(QueryLanguage.SPARQL, query, null);
			if (baseIRI != null) {
				// TODO co tohle dìlá? dát za try{}
				// assure that only the graph baseIRI can be queried
				DatasetImpl dataSet = new DatasetImpl();
				dataSet.addDefaultGraph(con.getValueFactory().createURI(baseIRI));
				q.setDataset(dataSet);
			}
			if (q.getClass() == SailGraphQuery.class) {
				GraphQuery gq = null;
				gq = (GraphQuery) q;
				QueryTracer.startQuery();
				QueryTracer.trace("Length of evaluate() method in seconds", new String[0]);
				QueryTracer.enterOrderBy();
				SparqlTurtleResultHandler handler = new SparqlTurtleResultHandler();
				gq.evaluate(handler);
				return new SparqlGraphQueryResult(handler);
			} else if (q.getClass() == SailTupleQuery.class) {
				TupleQuery tq = null;
				tq = (TupleQuery) q;
				QueryTracer.startQuery();
				QueryTracer.trace("Length of evaluate() method in seconds", new String[0]);
				QueryTracer.enterOrderBy();
				SPARQLResultHandler handler = new SPARQLResultHandler();
				tq.evaluate(handler);
				return new SparqlTupleQueryResult(handler);
				// TODO proè to dìlám pøes handler zkusit pøímo na výsledek?

			} else {
				// nepodporovaný typ dotazu (boolean)
			}
			// ArrayList<String> list = new ArrayList<String>();
			// list.add("list");
			// String[] array = { "a", "b", "c" };
			// QueryTracer.trace("1", array);
			// QueryTracer.enter(list);
			// QueryTracer.leave(list);
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TupleQueryResultHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RDFHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			QueryTracer.leaveOrderBy();
			QueryTracer.endQuery();
			try {
				con.close();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * TODO doc
	 */
	@SuppressWarnings("null")
	public QueryResultIF executeTuple(String query) throws InvalidQueryException {
		TupleQuery tq = null;
		RepositoryConnection con = null;
		SPARQLResultHandler handler = new SPARQLResultHandler();

		try {
			con = repository.getConnection();
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query, null);
			if (baseIRI != null) {
				// TODO co tohle dìlá?
				// assure that only the graph baseIRI can be queried
				DatasetImpl dataSet = new DatasetImpl();
				dataSet.addDefaultGraph(con.getValueFactory().createURI(baseIRI));
				tq.setDataset(dataSet);
			}
			// ArrayList<String> list = new ArrayList<String>();
			// list.add("list");
			// String[] array = { "a", "b", "c" };
			// QueryTracer.trace("1", array);
			// QueryTracer.enter(list);
			// QueryTracer.leave(list);
			QueryTracer.startQuery();
			QueryTracer.trace("Length of evaluate() method in seconds", new String[0]);
			QueryTracer.enterOrderBy();
			tq.evaluate(handler);
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (TupleQueryResultHandlerException e) {
			e.printStackTrace();
		} finally {
			QueryTracer.leaveOrderBy();
			QueryTracer.endQuery();
			try {
				con.close();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
		return new SparqlTupleQueryResult(handler);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, DeclarationContextIF context)
			throws InvalidQueryException {
		logger.warn("DeclarationContextIF parameter was not used in query. The query: " + query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arguments)
			throws InvalidQueryException {
		logger.warn("Parameters from arguments parameter were not bind to query. The query: "
				+ query);
		return execute(query);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResultIF execute(String query, Map<String, ?> arguments,
			DeclarationContextIF context) throws InvalidQueryException {
		logger
				.warn("DeclarationContextIF parameter was not used in query. Parameters from arguments parameter were not bind to query. The query: "
						+ query);
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
	 * TODO doc
	 */
	public ParsedQueryIF parse(String query) throws InvalidQueryException {
		ParsedQuery parsedQuery;
		try {
			parsedQuery = QueryParserUtil.parseQuery(QueryLanguage.SPARQL, query, baseIRI);
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
	public ParsedQueryIF parse(String query, DeclarationContextIF context)
			throws InvalidQueryException {
		logger.warn("DeclarationContextIF parameter was not used in parsed query. The query: "
				+ query);
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
