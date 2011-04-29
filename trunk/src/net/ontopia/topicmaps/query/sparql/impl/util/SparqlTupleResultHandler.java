package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;

public class SparqlTupleResultHandler implements TupleQueryResultHandler,
		OntopiaResultHandler<List<BindingSet>> {

	private List<String> columnNames;
	private List<BindingSet> rows;

	public SparqlTupleResultHandler() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler#close()
	 */
	public void close() {
		columnNames = null;
		rows = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler#
	 * getColumnNames()
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler#getRows
	 * ()
	 */
	public List<BindingSet> getRows() {
		// TODO list<Object>??
		return rows;
	}

}
