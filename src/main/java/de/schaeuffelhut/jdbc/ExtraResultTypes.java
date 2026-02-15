/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;
import java.util.function.Function;

/**
 * A collection of less common or extended {@link ResultType} implementations.
 */
public class ExtraResultTypes
{
    /**
     * A {@code ResultType} that reads a string from the database and converts it to a {@link UUID}.
     */
    public static final UUIDStringConvertingResultType UUID = new UUIDStringConvertingResultType();

    /**
     * A {@code ResultType} that reads a UTC timestamp from the database and converts it to a Joda {@link DateTime}
     * object, respecting the default JVM time zone.
     */
    public final static ResultType<DateTime> UtcTimestampAsJodaDateTimeWithDefaultTimeZone = new UtcTimestampAsJodaDateTimeWithDefaultTimeZone();

    /**
     * A {@code ResultType} that reads a string from the database and converts it to a {@link URI}.
     */
    public final static ResultType<java.net.URI> URI = new URIResultType();

    /**
     * Creates a new {@link ConvertingResultType} that is aware of {@code null} values.
     * <p>
     * If the input value from the underlying {@code ResultType} is {@code null}, this converter
     * will immediately return {@code null} without invoking the converter function.
     *
     * @param outClass   the target class type after conversion.
     * @param resultType the underlying {@code ResultType} that reads the initial value from the {@code ResultSet}.
     * @param converter  the function to apply to non-null values.
     * @param <Tout>     the target type.
     * @param <Tin>      the source type from the database.
     * @return a new null-aware {@code ConvertingResultType}.
     */
    public static <Tout, Tin> ConvertingResultType<Tout, Tin> nullAwareConverter(Class<Tout> outClass, ResultType<Tin> resultType, Function<Tin, Tout> converter)
    {
        return new ConvertingResultType<>( resultType )
        {
            @Override
            protected Tout convert(Tin value)
            {
                if (value == null)
                    return null;
                return converter.apply( value );
            }

            @Override
            public Class<Tout> getResultType()
            {
                return outClass;
            }
        };
    }

    private static class UUIDStringConvertingResultType extends ConvertingResultType<UUID, String>
    {
        public UUIDStringConvertingResultType()
        {
            super( ResultTypes.String );
        }

        @Override
        protected UUID convert(String value)
        {
            return value == null ? null : java.util.UUID.fromString( value );
        }

        @Override
        public Class<UUID> getResultType()
        {
            return UUID.class;
        }
    }

    static class UtcTimestampAsJodaDateTimeWithDefaultTimeZone implements ResultType<DateTime>
    {
        private UtcTimestampAsJodaDateTimeWithDefaultTimeZone()
        {
        }

        @Override
        public final DateTime getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
        {
            Timestamp timestamp = resultSet.getTimestamp( index.next(), Calendar.getInstance( DateTimeZone.UTC.toTimeZone() ) );
            return timestamp == null ? null : new DateTime( timestamp.getTime() );
        }

        public Class<DateTime> getResultType()
        {
            return DateTime.class;
        }
    }

    static class URIResultType extends ConvertingResultType<java.net.URI, String>
    {

        public URIResultType()
        {
            super( ResultTypes.String );
        }

        @Override
        protected java.net.URI convert(String value)
        {
            if (value == null)
                return null;
            try
            {
                return new URI( value );
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException( e );
            }
        }

        @Override
        public Class<java.net.URI> getResultType()
        {
            return java.net.URI.class;
        }
    }
}
