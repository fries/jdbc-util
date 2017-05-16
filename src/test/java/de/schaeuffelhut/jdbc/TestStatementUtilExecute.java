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
import java.sql.Date;
import java.sql.DriverManager;
import java.util.Calendar;

import org.junit.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;




/**
 * @author Friedrich Schäuffelhut
 *
 */
@Ignore
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
				StatementParameters.String("John"),
				StatementParameters.Date( new Date( cal.getTimeInMillis() ) ),
				StatementParameters.String("Samplestreet 12")
		);
		
		Assert.assertEquals( 
				(Integer)1,
				StatementUtil.selectInto( connection,
						"SELECT count(*) FROM person",
						ResultSetReaders.readScalar( ResultTypes.Integer )
				)
		);

		StatementUtil.execute( connection,
				"DELETE FROM person" +
				" WHERE name = ?" +
				"   AND birthday = ?" +
				"   AND address = ?",
				StatementParameters.String("John"),
				StatementParameters.Date( new Date( cal.getTimeInMillis() ) ),
				StatementParameters.String("Samplestreet 12")
		);
		
		Assert.assertEquals( 
				(Integer)0,
				StatementUtil.selectInto( connection,
						"SELECT count(*) FROM person",
						ResultSetReaders.readScalar( ResultTypes.Integer )
				)
		);
	}
	
}
