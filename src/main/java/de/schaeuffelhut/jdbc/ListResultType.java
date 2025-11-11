/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ListResultType<T> implements ResultType<List<T>>
{
    private final ResultType<T> resultType;

    public ListResultType(ResultType<T> resultType)
    {
        this.resultType = resultType;
    }

    @Override
    public List<T> getResult(ResultSet resultSet, int index) throws SQLException
    {
        ArrayList<T> result = new ArrayList<>();
        Array array = resultSet.getArray( index );
        try (ResultSet arrayResultSet = array.getResultSet())
        {
            while (arrayResultSet.next())
            {
                T t = resultType.getResult( arrayResultSet, 2 );
                result.add( t );
            }
        }
        return result;
    }

    @Override
    public Class<List<T>> getResultType()
    {
        return (Class)List.class;
    }
}
