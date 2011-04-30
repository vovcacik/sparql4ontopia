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
	private boolean running;
	private boolean done;

	/**
	 * Constructor.
	 */
	public SparqlTupleResultHandler() {
		columnNames = new ArrayList<String>();
		rows = new ArrayList<BindingSet>();
		running = false;
		done = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void startQueryResult(List<String> bindingNames) throws TupleQueryResultHandlerException {
		if (running || done) {
			throw new RuntimeException("Obtaining results has been already started.");
		}
		columnNames = bindingNames;
		running = true;
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
		if (!running) {
			throw new RuntimeException("Cannot endQueryResult: Obtaining results has not been started.");
		}
		running = false;
		done = true;
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
		if (!done) {
			throw new RuntimeException("Result column names were not collected yet.");
		}
		return columnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<BindingSet> getRows() {
		if (!done) {
			throw new RuntimeException("Result rows were not collected yet.");
		}
		return rows;
	}

}
