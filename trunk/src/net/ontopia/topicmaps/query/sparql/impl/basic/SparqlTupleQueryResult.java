package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.List;

import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;

public class SparqlTupleQueryResult implements QueryResultIF {

	private int currentRowIndex;
	private List<String> columnNames;
	private List<BindingSet> rows;
	private OntopiaResultHandler<String, BindingSet> handler;

	/**
	 * constructor
	 * 
	 * @param
	 */

	public SparqlTupleQueryResult(OntopiaResultHandler<String, BindingSet> handler) {
		this.currentRowIndex = -1;
		this.columnNames = handler.getColumnNames();
		this.rows = handler.getRows();
		this.handler = handler;
	}

	public void close() {
		handler.close();
		columnNames = null;
		rows = null;
	}

	public String getColumnName(int ix) {
		return columnNames.get(ix);
	}

	public String[] getColumnNames() {
		return columnNames.toArray(new String[0]);
	}

	public int getIndex(String colname) {
		return columnNames.indexOf(colname);
	}

	public Object getValue(int ix) {
		String colname = getColumnName(ix);
		return getValue(colname);
	}

	public Object getValue(String colname) {
		BindingSet row = rows.get(currentRowIndex);
		Value value = row.getValue(colname);
		return value.stringValue();
	}

	public Object[] getValues() {
		int size = columnNames.size();
		Object[] row = new Object[size];
		for (int i = 0; i < size; i++) {
			row[i] = getValue(i);
		}
		return row;
	}

	public Object[] getValues(Object[] values) {
		Object[] row = getValues();
		System.arraycopy(row, 0, values, 0, row.length);
		return values;
	}

	public int getWidth() {
		return columnNames.size();
	}

	public boolean next() {
		currentRowIndex++;
		return currentRowIndex < rows.size();
	}

}
