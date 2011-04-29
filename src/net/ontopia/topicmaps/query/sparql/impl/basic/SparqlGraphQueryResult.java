package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.List;

import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler;

import org.apache.commons.lang.StringEscapeUtils;

public class SparqlGraphQueryResult implements QueryResultIF {

	private List<String> columnNames;
	private List<String[]> rows;
	private int currentRowIndex;
	private OntopiaResultHandler<List<String[]>> handler;

	public SparqlGraphQueryResult(OntopiaResultHandler<List<String[]>> handler) {
		currentRowIndex = -1;
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
		String[] row = rows.get(currentRowIndex);
		return escape(row[ix]);

	}

	private String escape(String string) {
		String escapedStr = StringEscapeUtils.escapeHtml(string);
		return "<pre>" + escapedStr + "</pre>";
	}

	public Object getValue(String colname) {
		return getValue(getIndex(colname));
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
