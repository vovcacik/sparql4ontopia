package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.net.MalformedURLException;
import java.util.List;

import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.impl.tmapi2.TopicMapImpl;

import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.TopicMap;

/**
 * This class represents result of <i>SPARQL</i> tuple query.
 * <p>
 * It provides access to column names (result table header), sequential access to result values (result table rows) and
 * single table cell values.
 * 
 * @author Vlastimil OvË·ËÌk
 * @see {@link AbstractQueryResult}
 * 
 */
public class SparqlTupleQueryResult extends AbstractQueryResult {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparqlTupleQueryResult.class);
	private int currentRowIndex;
	private List<BindingSet> rows;
	private final OntopiaResultHandler<List<BindingSet>> handler;
	private final TopicMap topicMap;

	/**
	 * Constructor.
	 * <p>
	 * This instance should be closed with <code>close()</code> method.
	 * 
	 * @param handler
	 *            the handler used to gather results
	 * @param topicMap
	 *            the queried topic map
	 */
	public SparqlTupleQueryResult(final OntopiaResultHandler<List<BindingSet>> handler, final TopicMap topicMap) {
		this.currentRowIndex = -1;
		this.columnNames = handler.getColumnNames();
		this.rows = handler.getRows();
		this.handler = handler;
		this.topicMap = topicMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean next() {
		currentRowIndex++;
		return currentRowIndex < rows.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(final int index) {
		final String colname = getColumnName(index);
		return getValue(colname);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(final String colname) {
		final BindingSet row = rows.get(currentRowIndex);
		final Value value = row.getValue(colname);
		final String stringValue = value.stringValue();
        final TMObjectIF object = getObjectByItemURI(value);
		// Value is Literal or Resource. If it is resource it may be blank node or URI. Then URI is item identifier.
		// Affects displaying result in web interface.
		if (object == null) {
			return stringValue;
		} else {
			return object;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		handler.close();
		columnNames = null;
		rows = null;
	}

    /**
     * Provides access to {@link TMObjectIF} objects in queried topic map. Value param is subject or item identifier.
     * <p>
     * Item identifier is URL pointing to <i>topic map object</i> stored/defined in the topic map file. <br>
     * Format: <code>file:/path/TMName.ltm#objectID</code> <br>
     * An Example: <code>file:/ontopia/topicmaps/ItalianOpera.ltm#cause-of-death</code>
     * 
     * @param value
     *            URI supposed to identify topic map object
     * @return TMObjectIF instance representing the referenced object otherwise null
     */
    private TMObjectIF getObjectByItemURI(final Value value) {
		TMObjectIF object = null;
        if (value instanceof URI) {
			try {
				final URILocator loc = new URILocator(value.stringValue());
                TopicMapIF wrappedTM = ((TopicMapImpl) topicMap).getWrapped();

                // item identifier first
                object = wrappedTM.getObjectByItemIdentifier(loc);
                // subject identifier second
                if (object == null) {
                    object = wrappedTM.getTopicBySubjectIdentifier(loc);
                }
                // finally subject locator
                if (object == null) {
                    // TODO write test for this case
                    wrappedTM.getTopicBySubjectLocator(loc);
                }
			} catch (MalformedURLException e) {
                LOGGER.warn("Item locator is malformed.", e);
			}
		}
		return object;
	}
}
