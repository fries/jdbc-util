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

class ArrayResultType<T> implements ResultType<T[]>
{
    private final ResultType<T> resultType;

    public ArrayResultType(ResultType<T> resultType)
    {
        this.resultType = resultType;
    }

    @Override
    public T[] getResult(ResultSet resultSet, ColumnIndex index) throws SQLException
    {
        ArrayList<T> result = new ArrayList<>();
        Array array = resultSet.getArray( index.next() );
        try (ResultSet arrayResultSet = array.getResultSet())
        {
            while (arrayResultSet.next())
            {
                T t = resultType.getResult( arrayResultSet, ColumnIndex.create( 2 ) );
                result.add( t );
            }
        }
        return result.toArray( size -> (T[]) java.lang.reflect.Array.newInstance( resultType.getResultType(), size ) );
    }

    @Override
    public Class<T[]> getResultType()
    {
        return (Class<T[]>) resultType.getResultType().arrayType();
    }
}
