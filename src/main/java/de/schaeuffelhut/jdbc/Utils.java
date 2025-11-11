/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.lang.reflect.Constructor;
import java.util.*;

class Utils
{
    public static <T> Iterable<T> asIterable(T[] array)
    {
        return () -> new Iterator<>()
        {
            private int index = 0;

            @Override
            public boolean hasNext()
            {
                return index < array.length;
            }

            @Override
            public T next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                return array[index++];
            }
        };
    }

    // Copy of /de/schaeuffelhut/jdbc/ResultSetUtil.java:174
    @SuppressWarnings("unchecked")
    static <T> Constructor<T> findConstructor(Class<T> type, Class<?>[] argTypes)
    {
        Constructor<?>[] constructors = type.getConstructors();

        //TODO: finds first compatible constructor, but should find closest matching constructor
        loop:
        for (Constructor<?> constructor : constructors)
        {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != argTypes.length)
            {
                continue;
            }

            for (int i = 0; i < argTypes.length; i++)
            {
                // call boxedClass() for comparing primitive types to boxed counterparts
                if (!boxedClass( parameterTypes[i] ).isAssignableFrom( argTypes[i] ))
                {
                    continue loop;
                }
            }
            return (Constructor<T>) constructor;
        }

        StringBuilder args = new StringBuilder();
        for (Class<?> argType : argTypes)
        {
            if (!args.isEmpty())
                args.append( ", " );
            args.append( argType.getName() );
        }
        throw new NoSuchMethodError( type.getName() + ".<init>(" + args + ")" );
    }

    /**
     * If type is representing a primitive type,
     * this method returns the corresponding boxed type,
     * otherwise this method returns type
     *
     * @param type The type to convert to its boxed counter part if type is primitive.
     * @param <T>
     * @return The type, or if type is primitive the boxed counter part.
     */
    private static <T> Class<T> boxedClass(Class<T> type)
    {
        return type.isPrimitive() ? (Class<T>) PRIMITIVES_TO_WRAPPERS.get( type ) : type;
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS;

    static
    {
        HashMap<Class<?>, Class<?>> map = new HashMap<>();
        map.put( boolean.class, Boolean.class );
        map.put( byte.class, Byte.class );
        map.put( char.class, Character.class );
        map.put( double.class, Double.class );
        map.put( float.class, Float.class );
        map.put( int.class, Integer.class );
        map.put( long.class, Long.class );
        map.put( short.class, Short.class );
        map.put( void.class, Void.class );
        PRIMITIVES_TO_WRAPPERS = Collections.unmodifiableMap( map );
    }

}
