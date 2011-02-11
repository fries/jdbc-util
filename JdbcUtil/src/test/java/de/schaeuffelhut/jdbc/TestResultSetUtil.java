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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Friedrich Schäuffelhut
 *
 */
public class TestResultSetUtil
{
	private static Connection connection;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql://localhost/jdbcutil",
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
				"INSERT INTO person (name, birthday, address)" +
				" VALUES " +
				"('John', '1966-10-20', 'Samplestreet 12')," +
				"('Paul', '1993-07-26', 'Samplestreet 12')"
		);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		connection.close();
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

	/*
	 * read single row
	 */

	@Test
	public void testReadScalar() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name FROM person WHERE name = 'John'"
		);
		Assert.assertEquals( 
				"John",
				ResultSetUtil.readScalar( rs, ResultTypes.String )
		);
	}

	@Test
	public void testReadTuple() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'John'"
		);
		Object[] tuple = ResultSetUtil.readTuple( rs, ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "John", tuple[0] );
		Assert.assertEquals( "Samplestreet 12", tuple[1] );
	}

	@Test
	public void testReadMap() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'John'"
		);
		Map<String, Object> result = ResultSetUtil.readMap( rs, ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "John", result.get( "name" ) );
		Assert.assertEquals( "Samplestreet 12", result.get( "address" ) );
	}

	public static class Result {
		public String name;
		public String address;
	}

	@Test
	public void testReadObject() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'John'"
		);
		Result result = ResultSetUtil.readObject( rs, Result.class, ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "John", result.name );
		Assert.assertEquals( "Samplestreet 12", result.address );
	}

	@Test
	public void testReadObject2() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'John'"
		);
		Result result = ResultSetUtil.readObject( rs, new Result(), ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "John", result.name );
		Assert.assertEquals( "Samplestreet 12", result.address );
	}

	@Test
	public void testReadObject3() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'noname'"
		);
		Result result = ResultSetUtil.readObject( rs, new Result(), ResultTypes.String, ResultTypes.String );
		Assert.assertNull( result );
	}

	/*
	 * read multiple rows
	 */
	
	@Test
	public void testReadScalars() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name FROM person ORDER BY birthday"
		);
		Collection<String> results =
			ResultSetUtil.readScalars( rs, ResultTypes.String );
		
		Assert.assertEquals( 2, results.size() );
		
		Iterator<String> it = results.iterator();

		Assert.assertEquals( "Paul", it.next() );
		Assert.assertEquals( "John", it.next() );
	}

	@Test
	public void testReadTuples() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person ORDER BY birthday"
		);
		Collection<Object[]> tuples = ResultSetUtil.readTuples( rs, ResultTypes.String, ResultTypes.String );

		Assert.assertEquals( 2, tuples.size() );
		
		Iterator<Object[]> iterator = tuples.iterator();
		Object[] tuple;
		
		tuple = iterator.next();
		Assert.assertEquals( "Paul", tuple[0] );
		Assert.assertEquals( "Samplestreet 12", tuple[1] );
		
		tuple = iterator.next();
		Assert.assertEquals( "John", tuple[0] );
		Assert.assertEquals( "Samplestreet 12", tuple[1] );
	}

	@Test
	public void testReadMaps() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person ORDER BY birthday"
		);
		Collection<Map<String, Object>> maps = ResultSetUtil.readMaps( rs, ResultTypes.String, ResultTypes.String );

		Assert.assertEquals( 2, maps.size() );
		
		Iterator<Map<String, Object>> iterator = maps.iterator();
		Map<String, Object> map;

		map = iterator.next();
		Assert.assertEquals( "Paul", map.get( "name" ) );
		Assert.assertEquals( "Samplestreet 12", map.get( "address" ) );

		map = iterator.next();
		Assert.assertEquals( "John", map.get( "name" ) );
		Assert.assertEquals( "Samplestreet 12", map.get( "address" ) );
	}

	@Test
	public void testReadObjects() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person ORDER BY birthday"
		);
		Collection<Result> objects = ResultSetUtil.readObjects( rs, Result.class, ResultTypes.String, ResultTypes.String );

		Assert.assertEquals( 2, objects.size() );
		
		Iterator<Result> iterator = objects.iterator();
		Result result;

		result = iterator.next();
		Assert.assertEquals( "Paul", result.name );
		Assert.assertEquals( "Samplestreet 12", result.address );

		result = iterator.next();
		Assert.assertEquals( "John", result.name );
		Assert.assertEquals( "Samplestreet 12", result.address );
	}

	@Test
	public void testReadObjects2() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person ORDER BY birthday"
		);
		Runnable r = new Runnable() {
			int i = 0;
			public String name;
			public String address;
			public void run()
			{
				System.err.println(name+","+address);
				i++;
			}
		};
		ResultSetUtil.forResultsInvoke( r, rs, ResultTypes.String, ResultTypes.String );

//		Assert.assertEquals( 2, objects.size() );
//		
//		Iterator<Result> iterator = objects.iterator();
//		Result result;
//
//		result = iterator.next();
//		Assert.assertEquals( "Paul", result.name );
//		Assert.assertEquals( "Samplestreet 12", result.address );
//
//		result = iterator.next();
//		Assert.assertEquals( "John", result.name );
//		Assert.assertEquals( "Samplestreet 12", result.address );
	}

}
