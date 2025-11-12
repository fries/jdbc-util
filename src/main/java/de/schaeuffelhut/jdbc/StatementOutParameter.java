/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;


/**
 * Represents an output parameter for a {@link java.sql.CallableStatement}.
 *
 * <p>Extends {@link StatementParameter} to support:</p>
 * <ul>
 *   <li>Registration of OUT parameter types via
 *       {@link java.sql.CallableStatement#registerOutParameter(int, int)}</li>
 *   <li>Reading the returned value after execution</li>
 * </ul>
 *
 * <p>Used by {@link StatementUtil#executeCall(String, StatementParameter...)}
 * when calling stored procedures with OUT parameters.</p>
 *
 * <p>The {@link #configure(CallableStatement, int)} method is called
 * <strong>after</strong> {@link StatementParameter#modify(String)} to register
 * the parameter type.  The {@link #readValue(CallableStatement, int)} method
 * is called <strong>after</strong> execution to retrieve the value.</p>
 *
 * <p>Instances are typically created using factory methods in
 * {@link StatementOutParameters}:</p>
 * <pre>{@code
 * StatementOutParameter<Integer> nameOut = StatementOutParameters.Integer;
 * StatementOutParameter<Long> idOut = StatementOutParameters.Long;
 * }</pre>
 *
 * @param <T> the Java type of the output value
 * @author Friedrich Schäuffelhut
 * @see StatementOutParameters
 * @see StatementUtil#executeCall(String, StatementParameter...)
 * @see StatementParameter#modify(String)
 * @since 2018-06-01
 */
public interface StatementOutParameter<T> extends StatementParameter
{
    /**
     * Registers one or more OUT parameters on the {@link CallableStatement}.
     *
     * <p>Called after {@link StatementParameter#modify(String)} and before
     * the procedure is executed.  Use
     * {@link CallableStatement#registerOutParameter(int, int)} or overloads
     * to declare the expected SQL type.</p>
     *
     * <p>Return the number of placeholders registered (usually {@code 1}).</p>
     *
     * @param stmt  the callable statement
     * @param index the 1-based starting index
     * @return number of parameters registered
     * @throws SQLException if a database access error occurs
     */
    int configure(CallableStatement stmt, int index) throws SQLException;

    /**
     * Reads the value of the OUT parameter after statement execution.
     *
     * <p>Called once after the procedure completes.  Use
     * {@link CallableStatement#getObject(int)}, {@link CallableStatement#getString(int)},
     * or type-specific getters to retrieve the value.</p>
     *
     * @param stmt  the callable statement
     * @param index the 1-based index of the parameter
     * @return the output value
     * @throws SQLException if a database access error occurs
     */
    T readValue(CallableStatement stmt, int index) throws SQLException;
}
