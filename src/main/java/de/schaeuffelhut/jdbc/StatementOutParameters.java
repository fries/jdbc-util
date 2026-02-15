/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Factory class for creating common {@link StatementOutParameter} instances
 * for use with {@link StatementUtil#executeCall(String, StatementParameter...)}.
 */
public class StatementOutParameters
{
    private StatementOutParameters()
    {
    }

    /**
     * A {@link StatementOutParameter} for registering and reading {@link Integer} OUT parameters.
     */
    public final static StatementOutParameter<Integer> Integer = new IntegerOutParameter();
    /**
     * A {@link StatementOutParameter} for registering and reading {@link Long} OUT parameters.
     */
    public final static StatementOutParameter<Long> Long = new LongOutParameter();
    /**
     * A {@link StatementOutParameter} for registering and reading {@link String} OUT parameters.
     */
    public final static StatementOutParameter<String> String = new StringOutParameter();
    /**
     * A {@link StatementOutParameter} for registering and reading {@link java.util.UUID} OUT parameters from a string.
     */
    public final static StatementOutParameter<java.util.UUID> UUID = new UUIDOutParameter();

}

final class IntegerOutParameter implements StatementOutParameter<Integer>
{
    public int configure(CallableStatement stmt, int index) throws SQLException
    {
        stmt.registerOutParameter( index, Types.INTEGER );
        return 1;
    }

    public Integer readValue(CallableStatement stmt, int index) throws SQLException
    {
        int value = stmt.getInt( index );
        return stmt.wasNull() ? null : value;
    }

    public String modify(String sql)
    {
        return sql;
    }
}

final class LongOutParameter implements StatementOutParameter<Long>
{
    public int configure(CallableStatement stmt, int index) throws SQLException
    {
        stmt.registerOutParameter( index, Types.BIGINT );
        return 1;
    }

    public Long readValue(CallableStatement stmt, int index) throws SQLException
    {
        long value = stmt.getLong( index );
        return stmt.wasNull() ? null : value;
    }

    public String modify(String sql)
    {
        return sql;
    }
}

final class StringOutParameter implements StatementOutParameter<String>
{
    public int configure(CallableStatement stmt, int index) throws SQLException
    {
        stmt.registerOutParameter( index, Types.VARCHAR );
        return 1;
    }

    public String readValue(CallableStatement stmt, int index) throws SQLException
    {
        return stmt.getString( index );
    }

    public String modify(String sql)
    {
        return sql;
    }
}

final class UUIDOutParameter implements StatementOutParameter<java.util.UUID>
{
    public int configure(CallableStatement stmt, int index) throws SQLException
    {
        stmt.registerOutParameter( index, Types.VARCHAR );
        return 1;
    }

    public java.util.UUID readValue(CallableStatement stmt, int index) throws SQLException
    {
        String value = stmt.getString( index );
        return value == null ? null : java.util.UUID.fromString(value);
    }

    public String modify(String sql)
    {
        return sql;
    }
}