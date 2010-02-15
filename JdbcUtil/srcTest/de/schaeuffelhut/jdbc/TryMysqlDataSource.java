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

import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


/**
 * @author Friedrich Schäuffelhut
 *
 */
public class TryMysqlDataSource
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
