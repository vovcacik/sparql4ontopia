package net.ontopia.topicmaps.query.sparql.impl.basic;

import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.sparql.impl.util.SPARQLResultHandler;

public class SparqlTupleQueryResult implements QueryResultIF {

	private int currentRowIndex;
	private SPARQLResultHandler handler;

	/**
	 * constructor
	 * 
	 * @param
	 */

	public SparqlTupleQueryResult(SPARQLResultHandler handler) {
		this.currentRowIndex = -1;
		this.handler = handler;
	}

	public void close() {
		handler.close();
	}

	public String getColumnName(int ix) {
		return handler.getColumnName(ix);
	}

	public String[] getColumnNames() {
		return handler.getColumnNames();
	}

	public int getIndex(String colname) {
		return handler.getIndex(colname);
	}

	public Object getValue(int ix) {
		String colname = handler.getColumnName(ix);
		return getValue(colname);
	}

	public Object getValue(String colname) {
		return handler.getValue(colname, currentRowIndex);
		// TODO colname via get index method
	}

	public Object[] getValues() {
		int size = handler.getColumnNames().length;
		Object[] row = new Object[size];
		for (int i = 0; i < size; i++) {
			String colname = handler.getColumnName(i);
			row[i] = handler.getValue(colname, currentRowIndex);
		}
		return row;
	}

	public Object[] getValues(Object[] values) {
		Object[] row = getValues();
		System.arraycopy(row, 0, values, 0, row.length);
		return values;
	}

	public int getWidth() {
		return handler.getColumnNames().length;
	}

	public boolean next() {
		currentRowIndex++;
		return currentRowIndex < handler.getHeight();
	}

}
