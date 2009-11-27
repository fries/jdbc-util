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
package de.schaeuffelhut.jdbc.xx;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import de.schaeuffelhut.jdbc.IfcResultAdaptor;
import de.schaeuffelhut.jdbc.IfcResultFactory;
import de.schaeuffelhut.jdbc.IfcResultSetCollectionReader;
import de.schaeuffelhut.jdbc.IfcResultSetScalarReader;
import de.schaeuffelhut.jdbc.IfcResultType;
import de.schaeuffelhut.jdbc.IfcStatementInParameter;
import de.schaeuffelhut.jdbc.IfcStatementOutParameter;
import de.schaeuffelhut.jdbc.IfcStatementParameter;
import de.schaeuffelhut.jdbc.JdbcUtil;
import de.schaeuffelhut.jdbc.ResultSetUtil;


/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class StatementUtil
{
	private final static Logger logger = Logger.getLogger( StatementUtil.class );
	
	private StatementUtil(){}

	/*
	 * selectInto scalar and collection
	 */

	public final static <T> T selectInto(Connection connection, String sql, IfcResultSetScalarReader<T> resultReader, Collection<IfcStatementInParameter> parameters) throws Exception
	{
		return selectInto(connection, sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T> T selectInto(Connection connection, String sql, IfcResultSetScalarReader<T> resultReader, IfcStatementInParameter... parameters) throws Exception
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

	public final static <T> ArrayList<T> selectInto(Connection connection, String sql, IfcResultSetCollectionReader<T> resultReader, Collection<IfcStatementInParameter> parameters) throws Exception
	{
		return selectInto(connection, sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T> ArrayList<T> selectInto(Connection connection, String sql, IfcResultSetCollectionReader<T> resultReader, IfcStatementInParameter... parameters) throws Exception
	{
		ArrayList<T> results = new ArrayList<T>();
		selectInto( results, connection, sql, resultReader, parameters );
		return results;
	}

	public final static <T> void selectInto(Collection<T> results, Connection connection, String sql, IfcResultSetCollectionReader<T> resultReader, Collection<IfcStatementInParameter> parameters) throws Exception
	{
		selectInto(results, connection, sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T> void selectInto(Collection<T> results, Connection connection, String sql, IfcResultSetCollectionReader<T> resultReader, IfcStatementInParameter... parameters) throws Exception
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoObjects: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			resultReader.readResults( results, resultSet );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	
	/*
	 * select single row
	 */

	@Deprecated // use selectInto()
	public final static <T> T selectIntoScalar(Connection connection, String sql, IfcResultType<T> resultType) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		return selectIntoScalar( connection, sql, resultType, (IfcStatementInParameter[])null );
	}

	@Deprecated // use selectInto()
	public final static <T> T selectIntoScalar(Connection connection, String sql, IfcResultType<T> resultType, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoScalar: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			return ResultSetUtil.readScalar( resultSet, resultType );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static Object[] selectIntoTuple(Connection connection, String sql, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		return selectIntoTuple( connection, sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static Object[] selectIntoTuple(Connection connection, String sql, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoTuple: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			return ResultSetUtil.readTuple( resultSet, resultTypes );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static Map<String,Object> selectIntoMap(Connection connection, String sql, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		return selectIntoMap( connection, sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static Map<String,Object> selectIntoMap(Connection connection, String sql, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoMaps: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			return ResultSetUtil.readMap( resultSet, resultTypes );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static <T> T selectIntoObject(Connection connection, String sql, Class<T> type, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		return selectIntoObject( connection, sql, type, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static <T> T selectIntoObject(Connection connection, String sql, Class<T> type, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoObject: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			return ResultSetUtil.readObject( resultSet, type, resultTypes );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static <T> T selectInto(Connection connection, String sql, IfcResultFactory<T> resultFactory, IfcResultAdaptor<T>[] resultSetAdaptors, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoObject: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			return ResultSetUtil.readResult( resultSet, resultFactory, resultSetAdaptors );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	/*
	 * select multiple rows
	 */

	@Deprecated // use selectInto()
	public final static <T> ArrayList<T> selectIntoScalars(Connection connection, String sql, IfcResultType<T> resultType, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		ArrayList<T> results = new ArrayList<T>();
		selectIntoScalars( results, connection, sql, resultType, parameters );
		return results;
	}

	@Deprecated // use selectInto()
	public final static <T> void selectIntoScalars(Collection<T> results, Connection connection, String sql, IfcResultType<T> resultType, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoScalars: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			ResultSetUtil.readScalars( results, resultSet, resultType );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static ArrayList<Object[]> selectIntoTuples(Connection connection, String sql, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		return selectIntoTuples( connection, sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static ArrayList<Object[]> selectIntoTuples(Connection connection, String sql, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		ArrayList<Object[]> results = new ArrayList<Object[]>();
		selectIntoTuples( results, connection, sql, resultTypes );
		return results;
	}

	@Deprecated // use selectInto()
	public final static <T> void selectIntoTuples(Collection<Object[]> results, Connection connection, String sql, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		selectIntoTuples( results, connection, sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static <T> void selectIntoTuples(Collection<Object[]> results, Connection connection, String sql, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoTuples: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			ResultSetUtil.readTuples( results, resultSet, resultTypes );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static ArrayList<Map<String,Object>> selectIntoMaps(Connection connection, String sql, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		return selectIntoMaps( connection, sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static ArrayList<Map<String,Object>> selectIntoMaps(Connection connection, String sql, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		ArrayList<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		selectIntoMaps( results, connection, sql, resultTypes, parameters );
		return results;
	}
	
	@Deprecated // use selectInto()
	public final static <T> void selectIntoMaps(Collection<Map<String,Object>> results, Connection connection, String sql, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		selectIntoMaps( results, connection, sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static <T> void selectIntoMaps(Collection<Map<String,Object>> results, Connection connection, String sql, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoMaps: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			ResultSetUtil.readMaps( results, resultSet, resultTypes );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static <T> ArrayList<T> selectIntoObjects(Connection connection, String sql, Class<T> type, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		return selectIntoObjects( connection, sql, type, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static <T> ArrayList<T> selectIntoObjects(Connection connection, String sql, Class<T> type, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		ArrayList<T> results = new ArrayList<T>();
		selectIntoObjects( results, connection, sql, type, resultTypes, parameters );
		return results;
	}

	@Deprecated // use selectInto()
	public final static <T> void selectIntoObjects(Collection<T> results, Connection connection, String sql, Class<T> type, IfcResultType<?>... resultTypes) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		selectIntoObjects( results, connection, sql, type, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto()
	public final static <T> void selectIntoObjects(Collection<T> results, Connection connection, String sql, Class<T> type, IfcResultType<?>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoObjects: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			ResultSetUtil.readObjects( results, resultSet, type, resultTypes );
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	@Deprecated // use selectInto()
	public final static <T> ArrayList<T>  selectIntoCollection(Connection connection, String sql, IfcResultFactory<T> factory, IfcResultAdaptor<T>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		ArrayList<T> results = new ArrayList<T>();
		selectIntoCollection( results, connection, sql, factory, resultTypes, parameters );
		return results;
	}
	
	@Deprecated // use selectInto()
	public final static <T> void selectIntoCollection(
			Collection<T> results, Connection connection, String sql, IfcResultFactory<T> factory, IfcResultAdaptor<T>[] resultTypes, IfcStatementInParameter... parameters) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "selectIntoObjects: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			ResultSet resultSet = stmt.executeQuery();
			ResultSetUtil.readResults( results, resultSet, factory, resultTypes );
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
			return stmt.executeUpdate();
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}

	public final static int[] execute(Connection connection, String sql, IfcStatementInParameter[]... parameters) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "execute: " + sql );
			stmt = prepareStatement( connection, sql, parameters );
			return stmt.executeBatch();
		}
		finally
		{
			JdbcUtil.closeQuietly( stmt );
		}
	}
	
	// untested, what is a IfcStatementProperty? Is it a IfcStatementResult?
	public final static Object[] execute(Connection connection, IfcStatementProperty<?>[] properties, String sql, IfcStatementInParameter...parameters) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			if ( logger.isTraceEnabled() )
				logger.trace( "execute: " + sql );
			
			stmt = prepareStatement( connection, sql, parameters );
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

	// untested
	public final static Object[] executeCall(Connection connection, String sql, IfcStatementParameter... parameters) throws SQLException
	{
		return new Call().execute( connection, sql, parameters );
	}

	/*
	 * Expand SQL query and configure PreparedStatement
	 */

	private final static String modifySql(String sql,  IfcStatementParameter... parameters)
	{
		final String unmodifiedSQL = sql;
		
		if ( parameters != null )
			for(IfcStatementParameter param : parameters )
				sql = param.modify( sql );
		
		if ( unmodifiedSQL != sql )
			logger.trace( "modified sql: " +sql );
		
		return sql;
	}

	static PreparedStatement prepareStatement(Connection connection, String sql, IfcStatementInParameter... parameters) throws SQLException
	{
		sql = modifySql( sql, parameters );
		PreparedStatement stmt = connection.prepareStatement( sql );
		configureStatement( stmt, parameters );
		return stmt;
	}

	static PreparedStatement prepareStatement(Connection connection, String sql, IfcStatementInParameter[]... parameters) throws SQLException
	{
		if ( parameters != null && parameters.length > 0 )
		{
			sql = modifySql( sql, parameters[0] );
			PreparedStatement stmt = connection.prepareStatement( sql );
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
			
			int i = 0;
			int index = 1;
			if ( parameters != null )
			{
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

/*
 * Boolean b = execute( "{ ? := call isodd( ? ) }",
 *                      StmtOutParams.Boolean,
 *                      StmtInParams.Integer( 1 )
 *                    ) 
 * 
 *
 *
 *
 */