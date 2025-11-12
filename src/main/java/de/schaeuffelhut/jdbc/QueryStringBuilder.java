/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.util.ArrayList;

/**
 * Builds a SQL query string and its associated {@link StatementInParameter}s in a
 * type-safe way.
 *
 * <p>Designed to construct dynamic SQL while keeping parameters separate from the
 * query string — <strong>preventing SQL injection</strong> and enabling use with
 * {@link StatementUtil}.</p>
 *
 * <p><strong>Use case:</strong> Build complex queries with conditional clauses,
 * loops, or reusable fragments without string concatenation or manual parameter
 * tracking.</p>
 *
 * <p><strong>Design:</strong></p>
 * <ul>
 *   <li>SQL fragments are appended via {@link #append(String, StatementInParameter...)}</li>
 *   <li>Parameters are collected in order</li>
 *   <li>Final query and parameters are retrieved via {@link #getQueryString()}
 *       and {@link #getParameters()}</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>{@code
 * QueryStringBuilder qb = new QueryStringBuilder();
 * qb.append("SELECT * FROM users WHERE 1=1");
 *
 * if (name != null)
 *     qb.append(" AND name = ?", StatementParameters.String(name));
 *
 * if (active != null)
 *     qb.append(" AND active = ?", StatementParameters.Boolean(active));
 *
 * // Execute with StatementUtil
 * List<User> users = stmtUtil.selectInto(
 *     qb.getQueryString(),
 *     ResultSetReaders.readMany(),
 *     mapper,
 *     qb.getParameters()
 * );
 * }</pre>
 *
 * <p><strong>Thread safety:</strong> Not thread-safe.  Create a new instance per query.</p>
 *
 * <p><strong>Null safety:</strong> {@code append} accepts {@code null} varargs
 * (no parameters added).  Returned arrays from {@link #getParameters()} are
 * never {@code null}.</p>
 *
 * @author Friedrich Schäuffelhut
 * @see StatementUtil
 * @see StatementInParameter
 * @see StatementParameters
 * @since 2018-06-01
 */
public class QueryStringBuilder
{
    private final StringBuilder m_query = new StringBuilder();
    private final ArrayList<StatementInParameter> m_parameters = new ArrayList<>();

    /**
     * Appends a SQL fragment and its associated parameters.
     *
     * <p>Parameters are added in the order they appear.  Use {@code ?} in the
     * SQL fragment to match each parameter.</p>
     *
     * @param q      the SQL fragment (may not contain values)
     * @param params zero or more {@link StatementInParameter}s (may be {@code null})
     */
    public void append(String q, StatementInParameter... params)
    {
        m_query.append( q );
        if (params != null)
            for (StatementInParameter p : params)
                m_parameters.add( p );
    }

    /**
     * Returns the complete SQL query string.
     *
     * @return the built SQL (never {@code null})
     */
    public String getQueryString()
    {
        return m_query.toString();
    }

    /**
     * Returns the collected parameters as an array.
     *
     * @return a new array of {@link StatementInParameter}s (never {@code null})
     */
    public StatementInParameter[] getParameters()
    {
        return m_parameters.toArray( StatementInParameter[]::new );
    }
}