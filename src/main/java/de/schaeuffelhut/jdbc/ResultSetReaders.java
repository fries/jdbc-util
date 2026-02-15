/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Factory class for creating {@link ResultSetReader} instances.
 * These readers define how rows from a {@link ResultSet} are accumulated or processed.
 *
 * @author Friedrich Schäuffelhut
 * @since 2021-12-08
 */
public abstract class ResultSetReaders
{
    private ResultSetReaders()
    {
    }

    /**
     * Returns a {@link ResultSetReader} that reads at most one row and returns it as an {@link Optional}.
     * If no rows are found, an empty {@code Optional} is returned. If more than one row is found,
     * an {@link IllegalStateException} is thrown.
     *
     * @param <T> the type of the mapped row object.
     * @return a {@code ResultSetReader} for an optional single row.
     */
    @NotNull
    public static <T> ResultSetReader<T, Optional<T>> readOptional()
    {
        return new ReadOptionalResult<>();
    }

    /**
     * Returns a {@link ResultSetReader} that reads exactly one row.
     * If zero or more than one row is found, an {@link IllegalStateException} is thrown.
     *
     * @param <T> the type of the mapped row object.
     * @return a {@code ResultSetReader} for a single row.
     */
    @NotNull
    public static <T> ResultSetReader<T, T> readOne()
    {
        return new ReadOneResult<>();
    }

    /**
     * Returns a {@link ResultSetReader} that reads all rows and collects them into a {@link List}.
     *
     * @param <T> the type of the mapped row object.
     * @return a {@code ResultSetReader} for a list of rows.
     */
    @NotNull
    public static <T> ResultSetReader<T, List<T>> readMany()
    {
        return new ReadManyResultsIntoCollector<>( Collectors.toList() );
    }

    /**
     * Returns a {@link ResultSetReader} that reads all rows and passes each mapped row to the supplied {@link Consumer}.
     *
     * @param consumer the {@code Consumer} to accept each mapped row.
     * @param <T>      the type of the mapped row object.
     * @param <C>      the type of the consumer.
     * @return a {@code ResultSetReader} that processes rows with a consumer.
     */
    @NotNull
    public static <T, C extends Consumer<T>> ResultSetReader<T, C> readMany(C consumer)
    {
        return new ReadManyResultsIntoConsumer<>( consumer );
    }

    /**
     * Returns a {@link ResultSetReader} that reads all rows and collects them using the supplied {@link Collector}.
     * This is useful for collecting results into various collection types (e.e., {@code Set}, {@code Map}) or custom aggregations.
     *
     * @param collector the {@code Collector} to accumulate and transform the mapped rows.
     * @param <T>       the type of the mapped row object.
     * @param <A>       the intermediate accumulation type of the collector.
     * @param <R>       the final result type of the collector.
     * @return a {@code ResultSetReader} that collects rows with a collector.
     */
    @NotNull
    public static <T, A, R> ResultSetReader<T, R> readMany(Collector<T, A, R> collector)
    {
        return new ReadManyResultsIntoCollector<>( collector );
    }

    /**
     * Returns a {@link Collector} that accumulates elements into a {@link Stream}.
     * This is useful when you want to lazily process results or leverage Stream API capabilities.
     *
     * @param <T> the type of the elements in the stream.
     * @return a {@code Collector} that builds a {@code Stream}.
     */
    public static <T> Collector<T, Stream.Builder<T>, Stream<T>> toStream()
    {
        return Collector.of(
                Stream::builder,          // Supplier: Creates a new Stream.Builder
                Stream.Builder::add,       // Accumulator: Adds elements to the builder
                (left, right) -> {         // Combiner: Merges two builders (for parallel streams)
                    right.build().forEach( left::add );
                    return left;
                },
                Stream.Builder::build      // Finisher: Builds the final Stream from the builder
        );
    }
}

class ReadOptionalResult<T> implements ResultSetReader<T, Optional<T>>
{
    @Override
    public Optional<T> readResult(ResultSet resultSet, ResultSetMapper<T> resultMapper) throws SQLException
    {
        return readResult( resultSet, ColumnIndex.create( 1 ), resultMapper );
    }

    @Override
    public Optional<T> readResult(ResultSet resultSet, ColumnIndex columnIndex, ResultSetMapper<T> resultMapper) throws SQLException
    {
        if (resultSet.next())
        {
            final T result = resultMapper.map( resultSet, columnIndex.copy() );
            if (resultSet.next())
            {
                throw new IllegalStateException( "ResultSet returned more than one row" );
            }
            return Optional.of( result );
        }
        else
        {
            return Optional.empty();
        }
    }
}

class ReadOneResult<T> implements ResultSetReader<T, T>
{
    @Override
    public T readResult(ResultSet resultSet, ResultSetMapper<T> resultMapper) throws SQLException
    {
        return readResult( resultSet, ColumnIndex.create( 1 ), resultMapper );
    }

    @Override
    public T readResult(ResultSet resultSet, ColumnIndex columnIndex, ResultSetMapper<T> resultMapper) throws SQLException
    {
        if (resultSet.next())
        {
            final T result = resultMapper.map( resultSet, columnIndex.copy() );
            if (resultSet.next())
            {
                throw new IllegalStateException( "ResultSet returned more than one row" );
            }
            return result;
        }
        else
        {
            throw new IllegalStateException( "ResultSet did not return a row" );
        }
    }
}

class ReadManyResultsIntoCollector<T, A, R> implements ResultSetReader<T, R>
{
    private final Collector<T, A, R> collector;

    ReadManyResultsIntoCollector(Collector<T, A, R> collector)
    {
        this.collector = collector;
    }

    static <T> ReadManyResultsIntoCollector<T, ?, List<T>> toList()
    {
        return new ReadManyResultsIntoCollector<>( Collectors.toList() );
    }

    @Override
    public R readResult(ResultSet resultSet, ResultSetMapper<T> resultMapper) throws SQLException
    {
        return readResult( resultSet, ColumnIndex.create( 1 ), resultMapper );
    }

    @Override
    public R readResult(ResultSet resultSet, ColumnIndex columnIndex, ResultSetMapper<T> resultMapper) throws SQLException
    {
        A container = collector.supplier().get();
        BiConsumer<A, T> accumulator = collector.accumulator();
        while (resultSet.next())
        {
            accumulator.accept( container, resultMapper.map( resultSet, columnIndex.copy() ) );
        }
        return collector.finisher().apply( container );
    }
}

class ReadManyResultsIntoConsumer<T, C extends Consumer<T>> implements ResultSetReader<T, C>
{
    private final C consumer;

    ReadManyResultsIntoConsumer(C consumer)
    {
        this.consumer = consumer;
    }

    @Override
    public C readResult(ResultSet resultSet, ResultSetMapper<T> resultMapper) throws SQLException
    {
        return readResult( resultSet, ColumnIndex.create( 1 ), resultMapper );
    }

    @Override
    public C readResult(ResultSet resultSet, ColumnIndex columnIndex, ResultSetMapper<T> resultMapper) throws SQLException
    {
        while (resultSet.next())
        {
            consumer.accept( resultMapper.map( resultSet, columnIndex.copy() ) );
        }
        return consumer;
    }
}