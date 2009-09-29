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
