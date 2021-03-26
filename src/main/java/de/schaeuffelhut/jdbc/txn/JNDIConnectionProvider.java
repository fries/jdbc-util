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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;


public class JNDIConnectionProvider implements ConnectionProvider
{
	private static final Logger LOGGER = LoggerFactory.getLogger( JNDIConnectionProvider.class );

	private static final String PROP_JNDI_PATH = PROP_CONN_PROVIDER_PREFIX + "jndi-path";
	
	private DataSource	ds;

	public JNDIConnectionProvider(Properties properties) throws NamingException
	{
		if ( !properties.containsKey( PROP_JNDI_PATH ) )
    		LOGGER.warn( "missing property: " + PROP_JNDI_PATH );


		String jndiPath = properties.getProperty( PROP_JNDI_PATH );
		
		LOGGER.debug( String.format( "%s{%s}=%s", JdbcUtilProperties.BASE_NAME, PROP_JNDI_PATH,  jndiPath) );
		
		InitialContext initialContext = new InitialContext();
		ds = (DataSource)initialContext.lookup( jndiPath );
	}
	
	public final Connection open() throws Exception
	{
		Connection connection = ds.getConnection();
		onOpen( connection );
		return connection;
	}

	protected void onOpen(Connection connection) throws Exception
	{
		// override if desired
	}

	public final void close(Connection connection) throws Exception
	{
		onClose( connection );
		connection.close();
	}
	
	protected void onClose(Connection connection) throws Exception
	{
		// override if desired
	}
}
