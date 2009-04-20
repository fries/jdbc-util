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

import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public class TryDataSource
{
	@Test
	public void testDataSource() throws Exception
	{
//		com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
		
		MysqlDataSource ds = new MysqlDataSource();
		ds.setUrl( "jdbc:mysql://localhost/jdbcutil" );
		ds.setUser( "jdbcutil" );
		ds.setPassword( "jdbcutil" );
		
		Connection connection = ds.getConnection();
		connection.close();
	}

	@Test
	public void testPoolDataSource() throws Exception
	{
		MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
		ds.setUrl( "jdbc:mysql://localhost/jdbcutil" );
		ds.setUser( "jdbcutil" );
		ds.setPassword( "jdbcutil" );
		ds.getConnection();
		
		Connection connection = ds.getConnection();
		connection.close();
	}

	@Test
	public void testname() throws Exception
	{
	}
}
