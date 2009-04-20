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

import java.sql.Connection;
import java.sql.Driver;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class DefaultConnectionProvider implements ConnectionProvider
{
	final static Logger logger = getLogger(DefaultConnectionProvider.class);


	private static final String PROP_CONN_INFO_PREFIX = "jdbcutil.connection.info.";

	private static final String PROP_CONN_INFO_PASSWORD = PROP_CONN_INFO_PREFIX + "password";

	private static final String PROP_CONN_INFO_USER = PROP_CONN_INFO_PREFIX + "user";

	private static final String PROP_CONN_URL = "jdbcutil.connection.url";

	private static final String PROP_DRIVER = "jdbcutil.jdbcdriver";
    
    
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
		
		logger.debug( String.format( "%s{%s}=%s", JdbcUtilProperties.BASE_NAME, PROP_DRIVER,    driverClassName ) );
		logger.debug( String.format( "%s{%s}=%s", JdbcUtilProperties.BASE_NAME, PROP_CONN_URL , connectionUrl ) );

        driver = loadDriver();
        connectionInfo = createConnectionInfo();
	}
        
	private void checkProperty(String key)
	{
		if ( !jdbcUtilProperties.containsKey( key ) )
    		logger.warn( "missing property: " + key );
	}

	private final Properties createConnectionInfo()
	{
		logger.trace( String.format(
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
		        if ( logger.isTraceEnabled() )
		        	logger.trace( String.format(
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
			logger.debug( "loading jdbc driver: " + driverClassName );
			return ((Class<Driver>)Class.forName( driverClassName )).newInstance();
		}
		catch (Exception err)
		{
			throw new RuntimeException( err );
		}
	}


	public final Connection open() throws Exception
	{
		return driver.connect( connectionUrl, connectionInfo );
	}
	
	public final void close(Connection connection) throws Exception
	{
		if ( connection != null )
			connection.close();
	}
}
