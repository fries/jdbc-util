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

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import de.schaeuffelhut.jdbc.JdbcUtil;

public class TxnUtil
{
    public final static Logger logger = Logger.getLogger(TxnUtil.class);

    
    private TxnUtil()
    {
    }

    private static ConnectionProvider defaultConnectionProvider = null;

	public final static void initDefaultConnectionProvider()
	{
		final Properties properties = JdbcUtilProperties.findProperties();
		
		final String connectionProviderClassName = properties.getProperty( ConnectionProvider.PROP_CONN_PROVIDER );
		
		if ( connectionProviderClassName == null )
		{
			logger.debug( ConnectionProvider.PROP_CONN_PROVIDER + " undefined, using DefaultConnectionProvider");
			defaultConnectionProvider = new DefaultConnectionProvider( properties );
		}
		else
		{
			logger.debug( String.format( "%s=%s", ConnectionProvider.PROP_CONN_PROVIDER, connectionProviderClassName ) );
			try{
				logger.debug( String.format( "creating new instance via constructor %s(Properties prop)", connectionProviderClassName ) );
				Class<?> clazz = Class.forName( connectionProviderClassName );
				Constructor<?> constructor = clazz.getConstructor( Properties.class );
				defaultConnectionProvider = (ConnectionProvider)constructor.newInstance( properties );
			} catch (Exception e) {
				throw new RuntimeException( e );
			}
		}
	}

	public final static ConnectionProvider getDefaultConnectionProvider()
	{
		if ( defaultConnectionProvider== null )
			initDefaultConnectionProvider();
		return defaultConnectionProvider;
	}

	public final static void setDefaultConnectionProvider(ConnectionProvider connectionProvider)
	{
		defaultConnectionProvider = connectionProvider;
	}

	// XXX DON'T USE
//    public final static <T> T execute(Transactional<T> transactional)
//    {
//    	return execute( getDefaultConnectionProvider(), transactional );
//    }
    
	public final static <T> T execute(DataSource dataSource, Transactional<T> transactional)
    {
    	Connection connection = null;
        try 
        {
			connection = dataSource.getConnection();
            return execute( connection, transactional );
        }
        catch (RuntimeException e)
        {
        	throw e;
        }
        catch (SQLException e)
        {
        	throw new RuntimeException( e );
		}
        finally
        {
            JdbcUtil.closeQuietly( connection );
        }
    }

	
	// XXX DON'T USE
    public final static <T> T execute(
    		ConnectionProvider connectionProvider,
    		Transactional<T> transactional
    ){
    	Connection connection = null;
        try 
        {
			connection = open( connectionProvider );
            return execute( connection, transactional );
        }
        finally
        {
        	if ( connection != null )
				close( connectionProvider, connection );
        }
    }

	final static Connection open(ConnectionProvider connectionProvider)
	{
		if ( logger.isTraceEnabled() )
			logger.trace( "opening connection" );

		try
		{
			return connectionProvider.open();
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RuntimeException( e );
		}
	}

	final static void close(
			ConnectionProvider connectionProvider,
			Connection connection
	) {
		if ( logger.isTraceEnabled() )
			logger.trace( "closing connection" );

		try
     	{
			connectionProvider.close( connection );
     	}
     	catch (RuntimeException e)
     	{
		throw e;
         }
		catch (Exception e)
		{
			throw new RuntimeException( e );
		}
	}

	public final static <T> T execute(
			Connection connection,
			Transactional<T> transactional
	) {
		Boolean autoCommit = null;
		boolean commited = false;
		try
		{
		    autoCommit = connection.getAutoCommit();
		    connection.setAutoCommit( false );
		    
		    if (logger.isTraceEnabled())
		        logger.trace("txn invoking: " + transactional);
		    T result = transactional.run(new TxnContext( connection ) );
		    
		    if (logger.isTraceEnabled())
		        logger.trace("txn commiting: " + transactional);
		    if ( !connection.getAutoCommit() )
		    	connection.commit();
		    
		    commited = true;
		    return result;
		}
        catch (RuntimeException e)
        {
        	throw e;
        }
		catch(Exception e)
		{
			throw new RuntimeException( e );
		}
		finally
		{
		    if ( !commited )
		        JdbcUtil.rollbackQuietly(
		        		connection, "txn rollback: " +transactional.toString() );
		    if ( autoCommit != null)
		    	JdbcUtil.setAutoCommitQuietly( connection, autoCommit );
		}
	}
	
	public final static <T> T executeWithoutTxn(
			Connection connection,
			Transactional<T> transactional
	) {
		try
		{
			return transactional.run( new TxnContext( connection ) );
		}
		catch (Exception e)
		{
			throw new RuntimeException( e );
		}
	}
	
	/*
	 * Thread Local Lazy Context 
	 */
	
	private final static ThreadLocal<LazyTxnContext> threadLocalTxnContext = new ThreadLocal<LazyTxnContext>();
	
	public final static void executeWithThreadLocalContext( Runnable transactional )
	{
		executeWithThreadLocalContext( getDefaultConnectionProvider(), transactional );
	}

	public final static void executeWithThreadLocalContext( ConnectionProvider connectionProvider, Runnable transactional )
	{
		if ( threadLocalTxnContext.get() != null )
			throw new IllegalStateException( "Already executing a thread local transaction!" );
		
		final LazyTxnContext lazyTxnContext = new LazyTxnContext( connectionProvider );
		boolean commited = false;
		try
		{
			threadLocalTxnContext.set( lazyTxnContext );
			
		    if (logger.isTraceEnabled())
		        logger.trace( "txn invoking: " + transactional );

		    transactional.run();

		    if (logger.isTraceEnabled())
		        logger.trace("txn commiting: " + transactional);
		    
		    if ( lazyTxnContext.hasConnection() && !lazyTxnContext.getConnection().getAutoCommit() )
		    	lazyTxnContext.getConnection().commit();
		    
		    commited = true;
		}
		catch (SQLException e)
		{
			throw new RuntimeException( e );
		}
		finally
		{
		    if ( !commited && lazyTxnContext.hasConnection() )
		        JdbcUtil.rollbackQuietly(
		        		lazyTxnContext.getConnection(), "txn rollback: " +transactional.toString()
		        );
		    
			threadLocalTxnContext.remove();
			lazyTxnContext.close();
		}
	}
	
	public final static LazyTxnContext getThreadLocalTxnContext()
	{
		LazyTxnContext lazyTxnContext = threadLocalTxnContext.get();
		if ( lazyTxnContext == null )
			throw new IllegalStateException( "Not executing a thread local transaction" );
		return lazyTxnContext;
	}
	
	public final static Connection getThreadLocalConnection()
	{
		return getThreadLocalTxnContext().getConnection();
	}
}
