package net.ontopia.topicmaps.query.sparql.impl.util;

import java.util.List;

public interface OntopiaResultHandler<R> {

	public abstract void close();

	public abstract List<String> getColumnNames();

	public abstract R getRows();

}