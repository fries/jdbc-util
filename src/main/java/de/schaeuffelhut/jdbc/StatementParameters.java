/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import org.joda.time.*;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Friedrich Schäuffelhut
 */
public class StatementParameters
{
    public final static StatementInParameterType<Boolean> Boolean = new BooleanInParameterType();

    public static StatementInParameter Boolean(Boolean value)
    {
        return bindValue( Boolean, value );
    }

    public final static StatementInParameterType<Boolean> BooleanAsInteger = new BooleanAsIntegerInParameterType();

    public static StatementInParameter BooleanAsInteger(Boolean value)
    {
        return bindValue( BooleanAsInteger, value );
    }

    public final static StatementInParameterType<Byte> Byte = new ByteInParameterType();

    public static StatementInParameter Byte(Byte value)
    {
        return bindValue( Byte, value );
    }

    public final static StatementInParameterType<Character> Character = new CharacterInParameterType();

    public static StatementInParameter Character(Character value)
    {
        return bindValue( Character, value );
    }

    public final static StatementInParameterType<Short> Short = new ShortInParameterType();

    public static StatementInParameter Short(Short value)
    {
        return bindValue( Short, value );
    }

    public final static StatementInParameterType<Integer> Integer = new IntegerInParameterType();

    public static StatementInParameter Integer(Integer value)
    {
        return bindValue( Integer, value );
    }

    public final static StatementInParameterType<Long> Long = new LongInParameterType();

    public static StatementInParameter Long(Long value)
    {
        return bindValue( Long, value );
    }

    public final static StatementInParameterType<Float> Float = new FloatInParameterType();

    public static StatementInParameter Float(Float value)
    {
        return bindValue( Float, value );
    }

    public final static StatementInParameterType<Double> Double = new DoubleInParameterType();

    public static StatementInParameter Double(Double value)
    {
        return bindValue( Double, value );
    }

    public final static StatementInParameterType<BigDecimal> BigDecimal = new BigDecimalInParameterType();

    public static StatementInParameter BigDecimal(java.math.BigDecimal value)
    {
        return bindValue( BigDecimal, value );
    }

    public final static StatementInParameterType<String> String = new StringInParameterType();

    public static StatementInParameter String(String value)
    {
        return bindValue( String, value );
    }

    public final static StatementInParameterType<Collection<String>> CollectionOfString = new CollectionOfStringInParameterType();

    public static StatementInParameter StringArray(Collection<java.lang.String> value)
    {
        return bindValue( CollectionOfString, value );
    }

    public final static StatementInParameterType<Date> Date = new DateInParameterType();

    public static StatementInParameter Date(Date value)
    {
        return bindValue( Date, value );
    }

    public final static StatementInParameterType<Timestamp> Timestamp = new TimestampInParameterType();

    public static StatementInParameter Timestamp(Timestamp value)
    {
        return bindValue( Timestamp, value );
    }

    public final static StatementInParameterType<ZonedDateTime> ZonedDateTimeAtUTC = new ZonedDateTimeAtUTCInParameterType();

    public static StatementInParameter ZonedDateTimeAtUtc(ZonedDateTime value)
    {
        return bindValue( ZonedDateTimeAtUTC, value );
    }

    public final static StatementInParameterType<Object> Object = new ObjectInParameterType();

    public static StatementInParameter Object(Object value)
    {
        return bindValue( Object, value );
    }

    public final static StatementInParameterType<Object> Serializeable = new SerializableInParameterType();

    public static StatementInParameter Serializeable(Object value)
    {
        return bindValue( Serializeable, value );
    }

    public final static StatementInParameterType<byte[]> Bytes = new BytesInParameterType();

    public static StatementInParameter Bytes(byte[] value)
    {
        return bindValue( Bytes, value );
    }

    public final static StatementInParameterType<Enum<?>> EnumByName = new EnumByNameParameterType();

    public static StatementInParameter EnumByName(Enum<?> value)
    {
        return bindValue( EnumByName, value );
    }

    public final static StatementInParameterType<EnumIntKey> EnumByIntKey = new EnumByIntKeyParameterType();

    public static <E extends Enum<E> & EnumIntKey> StatementInParameter EnumByIntKey(E value)
    {
        return bindValue( EnumByIntKey, value );
    }

    public final static StatementInParameterType<Class<?>> Class = new ClassParameterType();

    public static StatementInParameter Class(Class<?> value)
    {
        return bindValue( Class, value );
    }

    public final static StatementInParameterType<DateTime> DateTime = new DateTimeInParameterType();

    public static StatementInParameter DateTime(DateTime value)
    {
        return bindValue( DateTime, value );
    }

    public final static StatementInParameterType<DateMidnight> DateMidnight = new DateMidnightInParameterType();

    public static StatementInParameter DateMidnight(org.joda.time.DateMidnight value)
    {
        return bindValue( DateMidnight, value );
    }

    public final static StatementInParameterType<DateMidnight> DateMidnightAsIsoString = new DateMidnightAsIsoStringInParameterType();

    public static StatementInParameter DateMidnightAsIsoString(org.joda.time.DateMidnight value)
    {
        return bindValue( DateMidnightAsIsoString, value );
    }

    public final static StatementInParameterType<Days> Days = new DaysInParameterType();

    public static StatementInParameter Days(Days value)
    {
        return bindValue( Days, value );
    }

    public final static StatementInParameterType<Hours> Hours = new HoursInParameterType();

    public static StatementInParameter Hours(Hours value)
    {
        return bindValue( Hours, value );
    }

    public final static StatementInParameterType<Minutes> Minutes = new MinutesInParameterType();

    public static StatementInParameter Minutes(Minutes value)
    {
        return bindValue( Minutes, value );
    }

    public final static StatementInParameterType<Months> Months = new MonthsInParameterType();

    public static StatementInParameter Months(Months value)
    {
        return bindValue( Months, value );
    }

    public final static StatementInParameterType<Seconds> Seconds = new SecondsInParameterType();

    public static StatementInParameter Seconds(Seconds value)
    {
        return bindValue( Seconds, value );
    }

    public final static StatementInParameterType<Weeks> Weeks = new WeeksInParameterType();

    public static StatementInParameter Weeks(Weeks value)
    {
        return bindValue( Weeks, value );
    }

    public final static StatementInParameterType<Years> Years = new YearsInParameterType();

    public static StatementInParameter Years(Years value)
    {
        return bindValue( Years, value );
    }

    public final static StatementInParameterType<Period> PeriodIsoEncoded = new PeriodIsoEncodedInParameterType();

    public static StatementInParameter PeriodIsoEncoded(Period value)
    {
        return bindValue( PeriodIsoEncoded, value );
    }

    public final static StatementInParameterType<Duration> DurationAsLong = new DurationAsLongInParameterType();

    public static StatementInParameter DurationAsLong(org.joda.time.Duration value)
    {
        return bindValue( DurationAsLong, value );
    }

    public final static StatementInParameterType<DateTimeZone> DateTimeZone = new DateTimeZoneInParameterType();

    public static StatementInParameter DateTimeZone(DateTimeZone value)
    {
        return bindValue( DateTimeZone, value );
    }

    public static StatementInParameter QueryTimeout(int value)
    {
        return new QueryTimeoutParameter( value );
    }

    // Array

    public static <T> StatementInParameterType<T[]> Array(StatementInParameterType<T> type, String placeholder)
    {
        return new ArrayInParameterType<>( type, placeholder );
    }

    public static <T> StatementInParameter Array(StatementInParameterType<T> type, String placeholder, T... values)
    {
        return bindValue( new ArrayInParameterType<>( type, placeholder ), values );
    }

    public static <T> StatementInParameterType<T[]> Array(StatementInParameterType<T> type, String placeholder, String placeholderReplacement)
    {
        return new ArrayInParameterType<>( type, placeholder, placeholderReplacement );
    }

    public static <T> StatementInParameter Array(StatementInParameterType<T> type, String placeholder, String placeholderReplacement, T... values)
    {
        return bindValue( new ArrayInParameterType<>( type, placeholder, placeholderReplacement ), values );
    }

    public static <T> StatementInParameterType<? extends Collection<T>> Collection(StatementInParameterType<T> type, String placeholder)
    {
        return new CollectionInParameterType<>( type, placeholder );
    }

    // Collection

    public static <T> StatementInParameter Collection(StatementInParameterType<T> type, String placeholder, java.util.Collection<T> values)
    {
        return bindValue( new CollectionInParameterType<>( type, placeholder ), values );
    }

    public static <T> StatementInParameterType<? extends Collection<T>> Collection(StatementInParameterType<T> type, String placeholder, String placeholderReplacement)
    {
        return new CollectionInParameterType<>( type, placeholder, placeholderReplacement );
    }

    public static <T> StatementInParameter Collection(StatementInParameterType<T> type, String placeholder, String placeholderReplacement, Collection<T> values)
    {
        return bindValue( new CollectionInParameterType<>( type, placeholder, placeholderReplacement ), values );
    }

    public static <T> StatementInParameter bindValue(StatementInParameterType<T> type, T value)
    {
        return new BoundValue<>( type, value );
    }

    // make a parameter array, allow nulls

    private static final StatementInParameter[] emptyInParameters = new StatementInParameter[0];

    public static StatementInParameter[] filterNulls(StatementInParameter... parameters)
    {
        if (parameters == null)
        {
            return emptyInParameters;
        }
        else
        {
            ArrayList<StatementInParameter> params = new ArrayList<>( parameters.length );
            for (StatementInParameter p : parameters)
                if (p != null)
                    params.add( p );
            return params.toArray( StatementInParameter[]::new );
        }
    }

    public static StatementInParameter[] inParams(StatementInParameter... parameters)
    {
        if (parameters == null)
            return emptyInParameters;
        else
            return parameters;
    }

    public static StatementInParameter[] inParams(Collection<StatementInParameter> parameters)
    {
        if (parameters == null)
            return emptyInParameters;
        else if (parameters.isEmpty())
            return emptyInParameters;
        else
            return parameters.toArray( StatementInParameter[]::new );
    }
}

abstract class AbstractStatementInParameterType<T> implements StatementInParameterType<T>
{
    @Override
    public String modify(String sql, T value)
    {
        return sql;
    }
}

final class BooleanInParameterType extends AbstractStatementInParameterType<Boolean>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Boolean value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.BOOLEAN );
        else
            stmt.setBoolean( pos, value );
        return 1;
    }
}

final class BooleanAsIntegerInParameterType extends ConvertingStatementInParameterType<Integer, Boolean>
{
    public BooleanAsIntegerInParameterType()
    {
        super( StatementParameters.Integer );
    }

    @Override
    protected Integer convert(Boolean value)
    {
        if (value == null)
            return null;
        return value ? 1 : 0;
    }
}

final class ByteInParameterType extends AbstractStatementInParameterType<Byte>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Byte value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.TINYINT );
        else
            stmt.setByte( pos, value );
        return 1;
    }
}

final class CharacterInParameterType extends AbstractStatementInParameterType<Character>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Character value) throws SQLException
    {
        stmt.setString( pos, value == null ? null : Character.toString( value ) );
        return 1;
    }
}

final class ShortInParameterType extends AbstractStatementInParameterType<Short>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Short value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.SMALLINT );
        else
            stmt.setShort( pos, value );
        return 1;
    }
}

final class IntegerInParameterType extends AbstractStatementInParameterType<Integer>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Integer value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.INTEGER );
        else
            stmt.setInt( pos, value );
        return 1;
    }
}

final class LongInParameterType extends AbstractStatementInParameterType<Long>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Long value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.BIGINT );
        else
            stmt.setLong( pos, value );
        return 1;
    }
}

final class FloatInParameterType extends AbstractStatementInParameterType<Float>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Float value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.FLOAT );
        else
            stmt.setFloat( pos, value );
        return 1;
    }
}

final class DoubleInParameterType extends AbstractStatementInParameterType<Double>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Double value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.DOUBLE );
        else
            stmt.setDouble( pos, value );
        return 1;
    }
}

final class BigDecimalInParameterType extends AbstractStatementInParameterType<BigDecimal>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, BigDecimal value) throws SQLException
    {
        stmt.setBigDecimal( pos, value );
        return 1;
    }
}

final class StringInParameterType extends AbstractStatementInParameterType<String>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, String value) throws SQLException
    {
        stmt.setString( pos, value );
        return 1;
    }
}

final class CollectionOfStringInParameterType extends AbstractStatementInParameterType<Collection<String>>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Collection<String> value) throws SQLException
    {
        if ( value == null )
            stmt.setNull( pos, Types.VARCHAR );
        else
            stmt.setArray( pos, stmt.getConnection().createArrayOf( "VARCHAR", value.stream().toArray(String[]::new) ) );
        return 1;
    }
}


final class DateInParameterType extends AbstractStatementInParameterType<Date>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Date value) throws SQLException
    {
        stmt.setDate( pos, value );
        return 1;
    }
}

final class TimestampInParameterType extends AbstractStatementInParameterType<Timestamp>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Timestamp value) throws SQLException
    {
        stmt.setTimestamp( pos, value );
        return 1;
    }
}

final class ZonedDateTimeAtUTCInParameterType extends AbstractStatementInParameterType<ZonedDateTime>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, ZonedDateTime value) throws SQLException
    {
        if ( value == null )
            stmt.setNull( pos, Types.TIMESTAMP );
        else
            stmt.setTimestamp( pos, Timestamp.from( value.toInstant() ) );
        return 1;
    }
}

final class ObjectInParameterType extends AbstractStatementInParameterType<Object>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Object value) throws SQLException
    {
        stmt.setObject( pos, value );
        return 1;
    }
}

final class SerializableInParameterType extends AbstractStatementInParameterType<Object>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Object value) throws SQLException
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream( baos );
            oos.writeObject( value );
            oos.close();

            stmt.setBytes( pos, baos.toByteArray() );
            return 1;
        }
        catch (IOException e)
        {
            throw new RuntimeException( e );
        }

    }
}

final class BytesInParameterType extends AbstractStatementInParameterType<byte[]>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, byte[] value) throws SQLException
    {
        stmt.setBytes( pos, value );
        return 1;
    }
}

final class EnumByNameParameterType extends AbstractStatementInParameterType<Enum<?>>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Enum<?> value) throws SQLException
    {
        if (value == null)
            stmt.setString( pos, null );
        else
            stmt.setString( pos, value.name() );
        return 1;
    }
}

final class EnumByIntKeyParameterType extends AbstractStatementInParameterType<EnumIntKey>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, EnumIntKey value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.INTEGER );
        else
            stmt.setInt( pos, value.getKey() );
        return 1;
    }
}

final class ClassParameterType extends AbstractStatementInParameterType<Class<?>>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Class<?> value) throws SQLException
    {
        stmt.setString( pos, value == null ? null : value.getName() );
        return 1;
    }
}

final class DateTimeParameterType extends AbstractStatementInParameterType<DateTime>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, DateTime value) throws SQLException
    {
        stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
        return 1;
    }
}

record ArrayInParameterType<T>(StatementInParameterType<T> type, String placeholder, String placeholderReplacement) implements StatementInParameterType<T[]>
{
    ArrayInParameterType(StatementInParameterType<T> type, String placeholder)
    {
        this( type, placeholder, "?" );
    }

    @Override
    public String modify(String sql, T[] values)
    {
        StringBuilder sb = new StringBuilder();
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                if (i > 0)
                    sb.append( ',' );
                sb.append( type.modify( placeholderReplacement, values[i] ) );
            }
        }
        return sql.replace( placeholder, sb );
    }

    @Override
    public int configure(PreparedStatement stmt, int pos, T[] values) throws SQLException
    {
        int posAdvance = 0;
        if (values != null)
            for (int i = 0; i < values.length; i++)
                posAdvance += type.configure( stmt, pos + posAdvance, values[i] );
        return posAdvance;
    }
}

record CollectionInParameterType<T>(StatementInParameterType<T> type, String placeholder, String placeholderReplacement) implements StatementInParameterType<Collection<T>>
{
    CollectionInParameterType(StatementInParameterType<T> type, String placeholder)
    {
        this( type, placeholder, "?" );
    }

    @Override
    public String modify(String sql, Collection<T> values)
    {
        StringBuilder sb = new StringBuilder();
        if (values != null)
        {
            int i = 0;
            for (T value : values)
            {
                if (i > 0)
                    sb.append( ',' );
                sb.append( type.modify( placeholderReplacement, value ) );
                i++;
            }
        }
        return sql.replace( placeholder, sb );
    }

    @Override
    public int configure(PreparedStatement stmt, int pos, Collection<T> values) throws SQLException
    {
        int posAdvance = 0;
        if (values != null)
            for (T value : values)
                posAdvance += type.configure( stmt, pos + posAdvance, value );
        return posAdvance;
    }
}


final class DateTimeInParameterType extends AbstractStatementInParameterType<DateTime>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, DateTime value) throws SQLException
    {
        stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
        return 1;
    }
}

final class DateMidnightInParameterType extends AbstractStatementInParameterType<DateMidnight>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, DateMidnight value) throws SQLException
    {
        stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
        return 1;
    }
}

final class DateMidnightAsIsoStringInParameterType extends AbstractStatementInParameterType<DateMidnight>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, DateMidnight value) throws SQLException
    {
        stmt.setString( pos, value == null ? null : ISODateTimeFormat.date().print( value ) );
        return 1;
    }
}

abstract class AbstractBaseSingleFieldPeriodInParameterType<T extends BaseSingleFieldPeriod> extends AbstractStatementInParameterType<T>
{
    @Override
    public final int configure(PreparedStatement stmt, int pos, T value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.INTEGER );
        else
            stmt.setInt( pos, getAmount( value ) );
        return 1;
    }

    protected abstract int getAmount(T baseSingleFieldPeriod);
}

final class DaysInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Days>
{
    @Override
    protected int getAmount(Days baseSingleFieldPeriod)
    {
        return baseSingleFieldPeriod.getDays();
    }
}

final class HoursInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Hours>
{
    @Override
    protected int getAmount(Hours baseSingleFieldPeriod)
    {
        return baseSingleFieldPeriod.getHours();
    }
}

final class MinutesInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Minutes>
{
    @Override
    protected int getAmount(Minutes baseSingleFieldPeriod)
    {
        return baseSingleFieldPeriod.getMinutes();
    }
}

final class MonthsInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Months>
{
    @Override
    protected int getAmount(Months baseSingleFieldPeriod)
    {
        return baseSingleFieldPeriod.getMonths();
    }
}

final class SecondsInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Seconds>
{
    @Override
    protected int getAmount(Seconds baseSingleFieldPeriod)
    {
        return baseSingleFieldPeriod.getSeconds();
    }
}

final class WeeksInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Weeks>
{
    @Override
    protected int getAmount(Weeks baseSingleFieldPeriod)
    {
        return baseSingleFieldPeriod.getWeeks();
    }
}

final class YearsInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Years>
{
    @Override
    protected int getAmount(Years baseSingleFieldPeriod)
    {
        return baseSingleFieldPeriod.getYears();
    }
}

final class PeriodIsoEncodedInParameterType extends AbstractStatementInParameterType<Period>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Period value) throws SQLException
    {
        stmt.setString( pos, value == null ? null : ISOPeriodFormat.standard().print( value ) );
        return 1;
    }
}

final class DurationAsLongInParameterType extends AbstractStatementInParameterType<Duration>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, Duration value) throws SQLException
    {
        if (value == null)
            stmt.setNull( pos, Types.BIGINT );
        else
            stmt.setLong( pos, value.getMillis() );
        return 1;
    }
}

final class DateTimeZoneInParameterType extends AbstractStatementInParameterType<DateTimeZone>
{
    @Override
    public int configure(PreparedStatement stmt, int pos, DateTimeZone value) throws SQLException
    {
        stmt.setString( pos, value == null ? null : value.getID() );
        return 1;
    }
}

record QueryTimeoutParameter(int timeout) implements StatementInParameter
{
    @Override
    public String modify(String sql)
    {
        return sql;
    }

    @Override
    public int configure(PreparedStatement stmt, int index) throws SQLException
    {
        stmt.setQueryTimeout( timeout );
        return 0;
    }
}
