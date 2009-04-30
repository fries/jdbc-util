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

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class ResultAdaptors
{
	/**
	 * @author M.Sc. Friedrich Schäuffelhut
	 *
	 */
	public static final class ArrayResultAdaptor
	implements IfcResultAdaptor<Object[]>
	{
		/**
		 * 
		 */
		private final int columnIndex;
		
		/**
		 * 
		 */
		private final IfcResultType<?> resultType;
	
		/**
		 * @param columnName
		 * @param resultType
		 */
		public ArrayResultAdaptor(
				int arrayIndex,
				IfcResultType<?> resultType
		){
			this.columnIndex = arrayIndex;
			this.resultType = resultType;
		}
	
		public final int adapt(
				Object[] array, ResultSet resultSet, int index
		) throws SQLException
		{
			array[columnIndex] = resultType.getResult( resultSet, index );
			return 1;
		}
	}

	
	@SuppressWarnings("unchecked")
	public final static IfcResultAdaptor<Object[]>[] createArrayResultAdaptors(
			IfcResultType<?>... resultTypes
	) throws SQLException
	{
		IfcResultAdaptor<Object[]>[] adaptors =
			(IfcResultAdaptor<Object[]>[])
			new IfcResultAdaptor[resultTypes.length];
	
		for(int i = 0; i < resultTypes.length; i++)
			adaptors[i] = new ArrayResultAdaptor(
					i,
					resultTypes[i]
			);
		return adaptors;
	}



	/**
	 * @author M.Sc. Friedrich Schäuffelhut
	 *
	 */
	public static final class MapResultAdaptor
	implements IfcResultAdaptor<Map<String, Object>>
	{
		/**
		 * 
		 */
		private final String columnName;
		
		/**
		 * 
		 */
		private final IfcResultType<?> resultType;
	
		/**
		 * @param columnName
		 * @param resultType
		 */
		public MapResultAdaptor(
				String columnName,
				IfcResultType<?> resultType
		){
			this.resultType = resultType;
			this.columnName = columnName;
		}
	
		public final int adapt(
				Map<String,Object> map, ResultSet resultSet, int index
		) throws SQLException
		{
			map.put( columnName, resultType.getResult( resultSet, index ) );
			return 1;
		}
	}

	public final static IfcResultAdaptor<Map<String, Object>>[] createMapResultAdaptors(
			ResultSet resultSet, IfcResultType<?>... resultTypes
	) throws SQLException
	{
		final int columnIndex = 1;
		return createMapResultAdaptors( resultSet, columnIndex, resultTypes ); 
	}
	
	@SuppressWarnings("unchecked")
	public final static IfcResultAdaptor<Map<String, Object>>[] createMapResultAdaptors(
			ResultSet resultSet, int columnIndex, IfcResultType<?>... resultTypes
	) throws SQLException
	{
		IfcResultAdaptor<Map<String,Object>>[] adaptors =
			(IfcResultAdaptor<Map<String,Object>>[])
			new IfcResultAdaptor[resultTypes.length];
	
		ResultSetMetaData metaData = resultSet.getMetaData();
		for(int i = 0; i < resultTypes.length; i++)
			adaptors[i] = new MapResultAdaptor(
					metaData.getColumnName( columnIndex + i ),
					resultTypes[i]
			);
		return adaptors;
	}

	/**
	 * @author M.Sc. Friedrich Schäuffelhut
	 *
	 */
	public static final class FieldResultAdaptor<T> 
	implements IfcResultAdaptor<T>
	{
		/**
		 * 
		 */
		private final Field field;
		
		/**
		 * 
		 */
		private final IfcResultType<?> resultType;
	
		/**
		 * @param columnName
		 * @param resultType
		 */
		public FieldResultAdaptor(
				Class<T> clazz,
				String columnName,
				IfcResultType<?> resultType
		){
			try
			{
				this.field = clazz.getDeclaredField( columnName );
				this.field.setAccessible( true );
				this.resultType = resultType;
			}
			catch (SecurityException e)
			{
				throw new RuntimeException( e );
			}
			catch (NoSuchFieldException e)
			{
				throw new RuntimeException( e );
			}
		}
	
		
		public final int adapt(
				T object, ResultSet resultSet, int index
		) throws SQLException
		{
			try
			{
				field.set( object, resultType.getResult( resultSet, index ) );
				return 1;
			}
			catch (IllegalArgumentException e)
			{
				throw new RuntimeException( e );
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException( e );
			}
		}
	}

	@SuppressWarnings("unchecked")
	public final static <T> IfcResultAdaptor<T>[] createFieldResultAdaptors(
			ResultSet resultSet,
			Class<T> clazz,
			IfcResultType<?>... resultTypes
	) throws SQLException
	{
		IfcResultAdaptor<T>[] adaptors =
			(IfcResultAdaptor<T>[])
			new IfcResultAdaptor[resultTypes.length];
	
		ResultSetMetaData metaData = resultSet.getMetaData();
		for(int i = 0; i < resultTypes.length; i++)
			adaptors[i] = new FieldResultAdaptor(
					clazz,
					metaData.getColumnName( i + 1 ),
					resultTypes[i]
			);
		return adaptors;
	}


	private ResultAdaptors()
	{
		// TODO Auto-generated constructor stub
	}

	
	
	public final static <T> void adapt(
			ResultSet rs, T t, IfcResultAdaptor<T>... adaptors
	) throws SQLException
	{
		final int columnIndex = 1;
		adapt( rs, columnIndex, t, adaptors );
	}


	public final static <T> int adapt(
			ResultSet rs,
			int columnIndex,
			T t,
			IfcResultAdaptor<T>... adaptors
	) throws SQLException
	{
		int columnOffset = 0;
		for(int i = 0; i < adaptors.length; i++ )
			if ( adaptors[i] != null )
				columnOffset += adaptors[i].adapt(
						t, rs, columnIndex + columnOffset );
		return columnOffset;
	}
}
