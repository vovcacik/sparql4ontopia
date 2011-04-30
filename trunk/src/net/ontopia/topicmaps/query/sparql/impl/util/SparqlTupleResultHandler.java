package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;

/**
 * This class receives <i>tuple</i> query result and provides access to it in tabular form.
 * <p>
 * In the first phase the solution is collected via push methods (<code>handleSolution()</code>). In the second phase
 * the result document can be obtain via get methods.
 * <p>
 * Handler implements two interfaces. One for OpenRDF side ({@link TupleQueryResultHandler}) and one for Ontopia side (
 * {@link OntopiaResultHandler})
 * 
 * @author Vlastimil OvË·ËÌk
 */
/**
 * @author Vlastimil OvË·ËÌk
 * 
 */
public class SparqlTupleResultHandler implements TupleQueryResultHandler, OntopiaResultHandler<List<BindingSet>> {

	private List<String> columnNames;
	private List<BindingSet> rows;

	/**
	 * Constructor.
	 */
	public SparqlTupleResultHandler() {
		columnNames = new ArrayList<String>();
		rows = new ArrayList<BindingSet>();
	}

	/**
	 * {@inheritDoc}
	 */
	public void startQueryResult(List<String> bindingNames) throws TupleQueryResultHandlerException {
		columnNames = bindingNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
		rows.add(bindingSet);
	}

	/**
	 * {@inheritDoc}
	 */
	public void endQueryResult() throws TupleQueryResultHandlerException {
		// TODO implement this
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		columnNames = null;
		rows = null;

	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BindingSet> getRows() {
		return rows;
	}

}
