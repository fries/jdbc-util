/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import de.schaeuffelhut.jdbc.ResultSetMappers.F1;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional interface for mapping a single row from a {@link java.sql.ResultSet} to a Java object.
 *
 * <p>Implementations are created via factory methods in {@link ResultSetMappers}, such as
 * {@link ResultSetMappers#scalar(ResultType)}, {@link ResultSetMappers#object(F1, ResultType)},
 * {@link ResultSetMappers#tuple(ResultType...)} or {@link ResultSetMappers#map(ResultType...)}.</p>
 *
 * <p>The {@link #map(ResultSet)} method is called <strong>once per row</strong>. The optional
 * {@link #initialize(ResultSet)} method allows caching of column metadata before the first row.</p>
 *
 * @param <T> the type of object produced from each row
 * @see ResultSetMappers
 * @see ResultType
 * @see java.sql.ResultSet
 * @see java.sql.SQLException
 * @since 1.0
 */
public interface ResultSetMapper<T>
{
    /**
     * Initializes the mapper before the first row is processed.
     *
     * <p>Called <strong>once</strong> when the result set is positioned before its first row.
     * Use to inspect {@link ResultSet#getMetaData()} and cache column information.</p>
     *
     * <p>The default implementation does nothing.</p>
     *
     * @param resultSet the result set to be mapped
     * @throws SQLException if metadata access fails
     * @see #map(ResultSet)
     */
    default void initialize(ResultSet resultSet) throws SQLException
    {
        // no-op
    }

    /**
     * Maps the current row of the result set to an instance of {@code T}.
     *
     * <p>Called for <strong>every row</strong>. The cursor is already on a valid row.</p>
     *
     * @param resultSet the result set positioned on the current row
     * @return the mapped object
     * @throws SQLException if a database error occurs
     * @see #initialize(ResultSet)
     */
    T map(ResultSet resultSet) throws SQLException;
}
