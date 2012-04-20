/**
 * Copyright 2009 Friedrich Schäuffelhut
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package de.schaeuffelhut.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;


/**
 * @author Friedrich Schäuffelhut
 *
 */
public final class ResultTypes
{
	public final static IfcResultType<Boolean> Boolean = new BooleanResultType();
	public final static IfcResultType<Byte> Byte = new ByteResultType();
	public final static IfcResultType<Character> Character = new CharacterResultType();
	public final static IfcResultType<Short> Short = new ShortResultType();
	public final static IfcResultType<Integer> Integer = new IntegerResultType();
	public final static IfcResultType<Long> Long = new LongResultType();
	public final static IfcResultType<Float> Float = new FloatResultType();
	public final static IfcResultType<Double> Double = new DoubleResultType();
	public final static IfcResultType<BigDecimal> BigDecimal = new BigDecimalResultType();
	public final static IfcResultType<String> String = new StringResultType();
	public final static IfcResultType<Date> Date = new DateResultType();
	public final static IfcResultType<Timestamp> Timestamp = new TimestampResultType();
	public final static IfcResultType<Object> Object = new ObjectResultType<Object>( Object.class );
	public final static <T> IfcResultType<T> Object(Class<T> type) { return new ObjectResultType<T>( type ); }
	public static final IfcResultType<Object>	Serializeable	= new SerializeableResultType<Object>( Object.class );
	public static final <T> IfcResultType<T>	Serializeable(Class<T> type) { return new SerializeableResultType<T>( type ); }
	public final static IfcResultType<byte[]> Bytes = new BytesResultType();
	
	public final static <E extends Enum<E>> IfcResultType<E> enumByName(Class<E> type)
	{
		return new EnumByNameResultType<E>(type);
	}

	public final static <E extends Enum<E> & IfcEnumIntKey> IfcResultType<E> enumByIntKey(Class<E> type)
	{
		return new EnumByIntKeyResultType<E>( type );
	}

	public final static <K,E extends Enum<E> & IfcEnumKey<K>> IfcResultType<E> enumByKey(Class<E> type, IfcResultType<K> resultType)
	{
		return new EnumByKeyResultType<K,E>( type, resultType );
	}

	public final static IfcResultType<Class<?>> Class = new ClassResultType();
	public final static IfcResultType<Class<?>> Class(ClassLoader classLoader) { return new ClassResultType( classLoader ); }

	public final static IfcResultType<DateTime> DateTime = new DateTimeResultType();
	public final static IfcResultType<DateMidnight> DateMidnight = new DateMidnightResultType();
	public final static IfcResultType<DateMidnight> DateMidnightAsIsoString = new DateMidnightAsIsoStringResultType();
	public final static IfcResultType<Days>		Days = new DaysResultType();
	public final static IfcResultType<Hours>	Hours = new HoursResultType();
	public final static IfcResultType<Minutes>	Minutes = new MinutesResultType();
	public final static IfcResultType<Months>	Months = new MonthsResultType();
	public final static IfcResultType<Seconds>	Seconds = new SecondsResultType();
	public final static IfcResultType<Weeks>	Weeks = new WeeksResultType();
	public final static IfcResultType<Years>	Years = new YearsResultType();
	public final static IfcResultType<Period>	PeriodIsoEncoded = new PeriodIsoEncodedResultType();
	public final static IfcResultType<DateTimeZone> DateTimeZone = new DateTimeZoneResultType();
	
	
	public final static <T> IfcResultType<T> ifNull(IfcResultType<T> resultType, T defaultValue)
	{
		return new IfNullResultType<T>(resultType, defaultValue);
	}
 	
	public final static IfcResultType<?>[] resultTypes(IfcResultType<?>... resultTypes)
	{
		return resultTypes;
	}
}

final class BooleanResultType implements IfcResultType<Boolean>
{
	private static final long	serialVersionUID	= -2008227876380856855L;

	public final Boolean getResult(ResultSet resultSet, int index) throws SQLException
	{
		boolean value = resultSet.getBoolean( index );
		return resultSet.wasNull() ? null : value;
	}
	
	public Class<Boolean> getResultType()
	{
		return Boolean.class;
	}
}

final class ByteResultType implements IfcResultType<Byte>
{
	private static final long	serialVersionUID	= 2585369947605652480L;

	public final Byte getResult(ResultSet resultSet, int index) throws SQLException
    {
        byte value = resultSet.getByte( index );
		return resultSet.wasNull() ? null : value;
	}
	
	public Class<Byte> getResultType()
	{
		return Byte.class;
	}
}

final class CharacterResultType implements IfcResultType<Character>
{
	private static final long	serialVersionUID	= -1419579095293210858L;

	public final Character getResult(ResultSet resultSet, int index) throws SQLException
	{
		String value = resultSet.getString( index );
		if ( value == null )
			return null;
		else if ( value.length() == 0 )
			return null;
		else if ( value.length() == 1 )
			return value.charAt( 0 );
		else
			throw new RuntimeException( "returned string contains more than one character" );
	}
	
	public Class<Character> getResultType()
	{
		return Character.class;
	}
}

final class ShortResultType implements IfcResultType<Short>
{
	private static final long	serialVersionUID	= 3487161714183522196L;

	public final Short getResult(ResultSet resultSet, int index) throws SQLException
    {
        short value = resultSet.getShort( index );
		return resultSet.wasNull() ? null : value;
	}
	
	public Class<Short> getResultType()
	{
		return Short.class;
	}
}

final class IntegerResultType implements IfcResultType<Integer>
{
	private static final long	serialVersionUID	= 9080989310943910267L;

	public final Integer getResult(ResultSet resultSet, int index) throws SQLException
    {
        int value = resultSet.getInt( index );
		return resultSet.wasNull() ? null : value;
	}
	
	public Class<Integer> getResultType()
	{
		return Integer.class;
	}
}

final class LongResultType implements IfcResultType<Long>
{
	private static final long	serialVersionUID	= 1965506792042766449L;

	public final Long getResult(ResultSet resultSet, int index) throws SQLException
    {
        long value = resultSet.getLong( index );
		return resultSet.wasNull() ? null : value;
	}
	
	public Class<Long> getResultType()
	{
		return Long.class;
	}
}

final class FloatResultType implements IfcResultType<Float>
{
	private static final long	serialVersionUID	= -726544385328036462L;

	public final Float getResult(ResultSet resultSet, int index) throws SQLException
    {
        float value = resultSet.getFloat( index );
		return resultSet.wasNull() ? null : value;
	}
	
	public Class<Float> getResultType()
	{
		return Float.class;
	}
}

final class DoubleResultType implements IfcResultType<Double>
{
	private static final long	serialVersionUID	= -4903823742081361716L;

	public final Double getResult(ResultSet resultSet, int index) throws SQLException
    {
        double value = resultSet.getDouble( index );
		return resultSet.wasNull() ? null : value;
	}
	
	public Class<Double> getResultType()
	{
		return Double.class;
	}
}

final class BigDecimalResultType implements IfcResultType<BigDecimal>
{
	private static final long	serialVersionUID	= -9076549453785581576L;

	public final BigDecimal getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getBigDecimal( index );
    }
	
	public Class<BigDecimal> getResultType()
	{
		return BigDecimal.class;
	}
}

final class StringResultType implements IfcResultType<String>
{
	private static final long	serialVersionUID	= -3207490720613511505L;

	public final String getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getString( index );
    }
	
	public Class<String> getResultType()
	{
		return String.class;
	}
}

final class DateResultType implements IfcResultType<Date>
{
	private static final long	serialVersionUID	= -4737217310016513942L;

	public final Date getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getDate( index );
    }
	
	public Class<Date> getResultType()
	{
		return Date.class;
	}
}

final class TimestampResultType implements IfcResultType<Timestamp>
{
	private static final long	serialVersionUID	= 1404401761056662230L;

	public final Timestamp getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getTimestamp( index );
    }
	
	public Class<Timestamp> getResultType()
	{
		return Timestamp.class;
	}
}

final class ObjectResultType<T> implements IfcResultType<T>
{
	private static final long	serialVersionUID	= -4223724941291746384L;

	final Class<T> type;
	
	public ObjectResultType(Class<T> type)
	{
		this.type = type;
	}
	
	public final T getResult(ResultSet resultSet, int index) throws SQLException
    {
        return type.cast( resultSet.getObject( index ) );
    }
	
	public Class<T> getResultType()
	{
		return type;
	}
}

final class SerializeableResultType<T> implements IfcResultType<T>
{
	private static final long	serialVersionUID	= -4223724941291746384L;

	final Class<T> type;
	
	public SerializeableResultType(Class<T> type)
	{
		this.type = type;
	}
	
	public final T getResult(ResultSet resultSet, int index) throws SQLException
    {
		try
		{
			byte[] bytes = resultSet.getBytes( index );
			ByteArrayInputStream bais = new ByteArrayInputStream( bytes );
			ObjectInputStream ois = new ObjectInputStream( bais ){
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException
                {
                    String name = desc.getName();
                    try {
                        return Class.forName(name, false, Thread.currentThread().getContextClassLoader() );
                    } catch (ClassNotFoundException ex) {
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

final class BytesResultType implements IfcResultType<byte[]>
{
	private static final long serialVersionUID = -4879761216153157461L;

	public final byte[] getResult(ResultSet resultSet, int i) throws SQLException
	{
		return resultSet.getBytes( i );
	}
	
	public Class<byte[]> getResultType()
	{
		return byte[].class;
	}
}

final class EnumByNameResultType<E extends Enum<E>> implements IfcResultType<E>
{
	private static final long	serialVersionUID	= 5240642076796760283L;

	final Class<E> type;
	EnumByNameResultType(Class<E> type)
	{
		this.type = type;
	}

	public final E getResult(ResultSet resultSet, int index) throws SQLException
	{
		String string = resultSet.getString( index );
		if ( string == null )
			return null;
		else
			return Enum.valueOf( type, string.trim() );
	}
	
	public Class<E> getResultType()
	{
		return type;
	}
}

final class EnumByIntKeyResultType<E extends Enum<E> & IfcEnumIntKey>
implements IfcResultType<E>
{
	private static final long	serialVersionUID	= 358202867595098738L;

	final Class<E>	type;

	public EnumByIntKeyResultType(Class<E> type)
	{
		this.type = type;
	}

	public final E getResult(ResultSet resultSet, int index) throws SQLException
	{
		int key = resultSet.getInt( index );
		if ( resultSet.wasNull() )
			return null;
		else
			return getEnum( key );
	}

	private E getEnum(final int key)
	{
		for ( E t : type.getEnumConstants() )
			if ( t.getKey() == key )
				return t;
		throw new UnexpectedValueException( String.format(
				"No %s enum for value %d found", type.getSimpleName(), key ) );
	}
	
	public Class<E> getResultType()
	{
		return type;
	}
}

final class EnumByKeyResultType<K,E extends Enum<E> & IfcEnumKey<K>>
implements IfcResultType<E>
{
	private static final long	serialVersionUID	= 3148528685112236111L;

	final Class<E>	type;
	final IfcResultType<K>	resultType;

	public EnumByKeyResultType(Class<E> type, IfcResultType<K> resultType)
	{
		this.type = type;
		this.resultType = resultType;
	}

	public final E getResult(ResultSet resultSet, int index) throws SQLException
	{
		K key = resultType.getResult( resultSet, index );
		return key == null ? null : getEnum( key );
	}

	private final E getEnum(final K key)
	{
		for ( E e : type.getEnumConstants() )
			if ( e.getKey().equals( key ) )
				return e;
		throw new UnexpectedValueException( String.format(
				"No %s enum for value %d found", type.getSimpleName(), key ) );
	}
	
	public Class<E> getResultType()
	{
		return type;
	}
}

final class ClassResultType implements IfcResultType<Class<?>>
{
	private static final long	serialVersionUID	= -3217514556414780833L;
	private final ClassLoader classLoader;

	public ClassResultType()
	{
		this( ClassResultType.class.getClassLoader() );
	}
	
	public ClassResultType(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	public final Class<?> getResult(ResultSet resultSet, int index) throws SQLException
    {
    	String className = resultSet.getString( index );
    	Class<?> clazz;
    	if ( className == null )
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class<Class<?>> getResultType()
	{
		return (Class)Class.class;
	}
}

final class DateTimeResultType implements IfcResultType<DateTime>
{
	private static final long	serialVersionUID	= -3087814275246876350L;

	public final DateTime getResult(ResultSet resultSet, int index) throws SQLException
    {
        Timestamp timestamp = resultSet.getTimestamp( index );
		return timestamp == null ? null : new DateTime( timestamp );
	}
	
	public Class<DateTime> getResultType()
	{
		return DateTime.class;
	}
}

final class DateMidnightResultType implements IfcResultType<DateMidnight>
{
	private static final long	serialVersionUID	= -3087814275246876350L;

	public final DateMidnight getResult(ResultSet resultSet, int index) throws SQLException
    {
        Timestamp timestamp = resultSet.getTimestamp( index );
		return timestamp == null ? null : new DateMidnight( timestamp );
    }
	
	public Class<DateMidnight> getResultType()
	{
		return DateMidnight.class;
	}
}

final class DateMidnightAsIsoStringResultType implements IfcResultType<DateMidnight>
{
	private static final long	serialVersionUID	= -3087814275246876350L;

	public final DateMidnight getResult(ResultSet resultSet, int index) throws SQLException
    {
        String timestamp = resultSet.getString( index );
		return timestamp == null ? null : ISODateTimeFormat.date().parseDateTime( timestamp ).toDateMidnight();
    }
	
	public Class<DateMidnight> getResultType()
	{
		return DateMidnight.class;
	}
}

abstract class AbstractBaseSingleFieldPeriodResultType<T extends BaseSingleFieldPeriod> implements IfcResultType<T>
{
	private static final long	serialVersionUID	= -5549143876703023099L;

	public final T getResult(ResultSet resultSet, int index) throws SQLException
    {
		final int amount = resultSet.getInt( index );
		final T baseSingleFieldPeriod;
		if ( resultSet.wasNull() )
			baseSingleFieldPeriod = null;
		else
			baseSingleFieldPeriod = createBaseSingleFieldPeriod( amount );
		return baseSingleFieldPeriod;
    }

	protected abstract T createBaseSingleFieldPeriod(int value);
}


final class DaysResultType extends AbstractBaseSingleFieldPeriodResultType<Days>
{
	private static final long	serialVersionUID	= 6315203655997344989L;

	@Override
	protected final Days createBaseSingleFieldPeriod(int amount)
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
	private static final long	serialVersionUID	= 5557286393931715691L;

	@Override
	protected final Hours createBaseSingleFieldPeriod(int amount)
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
	private static final long	serialVersionUID	= 3556968653062290471L;

	@Override
	protected final Minutes createBaseSingleFieldPeriod(int amount)
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
	private static final long	serialVersionUID	= 2716465322259816275L;

	@Override
	protected final Months createBaseSingleFieldPeriod(int amount)
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
	private static final long	serialVersionUID	= -1581181400815349197L;

	@Override
	protected final Seconds createBaseSingleFieldPeriod(int amount)
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
	private static final long	serialVersionUID	= 6241522382568882221L;

	@Override
	protected final Weeks createBaseSingleFieldPeriod(int amount)
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
	private static final long	serialVersionUID	= -7470839670579041917L;

	@Override
	protected final Years createBaseSingleFieldPeriod(int amount)
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
	private static final long serialVersionUID = -2500916560042647993L;
	
	public PeriodIsoEncodedResultType()
	{
		super( ResultTypes.String );
	}
	
	@Override
	protected final Period convert(String value)
	{
		return value == null ? null : ISOPeriodFormat.standard().parsePeriod( value );
	}
	
	@Override
	public final Class<Period> getResultType()
	{
		return Period.class;
	}
}
final class DateTimeZoneResultType extends ConvertingResultType<DateTimeZone, String>
{
	private static final long	serialVersionUID	= -1042811119544078910L;

	public DateTimeZoneResultType()
	{
		super( ResultTypes.String );
	}
	
	@Override
	protected final DateTimeZone convert(String value)
	{
		return value == null ? null : DateTimeZone.forID( value );
	}
	
	@Override
	public final Class<DateTimeZone> getResultType()
	{
		return DateTimeZone.class;
	}
}



final class IfNullResultType<T> implements IfcResultType<T>
{
	private static final long serialVersionUID = 186179726802807891L;
	
	private final IfcResultType<T> resultType;
	private final T defaultValue;
	
	IfNullResultType(IfcResultType<T> resultType, T defaultValue)
	{
		this.resultType = resultType;
		this.defaultValue = defaultValue;
	}

	public T getResult(ResultSet resultSet, int index) throws SQLException
	{
		T result = resultType.getResult(resultSet, index);
		return result == null ? defaultValue : result;
	}

	public java.lang.Class<T> getResultType() {
		return resultType.getResultType();
	}
}

