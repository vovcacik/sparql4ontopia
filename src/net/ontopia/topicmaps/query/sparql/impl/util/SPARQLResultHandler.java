package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;

public class SPARQLResultHandler implements TupleQueryResultHandler {

	private List<String> columnNames;
	private List<BindingSet> rows;

	public SPARQLResultHandler() {
		columnNames = new ArrayList<String>();
		rows = new ArrayList<BindingSet>();
	}

	public void endQueryResult() throws TupleQueryResultHandlerException {
		// TODO doplnit zajištìní?!
	}

	public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
		rows.add(bindingSet);
	}

	public void startQueryResult(List<String> bindingNames) throws TupleQueryResultHandlerException {
		columnNames = bindingNames;
	}

	public String getColumnName(int index) {
		return columnNames.get(index);
	}

	public String[] getColumnNames() {
		return columnNames.toArray(new String[0]);
	}

	public int getIndex(String columnName) {
		if (columnNames.contains(columnName)) {
			return columnNames.indexOf(columnName);
		} else {
			return -1;
		}
	}

	public String getValue(String columnName, int rowIndex) {
		BindingSet row = rows.get(rowIndex);
		Value value = row.getValue(columnName);
		return value.stringValue();
	}

	public int getHeight() {
		return rows.size();
	}

	public void close() {
		columnNames = null;
		rows = null;
	}
}
