/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface for processing a {@link ResultSet} to produce a result of type {@code T}.
 * <p>
 * This interface defines a method for processing a {@link ResultSet} to produce an output of type {@code T}.
 * Implementations of this interface can define custom processing logic to handle data retrieved from the
 * {@link ResultSet}, such as aggregating values, transforming data, or mapping rows to specific objects.
 * </p>
 *
 * @param <T> The type of the result produced by processing the {@link ResultSet}.
 */
public interface ResultSetProcessor<T>
{
    T process(ResultSet resultSet) throws SQLException;
}
