package io.ruin.api.database;

import io.ruin.api.utils.ServerWrapper;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseStatement {

	interface Typed<R> {
		R execute(Connection connection) throws SQLException;
	}

	void execute(Connection connection) throws SQLException;

	default void failed(Throwable t) {
		ServerWrapper.logError("", t);
	}

}
