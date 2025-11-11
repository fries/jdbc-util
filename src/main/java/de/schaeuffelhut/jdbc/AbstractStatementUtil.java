/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Supplier;

import static de.schaeuffelhut.jdbc.Utils.asIterable;

public abstract class AbstractStatementUtil implements StatementUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractStatementUtil.class );

    @FunctionalInterface
    public interface Transactional<R, E extends Exception>
    {
        R execute(Connection connection) throws E;
    }

    public <R, E extends Exception> R execute(Transactional<R, E> transactional)
    {
        return execute( transactional, "execute", Objects.toString( transactional ) );
    }

    protected abstract <R, E extends Exception> R execute(Transactional<R, E> transactional, String task, String sql);

    protected abstract RuntimeException translate(String task, String sql, SQLException e);

    public static StatementUtil createStatementUtilFor(Supplier<Connection> s)
    {
        return new AbstractStatementUtil()
        {
            @Override
            protected <R, E extends Exception> R execute(Transactional<R, E> transactional, String task, String sql)
            {
                try
                {
                    return transactional.execute( s.get() );
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

            @Override
            protected RuntimeException translate(String task, String sql, SQLException e)
            {
                return null;
            }
        };
    }


    /*
     * selectInto
     */


    @Override
    public <T, R> R selectInto(
            String sql,
            ResultSetReader<T, R> resultSetReader,
            ResultSetMapper<T> resultSetMapper,
            StatementInParameter... parameters
    )
    {
        return selectInto( sql, resultSetReader, resultSetMapper, asIterable( parameters ) );
    }

    @Override
    public <T, R> R selectInto(
            String sql,
            ResultSetReader<T, R> resultSetReader,
            ResultSetMapper<T> resultSetMapper,
            Iterable<StatementInParameter> parameters
    )
    {
        return execute(
                sql,
                resultSet -> {
                    resultSetMapper.initialize( resultSet );
                    return resultSetReader.readResult( resultSet, resultSetMapper );
                },
                parameters,
                "selectInto"
        );
    }

    @Override
    public final <T> T process(
            String sql,
            ResultSetProcessor<T> resultSetProcessor,
            StatementInParameter... parameters
    )
    {
        return process( sql, resultSetProcessor, asIterable( parameters ) );
    }

    @Override
    public final <T> T process(
            String sql,
            ResultSetProcessor<T> resultSetProcessor,
            Iterable<StatementInParameter> parameters
    )
    {
        return execute( sql, resultSetProcessor, parameters, "process" );
    }

    private <V> V execute(String sql, ResultSetProcessor<V> resultSetProcessor, Iterable<StatementInParameter> parameters, String task)
    {
        if (LOGGER.isTraceEnabled())
            LOGGER.trace( "{}: {}", task, sql );

        return execute(
                connection -> {
                    try (PreparedStatement stmt = PreparedStatementUtil.prepareStatement( connection, sql, parameters );
                         ResultSet resultSet = stmt.executeQuery()
                    )
                    {
                        return resultSetProcessor.process( resultSet );
                    }
                },
                task,
                sql
        );
    }

    /*
     * inserts / updates
     */

    @Override
    public final int execute(String sql, StatementInParameter... parameters)
    {
        return execute( sql, asIterable( parameters ) );
    }

    @Override
    public final int execute(String sql, Iterable<StatementInParameter> parameters)
    {
        return execute( connection -> PreparedStatementUtil.execute( connection, sql, parameters ), "execute", sql );
    }

    @Override
    public final <T> T execute(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, StatementInParameter... parameters)
    {
        return execute( generatedKeys, statementProperty, sql, asIterable( parameters ) );
    }

    @Override
    public final <T> T execute(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, Iterable<StatementInParameter> parameters)
    {
        return execute( connection -> PreparedStatementUtil.execute( connection, generatedKeys, statementProperty, sql, parameters ), "execute", sql );
    }

    @Override
    public final Object[] execute(GeneratedKeys generatedKeys, StatementProperty<?>[] properties, String sql, StatementInParameter... parameters)
    {
        return execute( generatedKeys, properties, sql, asIterable( parameters ) );
    }

    @Override
    public final Object[] execute(GeneratedKeys generatedKeys, StatementProperty<?>[] properties, String sql, Iterable<StatementInParameter> parameters)
    {
        return execute( connection -> PreparedStatementUtil.execute( connection, generatedKeys, properties, sql, parameters ), "execute", sql );
    }

    /*
     * executing batch statements
     */

    @Override
    public final int[] executeBatch(String sql, Iterable<StatementInParameter[]> parameters)
    {
        return execute( connection -> PreparedStatementUtil.executeBatch( connection, sql, parameters ), "executeBatch", sql );
    }

    @Override
    public final <T> T executeBatch(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, Iterable<StatementInParameter[]> parameters)
    {
        return execute( connection -> PreparedStatementUtil.executeBatch( connection, generatedKeys, statementProperty, sql, parameters ), "executeBatch", sql );
    }


    // untested
    @Override
    public final Object[] executeCall(String sql, StatementParameter... parameters)
    {
        return execute( connection -> PreparedStatementUtil.executeCall( connection, sql, parameters ), "executeCall", sql );
    }

    /*
     * Expand SQL query and configure PreparedStatement
     */

    public final String modifySql(String sql, Iterable<StatementParameter> parameters)
    {
        return PreparedStatementUtil.modifySql( sql, parameters );
    }

    public PreparedStatement prepareStatement(String sql, Iterable<StatementInParameter> parameters)
    {
        return execute( connection -> PreparedStatementUtil.prepareStatement( connection, sql, parameters ), "prepareStatement", sql );
    }

    public PreparedStatement prepareStatement(String sql, GeneratedKeys generatedKeys, Iterable<StatementInParameter> parameters)
    {
        return execute( connection -> PreparedStatementUtil.prepareStatement( connection, sql, generatedKeys, parameters ), "prepareStatement", sql );
    }

    public PreparedStatement prepareBatchStatement(String sql, Iterable<StatementInParameter[]> parameters)
    {
        return execute( connection -> PreparedStatementUtil.prepareBatchStatement( connection, sql, null, parameters ), "prepareStatement", sql );
    }

    public PreparedStatement prepareBatchStatement(String sql, GeneratedKeys generatedKeys, Iterable<StatementInParameter[]> parameters)
    {
        return execute( connection -> PreparedStatementUtil.prepareBatchStatement( connection, sql, generatedKeys, parameters ), "prepareStatement", sql );
    }

    public void configureStatement(PreparedStatement stmt, Iterable<StatementInParameter> parameters)
    {
        try
        {
            PreparedStatementUtil.configureStatement( stmt, parameters );
        }
        catch (SQLException e)
        {
            throw translate( "configureStatement", "SQL unknown", e );
        }
    }
}