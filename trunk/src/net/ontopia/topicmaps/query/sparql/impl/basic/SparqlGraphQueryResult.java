package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.List;


import org.apache.commons.lang.StringEscapeUtils;

/**
 * This class represents result of <i>SPARQL</i> graph query.
 * <p>
 * It provides access to column names (result table header), sequential access to result values (result table rows) and
 * single table cell values.
 * <p>
 * Because result of <i>graph</i> query is single document in a RDF notation, the result table consist of: <br>
 * <b>Header</b> - usually name of the used RDF notation <br>
 * <b>One only cell</b> - containing the RDF document
 * 
 * @author Vlastimil OvË·ËÌk
 * 
 */
public class SparqlGraphQueryResult extends AbstractQueryResult {

	private List<String[]> rows;
	private int currentRowIndex;
	private final OntopiaResultHandler<List<String[]>> handler;

	/**
	 * Constructor.
	 * <p>
	 * The instance should be closed via <code>close()</code> method.
	 * 
	 * @param handler
	 *            handler used to gather and obtain results
	 */
	public SparqlGraphQueryResult(final OntopiaResultHandler<List<String[]>> handler) {
		currentRowIndex = -1;
		this.columnNames = handler.getColumnNames();
		this.rows = handler.getRows();
		this.handler = handler;
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
		final String[] row = rows.get(currentRowIndex);
		return escapeToCode(row[index]);

	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(final String colname) {
		return getValue(getIndex(colname));
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
	 * Formats code to HTML.
	 * 
	 * @param string
	 *            String representation of the code.
	 * @return HTML element preserving provided code string formatting.
	 */
	private String escapeToCode(final String string) {
		final String escapedStr = StringEscapeUtils.escapeHtml(string);
		return "<pre>" + escapedStr + "</pre>";
	}

}
