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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class JdbcUtilProperties
{
	private final static Logger logger = Logger.getLogger( JdbcUtilProperties.class );
	
	final static String SYSPROP_JDBCUTIL_PROPERTIES = "jdbcutil.properties";
	final static String BASE_NAME = "/connection.properties";

	private JdbcUtilProperties()
	{
	}

	final static Properties findProperties()
	{
		logger.debug( "locating connection properties for jdbc util" );
		try
		{
			final Properties properties = new Properties();
			
			logger.debug( String.format(
					"checking system property '%s' for property file location",
					SYSPROP_JDBCUTIL_PROPERTIES
			));
			
			String propertyFilename = System.getProperty(
					SYSPROP_JDBCUTIL_PROPERTIES );
			
			if ( propertyFilename != null )
			{
				logger.debug( String.format( 
						"system property '%s' refers to '%s'",
						SYSPROP_JDBCUTIL_PROPERTIES,
						propertyFilename
				));
				logger.debug( String.format( 
						"loading property file from '%s' ",
						propertyFilename
				));
				properties.load( new FileInputStream(
						propertyFilename ) );
			}
			else
			{
				logger.debug( String.format( 
						"system property '%s' is not set",
						SYSPROP_JDBCUTIL_PROPERTIES,
						propertyFilename
				));
				logger.debug( String.format(
						"loading property file via class loader from '%s'",
						BASE_NAME
				));
				InputStream is = JdbcUtilProperties.class.getResourceAsStream(
						BASE_NAME );
				if ( is == null )
				{
					throw new FileNotFoundException(
							"property file not found: " + JdbcUtilProperties.BASE_NAME ) ;
				}
				properties.load( is );
			}

			logger.debug( "connection properties for jdbc util loaded" );
			
			return properties;
		}
		catch (Exception e)
		{
			throw new RuntimeException( e );
		}
	}
}
