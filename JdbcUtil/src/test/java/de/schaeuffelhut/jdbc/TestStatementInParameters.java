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

import static de.schaeuffelhut.jdbc.StatementParameters.bindValue;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;

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
public class TestStatementInParameters
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

	@Test
	public void testTypes() throws Exception
	{
		check( ResultTypes.Boolean, StatementParameters.Boolean, true );
		
		check( ResultTypes.Byte, StatementParameters.Byte, (byte)0x55 );
		
		check( ResultTypes.Byte, StatementParameters.Byte, Byte.MIN_VALUE );
		check( ResultTypes.Byte, StatementParameters.Byte, Byte.MAX_VALUE );
		
		check( ResultTypes.Short, StatementParameters.Short, Short.MIN_VALUE );
		check( ResultTypes.Short, StatementParameters.Short, Short.MAX_VALUE );
	
		check( ResultTypes.Integer, StatementParameters.Integer, Integer.MIN_VALUE );
		check( ResultTypes.Integer, StatementParameters.Integer, Integer.MAX_VALUE );

		check( ResultTypes.Long, StatementParameters.Long, Long.MIN_VALUE );
		check( ResultTypes.Long, StatementParameters.Long, Long.MAX_VALUE );

		check( ResultTypes.Float, StatementParameters.Float, 2f );
		check( ResultTypes.Float, StatementParameters.Float, 100f );

		check( ResultTypes.Double, StatementParameters.Double, 2d );
		check( ResultTypes.Double, StatementParameters.Double, 100d );
	}

	
	@Test
	public void testArray() throws Exception
	{
		check( ResultTypes.Boolean, StatementParameters.Boolean, true, false );
		
		check( ResultTypes.Byte, StatementParameters.Byte, (byte)0x55, (byte)0x66 );
		
		check( ResultTypes.Byte, StatementParameters.Byte, Byte.MIN_VALUE, Byte.MAX_VALUE );
		
		check( ResultTypes.Short, StatementParameters.Short, Short.MIN_VALUE, Short.MAX_VALUE );
	
		check( ResultTypes.Integer, StatementParameters.Integer, Integer.MIN_VALUE, Integer.MAX_VALUE );

		check( ResultTypes.Long, StatementParameters.Long, Long.MIN_VALUE, Long.MAX_VALUE );

		check( ResultTypes.Float, StatementParameters.Float, 2f, 100f );

		check( ResultTypes.Double, StatementParameters.Double, 2d, 100d );
	}

	@Test
	public void testSubArray() throws Exception
	{
		Object[] result = StatementUtil.selectInto( connection, 
				"SELECT @array",
				ResultSetReaders.readTuple( 
						ResultTypes.Integer,ResultTypes.Integer,
						ResultTypes.Integer,ResultTypes.Integer,
						ResultTypes.Integer,ResultTypes.Integer
				), 
				StatementParameters.Array( 
						StatementParameters.Array( 
								StatementParameters.Integer,
								"@subarray"
						),
						"@array",
						"@subarray",
						new Integer[] { 1,2,3,4 },
						new Integer[] { 5,6 }
				)
		);
		assertEquals( 1, result[0] );
		assertEquals( 2, result[1] );
		assertEquals( 3, result[2] );
		assertEquals( 4, result[3] );
		assertEquals( 5, result[4] );
		assertEquals( 6, result[5] );
	}

	
	private <T> void check(IfcResultType<T> resultType, IfcStatementInParameterType<T> statementParameter, T value) throws Exception
	{
		assertEquals( value, StatementUtil.selectInto( connection, 
				"SELECT ?",
				ResultSetReaders.readScalar( resultType ),
				bindValue( statementParameter, value )
		));
	}
	
	@SuppressWarnings("unchecked")
	private <T> void check(IfcResultType<T> resultType, IfcStatementInParameterType<T> statementParameter, T value1, T value2) throws Exception
	{
		Object[] result = StatementUtil.selectInto( connection, 
				"SELECT @array",
				ResultSetReaders.readTuple( 
						ResultTypes.resultTypes( resultType, resultType )
				), 
				StatementParameters.Array( statementParameter, "@array", value1, value2 )
		);
		assertEquals( value1, result[0] );
		assertEquals( value2, result[1] );
	}

}
