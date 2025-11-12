/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;

/**
 * Represents a parameter that may expand placeholders in a SQL string before execution.
 *
 * <p>Implementations are used by {@link StatementUtil} <strong>only</strong> to
 * <em>replace placeholders with the correct number of {@code ?}</em> when the
 * number of values is dynamic (e.g., {@code IN (?, ?, ?)} for a variable-size list).</p>
 *
 * <p><strong>Important:</strong> This method must <strong>never</strong> inline
 * literal values into the SQL string.  Direct substitution leads to SQL injection
 * vulnerabilities and defeats the purpose of prepared statements.</p>
 *
 * <p>The {@link #modify(String)} method is called <strong>once</strong> per statement,
 * before parameter binding.  It may return the original SQL unchanged.</p>
 *
 * <p>This interface is extended by:</p>
 * <ul>
 *   <li>{@link StatementInParameter} – for binding input values</li>
 *   <li>{@link StatementOutParameter} – for registering and reading output parameters</li>
 * </ul>
 *
 * @author Friedrich Schäuffelhut
 * @see StatementInParameter
 * @see StatementOutParameter
 * @see StatementUtil
 */
public interface StatementParameter
{
    /**
     * Expands placeholders in the SQL string.
     *
     * <p>Called before parameter configuration.  The implementation should
     * replace dynamic placeholder markers (e.g., {@code @params}) with the appropriate
     * number of {@code ?} bind markers.</p>
     *
     * <p><strong>Security requirement:</strong> This method <strong>must not</strong>
     * embed parameter values directly into the SQL string.  Values are bound safely
     * via {@link java.sql.PreparedStatement} in {@link StatementInParameter#configure(PreparedStatement, int)}.</p>
     *
     * <p><strong>Example</strong></p>
     * <pre>{@code
     * // Input:  "SELECT * FROM users WHERE id IN (@params)"
     * // Values: [1, 2, 3]
     * // Output: "SELECT * FROM users WHERE id IN (?, ?, ?)"
     * }</pre>
     *
     * @param sql the original SQL statement
     * @return the SQL with expanded placeholders
     */
    String modify(String sql);
}