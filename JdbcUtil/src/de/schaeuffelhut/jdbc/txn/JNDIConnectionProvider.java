package de.schaeuffelhut.jdbc.txn;

import java.sql.Connection;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class JNDIConnectionProvider implements ConnectionProvider
{
	final static Logger logger = Logger.getLogger( JNDIConnectionProvider.class );
	
	private static final String PROP_JNDI_PATH = PROP_CONN_PROVIDER_PREFIX + "jndi-path";
	
	private DataSource	ds;

	public JNDIConnectionProvider(Properties properties) throws NamingException
	{
		if ( !properties.containsKey( PROP_JNDI_PATH ) )
    		logger.warn( "missing property: " + PROP_JNDI_PATH );


		String jndiPath = properties.getProperty( PROP_JNDI_PATH );
		
		logger.debug( String.format( "%s{%s}=%s", JdbcUtilProperties.BASE_NAME, PROP_JNDI_PATH,  jndiPath) );
		
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
