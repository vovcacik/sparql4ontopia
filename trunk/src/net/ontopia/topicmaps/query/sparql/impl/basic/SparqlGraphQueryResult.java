package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.ArrayList;
import java.util.List;

import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.sparql.impl.util.SparqlTurtleResultHandler;

import org.apache.commons.lang.StringEscapeUtils;

public class SparqlGraphQueryResult implements QueryResultIF {

	private List<String> columnNames;
	private List<String[]> rows;
	private int currentRowIndex;

	public SparqlGraphQueryResult(SparqlTurtleResultHandler handler) {
		columnNames = new ArrayList<String>();
		columnNames.add("Document");

		rows = new ArrayList<String[]>();
		String escapedDoc = StringEscapeUtils.escapeHtml(handler.getDocument());
		rows.add(new String[] { "<pre>" + escapedDoc + "</pre>" });

		currentRowIndex = -1;
	}

	public void close() {
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
		return row[ix];

	}

	public Object getValue(String colname) {
		return getValue(getIndex(colname));
	}

	public Object[] getValues() {
		return rows.get(currentRowIndex);
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
