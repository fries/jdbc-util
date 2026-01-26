/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Adapts an existing {@link ResultType} for a different output type by converting
 * the extracted value before returning it.
 *
 * <p>This is a <strong>decorator</strong> that lets you reuse a {@code ResultType}
 * with a different Java type.  It first extracts the value using the delegate
 * ({@code Tin}), then converts it to the desired type ({@code Tout}) via
 * {@link #convert(Object)}.</p>
 *
 * <p><strong>Use case:</strong> You have a {@code ResultType<Integer>} but your
 * domain model uses {@code Long}.  Wrap it to convert {@code Integer} to {@code Long}.</p>
 *
 * <p><strong>Design:</strong></p>
 * <ul>
 *   <li><strong>Delegate</strong> — {@code ResultType<Tin>}</li>
 *   <li><strong>Extracted</strong> — {@code Tin value}</li>
 *   <li><strong>Conversion</strong> — {@code convert(Tin) → Tout}</li>
 *   <li><strong>Result</strong> — {@code ResultType<Tout>}</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * // Reuse INTEGER template for Long values
 * ResultType<Long> LONG_FROM_INT = new ConvertingResultType<>(ResultType.INTEGER) {
 *     @Override protected Long convert(Integer value) { return value == null ? null : value.longValue(); }
 *     @Override public Class<Long> getResultType() { return Long.class; }
 * };
 *
 * // Use exactly like a normal ResultType
 * ResultSetMapper<User> mapper = ResultSetMappers.object(
 *     User::new,
 *     LONG_FROM_INT,                 // converts Integer → Long
 *     ResultType.STRING
 * );
 * }</pre>
 *
 * <p><strong>Null handling:</strong> The delegate may return {@code null} for SQL {@code NULL}.
 * The conversion must handle {@code null} appropriately (e.g., return {@code null}
 * or a sentinel).</p>
 *
 * @param <Tout> the desired output type
 * @param <Tin>  the type produced by the delegate
 * @author Friedrich Schäuffelhut
 * @see ResultType
 * @see ResultSetMappers
 * @since 2018-06-01
 */
public abstract class ConvertingResultType<Tout, Tin> implements ResultType<Tout>
{

    final ResultType<Tin> delegate;

    /**
     * Creates a converter that delegates extraction to the given {@link ResultType}
     * and then applies {@link #convert(Object)}.
     *
     * @param delegate the source {@link ResultType}
     */
    public ConvertingResultType(ResultType<Tin> delegate)
    {
        this.delegate = delegate;
    }

    /**
     * Extracts the value using the delegate and converts it.
     */
    @Override
    public Tout getResult(ResultSet resultSet, int index) throws SQLException
    {
        return convert( delegate.getResult( resultSet, index ) );
    }

    /**
     * Converts the value extracted by the delegate to the target type.
     *
     * <p>Subclasses must implement this method.  The input may be {@code null}
     * if the database column was {@code NULL}.</p>
     *
     * @param value the value from the delegate (may be {@code null})
     * @return the converted value
     */
    protected abstract Tout convert(Tin value);

    /**
     * Returns the Java class of the final result type.
     *
     * <p>Subclasses must implement this method to return the correct
     * {@link Class} for {@code Tout}.</p>
     *
     * @return the target class
     */
    public abstract Class<Tout> getResultType();
}