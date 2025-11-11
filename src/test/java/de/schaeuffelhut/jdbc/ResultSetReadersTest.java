/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ResultSetReadersTest
{
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
                """
                CREATE TABLE employees (
                    id   INT PRIMARY KEY,
                    name VARCHAR(255)
                );
                INSERT INTO employees (id, name) VALUES
                    (1, 'Alice'),
                    (2, 'Bob');
                """
        );
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
                """
                DROP TABLE employees
                """
        );
    }


    record Employees(int id, String name)
    {
    }

    private final StatementUtil statementUtil = new H2StatementUtil();

    @Test
    void selectInto_readOptional_no_result()
    {
        Optional<Employees> optionalRow = statementUtil.selectInto(
                """
                SELECT id, name FROM employees WHERE id = ?
                """,
                ResultSetReaders.readOptional(),
                ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String ),
                StatementParameters.Integer( 0 )
        );
        assertThat( optionalRow ).isEmpty();
    }

    @Test
    void selectInto_readOptional_one_result()
    {
        Optional<Employees> optionalRow = statementUtil.selectInto(
                """
                SELECT id, name FROM employees WHERE id = ?
                """,
                ResultSetReaders.readOptional(),
                ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String ),
                StatementParameters.Integer( 1 )
        );
        assertThat( optionalRow ).isPresent();
        assertThat( optionalRow.get() ).isEqualTo( new Employees( 1, "Alice" ) );
    }

    @Test
    void selectInto_readOptional_two_results()
    {
        assertThatCode( () -> {
            Optional<Employees> optionalRow = statementUtil.selectInto(
                    """
                    SELECT id, name FROM employees
                    """,
                    ResultSetReaders.readOptional(),
                    ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String )
            );
        } )
                .isInstanceOf( IllegalStateException.class )
                .hasMessage( "ResultSet returned more than one row" );
    }


    @Test
    void selectInto_readOne_no_result()
    {
        assertThatCode( () -> {
            Employees one = statementUtil.selectInto(
                    """
                    SELECT id, name FROM employees WHERE id = ?
                    """,
                    ResultSetReaders.readOne(),
                    ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String ),
                    StatementParameters.
                            Integer( 0 )
            );
        } )
                .isInstanceOf( IllegalStateException.class )
                .hasMessage( "ResultSet did not return a row" );
    }

//    @Test
//    void selectInto_readOne_no_result_with_custom_exception()
//    {
//        assertThatCode( () -> {
//            Employees one = statementUtil.selectInto(
//                    """
//                    SELECT id, name FROM employees WHERE id = ?
//                    """,
//                    ResultSetReaders.readOne().orThrow( () -> throw new RuntimeException("custom")),
//                    ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String ),
//                    StatementParameters.Integer( 0 )
//            );
//        } )
//                .isInstanceOf( IllegalStateException.class )
//                .hasMessage( "ResultSet did not return a row" );
//    }

    @Test
    void selectInto_readOne_one_result()
    {
        Employees one = statementUtil.selectInto(
                """
                SELECT id, name FROM employees WHERE id = ?
                """,
                ResultSetReaders.readOne(),
                ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String ),
                StatementParameters.Integer( 2 )
        );
        assertThat( one ).isEqualTo( new Employees( 2, "Bob" ) );
    }

    @Test
    void selectInto_readOne_two_results()
    {
        assertThatCode( () -> {
            Employees one = statementUtil.selectInto(
                    """
                    SELECT id, name FROM employees
                    """,
                    ResultSetReaders.readOne(),
                    ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String )
            );
        } )
                .isInstanceOf( IllegalStateException.class );
    }

    @Test
    void selectInto_readMany()
    {
        List<Employees> manyRows = statementUtil.selectInto(
                """
                SELECT id, name FROM employees
                """,
                ResultSetReaders.readMany(),
                ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String )
        );
        assertThat( manyRows )
                .isInstanceOf( ArrayList.class )
                .hasSize( 2 )
                .contains(
                        new Employees( 1, "Alice" ),
                        new Employees( 2, "Bob" )
                );
    }

    @Test
    void selectInto_readMany_toSet()
    {
        Set<Employees> manyRows = statementUtil.selectInto(
                """
                SELECT id, name FROM employees
                """,
                ResultSetReaders.readMany( Collectors.toSet() ),
                ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String )
        );
        assertThat( manyRows )
                .isInstanceOf( HashSet.class )
                .hasSize( 2 )
                .contains(
                        new Employees( 1, "Alice" ),
                        new Employees( 2, "Bob" )
                );
    }

    @Test
    void selectInto_readMany_toStream()
    {
        Stream<Employees> manyRows = statementUtil.selectInto(
                """
                SELECT id, name FROM employees
                """,
                ResultSetReaders.readMany( ResultSetReaders.toStream() ),
                ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String )
        );
        assertThat( manyRows )
                .isInstanceOf( Stream.class )
                .hasSize( 2 )
                .contains(
                        new Employees( 1, "Alice" ),
                        new Employees( 2, "Bob" )
                );
    }

    @Test
    void selectInto_readMany_toConsumer()
    {
        class MyConsumer<T> implements Consumer<T>
        {
            List<T> list = new ArrayList<>();

            @Override
            public void accept(T t)
            {
                list.add( t );
            }
        }
        MyConsumer<Employees> consumer = statementUtil.selectInto(
                """
                SELECT id, name FROM employees
                """,
                ResultSetReaders.readMany( new MyConsumer<>() ),
                ResultSetMappers.object( Employees::new, ResultTypes.Integer, ResultTypes.String )
        );
        assertThat( consumer.list )
                .isInstanceOf( ArrayList.class )
                .hasSize( 2 )
                .contains(
                        new Employees( 1, "Alice" ),
                        new Employees( 2, "Bob" )
                );
    }
}