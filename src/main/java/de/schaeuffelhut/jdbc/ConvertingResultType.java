/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A JdbcUtil {@link ResultType} which delegates the data retrieval to an
 * other {@link ResultType} and then converts to the actual type.
 *
 * @author Friedrich Schäuffelhut
 *
 * @param <Tout>
 * @param <Tin>
 */
public abstract class ConvertingResultType<Tout, Tin> implements ResultType<Tout>
{
    final ResultType<Tin> delegate;

    public ConvertingResultType(ResultType<Tin> delegate)
    {
        this.delegate = delegate;
    }

    public Tout getResult(ResultSet resultSet, int index) throws SQLException
    {
        return convert( delegate.getResult( resultSet, index ) );
    }

    protected abstract Tout convert(Tin value);

    public abstract Class<Tout> getResultType();
}