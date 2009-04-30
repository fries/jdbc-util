/**
 * (C) Copyright 2007 M.Sc. Friedrich Schäuffelhut
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * $Revison$
 * $Author$
 * $Date$
 */
package de.schaeuffelhut.jdbc;

import static junit.framework.Assert.assertEquals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.schaeuffelhut.jdbc.xx.StatementUtil;



/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public class TestSelectInto
{
	private static Connection connection;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql://localhost/jdbcutil?useServerPrepStmts=false",
	        "jdbcutil", "jdbcutil");
		
		connection.createStatement().execute(
				"DROP TABLE IF EXISTS person"
		);
		connection.createStatement().execute(
				"CREATE TABLE person (" +
				" name CHAR(30)," +
				" birthday DATE," +
				" address CHAR(30) "+
				")"
		);
		connection.createStatement().execute(
				"INSERT INTO person (name, birthday, address) VALUES ('Friedrich','1975-12-27','Unterhaching')"
		);
		connection.createStatement().execute(
				"INSERT INTO person (name, birthday, address) VALUES ('Sarah','1973-05-23','Unterhaching')"
		);
		connection.createStatement().execute(
				"INSERT INTO person (name, birthday, address) VALUES ('Sylvia','2008-08-11','Unterhaching')"
		);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testConnection() throws Exception
	{
		Assert.assertNotNull( connection );
	}

	static class Person {
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
		String name = StatementUtil.selectInto( connection, 
				"SELECT name FROM person WHERE birthday = ?",
				ResultSetReaders.readScalar( ResultTypes.String ),
				StatementParameters.String( "1975-12-27" )
		);
		assertEquals( "Friedrich", name );
	}

	@Test
	public void testSelectInto_Tuple() throws Exception
	{
		Object[] person = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE birthday = ?",
				ResultSetReaders.readTuple( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "1975-12-27" )
		);
		assertEquals( "Friedrich", person[0] );
		assertEquals( "Unterhaching", person[1] );
	}

	@Test
	public void testSelectInto_Map() throws Exception
	{
		Map<String, Object> person = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE birthday = ?",
				ResultSetReaders.readMap( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "1975-12-27" )
		);
		assertEquals( "Friedrich", person.get( "name" ) );
		assertEquals( "Unterhaching", person.get( "address" ) );
	}

	@Test
	public void testSelectInto_Object() throws Exception
	{
		Person person = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE birthday = ?",
				ResultSetReaders.readObject( Person.class, ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "1975-12-27" )
		);
		assertEquals( "Friedrich", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Unterhaching", person.address );
	}

	/*
	 * read multiple row
	 */

	@Test
	public void testSelectInto_Scalars() throws Exception
	{
		ArrayList<String> names = StatementUtil.selectInto( connection, 
				"SELECT name FROM person WHERE address = ? ORDER BY name",
				ResultSetReaders.readScalars( ResultTypes.String ),
				StatementParameters.String( "Unterhaching" )
		);
		Assert.assertTrue( names.contains( "Friedrich" ) );
		Assert.assertTrue( names.contains( "Sarah" ) );
		Assert.assertTrue( names.contains( "Sylvia" ) );
	}
	
	@Test
	public void testSelectInto_Tuples() throws Exception
	{
		ArrayList<Object[]> persons = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE address = ? ORDER BY name",
				ResultSetReaders.readTuples( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "Unterhaching" )
		);
		
		Iterator<Object[]> iterator = persons.iterator();
		Object[] person;
		
		person = iterator.next();
		assertEquals( "Friedrich", person[0] );
		assertEquals( "Unterhaching", person[1] );

		person = iterator.next();
		assertEquals( "Sarah", person[0] );
		assertEquals( "Unterhaching", person[1] );

		person = iterator.next();
		assertEquals( "Sylvia", person[0] );
		assertEquals( "Unterhaching", person[1] );
	}

	@Test
	public void testSelectInto_Maps() throws Exception
	{
		ArrayList<Map<String, Object>> persons = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE address = ? ORDER BY name",
				ResultSetReaders.readMaps( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "Unterhaching" )
		);
		
		Iterator<Map<String, Object>> iterator = persons.iterator();
		Map<String, Object> person;
		
		person = iterator.next();
		assertEquals( "Friedrich", person.get( "name" ) );
		assertEquals( "Unterhaching", person.get( "address" ) );

		person = iterator.next();
		assertEquals( "Sarah", person.get( "name" ) );
		assertEquals( "Unterhaching", person.get( "address" ) );

		person = iterator.next();
		assertEquals( "Sylvia", person.get( "name" ) );
		assertEquals( "Unterhaching", person.get( "address" ) );
	}

	@Test
	public void testSelectInto_Objects() throws Exception
	{
		ArrayList<Person> persons = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE address = ? ORDER BY name",
				ResultSetReaders.readObjects( Person.class, ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "Unterhaching" )
		);

		Iterator<Person> iterator = persons.iterator();
		Person person;
		
		person = iterator.next();
		assertEquals( "Friedrich", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Unterhaching", person.address );

		person = iterator.next();
		assertEquals( "Sarah", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Unterhaching", person.address );

		person = iterator.next();
		assertEquals( "Sylvia", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Unterhaching", person.address );
	}

}
