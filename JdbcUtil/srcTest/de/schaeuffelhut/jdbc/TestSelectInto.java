/**
 * Copyright 2009 Friedrich Schäuffelhut
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
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
 * @author Friedrich Schäuffelhut
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
				"INSERT INTO person (name, birthday, address) VALUES ('John','1975-12-27','Samplecity')"
		);
		connection.createStatement().execute(
				"INSERT INTO person (name, birthday, address) VALUES ('Paul','1973-05-23','Samplecity')"
		);
		connection.createStatement().execute(
				"INSERT INTO person (name, birthday, address) VALUES ('Claudia','2008-08-11','Samplecity')"
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
		assertEquals( "John", name );
	}

	@Test
	public void testSelectInto_Tuple() throws Exception
	{
		Object[] person = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE birthday = ?",
				ResultSetReaders.readTuple( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "1975-12-27" )
		);
		assertEquals( "John", person[0] );
		assertEquals( "Samplecity", person[1] );
	}

	@Test
	public void testSelectInto_Map() throws Exception
	{
		Map<String, Object> person = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE birthday = ?",
				ResultSetReaders.readMap( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "1975-12-27" )
		);
		assertEquals( "John", person.get( "name" ) );
		assertEquals( "Samplecity", person.get( "address" ) );
	}

	@Test
	public void testSelectInto_Object() throws Exception
	{
		Person person = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE birthday = ?",
				ResultSetReaders.readObject( Person.class, ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "1975-12-27" )
		);
		assertEquals( "John", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Samplecity", person.address );
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
				StatementParameters.String( "Samplecity" )
		);
		Assert.assertTrue( names.contains( "John" ) );
		Assert.assertTrue( names.contains( "Paul" ) );
		Assert.assertTrue( names.contains( "Claudia" ) );
	}
	
	@Test
	public void testSelectInto_Tuples() throws Exception
	{
		ArrayList<Object[]> persons = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE address = ? ORDER BY name",
				ResultSetReaders.readTuples( ResultTypes.String, ResultTypes.String ),
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
		ArrayList<Map<String, Object>> persons = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE address = ? ORDER BY name",
				ResultSetReaders.readMaps( ResultTypes.String, ResultTypes.String ),
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
		ArrayList<Person> persons = StatementUtil.selectInto( connection, 
				"SELECT name, address FROM person WHERE address = ? ORDER BY name",
				ResultSetReaders.readObjects( Person.class, ResultTypes.String, ResultTypes.String ),
				StatementParameters.String( "Samplecity" )
		);

		Iterator<Person> iterator = persons.iterator();
		Person person;
		
		person = iterator.next();
		assertEquals( "John", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Samplecity", person.address );

		person = iterator.next();
		assertEquals( "Paul", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Samplecity", person.address );

		person = iterator.next();
		assertEquals( "Claudia", person.name );
		assertEquals( null, person.birthday);
		assertEquals( "Samplecity", person.address );
	}

}
