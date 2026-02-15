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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;


/**
 * Collection of built-in {@link ResultType} implementations for common JDBC types.
 *
 * <p>Provides standard mappings for Java primitives, wrappers, strings, dates,
 * and more. Also includes utilities for Enums, Arrays, Lists, and mapper adaptation.</p>
 *
 * @author Friedrich Schäuffelhut
 * @see ResultType
 * @see ResultSetMappers
 * @since 1.0
 */
public final class ResultTypes
{
    /**
     * A {@code ResultType} for reading {@link Boolean} values. Handles {@code BOOLEAN} SQL types.
     */
    public final static ResultType<Boolean> Boolean = new BooleanResultType();
    /**
     * A {@code ResultType} for reading {@link Boolean} values stored as integers (0 for false, 1 for true).
     */
    public final static ResultType<Boolean> BooleanAsInteger = new BooleanAsIntegerResultType();
    /**
     * A {@code ResultType} for reading {@link Byte} values. Handles {@code TINYINT} SQL types.
     */
    public final static ResultType<Byte> Byte = new ByteResultType();
    /**
     * A {@code ResultType} for reading {@link Character} values. Expects a single-character string from the database.
     */
    public final static ResultType<Character> Character = new CharacterResultType();
    /**
     * A {@code ResultType} for reading {@link Short} values. Handles {@code SMALLINT} SQL types.
     */
    public final static ResultType<Short> Short = new ShortResultType();
    /**
     * A {@code ResultType} for reading {@link Integer} values. Handles {@code INT} SQL types.
     */
    public final static ResultType<Integer> Integer = new IntegerResultType();
    /**
     * A {@code ResultType} for reading {@link Long} values. Handles {@code BIGINT} SQL types.
     */
    public final static ResultType<Long> Long = new LongResultType();
    /**
     * A {@code ResultType} for reading {@link Float} values. Handles {@code REAL} SQL types.
     */
    public final static ResultType<Float> Float = new FloatResultType();
    /**
     * A {@code ResultType} for reading {@link Double} values. Handles {@code DOUBLE} SQL types.
     */
    public final static ResultType<Double> Double = new DoubleResultType();
    /**
     * A {@code ResultType} for reading {@link BigDecimal} values. Handles {@code DECIMAL}, {@code NUMERIC} SQL types.
     */
    public final static ResultType<BigDecimal> BigDecimal = new BigDecimalResultType();
    /**
     * A {@code ResultType} for reading {@link String} values. Handles {@code VARCHAR}, {@code TEXT} SQL types.
     */
    public final static ResultType<String> String = new StringResultType();
    /**
     * A {@code ResultType} for reading {@link Date} values. Handles {@code DATE} SQL types.
     */
    public final static ResultType<Date> Date = new DateResultType();
    /**
     * A {@code ResultType} for reading {@link Timestamp} values. Handles {@code TIMESTAMP} SQL types.
     */
    public final static ResultType<Timestamp> Timestamp = new TimestampResultType();
    /**
     * A {@code ResultType} for reading UTC {@link ZonedDateTime} values.
     */
    public final static ResultType<ZonedDateTime> ZonedDateTimeAtUtc = new ZonedDateTimeResultType();
    /**
     * A {@code ResultType} for reading generic {@link Object} values.
     */
    public final static ResultType<Object> Object = new ObjectResultType<Object>( Object.class );

    /**
     * Creates a {@code ResultType} for reading generic {@link Object} values of a specific type.
     *
     * @param type the expected class type of the object.
     * @param <T>  the type of the object.
     * @return a {@code ResultType} for the specified object type.
     */
    public static <T> ResultType<T> Object(Class<T> type)
    {
        return new ObjectResultType<T>( type );
    }

    /**
     * A {@code ResultType} for reading {@link java.io.Serializable} objects from byte arrays (BLOBs).
     */
    public static final ResultType<Object> Serializeable = new SerializeableResultType<Object>( Object.class );

    /**
     * Creates a {@code ResultType} for reading {@link java.io.Serializable} objects of a specific type from byte arrays (BLOBs).
     *
     * @param type the expected class type of the serializable object.
     * @param <T>  the type of the serializable object.
     * @return a {@code ResultType} for the specified serializable object type.
     */
    public static <T> ResultType<T> Serializeable(Class<T> type)
    {
        return new SerializeableResultType<T>( type );
    }

    /**
     * A {@code ResultType} for reading raw byte arrays (BLOBs).
     */
    public final static ResultType<byte[]> Bytes = new BytesResultType();

    /**
     * Creates a {@code ResultType} for reading {@link Enum} values by their name (string representation).
     *
     * @param type the {@code Enum} class.
     * @param <E>  the {@code Enum} type.
     * @return a {@code ResultType} for the specified enum type.
     */
    public static <E extends Enum<E>> ResultType<E> enumByName(Class<E> type)
    {
        return new EnumByNameResultType<E>( type );
    }

    /**
     * Creates a {@code ResultType} for reading {@link Enum} values that implement {@link EnumIntKey} by their integer key.
     *
     * @param type the {@code Enum} class implementing {@code EnumIntKey}.
     * @param <E>  the {@code Enum} type.
     * @return a {@code ResultType} for the specified enum type.
     */
    public static <E extends Enum<E> & EnumIntKey> ResultType<E> enumByIntKey(Class<E> type)
    {
        return new EnumByIntKeyResultType<E>( type );
    }

    /**
     * Creates a {@code ResultType} for reading {@link Enum} values that implement {@link EnumKey} by a generic key type.
     *
     * @param type       the {@code Enum} class implementing {@code EnumKey}.
     * @param resultType the {@code ResultType} for reading the key itself from the {@code ResultSet}.
     * @param <K>        the type of the key.
     * @param <E>        the {@code Enum} type.
     * @return a {@code ResultType} for the specified enum type.
     */
    public static <K, E extends Enum<E> & EnumKey<K>> ResultType<E> enumByKey(Class<E> type, ResultType<K> resultType)
    {
        return new EnumByKeyResultType<K, E>( type, resultType );
    }

    /**
     * A {@code ResultType} for reading {@link Class} objects by their fully qualified name.
     */
    public final static ResultType<Class<?>> Class = new ClassResultType();

    /**
     * Creates a {@code ResultType} for reading {@link Class} objects using a specific {@link ClassLoader}.
     *
     * @param classLoader the {@code ClassLoader} to use for loading classes.
     * @return a {@code ResultType} for {@code Class} objects.
     */
    public static ResultType<Class<?>> Class(ClassLoader classLoader)
    {
        return new ClassResultType( classLoader );
    }

    /**
     * A {@code ResultType} for reading Joda {@link DateTime} values from timestamps.
     */
    public final static ResultType<DateTime> DateTime = new DateTimeResultType();
    /**
     * A {@code ResultType} for reading Joda {@link DateMidnight} values from timestamps.
     */
    public final static ResultType<DateMidnight> DateMidnight = new DateMidnightResultType();
    /**
     * A {@code ResultType} for reading Joda {@link DateMidnight} values from ISO 8601 formatted strings.
     */
    public final static ResultType<DateMidnight> DateMidnightAsIsoString = new DateMidnightAsIsoStringResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Days} values from integers.
     */
    public final static ResultType<Days> Days = new DaysResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Hours} values from integers.
     */
    public final static ResultType<Hours> Hours = new HoursResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Minutes} values from integers.
     */
    public final static ResultType<Minutes> Minutes = new MinutesResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Months} values from integers.
     */
    public final static ResultType<Months> Months = new MonthsResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Seconds} values from integers.
     */
    public final static ResultType<Seconds> Seconds = new SecondsResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Weeks} values from integers.
     */
    public final static ResultType<Weeks> Weeks = new WeeksResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Years} values from integers.
     */
    public final static ResultType<Years> Years = new YearsResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Period} values from ISO 8601 formatted strings.
     */
    public final static ResultType<Period> PeriodIsoEncoded = new PeriodIsoEncodedResultType();
    /**
     * A {@code ResultType} for reading Joda {@link DateTimeZone} values from string IDs.
     */
    public final static ResultType<DateTimeZone> DateTimeZone = new DateTimeZoneResultType();
    /**
     * A {@code ResultType} for reading Joda {@link Duration} values from long milliseconds.
     */
    public final static ResultType<Duration> Duration = new DurationResultType();

    /**
     * Creates a {@code ResultType} for reading arrays of a specified type.
     *
     * @param resultType the {@code ResultType} for the elements within the array.
     * @param <T>        the type of the array elements.
     * @return a {@code ResultType} for arrays of the specified type.
     */
    public static <T> ResultType<T[]> arrayOf(ResultType<T> resultType)
    {
        return new ArrayResultType<T>( resultType );
    }

    /**
     * Creates a {@code ResultType} for reading {@link List}s of a specified type.
     *
     * @param resultType the {@code ResultType} for the elements within the list.
     * @param <T>        the type of the list elements.
     * @return a {@code ResultType} for lists of the specified type.
     */
    public static <T> ResultType<List<T>> listOf(ResultType<T> resultType)
    {
        return new ListResultType<T>( resultType );
    }

    /**
     * Creates a {@code ResultType} that provides a default value if the underlying {@code ResultType} returns {@code null}.
     *
     * @param resultType   the original {@code ResultType}.
     * @param defaultValue the value to return if {@code resultType} produces {@code null}.
     * @param <T>          the type of the result.
     * @return a null-safe {@code ResultType} with a default value.
     */
    public static <T> ResultType<T> ifNull(ResultType<T> resultType, T defaultValue)
    {
        return new IfNullResultType<T>( resultType, defaultValue );
    }

    /**
     * Adapts a {@link ResultSetMapper} into a {@link ResultType}.
     *
     * <p>This allows using complex mappers (e.g. created via {@link ResultSetMappers#object(ResultSetMappers.F1, ResultType)})
     * as if they were a single column {@link ResultType}. This is useful for nesting mappers
     * for composite objects.</p>
     *
     * @param type   the class of the object produced by the mapper
     * @param mapper the mapper to adapt
     * @param <T>    the result type
     * @return a {@link ResultType} that delegates to the given mapper
     * @since 2025-02-15
     */
    public static <T> ResultType<T> mapper(Class<T> type, ResultSetMapper<T> mapper)
    {
        return new ResultType<T>()
        {
            @Override
            public void initialize(ResultSet resultSet, ColumnIndex index) throws SQLException
            {
                mapper.initialize( resultSet, index );
            }

            @Override
            public T getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
            {
                return mapper.map( resultSet, index );
            }

            @Override
            public Class<T> getResultType()
            {
                return type;
            }
        };
    }

    /**
     * A helper method to create an array of {@link ResultType} instances.
     * This can be useful when you need to pass a var-args list of {@code ResultType}s explicitly.
     *
     * @param resultTypes the {@code ResultType} instances.
     * @return an array of {@code ResultType} instances.
     */
    public static ResultType<?>[] resultTypes(ResultType<?>... resultTypes)
    {
        return resultTypes;
    }
}

final class BooleanResultType implements ResultType<Boolean>
{
    @Override
    public final Boolean getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        boolean value = resultSet.getBoolean( index.next() );
        return resultSet.wasNull() ? null : value;
    }

    public Class<Boolean> getResultType()
    {
        return Boolean.class;
    }
}

final class BooleanAsIntegerResultType extends ConvertingResultType<Boolean, Integer>
{
    public BooleanAsIntegerResultType()
    {
        super( ResultTypes.Integer );
    }

    @Override
    protected Boolean convert(Integer value)
    {
        if (value == null)
            return null;
        return value == 0 ? false : true;
    }

    public Class<Boolean> getResultType()
    {
        return Boolean.class;
    }
}

final class ByteResultType implements ResultType<Byte>
{
    @Override
    public Byte getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        byte value = resultSet.getByte( index.next() );
        return resultSet.wasNull() ? null : value;
    }

    public Class<Byte> getResultType()
    {
        return Byte.class;
    }
}

final class CharacterResultType implements ResultType<Character>
{
    @Override
    public Character getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        String value = resultSet.getString( index.next() );
        if (value == null)
            return null;
        else if (value.length() == 0)
            return null;
        else if (value.length() == 1)
            return value.charAt( 0 );
        else
            throw new RuntimeException( "returned string contains more than one character" );
    }

    public Class<Character> getResultType()
    {
        return Character.class;
    }
}

final class ShortResultType implements ResultType<Short>
{
    @Override
    public Short getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        short value = resultSet.getShort( index.next() );
        return resultSet.wasNull() ? null : value;
    }

    public Class<Short> getResultType()
    {
        return Short.class;
    }
}

final class IntegerResultType implements ResultType<Integer>
{
    @Override
    public Integer getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        int value = resultSet.getInt( index.next() );
        return resultSet.wasNull() ? null : value;
    }

    public Class<Integer> getResultType()
    {
        return Integer.class;
    }
}

final class LongResultType implements ResultType<Long>
{
    @Override
    public Long getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        long value = resultSet.getLong( index.next() );
        return resultSet.wasNull() ? null : value;
    }

    public Class<Long> getResultType()
    {
        return Long.class;
    }
}

final class FloatResultType implements ResultType<Float>
{
    @Override
    public Float getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        float value = resultSet.getFloat( index.next() );
        return resultSet.wasNull() ? null : value;
    }

    public Class<Float> getResultType()
    {
        return Float.class;
    }
}

final class DoubleResultType implements ResultType<Double>
{
    @Override
    public Double getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        double value = resultSet.getDouble( index.next() );
        return resultSet.wasNull() ? null : value;
    }

    public Class<Double> getResultType()
    {
        return Double.class;
    }
}

final class BigDecimalResultType implements ResultType<BigDecimal>
{
    @Override
    public BigDecimal getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        return resultSet.getBigDecimal( index.next() );
    }

    public Class<BigDecimal> getResultType()
    {
        return BigDecimal.class;
    }
}

final class StringResultType implements ResultType<String>
{
    @Override
    public String getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        return resultSet.getString( index.next() );
    }

    public Class<String> getResultType()
    {
        return String.class;
    }
}

final class DateResultType implements ResultType<Date>
{
    @Override
    public Date getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        return resultSet.getDate( index.next() );
    }

    public Class<Date> getResultType()
    {
        return Date.class;
    }
}

final class TimestampResultType implements ResultType<Timestamp>
{
    @Override
    public Timestamp getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        return resultSet.getTimestamp( index.next() );
    }

    public Class<Timestamp> getResultType()
    {
        return Timestamp.class;
    }
}

final class ZonedDateTimeResultType implements ResultType<ZonedDateTime>
{
    private static final ZoneId UTC = ZoneId.of( "UTC" );

    @Override
    public ZonedDateTime getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        Timestamp timestamp = resultSet.getTimestamp( index.next() );
        if (timestamp == null)
            return null;
        return timestamp.toInstant().atZone( UTC );
    }

    public Class<ZonedDateTime> getResultType()
    {
        return ZonedDateTime.class;
    }
}

final class ObjectResultType<T> implements ResultType<T>
{
    final Class<T> type;

    public ObjectResultType(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public T getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        return type.cast( resultSet.getObject( index.next() ) );
    }

    public Class<T> getResultType()
    {
        return type;
    }
}

final class SerializeableResultType<T> implements ResultType<T>
{
    final Class<T> type;

    public SerializeableResultType(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public T getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        try
        {
            byte[] bytes = resultSet.getBytes( index.next() );
            ByteArrayInputStream bais = new ByteArrayInputStream( bytes );
            ObjectInputStream ois = new ObjectInputStream( bais )
            {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException
                {
                    String name = desc.getName();
                    try
                    {
                        return Class.forName( name, false, Thread.currentThread().getContextClassLoader() );
                    }
                    catch (ClassNotFoundException ex)
                    {
                        // Ignore and delegate to super.resolveClass( desc )
                        // which will throw the final ClassNotFoundException.
                    }
                    return super.resolveClass( desc );
                }
            };
            Object object = ois.readObject();
            return type.cast( object );
        }
        catch (IOException e)
        {
            throw new RuntimeException( e );
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException( e );
        }
    }

    public Class<T> getResultType()
    {
        return type;
    }
}

final class BytesResultType implements ResultType<byte[]>
{
    @Override
    public byte[] getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        return resultSet.getBytes( index.next() );
    }

    public Class<byte[]> getResultType()
    {
        return byte[].class;
    }
}

final class EnumByNameResultType<E extends Enum<E>> implements ResultType<E>
{
    final Class<E> type;

    EnumByNameResultType(Class<E> type)
    {
        this.type = type;
    }

    @Override
    public E getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        String string = resultSet.getString( index.next() );
        if (string == null)
            return null;
        else
            return Enum.valueOf( type, string.trim() );
    }

    public Class<E> getResultType()
    {
        return type;
    }
}

final class EnumByIntKeyResultType<E extends Enum<E> & EnumIntKey>
        implements ResultType<E>
{
    final Class<E> type;

    public EnumByIntKeyResultType(Class<E> type)
    {
        this.type = type;
    }

    @Override
    public E getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        int key = resultSet.getInt( index.next() );
        if (resultSet.wasNull())
            return null;
        else
            return getEnum( key );
    }

    private E getEnum(final int key)
    {
        for (E t : type.getEnumConstants())
            if (t.getKey() == key)
                return t;
        throw new UnexpectedValueException( String.format(
                "No %s enum for value %d found", type.getSimpleName(), key ) );
    }

    public Class<E> getResultType()
    {
        return type;
    }
}

final class EnumByKeyResultType<K, E extends Enum<E> & EnumKey<K>>
        implements ResultType<E>
{
    final Class<E> type;
    final ResultType<K> resultType;

    public EnumByKeyResultType(Class<E> type, ResultType<K> resultType)
    {
        this.type = type;
        this.resultType = resultType;
    }

    @Override
    public E getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        K key = resultType.getResult( resultSet, index );
        return key == null ? null : getEnum( key );
    }

    private E getEnum(final K key)
    {
        for (E e : type.getEnumConstants())
            if (e.getKey().equals( key ))
                return e;
        throw new UnexpectedValueException( String.format(
                "No %s enum for value %s found", type.getSimpleName(), key ) );
    }

    public Class<E> getResultType()
    {
        return type;
    }
}

final class ClassResultType implements ResultType<Class<?>>
{
    private final ClassLoader classLoader;

    public ClassResultType()
    {
        this( ClassResultType.class.getClassLoader() );
    }

    public ClassResultType(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    @Override
    public Class<?> getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        String className = resultSet.getString( index.next() );
        Class<?> clazz;
        if (className == null)
            clazz = null;
        else
        {
            try
            {
                clazz = Class.forName( className, true, classLoader );
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException( e );
            }
        }
        return clazz;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Class<Class<?>> getResultType()
    {
        return (Class) Class.class;
    }
}

final class DateTimeResultType implements ResultType<DateTime>
{
    @Override
    public DateTime getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        Timestamp timestamp = resultSet.getTimestamp( index.next() );
        return timestamp == null ? null : new DateTime( timestamp );
    }

    public Class<DateTime> getResultType()
    {
        return DateTime.class;
    }
}

final class DateMidnightResultType implements ResultType<DateMidnight>
{
    @Override
    public DateMidnight getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        Timestamp timestamp = resultSet.getTimestamp( index.next() );
        return timestamp == null ? null : new DateMidnight( timestamp );
    }

    public Class<DateMidnight> getResultType()
    {
        return DateMidnight.class;
    }
}

final class DateMidnightAsIsoStringResultType implements ResultType<DateMidnight>
{
    @Override
    public DateMidnight getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        String timestamp = resultSet.getString( index.next() );
        return timestamp == null ? null : ISODateTimeFormat.date().parseDateTime( timestamp ).toDateMidnight();
    }

    public Class<DateMidnight> getResultType()
    {
        return DateMidnight.class;
    }
}

abstract class AbstractBaseSingleFieldPeriodResultType<T extends BaseSingleFieldPeriod> implements ResultType<T>
{
    @Override
    public final T getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        final int amount = resultSet.getInt( index.next() );
        final T baseSingleFieldPeriod;
        if (resultSet.wasNull())
            baseSingleFieldPeriod = null;
        else
            baseSingleFieldPeriod = createBaseSingleFieldPeriod( amount );
        return baseSingleFieldPeriod;
    }

    protected abstract T createBaseSingleFieldPeriod(int value);
}


final class DaysResultType extends AbstractBaseSingleFieldPeriodResultType<Days>
{
    @Override
    protected Days createBaseSingleFieldPeriod(int amount)
    {
        return Days.days( amount );
    }

    public Class<Days> getResultType()
    {
        return Days.class;
    }
}

final class HoursResultType extends AbstractBaseSingleFieldPeriodResultType<Hours>
{
    @Override
    protected Hours createBaseSingleFieldPeriod(int amount)
    {
        return Hours.hours( amount );
    }

    public Class<Hours> getResultType()
    {
        return Hours.class;
    }
}

final class MinutesResultType extends AbstractBaseSingleFieldPeriodResultType<Minutes>
{
    @Override
    protected Minutes createBaseSingleFieldPeriod(int amount)
    {
        return Minutes.minutes( amount );
    }

    public Class<Minutes> getResultType()
    {
        return Minutes.class;
    }
}

final class MonthsResultType extends AbstractBaseSingleFieldPeriodResultType<Months>
{
    @Override
    protected Months createBaseSingleFieldPeriod(int amount)
    {
        return Months.months( amount );
    }

    public Class<Months> getResultType()
    {
        return Months.class;
    }
}

final class SecondsResultType extends AbstractBaseSingleFieldPeriodResultType<Seconds>
{
    @Override
    protected Seconds createBaseSingleFieldPeriod(int amount)
    {
        return Seconds.seconds( amount );
    }

    public Class<Seconds> getResultType()
    {
        return Seconds.class;
    }
}

final class WeeksResultType extends AbstractBaseSingleFieldPeriodResultType<Weeks>
{
    @Override
    protected Weeks createBaseSingleFieldPeriod(int amount)
    {
        return Weeks.weeks( amount );
    }

    public Class<Weeks> getResultType()
    {
        return Weeks.class;
    }
}

final class YearsResultType extends AbstractBaseSingleFieldPeriodResultType<Years>
{
    @Override
    protected Years createBaseSingleFieldPeriod(int amount)
    {
        return Years.years( amount );
    }

    public Class<Years> getResultType()
    {
        return Years.class;
    }
}

final class PeriodIsoEncodedResultType extends ConvertingResultType<Period, String>
{
    public PeriodIsoEncodedResultType()
    {
        super( ResultTypes.String );
    }

    @Override
    protected Period convert(String value)
    {
        return value == null ? null : ISOPeriodFormat.standard().parsePeriod( value );
    }

    @Override
    public Class<Period> getResultType()
    {
        return Period.class;
    }
}

final class DateTimeZoneResultType extends ConvertingResultType<DateTimeZone, String>
{
    public DateTimeZoneResultType()
    {
        super( ResultTypes.String );
    }

    @Override
    protected DateTimeZone convert(String value)
    {
        return value == null ? null : DateTimeZone.forID( value );
    }

    @Override
    public Class<DateTimeZone> getResultType()
    {
        return DateTimeZone.class;
    }
}

final class DurationResultType extends ConvertingResultType<Duration, Long>
{
    public DurationResultType()
    {
        super( ResultTypes.Long );
    }

    @Override
    protected Duration convert(Long value)
    {
        return value == null ? null : Duration.millis( value );
    }

    @Override
    public Class<Duration> getResultType()
    {
        return Duration.class;
    }
}


final class IfNullResultType<T> implements ResultType<T>
{
    private final ResultType<T> resultType;
    private final T defaultValue;

    IfNullResultType(ResultType<T> resultType, T defaultValue)
    {
        this.resultType = resultType;
        this.defaultValue = defaultValue;
    }

    @Override
    public T getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        T result = resultType.getResult( resultSet, index );
        return result == null ? defaultValue : result;
    }

    public java.lang.Class<T> getResultType()
    {
        return resultType.getResultType();
    }
}


