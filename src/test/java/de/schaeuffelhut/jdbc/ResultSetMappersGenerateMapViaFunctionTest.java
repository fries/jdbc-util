/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Disabled
class ResultSetMappersGenerateMapViaFunctionTest
{
    @Test
    void generateMapViaFunction()
    {
        StringBuilder code = new StringBuilder();

        IntStream.rangeClosed( 1, 96 ).forEach( i -> {
            code.append( generateFunctionalInterface( i ) );
            code.append( generateMapViaFunctionalMethod( i ).replaceAll( "\n", "" ).replaceAll( "\s+", " " ) );
            code.append( "\n" );
        } );

        System.out.println( code.toString() );
    }

    private static String generateFunctionalInterface(int argCount)
    {
        // Generate type parameters like "T1, T2, ..."
        String typeParams = IntStream.rangeClosed( 1, argCount )
                .mapToObj( i -> "T%d".formatted( i ) )
                .collect( Collectors.joining( ", " ) );

        // Generate method parameters like "T1 t1, T2 t2, ..."
        String methodParams = IntStream.rangeClosed( 1, argCount )
                .mapToObj( i -> "T%d t%d".formatted( i, i ) )
                .collect( Collectors.joining( ", " ) );

        return """
               public @FunctionalInterface interface F%d<R, %s> { R map(%s); }
               """.formatted( argCount, typeParams, methodParams );
    }

    private static String generateMapViaFunctionalMethod(int argCount)
    {
        // Generate type parameters like "T1, T2, ..."
        String typeParams = IntStream.rangeClosed( 1, argCount )
                .mapToObj( i -> "T%d".formatted( i ) )
                .collect( Collectors.joining( ", " ) );

        // Generate functional interface parameter like "F2<R, T1, T2>"
        String functionalInterface = "F%d<R, %s>".formatted( argCount, typeParams );

        // Generate method parameters like "IfcResultType<T1> t1, IfcResultType<T2> t2, ..."
        String methodParams = IntStream.rangeClosed( 1, argCount )
                .mapToObj( i -> "IfcResultType<T%d> t%d".formatted( i, i ) )
                .collect( Collectors.joining( ", " ) );

        // Generate result mapping like "t1.getResult(resultSet, index++), t2.getResult(resultSet, index++), ..."
        String resultMapping = IntStream.rangeClosed( 1, argCount )
                .mapToObj( i -> "             t%d.getResult(resultSet, %d)".formatted( i, i ) )
                .collect( Collectors.joining( ",\n" ) );

        //String nameSuffix = argCount < 30 ? "" : Integer.toString( argCount / 10 * 10 );
        String nameSuffix = "";

        return """
               public static <R, %s> IfcResultSetMapper<R> object%s(%s f%d, %s\n) {
                    return resultSet -> f%d.map(
               %s
                    );
               }
               """.formatted( typeParams, nameSuffix, functionalInterface, argCount, methodParams, argCount, resultMapping );
    }
}