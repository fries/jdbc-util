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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author M.Sc. Friedrich Schäuffelhut
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
				"('fries', '1975-12-27', 'Hofmarkweg 12')," +
				"('sarah', '1973-05-23', 'Hofmarkweg 12')"
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
				"SELECT name FROM person WHERE name = 'fries'"
		);
		Assert.assertEquals( 
				"fries",
				ResultSetUtil.readScalar( rs, ResultTypes.String )
		);
	}

	@Test
	public void testReadTuple() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'fries'"
		);
		Object[] tuple = ResultSetUtil.readTuple( rs, ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "fries", tuple[0] );
		Assert.assertEquals( "Hofmarkweg 12", tuple[1] );
	}

	@Test
	public void testReadMap() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'fries'"
		);
		Map<String, Object> result = ResultSetUtil.readMap( rs, ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "fries", result.get( "name" ) );
		Assert.assertEquals( "Hofmarkweg 12", result.get( "address" ) );
	}

	public static class Result {
		public String name;
		public String address;
	}

	@Test
	public void testReadObject() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'fries'"
		);
		Result result = ResultSetUtil.readObject( rs, Result.class, ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "fries", result.name );
		Assert.assertEquals( "Hofmarkweg 12", result.address );
	}

	@Test
	public void testReadObject2() throws Exception
	{
		ResultSet rs = connection.createStatement().executeQuery(
				"SELECT name, address FROM person WHERE name = 'fries'"
		);
		Result result = ResultSetUtil.readObject( rs, new Result(), ResultTypes.String, ResultTypes.String );
		Assert.assertEquals( "fries", result.name );
		Assert.assertEquals( "Hofmarkweg 12", result.address );
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

		Assert.assertEquals( "sarah", it.next() );
		Assert.assertEquals( "fries", it.next() );
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
		Assert.assertEquals( "sarah", tuple[0] );
		Assert.assertEquals( "Hofmarkweg 12", tuple[1] );
		
		tuple = iterator.next();
		Assert.assertEquals( "fries", tuple[0] );
		Assert.assertEquals( "Hofmarkweg 12", tuple[1] );
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
		Assert.assertEquals( "sarah", map.get( "name" ) );
		Assert.assertEquals( "Hofmarkweg 12", map.get( "address" ) );

		map = iterator.next();
		Assert.assertEquals( "fries", map.get( "name" ) );
		Assert.assertEquals( "Hofmarkweg 12", map.get( "address" ) );
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
		Assert.assertEquals( "sarah", result.name );
		Assert.assertEquals( "Hofmarkweg 12", result.address );

		result = iterator.next();
		Assert.assertEquals( "fries", result.name );
		Assert.assertEquals( "Hofmarkweg 12", result.address );
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
//		Assert.assertEquals( "sarah", result.name );
//		Assert.assertEquals( "Hofmarkweg 12", result.address );
//
//		result = iterator.next();
//		Assert.assertEquals( "fries", result.name );
//		Assert.assertEquals( "Hofmarkweg 12", result.address );
	}

}
