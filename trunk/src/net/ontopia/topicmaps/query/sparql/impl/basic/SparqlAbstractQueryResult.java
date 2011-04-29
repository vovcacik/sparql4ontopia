package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.List;

import net.ontopia.topicmaps.query.core.QueryResultIF;

public abstract class SparqlAbstractQueryResult implements QueryResultIF {

	protected List<String> columnNames;

	public String getColumnName(int ix) {
		return columnNames.get(ix);
	}

	public String[] getColumnNames() {
		return columnNames.toArray(new String[0]);
	}

	public int getIndex(String colname) {
		return columnNames.indexOf(colname);
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

}
