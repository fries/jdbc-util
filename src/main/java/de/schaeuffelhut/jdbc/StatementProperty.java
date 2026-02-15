/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author Friedrich Schäuffelhut
 */
public interface StatementProperty<T> extends StatementParameter
{
    /**
     * Retrieves a value of type {@code T} after a statement has been executed.
     * This is typically used to extract generated keys from a {@link java.sql.Statement#getGeneratedKeys()} {@link java.sql.ResultSet}.
     *
     * @param stmt the {@link PreparedStatement} from which to extract the value.
     * @return the extracted value of type {@code T}.
     * @throws SQLException if a database access error occurs.
     */
    T get(PreparedStatement stmt) throws SQLException;
}
