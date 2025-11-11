/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultType<T>
{
    T getResult(ResultSet resultSet, int index) throws SQLException;

    Class<T> getResultType();

    default ResultType<T> withNullAs(T nullReplacementValue) {
        return new ResultType<T>()
        {
            @Override
            public T getResult(ResultSet resultSet, int index) throws SQLException
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
    };
}