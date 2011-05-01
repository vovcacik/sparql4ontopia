package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.List;

import net.ontopia.topicmaps.query.sparql.impl.basic.AbstractQueryResult;

/**
 * Defines interface which any RDF result handler class should implement in order to be usable by
 * {@link AbstractQueryResult} subclass.
 * 
 * @author Vlastimil OvË·ËÌk
 * 
 * @param <R>
 *            Any type used to store result rows.
 */
public interface OntopiaResultHandler<R> {

	/**
	 * Method will free up all used resources.
	 */
	void close();

	/**
	 * Getter for column names (result table header).
	 * 
	 * @return List of column names ordered as in <code>SELECT</code> of a query
	 * @see {@link AbstractQueryResult}
	 */
	List<String> getColumnNames();

	/**
	 * Getter for result rows.
	 * 
	 * @return result rows
	 */
	R getRows();

}