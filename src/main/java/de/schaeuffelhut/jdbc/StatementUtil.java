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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;




/**
 * @author Friedrich Schäuffelhut
 *
 */
public final class StatementUtil
{
	private final static Logger logger = Logger.getLogger( StatementUtil.class );
	
	private StatementUtil(){}

	/*
	 * selectInto scalar and collection
	 */

	public final static <T> T selectInto(
			Connection connection,
			String sql,
			IfcResultSetScalarReader<T> resultReader,
			Collection<IfcStatementInParameter> parameters
	) throws Exception
	{
		return selectInto(
				connection, sql, resultReader,
				parameters.toArray( new IfcStatementInParameter[parameters.size()] )
		);
	}
	
	public final static <T> T selectInto(
			Connection connection,
			String sql,
			IfcResultSetScalarReader<T> resultReader,
			IfcStatementInParameter... parameters
	) throws Exception
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoScalar: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			return resultReader.readResult( resultSet );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	public final static <E> ArrayList<E> selectInto(
			Connection connection,
			String sql,
			IfcResultSetCollectionReader<E> resultReader,
			Collection<IfcStatementInParameter> parameters
	) throws Exception
	{
		return selectInto(connection, sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <E> ArrayList<E> selectInto(
			Connection connection,
			String sql,
			IfcResultSetCollectionReader<E> resultReader,
			IfcStatementInParameter... parameters
	) throws Exception
	{
		return selectInto( new ArrayList<E>(), connection, sql, resultReader, parameters );
	}

	public final static <T extends Collection<E>,E> T selectInto(
			T results,
			Connection connection,
			String sql,
			IfcResultSetCollectionReader<E> resultReader,
			Collection<IfcStatementInParameter> parameters
	) throws Exception
	{
		return selectInto(results, connection, sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T extends Collection<E>,E> T selectInto(
			T results,
			Connection connection,
			String sql,
			IfcResultSetCollectionReader<E> resultReader,
			IfcStatementInParameter... parameters
	) throws Exception
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoObjects: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			resultReader.readResults( results, resultSet );
			return results;
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	public final static <V,T> V process(
			Connection connection,
			String sql,
			IfcResultSetProcessor<V,T> resultSetProcessor,
			IfcStatementInParameter... parameters
	) throws Exception
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoObjects: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			return resultSetProcessor.readResults( resultSet );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	/*
	 * inserts / updates
	 */
	
	public final static int execute(Connection connection, String sql, IfcStatementInParameter...parameters) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "execute: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			int count = stmt.executeUpdate();
			if ( logger.isTraceEnabled() )
				logger.trace( String.format( "updated %d records", count ) );			
			return count;
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}
	
	public final static <T> T execute(Connection connection, GeneratedKeys generatedKeys, IfcStatementProperty<T> statementProperty, String sql, IfcStatementInParameter...parameters) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "execute: " + sql );
			
			stmt = prepareStatement( connection, sql, generatedKeys, parameters );
			stmt.execute();
			
			final T returnValue;
			if ( statementProperty == null )
				returnValue = null;
			else
				returnValue = statementProperty.get( stmt );
			return returnValue;
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated
	public final static Object[] execute(Connection connection, IfcStatementProperty<?>[] properties, String sql, IfcStatementInParameter...parameters) throws SQLException
	{
		return execute(connection, null, properties, sql, parameters);
	}
	
	public final static Object[] execute(Connection connection, GeneratedKeys generatedKeys, IfcStatementProperty<?>[] properties, String sql, IfcStatementInParameter...parameters) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "execute: " + sql );
			
			stmt = prepareStatement( connection, sql, generatedKeys, parameters );
			stmt.execute();
			
			final Object[] returnValues;
			if ( properties != null )
			{
				returnValues = new Object[properties.length];
				for ( int i = 0; i < properties.length; i++ )
					returnValues[i] = properties[i].get( stmt );
			}
			else
			{
				returnValues = null;
			}
			
			return returnValues;
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	/*
	 * executing bulk statements
	 */

	public final static int[] execute(Connection connection, String sql, IfcStatementInParameter[][] parameters) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "execute: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			final int[] count;
			if ( stmt == null ) // happens if parameters == null
			{
				count = new int[0];
			}
			else
			{
				count = stmt.executeBatch();
				if ( logger.isTraceEnabled() )
					logger.trace( String.format( "updated %s records", Arrays.asList( count ) ) );			
			}
			return count;
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	public final static <T> T execute(Connection connection, GeneratedKeys generatedKeys, IfcStatementProperty<T> statementProperty, String sql, IfcStatementInParameter[][] parameters) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "execute: " + sql );
			
			stmt = prepareStatement( connection, sql, generatedKeys, parameters );
			stmt.executeBatch();
			
			final T returnValue;
			if ( statementProperty == null )
				returnValue = null;
			else
				returnValue = statementProperty.get( stmt );
			return returnValue;
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	
	
	// untested
	public final static Object[] executeCall(Connection connection, String sql, IfcStatementParameter... parameters) throws SQLException
	{
		return new Call().execute( connection, sql, parameters );
	}

	/*
	 * Expand SQL query and configure PreparedStatement
	 */

	public final static String modifySql(String sql,  IfcStatementParameter... parameters)
	{
		final String unmodifiedSQL = sql;
		
		if ( parameters != null )
			for(IfcStatementParameter param : parameters )
				sql = param.modify( sql );
		
		if ( unmodifiedSQL != sql )
			logger.trace( "modified sql: " +sql );
		
		return sql;
	}
	
	public enum GeneratedKeys
	{ 
		REPORT( Statement.RETURN_GENERATED_KEYS ),
		IGNORE( Statement.NO_GENERATED_KEYS );
		
		public final int autoGeneratedKey;

		private GeneratedKeys(int autoGeneratedKey) 
		{
			this.autoGeneratedKey = autoGeneratedKey;
		}
	}
	
	public static PreparedStatement prepareStatement(Connection connection, String sql, IfcStatementInParameter... parameters) throws SQLException
	{
		return prepareStatement(connection, sql, null, parameters);
	}
	
	public static PreparedStatement prepareStatement(Connection connection, String sql, GeneratedKeys generatedKeys, IfcStatementInParameter... parameters) throws SQLException
	{
		sql = modifySql( sql, parameters );
		
		PreparedStatement stmt;
		if ( generatedKeys == null )
			stmt = connection.prepareStatement( sql );
		else
			stmt = connection.prepareStatement( sql, generatedKeys.autoGeneratedKey );

		configureStatement( stmt, parameters );
		return stmt;
	}

	public static PreparedStatement prepareStatement(Connection connection, String sql, IfcStatementInParameter[][] parameters) throws SQLException
	{
		return prepareStatement(connection, sql, null, parameters);
	}
	
	public static PreparedStatement prepareStatement(Connection connection, String sql, GeneratedKeys generatedKeys, IfcStatementInParameter[][] parameters) throws SQLException
	{
		if ( parameters != null && parameters.length > 0 )
		{
			sql = modifySql( sql, parameters[0] );
			
			PreparedStatement stmt;
			if ( generatedKeys == null )
				stmt = connection.prepareStatement( sql );
			else
				stmt = connection.prepareStatement( sql, generatedKeys.autoGeneratedKey );

			for(int i = 0; i < parameters.length; i++)
			{
				configureStatement( stmt, parameters[i] );
				if ( logger.isTraceEnabled() )
					logger.trace( String.format("add batch %d", i ) );
				stmt.addBatch();
			}
			return stmt;
		}
		return null;
	}

	public static void configureStatement(PreparedStatement stmt, IfcStatementInParameter... parameters) throws SQLException
	{
		int index = 1;
		if ( parameters != null )
			for(IfcStatementInParameter param : parameters )
				index += param.configure( stmt, index );
	}

	// Untested
	final static class Call
	{
		private CallableStatement stmt;
		private IfcStatementParameter[] parameters;
		private int[] parameterIndices;
		
		final void prepare(Connection connection, String sql, IfcStatementParameter... parameters) throws SQLException
		{
			if ( stmt != null )
				throw new RuntimeException( "Statement is already prepared" );
			
			sql = modifySql( sql, parameters );

			this.stmt = connection.prepareCall( sql );
			this.parameterIndices = new int[parameters.length];
			this.parameters = new IfcStatementParameter[parameters.length];
			
			if ( parameters != null )
			{
				int i = 0;
				int index = 1;
				for(IfcStatementParameter param : parameters )
				{
					this.parameterIndices[i] = index;
					this.parameters[i] = parameters[i];
					
					if ( param instanceof IfcStatementInParameter )
						index += ((IfcStatementInParameter)param).configure( stmt, index );
					else if ( param instanceof IfcStatementOutParameter )
						index += ((IfcStatementOutParameter<?>)param).configure( stmt, index );
					else if ( param instanceof IfcStatementProperty )
						;//ignore
					else if ( param == null )
						;// ignore
					else
						throw new RuntimeException( String.format(
								"unkown implementation of interface %s: %s",
								IfcStatementParameter.class,
								param.getClass()
						));
					i++;
				}
			}

		}
		
		final Object[] read() throws SQLException
		{
			int countOutParams = 0;
			for(IfcStatementParameter p : parameters)
			{
				if ( p instanceof IfcStatementOutParameter )
					countOutParams++;
				else if ( p instanceof IfcStatementProperty )
					countOutParams++;
				else
					; // ignore
			}
			
			Object[] returnValues = new Object[countOutParams];
			
			for( int i = 0, j = 0; i < parameters.length; i++ )
			{
				if ( parameters[i] instanceof IfcStatementOutParameter )
				{
					IfcStatementOutParameter<?> p = (IfcStatementOutParameter<?>)parameters[i];
					returnValues[j++] = p.readValue( stmt, parameterIndices[i] );
				}
				else if ( parameters[i] instanceof IfcStatementProperty )
				{
					IfcStatementProperty<?> p = (IfcStatementProperty<?>)parameters[i];
					returnValues[j++] = p.get( stmt );
				}
				else
				{
					// ignore
				}
			}
			
			return returnValues;
		}
		
		final void close()
		{
			JdbcUtil.closeQuietly( stmt );
			stmt = null;
			parameterIndices = null;
			parameters = null;
		}
		
		final Object[] execute(Connection connection) throws SQLException
		{
			if ( stmt == null )
				throw new RuntimeException( "Statement is not prepared" );
			
			try
			{
				stmt.execute();
				return read();
			}
			finally
			{
				close();
			}
		}
		
		final Object[] execute(Connection connection, String sql, IfcStatementParameter... parameters) throws SQLException
		{
			prepare( connection, sql, parameters );
			return execute( connection );
		}

	}
}
