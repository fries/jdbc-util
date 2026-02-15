/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import de.schaeuffelhut.jdbc.ResultSetMappers.F1;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Defines how to extract a value of type {@code T} from a {@link java.sql.ResultSet}
 * and maps it to the corresponding Java type.
 *
 * <p>Used by {@link ResultSetMappers} (e.g., {@link ResultSetMappers#scalar(ResultType)},
 * {@link ResultSetMappers#object(ResultSetMappers.F1, ResultType)}) to safely convert SQL column values
 * to Java objects.</p>
 *
 * <p>Implementations are provided for common JDBC types via static factory methods
 * in {@link ResultTypes} (e.g., {@link ResultTypes#String}, {@link ResultTypes#Integer}).</p>
 *
 * <h2>Null Handling</h2>
 * <p>By default, {@code NULL} values in the database result in {@code null} in Java.
 * Use {@link #withNullAs(Object)} to replace {@code null} with a sentinel value:</p>
 * <pre>{@code
 * ResultType<String> safeString = ResultType.STRING.withNullAs("");
 * }</pre>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ResultSetMapper<User> mapper = ResultSetMappers.object(
 *     (id, name) -> new User(id, name),
 *     ResultType.LONG,
 *     ResultType.STRING.withNullAs("Unknown")
 * );
 * }</pre>
 *
 * @param <T> the Java type of the extracted value
 * @author Friedrich Schäuffelhut
 * @see ResultSetMappers
 * @see ResultTypes#String
 * @see #withNullAs(Object)
 * @since 2018-06-01
 */
public interface ResultType<T>
{
    /**
     * Extracts a value from the {@link ResultSet} using a {@link ColumnIndex}.
     *
     * <p>Called by {@link ResultSetMappers} for each row.</p>
     *
     * @param resultSet the result set
     * @param index     the column index pointer
     * @return the value, or {@code null} if the database value is {@code NULL}
     * @throws SQLException if a database access error occurs
     * @since 2025-02-15
     */
    T getResult(ResultSet resultSet, ColumnIndex index) throws SQLException;

    /**
     * Returns the Java class of the result type.
     *
     * <p>Used for reflection-based mapping and type safety checks.</p>
     *
     * @return the {@link Class} object for {@code T}
     */
    Class<T> getResultType();

    /**
     * Returns a new {@link ResultType} that replaces {@code null} values from the database
     * with a specified default value.
     *
     * <p><strong>Use cases:</strong></p>
     * <ul>
     *   <li><strong>Provide a default value</strong> — supply a meaningful fallback when
     *       a database column contains {@code NULL} (e.g., empty string, zero, false).</li>
     *   <li><strong>Avoid NPE during autounboxing</strong> — when mapping to a primitive
     *       field (e.g., {@code int id}), JDBC returns a wrapper (e.g., {@link Integer}).
     *       If the column is {@code NULL}, the value is {@code null} — and autounboxing
     *       to {@code int} throws a {@link NullPointerException}.</li>
     * </ul>
     *
     * <p><strong>Pitfall: Hard-to-Debug NPE in Method References</strong></p>
     * <p>If the database returns {@code NULL} and the target type is a primitive,
     * the {@link NullPointerException} occurs <strong>during autounboxing</strong>
     * — typically inside a method reference (e.g., {@code User::new}) or constructor call.</p>
     *
     * <p>The stack trace is <strong>misleading</strong>:</p>
     * <ul>
     *   <li>Exceptions have no clear source
     *   <li>Line numbers do not point to the actual method
     *   <li>Root cause is hidden — debugging becomes difficult</li>
     * </ul>
     *
     * <h4>Recommended: Use {@code withNullAs} for Primitive Wrappers</h4>
     * <pre>{@code
     * record User(int id, String name) {}
     *
     * ResultSetMapper<User> mapper = ResultSetMappers.object(
     *     User::new,
     *     ResultType.INTEGER.withNullAs(0),     // prevents NPE + gives default
     *     ResultType.STRING.withNullAs("N/A")   // clean default
     * );
     * }</pre>
     *
     * <h4>Pitfall Example — NPE with Opaque Stack Trace</h4>
     * <pre>{@code
     * record User(int id, String name) {}
     *
     * ResultSetMappers.object(
     *     User::new,
     *     ResultType.INTEGER,  // if DB returns NULL → NPE inside User::new!
     *     ResultType.STRING
     * );
     * }</pre>
     *
     * @param nullReplacementValue the default value to return when the database value is {@code NULL}
     *                              <strong>must not be {@code null} if {@code T} is a primitive wrapper</strong>
     * @return a new {@link ResultType} that returns the default instead of {@code null}
     */
    default ResultType<T> withNullAs(T nullReplacementValue)
    {
        return new ResultType<>()
        {
            @Override
            public T getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
            {
                final T result = ResultType.this.getResult( resultSet, index );
                return result == null ? nullReplacementValue : result;
            }

            @Override
            public Class<T> getResultType()
            {
                return ResultType.this.getResultType();
            }
        };
    }
}