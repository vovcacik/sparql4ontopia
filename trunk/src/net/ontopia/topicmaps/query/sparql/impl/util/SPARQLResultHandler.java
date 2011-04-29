package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.ArrayList;
import java.util.List;

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

	public void close() {
		columnNames = null;
		rows = null;

	}

	// interface metody
	public List<String> getColumnNames() {
		// TODO return kopii
		return columnNames;
	}

	public List<BindingSet> getRows() {
		// TODO returnkopii
		// TODO list<Object>??
		return rows;
	}

}
