/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * A utility class providing factory methods for common {@link StatementProperty} implementations.
 * These properties can be used with {@link StatementUtil#execute(GeneratedKeys, StatementProperty, String, StatementInParameter...)}
 * to extract information after a statement execution, such as generated keys.
 */
public abstract class StatementProperties
{
    private StatementProperties()
    {
    }

    /**
     * Creates an array of {@link StatementProperty} instances.
     * This is useful when an array is required but a var-args list is not directly supported by the method signature.
     *
     * @param ifcStatementProperties a var-args array of {@link StatementProperty} instances.
     * @return an array containing the provided {@link StatementProperty} instances.
     */
    public static StatementProperty<?>[] list(StatementProperty<?>... ifcStatementProperties)
    {
        return ifcStatementProperties;
    }

    /**
     * Creates a {@link StatementProperty} to retrieve a single generated key of a specified type.
     * This property will read the first generated key from the {@link ResultSet} and map it using the provided {@link ResultType}.
     *
     * @param resultType the {@link ResultType} to map the generated key to.
     * @param <T>        the type of the single generated key.
     * @return a {@code StatementProperty} for a single generated key.
     */
    public static <T> StatementProperty<T> GENERATED_KEY(ResultType<T> resultType)
    {
        return new GeneratedKeyStatementProperty<>( resultType );
    }

    /**
     * Creates a {@link StatementProperty} to retrieve multiple generated keys as a {@link List} of a specified type.
     * This property will read all generated keys from the {@link ResultSet} and map each using the provided {@link ResultType}.
     *
     * @param resultType the {@link ResultType} to map each generated key to.
     * @param <T>        the type of each generated key in the list.
     * @return a {@code StatementProperty} for a list of generated keys.
     */
    public static <T> StatementProperty<List<T>> GENERATED_KEYS(ResultType<T> resultType)
    {
        return new GeneratedKeysStatementProperty<>( resultType );
    }
}

record GeneratedKeyStatementProperty<T>(ResultType<T> resultType) implements StatementProperty<T>
{
    public T get(PreparedStatement stmt) throws SQLException
    {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys())
        {
            return ResultSetReaders
                    .<T>readOptional()
                    .readResult( generatedKeys, ResultSetMappers.scalar( resultType ) )
                    .orElse( null );
        }
    }

    public String modify(String sql)
    {
        return sql;
    }
}

record GeneratedKeysStatementProperty<T>(ResultType<T> resultType) implements StatementProperty<List<T>>
{
    public List<T> get(PreparedStatement stmt) throws SQLException
    {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys())
        {
            return ResultSetReaders
                    .<T>readMany()
                    .readResult( generatedKeys, ResultSetMappers.scalar( resultType ) );
        }
    }

    public String modify(String sql)
    {
        return sql;
    }
}
