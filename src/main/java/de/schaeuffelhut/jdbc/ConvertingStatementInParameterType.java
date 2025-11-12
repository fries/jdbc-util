/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;


/**
 * Adapts a {@link StatementInParameterType} for a different input type by converting
 * the value before delegating.
 *
 * <p>This is a <strong>decorator</strong> that allows reuse of an existing parameter
 * template with a different Java type.  It converts the incoming value ({@code Tin})
 * to the type expected by the delegate ({@code Tout}) using {@link #convert(Object)},
 * then forwards the call.</p>
 *
 * <p><strong>Use case:</strong> You have a template for {@code Integer}, but your
 * domain model uses {@code Long}.  Wrap it to convert {@code Long} → {@code Integer}.</p>
 *
 * <p><strong>Design:</strong></p>
 * <ul>
 *   <li><strong>Input</strong> — {@code Tin value}</li>
 *   <li><strong>Conversion</strong> — {@code convert(Tin) → Tout}</li>
 *   <li><strong>Delegate</strong> — {@code StatementInParameterType<Tout>}</li>
 *   <li><strong>Result</strong> — Works as {@code StatementInParameterType<Tin>}</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * // Reuse INTEGER template for Long values
 * StatementInParameterType<Long> LONG_AS_INT = new ConvertingStatementInParameterType<>(
 *     StatementParameters.INTEGER,
 *     Long::intValue
 * );
 *
 * // Use exactly like a normal template
 * StatementInParameter param = StatementParameters.bindValue(LONG_AS_INT, 123L);
 * stmtUtil.execute("UPDATE users SET score = ?", param);
 * }</pre>
 *
 * <p><strong>Security:</strong> Conversion happens before binding — no SQL injection risk.
 * The delegate handles placeholder expansion and safe binding.</p>
 *
 * @param <Tout> the type expected by the delegate template
 * @param <Tin>  the input type accepted by this adapter
 * @author Friedrich Schäuffelhut
 * @see StatementInParameterType
 * @see BoundValue
 * @see StatementParameters
 * @since 2018-06-01
 */
public abstract class ConvertingStatementInParameterType<Tout, Tin>
        implements StatementInParameterType<Tin>
{

    final StatementInParameterType<Tout> delegate;

    /**
     * Creates a converter that delegates to the given template after conversion.
     *
     * @param delegate the target parameter template
     */
    public ConvertingStatementInParameterType(StatementInParameterType<Tout> delegate)
    {
        this.delegate = delegate;
    }

    /**
     * Delegates SQL modification after converting the value.
     */
    @Override
    public final String modify(String sql, Tin value)
    {
        return delegate.modify( sql, convert( value ) );
    }

    /**
     * Delegates parameter binding after converting the value.
     */
    @Override
    public final int configure(PreparedStatement stmt, int pos, Tin value) throws SQLException
    {
        return delegate.configure( stmt, pos, convert( value ) );
    }

    /**
     * Converts the input value to the type expected by the delegate.
     *
     * <p>Subclasses must implement this method.  The result is passed directly
     * to the delegate's {@code modify} and {@code configure} methods.</p>
     *
     * @param value the input value of type {@code Tin}
     * @return the converted value of type {@code Tout}
     */
    protected abstract Tout convert(Tin value);
}