/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

public interface StatementUtil
{
    <T, R> R selectInto(
            String sql,
            ResultSetReader<T, R> resultSetReader,
            ResultSetMapper<T> resultSetMapper,
            StatementInParameter... parameters
    );

    <T, R> R selectInto(
            String sql,
            ResultSetReader<T, R> resultSetReader,
            ResultSetMapper<T> resultSetMapper,
            Iterable<StatementInParameter> parameters
    );

    <T> T process(
            String sql,
            ResultSetProcessor<T> resultSetProcessor,
            StatementInParameter... parameters
    );

    <T> T process(
            String sql,
            ResultSetProcessor<T> resultSetProcessor,
            Iterable<StatementInParameter> parameters
    );

    int execute(String sql, StatementInParameter... parameters);

    int execute(String sql, Iterable<StatementInParameter> parameters);

    <T> T execute(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, StatementInParameter... parameters);

    <T> T execute(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, Iterable<StatementInParameter> parameters);

    Object[] execute(GeneratedKeys generatedKeys, StatementProperty<?>[] properties, String sql, StatementInParameter... parameters);

    Object[] execute(GeneratedKeys generatedKeys, StatementProperty<?>[] properties, String sql, Iterable<StatementInParameter> parameters);

    int[] executeBatch(String sql, Iterable<StatementInParameter[]> parameters);

    <T> T executeBatch(GeneratedKeys generatedKeys, StatementProperty<T> statementProperty, String sql, Iterable<StatementInParameter[]> parameters);

    // untested
    Object[] executeCall(String sql, StatementParameter... parameters);
}
