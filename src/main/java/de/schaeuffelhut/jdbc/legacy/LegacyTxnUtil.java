/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc.legacy;

import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

@Deprecated
public abstract class LegacyTxnUtil
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger( LegacyTxnUtil.class );

    private LegacyTxnUtil()
    {
    }


    public final static <T> T execute(DataSource dataSource, LegacyTransactional<T> transactional)
    {
        try (Connection connection = dataSource.getConnection())
        {
            return execute( connection, transactional );
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (SQLException e)
        {
            throw new RuntimeException( e );
        }
    }

    public final static <T> T executeChecked(DataSource dataSource, LegacyTransactional<T> transactional) throws Exception
    {
        try (Connection connection = dataSource.getConnection())
        {
            return executeChecked( connection, transactional );
        }
    }


    public final static <T> T execute(
            Connection connection,
            LegacyTransactional<T> transactional
    )
    {
        try
        {
            return executeChecked( connection, transactional );
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }
    }

    private static <T> T executeChecked(Connection connection, LegacyTransactional<T> transactional) throws Exception
    {
        Boolean autoCommit = null;
        boolean commited = false;
        try
        {
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit( false );

            if (LOGGER.isTraceEnabled())
                LOGGER.trace( "txn invoking: " + transactional );
            T result = transactional.run( connection );

            if (LOGGER.isTraceEnabled())
                LOGGER.trace( "txn commiting: " + transactional );
            if (!connection.getAutoCommit())
                connection.commit();

            commited = true;
            return result;
        }
        finally
        {
            if (!commited)
                rollbackQuietly(
                        connection, "txn rollback: " + transactional.toString() );
            if (autoCommit != null)
                setAutoCommitQuietly( connection, autoCommit );
        }
    }

    public final static <T> T executeWithoutTxn(
            Connection connection,
            LegacyTransactional<T> transactional
    )
    {
        try
        {
            return transactional.run( connection );
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }
    }

    public final static void rollbackQuietly(Connection connection, String msg)
    {
        rollbackQuietly( connection, null, msg );
    }

    public static void rollbackQuietly(Connection connection, Savepoint savePoint, String msg)
    {
        try
        {
            if (msg != null && LOGGER.isTraceEnabled())
                LOGGER.trace( msg );
            if (connection != null && !connection.getAutoCommit())
            {
                if (savePoint == null)
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
            if (connection != null)
                connection.setAutoCommit( autoCommit );
        }
        catch (SQLException err)
        {
            LOGGER.error( "Exception during rollback", err );
        }
    }
}
