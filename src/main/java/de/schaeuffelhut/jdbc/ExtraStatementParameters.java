/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.joda.time.DateTime;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import static de.schaeuffelhut.jdbc.StatementParameters.bindValue;

/**
 * A collection of less common or extended {@link StatementInParameterType} implementations.
 *
 * @author Friedrich Schäuffelhut
 * @since 2017-11-12
 */
public class ExtraStatementParameters
{
    /**
     * A {@code StatementInParameterType} that binds a Joda {@link DateTime} as a UTC timestamp.
     */
    public static StatementInParameterType<DateTime> JodaDateTimeAsUtcTimestamp = new JodaDateTimeAsUtcTimestamp();

    /**
     * Creates a {@link StatementInParameter} that binds a Joda {@link DateTime} as a UTC timestamp.
     *
     * @param value the {@code DateTime} value to bind, or {@code null}.
     * @return a new {@code StatementInParameter}.
     */
    public static StatementInParameter JodaDateTimeAsUtcTimestamp(DateTime value)
    {
        return bindValue( JodaDateTimeAsUtcTimestamp, value );
    }

    static class JodaDateTimeAsUtcTimestamp implements StatementInParameterType<DateTime>
    {
        private JodaDateTimeAsUtcTimestamp()
        {
        }

        @Override
        public String modify(String sql, DateTime value)
        {
            return sql;
        }

        @Override
        public int configure(PreparedStatement stmt, int pos, DateTime value) throws SQLException
        {
            Timestamp x;
            if (value == null)
                x = null;
            else
                x = new Timestamp( value.getMillis() );
            stmt.setTimestamp( pos, x, Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) ) );
            return 1;
        }
    }

    /**
     * Creates a {@link StatementInParameter} that binds a {@link UUID} as a string.
     *
     * @param value the {@code UUID} value to bind, or {@code null}.
     * @return a new {@code StatementInParameter}.
     */
    public static StatementInParameter UUID(UUID value)
    {
        return StatementParameters.bindValue( UUID, value );
    }

    /**
     * A {@code StatementInParameterType} that binds a {@link UUID} as a string.
     */
    public static final StatementInParameterType<UUID> UUID = new UUIDInParameterType();

    final static class UUIDInParameterType implements StatementInParameterType<UUID>
    {
        UUIDInParameterType()
        {
        }

        @Override
        public String modify(String sql, UUID uuid)
        {
            return sql;
        }

        public int configure(PreparedStatement stmt, int pos, UUID value) throws SQLException
        {
            stmt.setString( pos, value == null ? null : value.toString() );
            return 1;
        }
    }

}
