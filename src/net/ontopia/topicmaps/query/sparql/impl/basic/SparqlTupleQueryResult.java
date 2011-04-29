package net.ontopia.topicmaps.query.sparql.impl.basic;

import java.net.MalformedURLException;
import java.util.List;

import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.impl.tmapi2.TopicMapImpl;
import net.ontopia.topicmaps.query.sparql.impl.util.OntopiaResultHandler;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.tmapi.core.TopicMap;

public class SparqlTupleQueryResult extends SparqlAbstractQueryResult {
	private int currentRowIndex;
	private List<BindingSet> rows;
	private OntopiaResultHandler<List<BindingSet>> handler;
	private TopicMap tm;

	/**
	 * constructor
	 * 
	 * @param topicMap
	 * @param topicMapSystem
	 * 
	 * @param
	 */

	public SparqlTupleQueryResult(OntopiaResultHandler<List<BindingSet>> handler, TopicMap topicMap) {
		this.currentRowIndex = -1;
		this.columnNames = handler.getColumnNames();
		this.rows = handler.getRows();
		this.handler = handler;
		this.tm = topicMap;
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

		// stringValue may be item identifier URL which can be resolved to TMObjectIF (usually TopicIF) or literal
		// Affects displaying result in web interface
		String stringValue = value.stringValue();
		TMObjectIF object = getObjectByItemIdentifier(stringValue);

		if (object != null) {
			return object;
		} else {
			return stringValue;
		}
	}

	private TMObjectIF getObjectByItemIdentifier(String itemID) {
		URILocator loc;
		try {
			loc = new URILocator(itemID);
		} catch (MalformedURLException e) {
			return null;
		}
		TMObjectIF object = ((TopicMapImpl) tm).getWrapped().getObjectByItemIdentifier(loc);
		return object;

	}

	public boolean next() {
		currentRowIndex++;
		return currentRowIndex < rows.size();
	}

}
