package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.util.List;

import net.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;

public class SparqlTupleQueryResult extends SparqlAbstractQueryResult {
	private int currentRowIndex;
	private List<BindingSet> rows;
	private OntopiaResultHandler<List<BindingSet>> handler;

	/**
	 * constructor
	 * 
	 * @param
	 */

	public SparqlTupleQueryResult(OntopiaResultHandler<List<BindingSet>> handler) {
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

	public Object getValue(int ix) {
		String colname = getColumnName(ix);
		return getValue(colname);
	}

	public Object getValue(String colname) {
		BindingSet row = rows.get(currentRowIndex);
		Value value = row.getValue(colname);
		return value.stringValue();
	}

	public boolean next() {
		currentRowIndex++;
		return currentRowIndex < rows.size();
	}

}
