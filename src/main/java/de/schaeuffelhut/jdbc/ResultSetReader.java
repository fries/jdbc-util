/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface for reading and processing data from a {@link ResultSet} using a specified {@link ResultSetMapper}.
 * <p>
 * This interface defines a method for reading a {@link ResultSet} and mapping its rows to objects of type {@code T}
 * using the provided {@code resultMapper}. The mapped results are then processed to produce an output of type {@code R}.
 * Implementations can define custom behavior for reading and transforming {@link ResultSet} data as needed.
 * </p>
 *
 * @param <T> The type of object that each {@link ResultSet} row is mapped to using the {@code resultMapper}.
 * @param <R> The type of result produced after reading and processing the {@link ResultSet}.
 */
public interface ResultSetReader<T, R>
{
    /**
     * Reads and processes data from the given {@link ResultSet} using the specified {@link ResultSetMapper}.
     * <p>
     * This method applies the {@code resultMapper} to each row in the {@link ResultSet} to produce objects of
     * type {@code T}, and then processes those objects to produce a final result of type {@code R}. It throws
     * a {@link SQLException} if any SQL-related errors occur during reading or mapping.
     * </p>
     *
     * @param resultSet    The {@link ResultSet} containing data to read and map.
     * @param resultMapper The {@link ResultSetMapper} used to map each row of the {@link ResultSet} to an object of type {@code T}.
     * @return The processed result of type {@code R} after reading and mapping the {@link ResultSet}.
     * @throws SQLException if an SQL error occurs while reading or mapping the {@link ResultSet}.
     */
    R readResult(ResultSet resultSet, ResultSetMapper<T> resultMapper) throws SQLException;
}
