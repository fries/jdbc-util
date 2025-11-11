/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author Friedrich Schäuffelhut
 */
public record BoundValue<T>(StatementInParameterType<T> parameter, T value) implements StatementInParameter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( BoundValue.class );

    @Override
    public String modify(String sql)
    {
        return parameter.modify( sql, value );
    }

    @Override
    public int configure(PreparedStatement stmt, int index) throws SQLException
    {
        if (LOGGER.isTraceEnabled())
            if (value == null)
                LOGGER.trace( String.format( "setting param %d to null", index ) );
            else
                LOGGER.trace( String.format(
                        "setting param %d (%s) = %s",
                        index, value.getClass().getSimpleName(), value ) );

        return parameter.configure( stmt, index, value );
    }
}
