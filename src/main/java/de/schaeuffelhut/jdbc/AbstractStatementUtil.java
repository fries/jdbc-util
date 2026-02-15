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

/**
 * Abstract base implementation of {@link StatementUtil} that delegates JDBC operations
 * to a {@link Connection} supplied via a {@link Transactional} callback.
 *
 * <p>Subclasses must implement:</p>
 * <ul>
 *   <li>{@link #execute(Transactional, String, String)} – to run the callback with a connection</li>
 *   <li>{@link #translate(String, String, SQLException)} – to wrap SQL exceptions</li>
 * </ul>
 *
 * <p>All {@link StatementUtil} methods are implemented in terms of these two hooks.
 * This enables:</p>
 * <ul>
 *   <li>Connection pooling</li>
 *   <li>Transaction management</li>
 *   <li>Logging, metrics, retries</li>
 *   <li>Testing with mock connections</li>
 * </ul>
 *
 * <h2>Creating an Instance</h2>
 * <p>Use the factory method:</p>
 * <pre>{@code
 * StatementUtil util = AbstractStatementUtil.createStatementUtilFor(dataSource::getConnection);
 * }</pre>
 *
 * <h2>Example: Transactional Wrapper</h2>
 * <pre>{@code
 * StatementUtil txUtil = new AbstractStatementUtil() {
 *     @Override
 *     protected <R, E extends Exception> R execute(Transactional<R, E> tx, String task, String sql) {
 *         try (Connection conn = dataSource.getConnection()) {
 *             conn.setAutoCommit(false);
 *             R result = tx.execute(conn);
 *             conn.commit();
 *             return result;
 *         } catch (Exception e) {
 *             rollback(conn);
 *             throw translate(task, sql, e instanceof SQLException ? (SQLException) e : new SQLException(e));
 *         }
 *     }
 *
 *     @Override
 *     protected RuntimeException translate(String task, String sql, SQLException e) {
 *         return new DataAccessException(task + ": " + sql, e);
 *     }
 * };
 * }</pre>
 *
 * @author Friedrich Schäuffelhut
 * @see StatementUtil
 * @see Transactional
 */
public abstract class AbstractStatementUtil implements StatementUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractStatementUtil.class );

    /**
     * Functional interface for executing JDBC logic within a {@link Connection}.
     *
     * <p>Used by {@link AbstractStatementUtil} to abstract connection acquisition,
     * transaction boundaries, and exception translation.</p>
     *
     * @param <R> the result type
     * @param <E> checked exception type thrown by the operation
     */
    @FunctionalInterface
    // TODO: Rename Transactional to JdbcOperation ?
    public interface Transactional<R, E extends Exception>
    {
        /**
         * Executes the operation using the provided connection.
         *
         * @param connection the JDBC connection
         * @return the result
         * @throws E if an error occurs
         */
        R execute(Connection connection) throws E;
    }

    /**
     * Executes a transactional JDBC operation using the configured connection strategy.
     *
     * <p>This is a convenience method that delegates to
     * {@link #execute(Transactional, String, String)} with a generic task name
     * and a string representation of the operation for logging and error reporting.</p>
     *
     * <p>Use this method when you do not need custom task metadata.  For fine-grained
     * control over logging or exception context, call the three-parameter version directly.</p>
     *
     * @param transactional the JDBC operation to execute
     * @param <R>           the result type
     * @param <E>           the checked exception type declared by the operation
     * @return              the result of the operation
     * @throws RuntimeException if the operation fails (wrapping checked exceptions)
     *
     * @see #execute(Transactional, String, String)
     */
    public <R, E extends Exception> R execute(Transactional<R, E> transactional)
    {
        return execute( transactional, "execute", Objects.toString( transactional ) );
    }

    /**
     * Executes the transactional operation using a {@link Connection}.
     *
     * <p><strong>Must be implemented by subclasses.</strong> This method is responsible for:</p>
     * <ul>
     *   <li>Obtaining a {@link Connection} (from pool, data source, etc.)</li>
     *   <li>Executing the {@link Transactional#execute(Connection)} callback</li>
     *   <li>Managing transaction boundaries (commit/rollback)</li>
     *   <li>Closing the connection</li>
     * </ul>
     *
     * <p>The {@code task} and {@code sql} parameters are for logging and error reporting.</p>
     *
     * @param transactional the JDBC operation
     * @param task          short description of the operation (e.g., "selectInto")
     * @param sql           the SQL string (or description if unknown)
     * @param <R>           result type
     * @param <E>           checked exception type
     * @return the result
     * @throws RuntimeException if execution fails
     */
    protected abstract <R, E extends Exception> R execute(Transactional<R, E> transactional, String task, String sql);

    /**
     * Translates a {@link SQLException} into a runtime exception.
     *
     * <p><strong>Must be implemented by subclasses.</strong> This allows:</p>
     * <ul>
     *   <li>Wrapping in domain-specific exceptions (e.g., {@code DataAccessException})</li>
     *   <li>Adding context (task, SQL)</li>
     *   <li>Suppressing or rethrowing</li>
     * </ul>
     *
     * <p>Return {@code null} to let the original exception propagate unchanged.</p>
     *
     * @param task the operation name (e.g., "executeBatch")
     * @param sql  the SQL string (or "unknown")
     * @param e    the SQLException to translate
     * @return     a RuntimeException, or {@code null}
     */
    protected abstract RuntimeException translate(String task, String sql, SQLException e);

    /**
     * Creates a minimal {@link StatementUtil} that uses the supplied connection provider.
     *
     * <p>The returned instance: </p>
     * <ul>
     *   <li>Calls {@code connectionSupplier.get()} for each operation</li>
     *   <li>Does <strong>not</strong> manage transactions or close connections</li>
     *   <li>Does <strong>not</strong> translate SQLExceptions (returns {@code null})</li>
     * </ul>
     *
     * <p>Use for testing or when the connection lifecycle is managed externally.</p>
     *
     * @param connectionSupplier provides a {@link Connection} per call
     * @return a basic {@link StatementUtil} instance
     */
    public static StatementUtil createStatementUtilFor(Supplier<Connection> connectionSupplier)
    {
        return new AbstractStatementUtil()
        {
            @Override
            protected <R, E extends Exception> R execute(Transactional<R, E> transactional, String task, String sql)
            {
                try
                {
                    return transactional.execute( connectionSupplier.get() );
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
                    ColumnIndex idx = ColumnIndex.create( 1 );
                    resultSetMapper.initialize( resultSet, idx );
                    return resultSetReader.readResult( resultSet, idx, resultSetMapper );
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

//    /*
//     * Expand SQL query and configure PreparedStatement
//     */
//
//    public final String modifySql(String sql, Iterable<StatementParameter> parameters)
//    {
//        return PreparedStatementUtil.modifySql( sql, parameters );
//    }
//
//    public PreparedStatement prepareStatement(String sql, Iterable<StatementInParameter> parameters)
//    {
//        return execute( connection -> PreparedStatementUtil.prepareStatement( connection, sql, parameters ), "prepareStatement", sql );
//    }
//
//    public PreparedStatement prepareStatement(String sql, GeneratedKeys generatedKeys, Iterable<StatementInParameter> parameters)
//    {
//        return execute( connection -> PreparedStatementUtil.prepareStatement( connection, sql, generatedKeys, parameters ), "prepareStatement", sql );
//    }
//
//    public PreparedStatement prepareBatchStatement(String sql, Iterable<StatementInParameter[]> parameters)
//    {
//        return execute( connection -> PreparedStatementUtil.prepareBatchStatement( connection, sql, null, parameters ), "prepareStatement", sql );
//    }
//
//    public PreparedStatement prepareBatchStatement(String sql, GeneratedKeys generatedKeys, Iterable<StatementInParameter[]> parameters)
//    {
//        return execute( connection -> PreparedStatementUtil.prepareBatchStatement( connection, sql, generatedKeys, parameters ), "prepareStatement", sql );
//    }
//
//    public void configureStatement(PreparedStatement stmt, Iterable<StatementInParameter> parameters)
//    {
//        try
//        {
//            PreparedStatementUtil.configureStatement( stmt, parameters );
//        }
//        catch (SQLException e)
//        {
//            throw translate( "configureStatement", "SQL unknown", e );
//        }
//    }
}