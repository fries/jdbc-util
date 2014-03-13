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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Friedrich Schäuffelhut
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

	public final static Properties findProperties()
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
