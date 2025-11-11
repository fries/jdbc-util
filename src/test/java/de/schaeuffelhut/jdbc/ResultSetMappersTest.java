/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultSetMappersTest
{
    @BeforeAll
    static void initDatabase()
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

    record Employees(int id, String name) { }

    private final StatementUtil statementUtil = new H2StatementUtil();

    @Test
    void scalar()
    {
        String scalar = statementUtil.selectInto(
                """
                SELECT name FROM employees WHERE id = 1
                """,
                ResultSetReaders.readOne(),
                ResultSetMappers.scalar( ResultTypes.String )
        );

        assertThat( scalar ).isEqualTo( "Alice" );
    }

    @Test
    void tuple()
    {
        var tuple = statementUtil.selectInto(
                """
                SELECT id, name FROM employees WHERE id = 1
                """,
                ResultSetReaders.readOne(),
                ResultSetMappers.tuple( ResultTypes.Integer, ResultTypes.String )
        );

        assertThat( tuple )
                .hasSize( 2 )
                .contains( 1, "Alice" );
    }

    @Test
    void map()
    {
        var map = statementUtil.selectInto(
                """
                SELECT id, name, count(*), SUM(id) AS "sum" FROM employees WHERE id = 1 GROUP BY 1,2
                """,
                ResultSetReaders.readOne(),
                ResultSetMappers.map( ResultTypes.Integer, ResultTypes.String, ResultTypes.Integer, ResultTypes.Integer )
        );

        assertThat( map )
                .hasSize( 4 )
                .containsEntry( "ID", 1 )
                .containsEntry( "NAME", "Alice" )
                .containsEntry( "COUNT(*)", 1 )
                .containsEntry( "sum", 1 );
    }

    @Test
    void object_via_method_reference()
    {
        record Employee(int id, String name) { }
        Employee object = statementUtil.selectInto(
                """
                SELECT id, name FROM employees WHERE id = 2
                """,
                ResultSetReaders.readOne(),
                ResultSetMappers.object( Employee::new, ResultTypes.Integer, ResultTypes.String )
        );

        assertThat( object ).isEqualTo( new Employee( 2, "Bob" ) );
    }

    public static class Employee_object_via_method_reference
    {
        int ID;
        String NAME;
    }

    @Test
    void object_via_reflection_with_newInstance()
    {
        var object = statementUtil.selectInto(
                """
                SELECT id AS "id", name AS "name" FROM employees WHERE id = 2
                """,
                ResultSetReaders.readOne(),
                ResultSetMappers.objectViaReflection(
                        Employee_object_via_method_reference.class,
                        ResultTypes.Integer,
                        ResultTypes.String
                )
        );

        assertThat( object )
                .hasFieldOrPropertyWithValue( "ID", 2 )
                .hasFieldOrPropertyWithValue( "NAME", "Bob" );
    }

    @Test
    void object_via_reflection_with_Supplier()
    {
        class Employee { int ID; String NAME;}
        var object = statementUtil.selectInto(
                """
                SELECT id AS "id", name AS "name" FROM employees WHERE id = 2
                """,
                ResultSetReaders.readOne(),
                ResultSetMappers.objectViaReflection(
                        Employee.class,
                        Employee::new,
                        ResultTypes.Integer,
                        ResultTypes.String
                )
        );

        assertThat( object )
                .hasFieldOrPropertyWithValue( "ID", 2 )
                .hasFieldOrPropertyWithValue( "NAME", "Bob" );
    }
}