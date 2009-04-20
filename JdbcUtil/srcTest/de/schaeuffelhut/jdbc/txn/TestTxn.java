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
package de.schaeuffelhut.jdbc.txn;

import java.io.File;
import java.sql.Connection;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public class TestTxn
{
	@BeforeClass
	public static void setup()
	{
		System.setProperty(
				JdbcUtilProperties.SYSPROP_JDBCUTIL_PROPERTIES,
				new File("srcTest").getAbsolutePath()+"/de/schaeuffelhut/jdbc/txn/jdbcutil.properties"
		);
	}
	
	@Test
	public void testDefaultConnectionProvider() throws Exception
	{
		DefaultConnectionProvider connectionProvider =
			new DefaultConnectionProvider();
		
		Connection conn = connectionProvider.open();
		connectionProvider.close( conn );
	}
	
	@Test
	public void testTxnUtil() throws Exception
	{
		TxnUtil.execute( new VoidTransactional() {
			public Void run(TxnContext context) throws Exception
			{
				System.err.println( context.connection.getMetaData().getDatabaseProductName() );
				return null;
			}
		});
	}
}
