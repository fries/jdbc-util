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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateTime;


/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class ResultTypes
{
	public final static IfcResultType<Boolean> Boolean = new BooleanResultType();
	public final static IfcResultType<Byte> Byte = new ByteResultType();
	public final static IfcResultType<Short> Short = new ShortResultType();
	public final static IfcResultType<Integer> Integer = new IntegerResultType();
	public final static IfcResultType<Long> Long = new LongResultType();
	public final static IfcResultType<Float> Float = new FloatResultType();
	public final static IfcResultType<Double> Double = new DoubleResultType();
	public final static IfcResultType<BigDecimal> BigDecimal = new BigDecimalResultType();
	public final static IfcResultType<String> String = new StringResultType();
	public final static IfcResultType<Date> Date = new DateResultType();
	public final static IfcResultType<Timestamp> Timestamp = new TimestampResultType();
	public final static IfcResultType<Object> Object = new ObjectResultType();

	public final static <E extends Enum<E>> IfcResultType<E> enumByName(Class<E> type)
	{
		return new EnumByNameResultType<E>(type);
	}

	public final static <E extends Enum<E> & IfcEnumIntKey> IfcResultType<E> enumByIntKey(Class<E> type)
	{
		return new EnumByIntKeyResultType<E>( type );
	}

	public final static IfcResultType<Class<?>> Class = new ClassResultType();

	public final static IfcResultType<DateTime> DateTime = new DateTimeResultType();

	public final static IfcResultType<?>[] resultTypes(IfcResultType<?>... resultTypes)
	{
		return resultTypes;
	}
}

final class BooleanResultType implements IfcResultType<Boolean>
{
    public final Boolean getResult(ResultSet resultSet, int index) throws SQLException
    {
        boolean value = resultSet.getBoolean( index );
		return resultSet.wasNull() ? null : value;
    }
}

final class ByteResultType implements IfcResultType<Byte>
{
    public final Byte getResult(ResultSet resultSet, int index) throws SQLException
    {
        byte value = resultSet.getByte( index );
		return resultSet.wasNull() ? null : value;
    }
}

final class ShortResultType implements IfcResultType<Short>
{
    public final Short getResult(ResultSet resultSet, int index) throws SQLException
    {
        short value = resultSet.getShort( index );
		return resultSet.wasNull() ? null : value;
    }
}

final class IntegerResultType implements IfcResultType<Integer>
{
    public final Integer getResult(ResultSet resultSet, int index) throws SQLException
    {
        int value = resultSet.getInt( index );
		return resultSet.wasNull() ? null : value;
    }
}

final class LongResultType implements IfcResultType<Long>
{
    public final Long getResult(ResultSet resultSet, int index) throws SQLException
    {
        long value = resultSet.getLong( index );
		return resultSet.wasNull() ? null : value;
    }
}

final class FloatResultType implements IfcResultType<Float>
{
    public final Float getResult(ResultSet resultSet, int index) throws SQLException
    {
        float value = resultSet.getFloat( index );
		return resultSet.wasNull() ? null : value;
    }
}

final class DoubleResultType implements IfcResultType<Double>
{
    public final Double getResult(ResultSet resultSet, int index) throws SQLException
    {
        double value = resultSet.getDouble( index );
		return resultSet.wasNull() ? null : value;
    }
}

final class BigDecimalResultType implements IfcResultType<BigDecimal>
{
    public final BigDecimal getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getBigDecimal( index );
    }
}

final class StringResultType implements IfcResultType<String>
{
    public final String getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getString( index );
    }
}

final class DateResultType implements IfcResultType<Date>
{
    public final Date getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getDate( index );
    }
}

final class TimestampResultType implements IfcResultType<Timestamp>
{
    public final Timestamp getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getTimestamp( index );
    }
}

final class ObjectResultType implements IfcResultType<Object>
{
    public final Object getResult(ResultSet resultSet, int index) throws SQLException
    {
        return resultSet.getObject( index );
    }
}

final class EnumByNameResultType<E extends Enum<E>> implements IfcResultType<E>
{
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
    		return Enum.valueOf( type, string );
    }
}

final class EnumByIntKeyResultType<E extends Enum<E> & IfcEnumIntKey>
		implements IfcResultType<E>
{
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
}

final class ClassResultType implements IfcResultType<Class<?>>
{
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
				clazz = Class.forName( className );
			}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException( e );
			}
    	}
        return clazz;
    }
}

final class DateTimeResultType implements IfcResultType<DateTime>
{
    public final DateTime getResult(ResultSet resultSet, int index) throws SQLException
    {
        Timestamp timestamp = resultSet.getTimestamp( index );
		return timestamp == null ? null : new DateTime( timestamp );
    }
}

