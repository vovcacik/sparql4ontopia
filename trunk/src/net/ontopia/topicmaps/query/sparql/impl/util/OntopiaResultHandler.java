package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.List;

public interface OntopiaResultHandler<C, R> {

	public abstract void close();

	public abstract List<C> getColumnNames();

	public abstract List<R> getRows();

}