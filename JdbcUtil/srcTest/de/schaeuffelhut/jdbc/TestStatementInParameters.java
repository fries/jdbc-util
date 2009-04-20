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

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.schaeuffelhut.jdbc.xx.StatementUtil;

import static de.schaeuffelhut.jdbc.StatementParameters.bindValue;
import static junit.framework.Assert.assertEquals;


/**
 * @author M.Sc. Friedrich Schäuffelhut
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
		Object[] result = StatementUtil.selectIntoTuple( connection, 
				"SELECT @array",
				ResultTypes.resultTypes( 
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
		assertEquals( value, StatementUtil.selectIntoScalar( connection, 
				"SELECT ?", resultType, bindValue( statementParameter, value )
		));
	}
	
	@SuppressWarnings("unchecked")
	private <T> void check(IfcResultType<T> resultType, IfcStatementInParameterType<T> statementParameter, T value1, T value2) throws Exception
	{
		Object[] result = StatementUtil.selectIntoTuple( connection, 
				"SELECT @array",
				ResultTypes.resultTypes( resultType, resultType ), 
				StatementParameters.Array( statementParameter, "@array", value1, value2 )
		);
		assertEquals( value1, result[0] );
		assertEquals( value2, result[1] );
	}

}
