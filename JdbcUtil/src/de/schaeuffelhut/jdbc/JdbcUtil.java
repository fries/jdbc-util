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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 * 
 */
public final class JdbcUtil
{
	private final static Logger logger = Logger.getLogger( JdbcUtil.class );

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
			logger.error( "Exception during commit", err );
		}
	}

	public final static void rollbackQuietly(Connection connection)
	{
		rollbackQuietly( connection, null );
	}

	public final static void rollbackQuietly(Connection connection, String msg)
	{
		try
		{
			if ( msg != null && logger.isTraceEnabled() )
				logger.trace( msg );
			if ( connection != null && !connection.getAutoCommit() )
				connection.rollback();
		}
		catch (SQLException err)
		{
			logger.error( "Exception during rollback", err );
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
			logger.error( "Exception during rollback", err );
		}
	}

}
