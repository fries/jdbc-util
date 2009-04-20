/**
 * (C) Copyright 2007 M.Sc. Friedrich Schäuffelhut
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * $Revison$
 * $Author$
 * $Date$
 */
package de.schaeuffelhut.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.joda.time.DateTime;

/**
 * @author M.Sc. Friedrich Schäuffelhut
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

	
	
	public final static <T> IfcStatementInParameter bindValue(
			IfcStatementInParameterType<T> type, T value)
	{
		return new BoundValue<T>( type, value );
	}
	
	
	private final static IfcStatementInParameter[] emptyInParameters = new IfcStatementInParameter[0];
	public final static IfcStatementInParameter[] inParams(
			IfcStatementInParameter... parameters)
	{
		if ( parameters == null )
			return emptyInParameters;
		else
			return parameters;
	}
}

abstract class AbstractStatementInParameterType<T> implements IfcStatementInParameterType<T>
{
	public String modify(String sql, T value)
	{
		return sql;
	}
}

final class BooleanInParameterType extends AbstractStatementInParameterType<Boolean>
{
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
	public int configure(PreparedStatement stmt, int pos, Character value) throws SQLException
	{
		stmt.setString( pos, value == null ? null : Character.toString( value ) );
		return 1;
	}
}

final class ShortInParameterType extends AbstractStatementInParameterType<Short>
{
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
	public int configure(PreparedStatement stmt, int pos, Double value) throws SQLException
	{
		if ( value == null )
			stmt.setNull( pos, Types.DOUBLE );
		else
			stmt.setDouble( pos, value );
		return 1;
	}
}

final class StringInParameterType extends AbstractStatementInParameterType<String>
{
	public int configure(PreparedStatement stmt, int pos, String value) throws SQLException
	{
		stmt.setString( pos, value );
		return 1;
	}
}

final class DateInParameterType extends AbstractStatementInParameterType<Date>
{
	public int configure(PreparedStatement stmt, int pos, Date value) throws SQLException
	{
		stmt.setDate( pos, value );
		return 1;
	}
}

final class TimestampInParameterType extends AbstractStatementInParameterType<Timestamp>
{
	public int configure(PreparedStatement stmt, int pos, Timestamp value) throws SQLException
	{
		stmt.setTimestamp( pos, value );
		return 1;
	}
}

final class EnumByNameParameterType
extends AbstractStatementInParameterType<Enum<?>>
{
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
	public int configure(PreparedStatement stmt, int pos, Class<?> value) throws SQLException
	{
		stmt.setString( pos, value == null ? null : value.getName() );
		return 1;
	}
}

final class DateTimeParameterType
extends AbstractStatementInParameterType<DateTime>
{
	public int configure(PreparedStatement stmt, int pos, DateTime value) throws SQLException
	{
		stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
		return 1;
	}
}

final class ArrayInParameterType<T> implements IfcStatementInParameterType<T[]>
{
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


final class DateTimeInParameterType extends AbstractStatementInParameterType<DateTime>
{
	public int configure(PreparedStatement stmt, int pos, DateTime value) throws SQLException
	{
		stmt.setTimestamp( pos, value == null ? null : new Timestamp( value.getMillis() ) );
		return 1;
	}
}
