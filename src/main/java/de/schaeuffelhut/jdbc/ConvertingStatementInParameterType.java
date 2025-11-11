/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public abstract class ConvertingStatementInParameterType<Tout, Tin> implements StatementInParameterType<Tin>
{
    final StatementInParameterType<Tout> delegate;

    public ConvertingStatementInParameterType(StatementInParameterType<Tout> delegate)
    {
        this.delegate = delegate;
    }

    public final String modify(String sql, Tin value)
    {
        return delegate.modify( sql, convert( value ) );
    }

    public final int configure(PreparedStatement stmt, int pos, Tin value) throws SQLException
    {
        return delegate.configure( stmt, pos, convert( value ) );
    }

    protected abstract Tout convert(Tin value);
}
