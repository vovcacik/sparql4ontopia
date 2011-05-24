package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.List;

import net.ontopia.topicmaps.query.core.QueryResultIF;

/**
 * Abstract class providing basic implementation of some methods used in {@link QueryResultIF}.
 * 
 * @see {@link SparqlTupleQueryResult}
 * @see {@link SparqlGraphQueryResult}
 * 
 * @author Vlastimil OvË·ËÌk
 * 
 */
public abstract class AbstractQueryResult implements QueryResultIF {

	/**
	 * Type depends on {@link OntopiaResultHandler}.
	 * 
	 * @see {@link OntopiaResultHandler} getColumnNames() method
	 */
	protected List<String> columnNames;

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getColumnName(final int index) {
		return columnNames.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getColumnNames() {
		return columnNames.toArray(new String[columnNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getIndex(final String colname) {
		return columnNames.indexOf(colname);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getValues() {
		final int size = columnNames.size();
		Object[] row = new Object[size];
		for (int i = 0; i < size; i++) {
			row[i] = getValue(i);
		}
		return row;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getValues(final Object[] values) {
		final Object[] row = getValues();
		System.arraycopy(row, 0, values, 0, row.length);
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getWidth() {
		return columnNames.size();
	}

}
