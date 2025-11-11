/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc.legacy;

import de.schaeuffelhut.jdbc.ResultSetMappers;
import de.schaeuffelhut.jdbc.ResultType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;


@Deprecated
public final class LegacyResultSetUtil
{
    private LegacyResultSetUtil()
    {
    }

    public static <T> T readObjectByConstructor(
            ResultSet resultSet,
            Class<T> type,
            ResultType<?>... resultTypes
    ) throws SQLException
    {
        final T result;

        Object[] values = ResultSetMappers.tuple( resultTypes ).map( resultSet );
        if (values == null)
        {
            result = null;
        }
        else if (hasOnlyNullValues( values ))
        {
            return null;
        }
        else
        {
            Class<?>[] argsTypes = new Class<?>[resultTypes == null ? 0 : resultTypes.length];
            for (int i = 0; i < argsTypes.length; i++)
                argsTypes[i] = resultTypes[i].getResultType();

            try
            {
                result = findConstructor( type, argsTypes ).newInstance( values );
            }
            catch (IllegalArgumentException | InstantiationException | IllegalAccessException |
                   InvocationTargetException e)
            {
                throw new RuntimeException( e );
            }
        }
        return result;
    }

    private static boolean hasOnlyNullValues(Object[] values)
    {
        for (Object o : values)
            if (o != null)
                return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findConstructor(Class<T> type, Class<?>[] argTypes)
    {
        Constructor<?>[] constructors = type.getConstructors();

        //TODO: finds first compatible constructor, but should find closest matching constructor
        loop:
        for (Constructor<?> constructor : constructors)
        {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != argTypes.length)
                continue;
            for (int i = 0; i < argTypes.length; i++)
            {
                if (!parameterTypes[i].isAssignableFrom( argTypes[i] ))
                {
                    // TODO: The type is not assignment compatible, however we might be able to coerce from a basic type to a boxed type
                    continue loop;
                }
            }
            return (Constructor<T>) constructor;
        }

        String args = "";
        for (Class<?> argType : argTypes)
        {
            if (args.length() != 0)
                args += ", ";
            args += argType.getName();
        }
        throw new NoSuchMethodError( type.getName() + ".<init>(" + args + ")" );
    }
}
