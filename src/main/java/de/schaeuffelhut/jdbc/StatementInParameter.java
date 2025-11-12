/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Represents an input parameter for a {@link java.sql.PreparedStatement}.
 *
 * <p>Extends {@link StatementParameter} to support:</p>
 * <ul>
 *   <li>Placeholder expansion via {@link StatementParameter#modify(String)}</li>
 *   <li>Safe binding of values using
 *       {@link java.sql.PreparedStatement#setObject(int, Object)} or type-specific setters</li>
 * </ul>
 *
 * <p>Used by {@link StatementUtil} for queries and updates.  The
 * {@link #configure(PreparedStatement, int)} method is called <strong>after</strong>
 * {@link StatementParameter#modify(String)} and binds one or more consecutive
 * placeholders starting at the given index.</p>
 *
 * <p>Instances are typically created via {@link StatementParameters}:</p>
 * <pre>{@code
 * StatementInParameter p = StatementParameters.ofString("Alice");
 * }</pre>
 *
 * @see StatementParameters
 * @see StatementUtil
 * @see StatementParameter#modify(String)
 * @author Friedrich Schäuffelhut
 */
public interface StatementInParameter extends StatementParameter {

    /**
     * Binds one or more parameter values to the {@link PreparedStatement}.
     *
     * <p>Called sequentially for each input parameter.  Use type-safe setters
     * such as {@link PreparedStatement#setInt(int, int)},
     * {@link PreparedStatement#setString(int, String)}, or
     * {@link PreparedStatement#setObject(int, Object)}.</p>
     *
     * <p>Return the number of placeholders consumed.  Usually {@code 1}.
     * Return a higher value when one parameter instance binds multiple
     * consecutive placeholders (e.g., expanding an {@code IN} clause).</p>
     *
     * @param stmt  the prepared statement
     * @param index the 1-based starting index of the placeholder(s)
     * @return      number of placeholders bound
     * @throws SQLException if a database access error occurs
     *
     * @see StatementParameters#Integer(Integer)
     * @see StatementParameters#String(String)
     */
    int configure(PreparedStatement stmt, int index) throws SQLException;
}