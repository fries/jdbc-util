/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Binds a {@link StatementInParameterType} with a concrete value to produce a
 * {@link StatementInParameter}.
 *
 * <p>This is the <strong>runtime bridge</strong> between a reusable parameter
 * template and a specific value.  It delegates {@code modify} and {@code configure}
 * to the template, passing the bound value.</p>
 *
 * <p><strong>Design:</strong></p>
 * <ul>
 *   <li><strong>Template</strong> — {@link StatementInParameterType<T>}</li>
 *   <li><strong>Value</strong> — {@code T value}</li>
 *   <li><strong>Result</strong> — {@code BoundValue<T>} → implements {@link StatementInParameter}</li>
 * </ul>
 *
 * <p><strong>Creation:</strong></p>
 * <p>Created automatically by {@link StatementParameters} factory methods:</p>
 * <pre>{@code
 * StatementInParameter param = StatementParameters.Boolean(true);
 * // → new BoundValue<>(StatementParameters.Boolean, true)
 * }</pre>
 *
 * <p><strong>Logging:</strong></p>
 * <p>Logs parameter binding at {@code TRACE} level for debugging.</p>
 *
 * @param <T> the Java type of the parameter value
 * @author Friedrich Schäuffelhut
 * @see StatementInParameterType
 * @see StatementParameters
 * @see StatementInParameter
 * @since 2018-06-01
 */
public record BoundValue<T>(
        StatementInParameterType<T> parameter,
        T value
) implements StatementInParameter
{

    private static final Logger LOGGER = LoggerFactory.getLogger( BoundValue.class );

    /**
     * Delegates SQL modification to the parameter template.
     */
    @Override
    public String modify(String sql)
    {
        return parameter.modify( sql, value );
    }

    /**
     * Delegates parameter binding to the template with trace logging.
     */
    @Override
    public int configure(PreparedStatement stmt, int index) throws SQLException
    {
        if (LOGGER.isTraceEnabled())
        {
            if (value == null)
            {
                LOGGER.trace( "setting param {} to null", index );
            }
            else
            {
                LOGGER.trace(
                        "setting param {} ({}) = {}",
                        index, value.getClass().getSimpleName(), value
                );
            }
        }
        return parameter.configure( stmt, index, value );
    }
}