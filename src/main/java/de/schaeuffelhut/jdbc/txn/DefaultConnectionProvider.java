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

import java.sql.Connection;
import java.sql.Driver;
import java.util.Map;
import java.util.Properties;

/**
 * @author Friedrich Schäuffelhut
 *
 */
public class DefaultConnectionProvider implements ConnectionProvider
{
	private static final Logger LOGGER = LoggerFactory.getLogger( DefaultConnectionProvider.class );


	public static final String PROP_CONN_INFO_PREFIX = "jdbcutil.connection.info.";

	public static final String PROP_CONN_INFO_PASSWORD = PROP_CONN_INFO_PREFIX + "password";

	public static final String PROP_CONN_INFO_USER = PROP_CONN_INFO_PREFIX + "user";

	public static final String PROP_CONN_URL = "jdbcutil.connection.url";

	public static final String PROP_DRIVER = "jdbcutil.jdbcdriver";
    
    
    final Properties jdbcUtilProperties;
    
    final String driverClassName;

    final Driver driver;
    
    final String connectionUrl;

    final Properties connectionInfo;

	public DefaultConnectionProvider()
	{
		this( JdbcUtilProperties.findProperties() );
	}
	
	public DefaultConnectionProvider(Properties jdbcUtilProperties)
	{
		this.jdbcUtilProperties = jdbcUtilProperties;
		
        checkProperty( PROP_DRIVER );
        checkProperty( PROP_CONN_URL );
        checkProperty( PROP_CONN_INFO_USER );
        checkProperty( PROP_CONN_INFO_PASSWORD );
		
		driverClassName = jdbcUtilProperties.getProperty( PROP_DRIVER );
		connectionUrl = jdbcUtilProperties.getProperty( PROP_CONN_URL );
		
		LOGGER.debug( String.format( "%s{%s}=%s", JdbcUtilProperties.BASE_NAME, PROP_DRIVER,    driverClassName ) );
		LOGGER.debug( String.format( "%s{%s}=%s", JdbcUtilProperties.BASE_NAME, PROP_CONN_URL , connectionUrl ) );

        driver = loadDriver();
        connectionInfo = createConnectionInfo();
	}
        
	private void checkProperty(String key)
	{
		if ( !jdbcUtilProperties.containsKey( key ) )
    		LOGGER.warn( "missing property: " + key );
	}

	private final Properties createConnectionInfo()
	{
		LOGGER.trace( String.format(
				"constructing connection info properties " +
				"from keys starting with '%s'",
				PROP_CONN_INFO_PREFIX
		));
		Properties connectionInfo = new Properties();
    	for(Map.Entry<Object, Object> property : jdbcUtilProperties.entrySet() )
    	{
			String key = (String)property.getKey();
			if ( key.startsWith( PROP_CONN_INFO_PREFIX ) )
			{
				String subkey = key.substring( PROP_CONN_INFO_PREFIX.length() );
				String value = (String)property.getValue();
				connectionInfo.put( subkey, value);
		        if ( LOGGER.isTraceEnabled() )
		        	LOGGER.trace( String.format(
		        			"connection.info{%s}=%s", subkey, value ) );
			}
    	}
		return connectionInfo;
	}        

	@SuppressWarnings("unchecked")
	private final Driver loadDriver()
	{
		try
		{
			LOGGER.debug( "loading jdbc driver: " + driverClassName );
			return ((Class<Driver>)Class.forName( driverClassName )).newInstance();
		}
		catch (Exception err)
		{
			throw new RuntimeException( err );
		}
	}


	public final Connection open() throws Exception
	{
		Connection connection = driver.connect( connectionUrl, connectionInfo );
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
