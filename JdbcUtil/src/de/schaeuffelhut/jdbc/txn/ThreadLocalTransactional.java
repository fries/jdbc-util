package de.schaeuffelhut.jdbc.txn;

import java.sql.Connection;

public abstract class ThreadLocalTransactional<T> implements Transactional<T>
{
	private final static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<Connection>();
		
	public static Connection getConnection()
	{
		if ( threadLocalConnection.get() == null )
			throw new RuntimeException( "no thread local connection, not in transaction" );
		return threadLocalConnection.get();
	}


	public T run(TxnContext context) throws Exception
	{
		threadLocalConnection.set( context.connection );
		try
		{
			return run();
		}
		finally
		{
			threadLocalConnection.remove();
		}
	}

	protected abstract T run() throws Exception;
}
