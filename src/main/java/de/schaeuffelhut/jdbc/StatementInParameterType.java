/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Defines how a value of type {@code T} is bound to a {@link PreparedStatement}
 * and optionally expands SQL placeholders.
 *
 * <p>This is a <strong>parameter template</strong> — it describes the <em>binding strategy</em>
 * for a specific Java type (e.g., {@code Boolean}, {@code List<Long>}), <strong>independent
 * of any concrete value</strong>.</p>
 *
 * <p>It is combined with a concrete value via {@link BoundValue} to produce a
 * {@link StatementInParameter} that can be used with {@link StatementUtil}.</p>
 *
 * <p><strong>Design:</strong></p>
 * <ul>
 *   <li><strong>Template</strong> — {@code StatementInParameterType<T>} (this interface)</li>
 *   <li><strong>Value</strong> — {@code T value}</li>
 *   <li><strong>Result</strong> — {@code BoundValue<T>} → implements {@link StatementInParameter}</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>{@code
 * // 1. Define template (in StatementParameters)
 * public static final StatementInParameterType<Boolean> Boolean = new BooleanInParameterType();
 *
 * // 2. Bind value → returns BoundValue
 * StatementInParameter param = StatementParameters.Boolean(true);
 *
 * // 3. Use in StatementUtil
 * stmtUtil.execute("UPDATE users SET active = ?", param);
 * }</pre>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li><strong>{@link #modify(String, Object)}</strong> — expand placeholders
 *       (e.g., {@code IN ({?})} → {@code IN (?, ?, ?)} for arrays). Must <strong>never</strong>
 *       inline values.</li>
 *   <li><strong>{@link #configure(PreparedStatement, int, Object)}</strong> — bind the
 *       value safely using {@code setXxx} methods.</li>
 * </ul>
 *
 * <p><strong>Security:</strong> {@code modify} must generate only {@code ?} placeholders.
 * Values are bound safely in {@code configure}.</p>
 *
 * @param <T> the Java type of the parameter value
 * @author Friedrich Schäuffelhut
 * @see BoundValue
 * @see StatementParameters
 * @see StatementInParameter
 * @see StatementUtil
 * @since 2018-06-01
 */
public interface StatementInParameterType<T>
{

    /**
     * Modifies the SQL by replacing a placeholder with expanded bind markers
     * based on the given value.
     *
     * <p>Called before binding.  Used for dynamic parameter lists
     * (e.g., expanding {@code IN ({?})} for arrays or collections).</p>
     *
     * <p><strong>Security:</strong> Must <strong>not</strong> embed the value in SQL.
     * Only generate {@code ?} placeholders.  The value is bound in {@link #configure}.</p>
     *
     * @param sql   the original SQL containing a placeholder
     * @param value the parameter value
     * @return the SQL with expanded {@code ?} placeholders
     */
    String modify(String sql, T value);

    /**
     * Binds the value to the {@link PreparedStatement} starting at the given index.
     *
     * <p>Called after {@link #modify}.  Use type-specific setters
     * (e.g., {@code setBoolean}, {@code setLong}) or {@code setObject}.</p>
     *
     * <p>Return the number of placeholders consumed (usually {@code 1},
     * or more for expanded lists).</p>
     *
     * @param stmt  the prepared statement
     * @param pos   1-based starting index
     * @param value the parameter value
     * @return number of placeholders bound
     * @throws SQLException if binding fails
     */
    int configure(PreparedStatement stmt, int pos, T value) throws SQLException;
}
