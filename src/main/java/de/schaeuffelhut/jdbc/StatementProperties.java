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

public abstract class StatementProperties
{
    private StatementProperties()
    {
    }

    public static StatementProperty<?>[] list(StatementProperty<?>... ifcStatementProperties)
    {
        return ifcStatementProperties;
    }

    public static <T> StatementProperty<T> GENERATED_KEY(ResultType<T> resultType)
    {
        return new GeneratedKeyStatementProperty<>( resultType );
    }

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
