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
public class TestStatementUtilWithParams
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
	public void testselectIntoScalar() throws Exception
	{
		Assert.assertEquals( 
				"fries",
				StatementUtil.selectIntoScalar( connection,
						"SELECT name FROM person WHERE name = ?",
						ResultTypes.String,
						StatementParameters.String("fries ")
				)
		);
	}

	@Test
	public void testselectIntoTuple() throws Exception
	{
		Object[] tuple = StatementUtil.selectIntoTuple( connection,
				"SELECT name, address FROM person WHERE name = ?",
				ResultTypes.resultTypes( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String("fries ")
		);
		Assert.assertEquals( "fries", tuple[0] );
		Assert.assertEquals( "Hofmarkweg 12", tuple[1] );
	}

	@Test
	public void testselectIntoMap() throws Exception
	{
		Map<String, Object> result = StatementUtil.selectIntoMap( connection, 
				"SELECT name, address FROM person WHERE name = ?",
				ResultTypes.resultTypes( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String("fries ")
		);
		Assert.assertEquals( "fries", result.get( "name" ) );
		Assert.assertEquals( "Hofmarkweg 12", result.get( "address" ) );
	}

	public static class Result {
		public String name;
		public String address;
	}

	@Test
	public void testselectIntoObject() throws Exception
	{
		Result result = StatementUtil.selectIntoObject( connection,
				"SELECT name, address FROM person WHERE name = ?",
				Result.class,
				ResultTypes.resultTypes( ResultTypes.String, ResultTypes.String ),
				StatementParameters.String("fries ")
		);
		Assert.assertEquals( "fries", result.name );
		Assert.assertEquals( "Hofmarkweg 12", result.address );
	}
	
	/*
	 * read multiple rows
	 */
	
//	@Test
//	public void testselectIntoScalars() throws Exception
//	{
//		Collection<String> results = StatementUtil.selectIntoScalars( connection,
//				"SELECT name FROM person ORDER BY birthday",
//				ResultTypes.String
//		);
//		
//		Assert.assertEquals( 2, results.size() );
//		
//		Iterator<String> it = results.iterator();
//
//		Assert.assertEquals( "sarah", it.next() );
//		Assert.assertEquals( "fries", it.next() );
//	}
//
//	@Test
//	public void testselectIntoTuples() throws Exception
//	{
//		ArrayList<Object[]> tuples = StatementUtil.selectIntoTuples( connection,
//				"SELECT name, address FROM person ORDER BY birthday",
//				ResultTypes.String, ResultTypes.String
//		);
//
//		Assert.assertEquals( 2, tuples.size() );
//		
//		Iterator<Object[]> iterator = tuples.iterator();
//		Object[] tuple;
//		
//		tuple = iterator.next();
//		Assert.assertEquals( "sarah", tuple[0] );
//		Assert.assertEquals( "Hofmarkweg 12", tuple[1] );
//		
//		tuple = iterator.next();
//		Assert.assertEquals( "fries", tuple[0] );
//		Assert.assertEquals( "Hofmarkweg 12", tuple[1] );
//	}
//
//	@Test
//	public void testselectIntoMaps() throws Exception
//	{
//		Collection<Map<String, Object>> maps = StatementUtil.selectIntoMaps( connection,
//				"SELECT name, address FROM person ORDER BY birthday",
//				ResultTypes.String, ResultTypes.String
//		);
//
//		Assert.assertEquals( 2, maps.size() );
//		
//		Iterator<Map<String, Object>> iterator = maps.iterator();
//		Map<String, Object> map;
//
//		map = iterator.next();
//		Assert.assertEquals( "sarah", map.get( "name" ) );
//		Assert.assertEquals( "Hofmarkweg 12", map.get( "address" ) );
//
//		map = iterator.next();
//		Assert.assertEquals( "fries", map.get( "name" ) );
//		Assert.assertEquals( "Hofmarkweg 12", map.get( "address" ) );
//	}
//
//	@Test
//	public void testselectIntoObjects() throws Exception
//	{
//		Collection<Result> objects = StatementUtil.selectIntoObjects( connection,
//				"SELECT name, address FROM person ORDER BY birthday",
//				Result.class, ResultTypes.String, ResultTypes.String
//		);
//
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
//	}
}
