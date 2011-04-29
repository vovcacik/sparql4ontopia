package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.List;

import net.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler;

import org.apache.commons.lang.StringEscapeUtils;

public class SparqlGraphQueryResult extends SparqlAbstractQueryResult {

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

	public boolean next() {
		currentRowIndex++;
		return currentRowIndex < rows.size();
	}

}
