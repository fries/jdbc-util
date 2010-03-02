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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.base.BaseSingleFieldPeriod;

/**
 * @author Friedrich Schäuffelhut
 * 
 */
public class StatementParameters
{
	public final static IfcStatementInParameterType<Boolean> Boolean = new BooleanInParameterType();
	public final static IfcStatementInParameter Boolean(Boolean value) { return bindValue(Boolean, value); }

	public final static IfcStatementInParameterType<Byte> Byte = new ByteInParameterType();
	public final static IfcStatementInParameter Byte(Byte value) { return bindValue(Byte, value); }

	public final static IfcStatementInParameterType<Character> Character = new CharacterInParameterType();
	public final static IfcStatementInParameter Character(Character value) { return bindValue(Character, value); }

	public final static IfcStatementInParameterType<Short> Short = new ShortInParameterType();
	public final static IfcStatementInParameter Short(Short value) { return bindValue(Short, value); }

	public final static IfcStatementInParameterType<Integer> Integer = new IntegerInParameterType();
	public final static IfcStatementInParameter Integer(Integer value) { return bindValue(Integer, value); }

	public final static IfcStatementInParameterType<Long> Long = new LongInParameterType();
	public final static IfcStatementInParameter Long(Long value) { return bindValue(Long, value); }

	public final static IfcStatementInParameterType<Float> Float = new FloatInParameterType();
	public final static IfcStatementInParameter Float(Float value) { return bindValue(Float, value); }

	public final static IfcStatementInParameterType<Double> Double = new DoubleInParameterType();
	public final static IfcStatementInParameter Double(Double value) { return bindValue(Double, value); }

	public final static IfcStatementInParameterType<BigDecimal> BigDecimal = new BigDecimalInParameterType();
	public final static IfcStatementInParameter BigDecimal(java.math.BigDecimal value) { return bindValue(BigDecimal, value); }

	public final static IfcStatementInParameterType<String> String = new StringInParameterType();
	public final static IfcStatementInParameter String(String value) { return bindValue(String, value); }

	public final static IfcStatementInParameterType<Date> Date = new DateInParameterType();
	public final static IfcStatementInParameter Date(Date value) { return bindValue(Date, value); }

	public final static IfcStatementInParameterType<Timestamp> Timestamp = new TimestampInParameterType();
	public final static IfcStatementInParameter Timestamp(Timestamp value) { return bindValue(Timestamp, value); }

	public final static IfcStatementInParameterType<Enum<?>> EnumByName = new EnumByNameParameterType();
	public final static IfcStatementInParameter EnumByName(Enum<?> value) { return bindValue(EnumByName, value); }

	public final static IfcStatementInParameterType<IfcEnumIntKey> EnumByIntKey = new EnumByIntKeyParameterType();
	public final static <E extends Enum<E> & IfcEnumIntKey> IfcStatementInParameter EnumByIntKey(E value) { return bindValue(EnumByIntKey, value); }

	public final static IfcStatementInParameterType<Class<?>> Class = new ClassParameterType();
	public final static IfcStatementInParameter Class(Class<?> value) { return bindValue(Class, value); }
	
	public final static IfcStatementInParameterType<DateTime> DateTime = new DateTimeInParameterType();
	public final static IfcStatementInParameter DateTime(DateTime value) { return bindValue(DateTime, value); }

	public final static IfcStatementInParameterType<DateMidnight> DateMidnight = new DateMidnightInParameterType();
	public final static IfcStatementInParameter DateMidnight(org.joda.time.DateMidnight value) { return bindValue(DateMidnight, value); }

	public final static IfcStatementInParameterType<Days> Days = new DaysInParameterType();
	public final static IfcStatementInParameter Days(Days value) { return bindValue(Days, value); }
	
	public final static IfcStatementInParameterType<Hours> Hours = new HoursInParameterType();
	public final static IfcStatementInParameter Hours(Hours value) { return bindValue(Hours, value); }
	
	public final static IfcStatementInParameterType<Minutes> Minutes = new MinutesInParameterType();
	public final static IfcStatementInParameter Minutes(Minutes value) { return bindValue(Minutes, value); }
	
	public final static IfcStatementInParameterType<Months> Months = new MonthsInParameterType();
	public final static IfcStatementInParameter Months(Months value) { return bindValue(Months, value); }
	
	public final static IfcStatementInParameterType<Seconds> Seconds = new SecondsInParameterType();
	public final static IfcStatementInParameter Seconds(Seconds value) { return bindValue(Seconds, value); }
	
	public final static IfcStatementInParameterType<Weeks> Weeks = new WeeksInParameterType();
	public final static IfcStatementInParameter Weeks(Weeks value) { return bindValue(Weeks, value); }
	
	public final static IfcStatementInParameterType<Years> Years = new YearsInParameterType();
	public final static IfcStatementInParameter Years(Years value) { return bindValue(Years, value); }
	
	// Array
	
	public final static <T> IfcStatementInParameterType<T[]> Array(IfcStatementInParameterType<T> type, String placeholder)
	{
		return new ArrayInParameterType<T>(type, placeholder);
	}

	public final static <T> IfcStatementInParameter Array(IfcStatementInParameterType<T> type, String placeholder, T... values)
	{
		return bindValue( new ArrayInParameterType<T>(type, placeholder), values );
	}

	public final static <T> IfcStatementInParameterType<T[]> Array(IfcStatementInParameterType<T> type, String placeholder, String placeholderReplacement)
	{
		return new ArrayInParameterType<T>(type, placeholder, placeholderReplacement);
	}

	public final static <T> IfcStatementInParameter Array(IfcStatementInParameterType<T> type, String placeholder, String placeholderReplacement, T... values)
	{
		return bindValue( new ArrayInParameterType<T>(type, placeholder, placeholderReplacement), values );
	}

	public final static <T> IfcStatementInParameterType<? extends java.util.Collection<T>> Collection(IfcStatementInParameterType<T> type, String placeholder)
	{
		return new CollectionInParameterType<T>(type, placeholder);
	}

	// Collection
	
	public final static <T> IfcStatementInParameter Collection(IfcStatementInParameterType<T> type, String placeholder, java.util.Collection<T> values)
	{
		return bindValue( new CollectionInParameterType<T>(type, placeholder), values );
	}

	public final static <T> IfcStatementInParameterType<? extends java.util.Collection<T>> Collection(IfcStatementInParameterType<T> type, String placeholder, String placeholderReplacement)
	{
		return new CollectionInParameterType<T>(type, placeholder, placeholderReplacement);
	}

	public final static <T> IfcStatementInParameter Collection(IfcStatementInParameterType<T> type, String placeholder, String placeholderReplacement, Collection<T> values)
	{
		return bindValue( new CollectionInParameterType<T>(type, placeholder, placeholderReplacement), values );
	}
	
	public final static <T> IfcStatementInParameter bindValue(
			IfcStatementInParameterType<T> type, T value)
	{
		return new BoundValue<T>( type, value );
	}
	
	// make a parameter array, allow nulls
	
	private final static IfcStatementInParameter[] emptyInParameters = new IfcStatementInParameter[0];
	public final static IfcStatementInParameter[] inParams( IfcStatementInParameter... parameters )
	{
		if ( parameters == null )
			return emptyInParameters;
		else
			return parameters;
	}

	public final static IfcStatementInParameter[] inParams( Collection<IfcStatementInParameter> parameters )
	{
		if ( parameters == null )
			return emptyInParameters;
		else if ( parameters.isEmpty() )
			return emptyInParameters;
		else
			return parameters.toArray( new IfcStatementInParameter[parameters.size()] );
	}
}

abstract class AbstractStatementInParameterType<T> implements IfcStatementInParameterType<T>
{
	private static final long	serialVersionUID	= 1080506691768998033L;

	public String modify(String sql, T value)
	{
		return sql;
	}
}

final class BooleanInParameterType extends AbstractStatementInParameterType<Boolean>
{
	private static final long	serialVersionUID	= -7524179426076727748L;

	public int configure(PreparedStatement stmt, int pos, Boolean value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.BOOLEAN );
		else
			stmt.setBoolean( pos, value );
		return 1;
	}
}

final class ByteInParameterType extends AbstractStatementInParameterType<Byte>
{
	private static final long	serialVersionUID	= 3276543340234344389L;

	public int configure(PreparedStatement stmt, int pos, Byte value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.TINYINT );
		else
			stmt.setByte( pos, value );
		return 1;
	}
}

final class CharacterInParameterType extends AbstractStatementInParameterType<Character>
{
	private static final long	serialVersionUID	= 8058124716481598135L;

	public int configure(PreparedStatement stmt, int pos, Character value) throws SQLException
	{
		stmt.setString( pos, value == null ? null : Character.toString( value ) );
		return 1;
	}
}

final class ShortInParameterType extends AbstractStatementInParameterType<Short>
{
	private static final long	serialVersionUID	= -3327050158970549254L;

	public int configure(PreparedStatement stmt, int pos, Short value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.SMALLINT );
		else
			stmt.setShort( pos, value );
		return 1;
	}
}

final class IntegerInParameterType extends AbstractStatementInParameterType<Integer>
{
	private static final long	serialVersionUID	= -5447866886419456863L;

	public int configure(PreparedStatement stmt, int pos, Integer value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.INTEGER );
		else
			stmt.setInt( pos, value );
		return 1;
	}
}

final class LongInParameterType extends AbstractStatementInParameterType<Long>
{
	private static final long	serialVersionUID	= 7351810604170815221L;

	public int configure(PreparedStatement stmt, int pos, Long value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.BIGINT );
		else
			stmt.setLong( pos, value );
		return 1;
	}
}


final class FloatInParameterType extends AbstractStatementInParameterType<Float>
{
	private static final long	serialVersionUID	= -2825273022431221813L;

	public int configure(PreparedStatement stmt, int pos, Float value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.FLOAT );
		else
			stmt.setFloat( pos, value );
		return 1;
	}
}

final class DoubleInParameterType extends AbstractStatementInParameterType<Double>
{
	private static final long	serialVersionUID	= 8197048169205460869L;

	public int configure(PreparedStatement stmt, int pos, Double value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.DOUBLE );
		else
			stmt.setDouble( pos, value );
		return 1;
	}
}

final class BigDecimalInParameterType extends AbstractStatementInParameterType<BigDecimal>
{
	private static final long	serialVersionUID	= -5647542748071958473L;

	public int configure(PreparedStatement stmt, int pos, BigDecimal value) throws SQLException
	{
		stmt.setBigDecimal( pos, value );
		return 1;
	}
}

final class StringInParameterType extends AbstractStatementInParameterType<String>
{
	private static final long	serialVersionUID	= 2569705798808925320L;

	public int configure(PreparedStatement stmt, int pos, String value) throws SQLException
	{
		stmt.setString( pos, value );
		return 1;
	}
}

final class DateInParameterType extends AbstractStatementInParameterType<Date>
{
	private static final long	serialVersionUID	= 885428146245470564L;

	public int configure(PreparedStatement stmt, int pos, Date value) throws SQLException
	{
		stmt.setDate( pos, value );
		return 1;
	}
}

final class TimestampInParameterType extends AbstractStatementInParameterType<Timestamp>
{
	private static final long	serialVersionUID	= 8660221331024108074L;

	public int configure(PreparedStatement stmt, int pos, Timestamp value) throws SQLException
	{
		stmt.setTimestamp( pos, value );
		return 1;
	}
}

final class EnumByNameParameterType
extends AbstractStatementInParameterType<Enum<?>>
{
	private static final long	serialVersionUID	= 1035562251364382806L;

	public int configure(PreparedStatement stmt, int pos, Enum<?> value) throws SQLException
	{
		if ( value == null )
			stmt.setString( pos, null );
		else
			stmt.setString( pos, value.name() );
		return 1;
	}
}

final class EnumByIntKeyParameterType
extends AbstractStatementInParameterType<IfcEnumIntKey>
{
	private static final long	serialVersionUID	= -854994345818350952L;

	public int configure(PreparedStatement stmt, int pos, IfcEnumIntKey value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.INTEGER );
		else
			stmt.setInt( pos, value.getKey() );
		return 1;
	}
}

final class ClassParameterType
extends AbstractStatementInParameterType<Class<?>>
{
	private static final long	serialVersionUID	= 4089853169817835313L;

	public int configure(PreparedStatement stmt, int pos, Class<?> value) throws SQLException
	{
		stmt.setString( pos, value == null ? null : value.getName() );
		return 1;
	}
}

final class DateTimeParameterType
extends AbstractStatementInParameterType<DateTime>
{
	private static final long	serialVersionUID	= 8992085626536517879L;

	public int configure(PreparedStatement stmt, int pos, DateTime value) throws SQLException
	{
		stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
		return 1;
	}
}

final class ArrayInParameterType<T> implements IfcStatementInParameterType<T[]>
{
	private static final long	serialVersionUID	= -4288132322340047766L;
	final IfcStatementInParameterType<T> type;
	final String placeholder;
	final String placeholderReplacement;

	ArrayInParameterType(IfcStatementInParameterType<T> type, String placeholder)
	{
		this( type, placeholder, "?" );
	}
	
	ArrayInParameterType(IfcStatementInParameterType<T> type, String placeholder, String placeholderReplacement)
	{
		this.placeholder = placeholder;
		this.type = type;
		this.placeholderReplacement = placeholderReplacement;
	}
	
	/* (non-Javadoc)
	 * @see de.schaeuffelhut.jdbc.StatementInParameterType#modify(java.lang.String)
	 */
	public final String modify(String sql, T[] values)
	{
		StringBuilder sb = new StringBuilder();
		if ( values != null )
		{
			for(int i = 0; i < values.length; i++)
			{
				if ( i > 0 )
					sb.append( ',' );
				sb.append( type.modify( placeholderReplacement, values[i] ) );
			}
		}
		return sql.replace( placeholder, sb );	
	}
	
	public final int configure(PreparedStatement stmt, int pos, T[] values) throws SQLException
	{
		int posAdvance = 0;
		if ( values != null )
			for(int i = 0; i < values.length; i++)
				posAdvance += type.configure( stmt, pos + posAdvance, values[i] );
		return posAdvance;
	};
}

final class CollectionInParameterType<T> implements IfcStatementInParameterType<Collection<T>>
{
	private static final long	serialVersionUID	= -4288132322340047766L;
	final IfcStatementInParameterType<T> type;
	final String placeholder;
	final String placeholderReplacement;

	CollectionInParameterType(IfcStatementInParameterType<T> type, String placeholder)
	{
		this( type, placeholder, "?" );
	}
	
	CollectionInParameterType(IfcStatementInParameterType<T> type, String placeholder, String placeholderReplacement)
	{
		this.placeholder = placeholder;
		this.type = type;
		this.placeholderReplacement = placeholderReplacement;
	}
	
	/* (non-Javadoc)
	 * @see de.schaeuffelhut.jdbc.StatementInParameterType#modify(java.lang.String)
	 */
	public final String modify(String sql, Collection<T> values)
	{
		StringBuilder sb = new StringBuilder();
		if ( values != null )
		{
			int i=0;
			for(T value : values)
			{
				if ( i > 0 )
					sb.append( ',' );
				sb.append( type.modify( placeholderReplacement, value ) );
				i++;
			}
		}
		return sql.replace( placeholder, sb );	
	}
	
	public final int configure(PreparedStatement stmt, int pos, Collection<T> values) throws SQLException
	{
		int posAdvance = 0;
		if ( values != null )
			for(T value : values)
				posAdvance += type.configure( stmt, pos + posAdvance, value );
		return posAdvance;
	};
}


final class DateTimeInParameterType extends AbstractStatementInParameterType<DateTime>
{
	private static final long	serialVersionUID	= 362585378891547200L;

	public int configure(PreparedStatement stmt, int pos, DateTime value) throws SQLException
	{
		stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
		return 1;
	}
}

final class DateMidnightInParameterType extends AbstractStatementInParameterType<DateMidnight>
{
	private static final long	serialVersionUID	= 362585378891547200L;

	public int configure(PreparedStatement stmt, int pos, DateMidnight value) throws SQLException
	{
		stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
		return 1;
	}
}

abstract class AbstractBaseSingleFieldPeriodInParameterType<T extends BaseSingleFieldPeriod> extends AbstractStatementInParameterType<T>
{
	private static final long	serialVersionUID	= 1978434854099312620L;

	public final int configure(PreparedStatement stmt, int pos, T value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.INTEGER );
		else
			stmt.setInt( pos, getAmount( value ) );
		return 1;
	}

	protected abstract int getAmount(T baseSingleFieldPeriod);
}
final class DaysInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Days>
{
	private static final long	serialVersionUID	= 6586725230659394219L;

	@Override
	protected int getAmount(Days baseSingleFieldPeriod)
	{
		return baseSingleFieldPeriod.getDays();
	}
}
final class HoursInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Hours>
{
	private static final long	serialVersionUID	= 2623522654226899799L;

	protected int getAmount(Hours baseSingleFieldPeriod)
	{
		return baseSingleFieldPeriod.getHours();
	};
}

final class MinutesInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Minutes>
{
	private static final long	serialVersionUID	= 942401460273082257L;

	@Override
	protected int getAmount(Minutes baseSingleFieldPeriod)
	{
		return baseSingleFieldPeriod.getMinutes();
	}
}
final class MonthsInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Months>
{
	private static final long	serialVersionUID	= 3461131092273692150L;

	@Override
	protected int getAmount(Months baseSingleFieldPeriod)
	{
		return baseSingleFieldPeriod.getMonths();
	}
}
final class SecondsInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Seconds>
{
	private static final long	serialVersionUID	= 8922273960196447482L;

	@Override
	protected int getAmount(Seconds baseSingleFieldPeriod)
	{
		return baseSingleFieldPeriod.getSeconds();
	}
}
final class WeeksInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Weeks>
{
	private static final long	serialVersionUID	= -7008193162056461183L;

	@Override
	protected int getAmount(Weeks baseSingleFieldPeriod)
	{
		return baseSingleFieldPeriod.getWeeks();
	}
}
final class YearsInParameterType extends AbstractBaseSingleFieldPeriodInParameterType<Years>
{
	private static final long	serialVersionUID	= 5488175347491622019L;

	@Override
	protected int getAmount(Years baseSingleFieldPeriod)
	{
		return baseSingleFieldPeriod.getYears();
	}
}
