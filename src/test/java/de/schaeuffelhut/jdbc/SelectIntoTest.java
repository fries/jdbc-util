/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static de.schaeuffelhut.jdbc.ResultSetMappers.*;
import static de.schaeuffelhut.jdbc.ResultSetReaders.readMany;
import static de.schaeuffelhut.jdbc.ResultSetReaders.readOne;
import static org.junit.Assert.assertEquals;


/**
 * @author Friedrich Schäuffelhut
 */
@Ignore
public class SelectIntoTest
{
    @BeforeAll
    public static void setUpBeforeClass() throws Exception
    {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
                """
                CREATE TABLE person (
                	name CHAR(30),
                	birthday DATE,
                	address CHAR(30)
                );
                INSERT INTO person (name, birthday, address) VALUES ('John','1975-12-27','Samplecity');
                INSERT INTO person (name, birthday, address) VALUES ('Paul','1973-05-23','Samplecity');
                INSERT INTO person (name, birthday, address) VALUES ('Claudia','2008-08-11','Samplecity');
                """
        );
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception
    {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
                """
                DROP TABLE person
                """
        );
    }

    private final StatementUtil statementUtil = new H2StatementUtil();

    static class Person
    {
        String name;
        Date birthday;
        String address;
    }

    /*
     * read single row
     */

    @Test
    public void testSelectInto_Scalar() throws Exception
    {
        String name = statementUtil.selectInto(
                "SELECT name FROM person WHERE birthday = ?",
                readOne(),
                scalar( ResultTypes.String ),
                StatementParameters.String( "1975-12-27" )
        );
        assertEquals( "John", name );
    }

    @Test
    public void testSelectInto_Tuple() throws Exception
    {
        Object[] person = statementUtil.selectInto(
                "SELECT name, address FROM person WHERE birthday = ?",
                readOne(),
                tuple( ResultTypes.String, ResultTypes.String ),
                StatementParameters.String( "1975-12-27" )
        );
        assertEquals( "John", person[0] );
        assertEquals( "Samplecity", person[1] );
    }

    @Test
    public void testSelectInto_Map() throws Exception
    {
        Map<String, Object> person = statementUtil.selectInto(
                "SELECT name, address FROM person WHERE birthday = ?",
                readOne(),
                map( ResultTypes.String, ResultTypes.String ),
                StatementParameters.String( "1975-12-27" )
        );
        assertEquals( "John", person.get( "name" ) );
        assertEquals( "Samplecity", person.get( "address" ) );
    }

    @Test
    public void testSelectInto_Object() throws Exception
    {
        Person person = statementUtil.selectInto(
                "SELECT name, address FROM person WHERE birthday = ?",
                readOne(),
                objectViaReflection( Person.class, ResultTypes.String, ResultTypes.String ),
                StatementParameters.String( "1975-12-27" )
        );
        assertEquals( "John", person.name );
        assertEquals( null, person.birthday );
        assertEquals( "Samplecity", person.address );
    }

    /*
     * read multiple row
     */

    @Test
    public void testSelectInto_Scalars() throws Exception
    {
        List<String> names = statementUtil.selectInto(
                "SELECT name FROM person WHERE address = ? ORDER BY name",
                readMany(),
                scalar( ResultTypes.String ),
                StatementParameters.String( "Samplecity" )
        );
        Assert.assertTrue( names.contains( "John" ) );
        Assert.assertTrue( names.contains( "Paul" ) );
        Assert.assertTrue( names.contains( "Claudia" ) );
    }

    @Test
    public void testSelectInto_Tuples() throws Exception
    {
        List<Object[]> persons = statementUtil.selectInto(
                "SELECT name, address FROM person WHERE address = ? ORDER BY name",
                readMany(),
                tuple( ResultTypes.String, ResultTypes.String ),
                StatementParameters.String( "Samplecity" )
        );

        Iterator<Object[]> iterator = persons.iterator();
        Object[] person;

        person = iterator.next();
        assertEquals( "John", person[0] );
        assertEquals( "Samplecity", person[1] );

        person = iterator.next();
        assertEquals( "Paul", person[0] );
        assertEquals( "Samplecity", person[1] );

        person = iterator.next();
        assertEquals( "Claudia", person[0] );
        assertEquals( "Samplecity", person[1] );
    }

    @Test
    public void testSelectInto_Maps() throws Exception
    {
        List<Map<String, Object>> persons = statementUtil.selectInto(
                "SELECT name, address FROM person WHERE address = ? ORDER BY name",
                readMany(),
                map( ResultTypes.String, ResultTypes.String ),
                StatementParameters.String( "Samplecity" )
        );

        Iterator<Map<String, Object>> iterator = persons.iterator();
        Map<String, Object> person;

        person = iterator.next();
        assertEquals( "John", person.get( "name" ) );
        assertEquals( "Samplecity", person.get( "address" ) );

        person = iterator.next();
        assertEquals( "Paul", person.get( "name" ) );
        assertEquals( "Samplecity", person.get( "address" ) );

        person = iterator.next();
        assertEquals( "Claudia", person.get( "name" ) );
        assertEquals( "Samplecity", person.get( "address" ) );
    }

    @Test
    public void testSelectInto_Objects() throws Exception
    {
        List<Person> persons = statementUtil.selectInto(
                "SELECT name, address FROM person WHERE address = ? ORDER BY name",
                readMany(),
                objectViaReflection( Person.class, ResultTypes.String, ResultTypes.String ),
                StatementParameters.String( "Samplecity" )
        );

        Iterator<Person> iterator = persons.iterator();
        Person person;

        person = iterator.next();
        assertEquals( "John", person.name );
        assertEquals( null, person.birthday );
        assertEquals( "Samplecity", person.address );

        person = iterator.next();
        assertEquals( "Paul", person.name );
        assertEquals( null, person.birthday );
        assertEquals( "Samplecity", person.address );

        person = iterator.next();
        assertEquals( "Claudia", person.name );
        assertEquals( null, person.birthday );
        assertEquals( "Samplecity", person.address );
    }
}
