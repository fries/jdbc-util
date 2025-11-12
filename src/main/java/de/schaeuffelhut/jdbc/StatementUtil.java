/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import de.schaeuffelhut.jdbc.ResultSetMappers.F1;

/**
 * Executes SQL statements and processes {@link java.sql.ResultSet}s.
 *
 * <p>All methods accept a SQL string and input parameters supplied as
 * {@link StatementInParameter} instances.  Row mapping is performed with a
 * {@link ResultSetMapper} (created via {@link ResultSetMappers}), while
 * result-set accumulation or custom iteration is performed with a
 * {@link ResultSetReader} or {@link ResultSetProcessor}.</p>
 *
 * <p>Overloaded variants accept parameters as var-args or as an
 * {@link Iterable}.</p>
 *
 * <h2>Key Factory Classes</h2>
 * <p>The objects required by the methods of this interface are most conveniently
 * obtained from the following factory classes:</p>
 * <ul>
 *   <li>{@link ResultSetMappers} – creates {@link ResultSetMapper}s
 *       ({@link ResultSetMappers#scalar(ResultType) scalar},
 *       {@link ResultSetMappers#object(F1,ResultType) object(...)}, …)</li>
 *   <li>{@link StatementParameters} – creates {@link StatementInParameter}s
 *       ({@link StatementParameters#Integer},
 *       {@link StatementParameters#String}, …)</li>
 *   <li>{@link ResultSetReaders} – creates {@link ResultSetReader}s
 *       ({@link ResultSetReaders#readOptional()},
 *       {@link ResultSetReaders#readOne()},
 *       {@link ResultSetReaders#readMany()})</li>
 * </ul>
 *
 * <h2>Available Operations</h2>
 * <table style="border-collapse: collapse; width: 100%; border: 1px solid #ccc;">
 *   <caption style="font-weight: bold; text-align: left; padding: 0.5em 0;">
 *     Statement execution and result-handling methods
 *   </caption>
 *   <tr>
 *     <th style="border: 1px solid #ccc; padding: 0.5em; text-align: left;">Category</th>
 *     <th style="border: 1px solid #ccc; padding: 0.5em; text-align: left;">Method</th>
 *     <th style="border: 1px solid #ccc; padding: 0.5em; text-align: left;">Purpose</th>
 *   </tr>
 *   <tr>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">Query + Mapping</td>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">
 *       {@link #selectInto(String,ResultSetReader,ResultSetMapper,StatementInParameter...) selectInto}
 *     </td>
 *     <td>Map rows and accumulate into a result (e.g. {@code List}, {@code Set})</td>
 *   </tr>
 *   <tr>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">Query + Processing</td>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">
 *       {@link #process(String,ResultSetProcessor,StatementInParameter...) process}
 *     </td>
 *     <td>Full control over result-set iteration</td>
 *   </tr>
 *   <tr>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">Update / DDL</td>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">
 *       {@link #execute(String,StatementInParameter...) execute}
 *     </td>
 *     <td>Execute non-query statements</td>
 *   </tr>
 *   <tr>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">Generated Keys</td>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">
 *       {@link #execute(GeneratedKeys,StatementProperty,String,StatementInParameter...) execute(...)}
 *     </td>
 *     <td>Capture auto-generated keys from {@code INSERT}</td>
 *   </tr>
 *   <tr>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">Batch</td>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">
 *       {@link #executeBatch(String,Iterable) executeBatch}
 *     </td>
 *     <td>Execute multiple parameter sets</td>
 *   </tr>
 *   <tr>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">Callable</td>
 *     <td style="border: 1px solid #ccc; padding: 0.5em;">
 *       {@link #executeCall(String,StatementParameter...) executeCall}
 *     </td>
 *     <td>Execute stored procedures (experimental)</td>
 *   </tr>
 * </table>
 *
 * <h2>Example: Query to List</h2>
 * <pre>{@code
 * List<User> users = statementUtil.selectInto(
 *     "SELECT id, name, email FROM users WHERE active = ?",
 *     ResultSetReaders.toList(),
 *     ResultSetMappers.object(User::new,
 *         ResultType.LONG, ResultType.STRING, ResultType.STRING),
 *     StatementParameters.ofBoolean(true)
 * );
 * }</pre>
 *
 * @see ResultSetMappers
 * @see StatementParameters
 * @see ResultSetReaders
 * @see ResultSetMapper
 * @see StatementInParameter
 * @see GeneratedKeys
 * @see StatementProperty
 * @author Friedrich Schäuffelhut
 * @since 2018-06-01
 */
public interface StatementUtil {

    // -----------------------------------------------------------------
    // QUERY + MAPPING
    // -----------------------------------------------------------------

    /**
     * Executes a query, maps each row with the supplied mapper, and accumulates the
     * results using the given reader.
     *
     * @param sql               the SQL query
     * @param resultSetReader   accumulates mapped rows
     * @param resultSetMapper   maps a row to an object of type {@code T}
     * @param parameters        input parameters (var-args)
     * @param <T>               type of a mapped row
     * @param <R>               type of the accumulated result
     * @return                  the accumulated result
     */
    <T, R> R selectInto(
            String sql,
            ResultSetReader<T, R> resultSetReader,
            ResultSetMapper<T> resultSetMapper,
            StatementInParameter... parameters
    );

    /**
     * Executes a query, maps each row with the supplied mapper, and accumulates the
     * results using the given reader.
     *
     * @param sql               the SQL query
     * @param resultSetReader   accumulates mapped rows
     * @param resultSetMapper   maps a row to an object of type {@code T}
     * @param parameters        input parameters as an {@link Iterable}
     * @param <T>               type of a mapped row
     * @param <R>               type of the accumulated result
     * @return                  the accumulated result
     */
    <T, R> R selectInto(
            String sql,
            ResultSetReader<T, R> resultSetReader,
            ResultSetMapper<T> resultSetMapper,
            Iterable<StatementInParameter> parameters
    );

    // -----------------------------------------------------------------
    // QUERY + PROCESSING
    // -----------------------------------------------------------------

    /**
     * Executes a query and passes the {@link java.sql.ResultSet} to the supplied
     * processor.
     *
     * @param sql                 the SQL query
     * @param resultSetProcessor  processes the result set
     * @param parameters          input parameters (var-args)
     * @param <T>                 result type
     * @return                    processor result
     */
    <T> T process(
            String sql,
            ResultSetProcessor<T> resultSetProcessor,
            StatementInParameter... parameters
    );

    /**
     * Executes a query and passes the {@link java.sql.ResultSet} to the supplied
     * processor.
     *
     * @param sql                 the SQL query
     * @param resultSetProcessor  processes the result set
     * @param parameters          input parameters as an {@link Iterable}
     * @param <T>                 result type
     * @return                    processor result
     */
    <T> T process(
            String sql,
            ResultSetProcessor<T> resultSetProcessor,
            Iterable<StatementInParameter> parameters
    );

    // -----------------------------------------------------------------
    // UPDATE / DDL
    // -----------------------------------------------------------------

    /**
     * Executes an update statement (INSERT, UPDATE, DELETE, or DDL).
     *
     * @param sql         the SQL statement
     * @param parameters  input parameters (var-args)
     * @return            number of affected rows
     */
    int execute(String sql, StatementInParameter... parameters);

    /**
     * Executes an update statement (INSERT, UPDATE, DELETE, or DDL).
     *
     * @param sql         the SQL statement
     * @param parameters  input parameters as an {@link Iterable}
     * @return            number of affected rows
     */
    int execute(String sql, Iterable<StatementInParameter> parameters);

    // -----------------------------------------------------------------
    // GENERATED KEYS
    // -----------------------------------------------------------------

    /**
     * Executes an INSERT and returns a generated key extracted by the supplied
     * property.
     *
     * @param generatedKeys      columns to return
     * @param statementProperty  extracts the key from the generated-keys result set
     * @param sql                the INSERT statement
     * @param parameters         input parameters (var-args)
     * @param <T>                type of the generated key
     * @return                   the extracted key
     */
    <T> T execute(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, StatementInParameter... parameters);

    /**
     * Executes an INSERT and returns a generated key extracted by the supplied
     * property.
     *
     * @param generatedKeys      columns to return
     * @param statementProperty  extracts the key from the generated-keys result set
     * @param sql                the INSERT statement
     * @param parameters         input parameters as an {@link Iterable}
     * @param <T>                type of the generated key
     * @return                   the extracted key
     */
    <T> T execute(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, Iterable<StatementInParameter> parameters);

    /**
     * Executes an INSERT and returns multiple statement properties like a generated key as an array.
     *
     * @param generatedKeys  columns to return
     * @param properties     extractors for each column
     * @param sql            the INSERT statement
     * @param parameters     input parameters (var-args)
     * @return               array of extracted keys
     */
    Object[] execute(GeneratedKeys generatedKeys, StatementProperty<?>[] properties, String sql, StatementInParameter... parameters);

    /**
     * Executes an INSERT and returns multiple statement properties like a generated key as an array.
     *
     * @param generatedKeys  columns to return
     * @param properties     extractors for each column
     * @param sql            the INSERT statement
     * @param parameters     input parameters as an {@link Iterable}
     * @return               array of extracted keys
     */
    Object[] execute(GeneratedKeys generatedKeys, StatementProperty<?>[] properties, String sql, Iterable<StatementInParameter> parameters);

    // -----------------------------------------------------------------
    // BATCH OPERATIONS
    // -----------------------------------------------------------------

    /**
     * Executes a batch of parameter sets for the same SQL statement.
     *
     * @param sql         the SQL statement
     * @param parameters  list of parameter arrays
     * @return            array of update counts
     */
    int[] executeBatch(String sql, Iterable<StatementInParameter[]> parameters);

    /**
     * Executes a batch INSERT and returns a generated key from the last statement.
     *
     * @param generatedKeys      columns to return
     * @param statementProperty  extracts the key from the generated-keys result set
     * @param sql                the INSERT statement
     * @param parameters         batch parameter sets
     * @param <T>                key type
     * @return                   key from the last insert
     */
    <T> T executeBatch(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, Iterable<StatementInParameter[]> parameters);

    // -----------------------------------------------------------------
    // STORED PROCEDURES (EXPERIMENTAL)
    // -----------------------------------------------------------------

    /**
     * Executes a stored procedure call.
     *
     * <p><strong>Note:</strong> This method is experimental and not fully tested.</p>
     *
     * @param sql         the CALL statement
     * @param parameters  input and output parameters
     * @return            array of OUT parameter values
     */
    Object[] executeCall(String sql, StatementParameter... parameters);
}