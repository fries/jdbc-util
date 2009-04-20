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
import java.sql.Date;
import java.sql.DriverManager;
import java.util.Calendar;

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
public class TestStatementUtilExecute
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
	public void testExecute1() throws Exception
	{
		Calendar cal = Calendar.getInstance();
		cal.set( 1075, 12, 27 );
		
		StatementUtil.execute( connection,
				"INSERT INTO person (name, birthday, address)" +
				" VALUES (?, ?, ?)",
				StatementParameters.String("fries"),
				StatementParameters.Date( new Date( cal.getTimeInMillis() ) ),
				StatementParameters.String("Hofmarkweg 12")
		);
		
		Assert.assertEquals( 
				(Integer)1,
				StatementUtil.selectIntoScalar( connection,
						"SELECT count(*) FROM person",
						ResultTypes.Integer
				)
		);

		StatementUtil.execute( connection,
				"DELETE FROM person" +
				" WHERE name = ?" +
				"   AND birthday = ?" +
				"   AND address = ?",
				StatementParameters.String("fries"),
				StatementParameters.Date( new Date( cal.getTimeInMillis() ) ),
				StatementParameters.String("Hofmarkweg 12")
		);
		
		Assert.assertEquals( 
				(Integer)0,
				StatementUtil.selectIntoScalar( connection,
						"SELECT count(*) FROM person",
						ResultTypes.Integer
				)
		);
	}
	
}
