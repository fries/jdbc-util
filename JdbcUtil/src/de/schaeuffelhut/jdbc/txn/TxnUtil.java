package de.schaeuffelhut.jdbc.txn;

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
		Properties properties = JdbcUtilProperties.findProperties();
		
		/*TODO: choose defaultConnectionProvider 
		 *      by property "jdbcutil.connection-provider"
		 */
		
		defaultConnectionProvider = new DefaultConnectionProvider( properties );
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

    public final static <T> T execute(Transactional<T> transactional)
    {
    	return execute( getDefaultConnectionProvider(), transactional );
    }
    
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

	private final static Connection open(ConnectionProvider connectionProvider)
	{
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

	private final static void close(
			ConnectionProvider connectionProvider,
			Connection connection
	) {
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
		        		connection, "txn commit: " +transactional.toString() );
		    if ( autoCommit != null)
		    	JdbcUtil.setAutoCommitQuietly( connection, autoCommit );
		}
	}
}
