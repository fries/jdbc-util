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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;


/**
 * @author Friedrich Schäuffelhut
 * 
 */
public final class JdbcUtil
{
	private static final Logger LOGGER = LoggerFactory.getLogger( JdbcUtil.class );

	private JdbcUtil()
	{
		// utility methods only
	}

	public final static void closeQuietly(Connection connection)
	{
		try
		{
			if ( connection != null )
				connection.close();
		}
		catch (SQLException e)
		{
			// ignore
		}
	}

	public final static void closeQuietly(Statement statement)
	{
		try
		{
			if ( statement != null )
				statement.close();
		}
		catch (SQLException e)
		{
			// ignore
		}
	}

	public final static void closeQuietly(ResultSet resultSet)
	{
		try
		{
			if ( resultSet != null )
				resultSet.close();
		}
		catch (SQLException e)
		{
			// ignore
		}
	}

	public final static void commitQuietly(Connection connection)
	{
		try
		{
			if ( connection != null && !connection.getAutoCommit() )
				connection.commit();
		}
		catch (SQLException err)
		{
			LOGGER.error( "Exception during commit", err );
		}
	}

	public final static void rollbackQuietly(Connection connection)
	{
		rollbackQuietly( connection, null, null );
	}

	public final static void rollbackQuietly(Connection connection, String msg)
	{
		rollbackQuietly( connection, null, msg );
	}
	
	public static void rollbackQuietly(Connection connection, Savepoint savePoint)
	{
		rollbackQuietly( connection, savePoint, null );
	}
	
	public static void rollbackQuietly(Connection connection, Savepoint savePoint, String msg)
	{
		try
		{
			if ( msg != null && LOGGER.isTraceEnabled() )
				LOGGER.trace( msg );
			if ( connection != null && !connection.getAutoCommit() )
			{
				if ( savePoint == null )
					connection.rollback();
				else
					connection.rollback( savePoint );
			}
		}
		catch (SQLException err)
		{
			LOGGER.error( "Exception during rollback", err );
		}
	}

	public final static void setAutoCommitQuietly(Connection connection, boolean autoCommit)
	{
		try
		{
			if ( connection != null  )
				connection.setAutoCommit( autoCommit );
		}
		catch (SQLException err)
		{
			LOGGER.error( "Exception during rollback", err );
		}
	}
}
