package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.Map;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.QueryProcessorFactoryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;

/**
 * Factory for SPARQL query language processor. An instance of a
 * {@link SparqlQueryProcessorFactory} creates appropriate
 * {@link SparqlQueryProcessor} for the provided {@link TopicMapIF}.
 * 
 * @author Vlastimil OvË·ËÌk
 */
public class SparqlQueryProcessorFactory implements QueryProcessorFactoryIF {

	private final static String NAME = "SPARQL";

	/**
	 * {@inheritDoc}
	 */
	public QueryProcessorIF createQueryProcessor(TopicMapIF topicmap, LocatorIF base,
			Map<String, String> properties) {
		return new SparqlQueryProcessor(topicmap); // TODO base address param
	}

	/**
	 * {@inheritDoc}
	 */
	public String getQueryLanguage() {
		return NAME;
	}

}
