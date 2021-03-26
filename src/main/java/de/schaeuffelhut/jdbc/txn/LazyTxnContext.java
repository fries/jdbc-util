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

import de.schaeuffelhut.jdbc.JdbcUtil;

import java.sql.Connection;

public final class LazyTxnContext
{
	private ConnectionProvider	m_connectionProvider;
	private boolean	m_closed = false;
    private Connection	m_connection;

	LazyTxnContext(ConnectionProvider connectionProvider)
    {
		m_connectionProvider = connectionProvider;
    }
    
	public final Connection getConnection()
    {
		if ( m_closed )
			throw new IllegalStateException( "Context has been closed" );
    	if ( m_connection == null )
    	{
			m_connection = TxnUtil.open( m_connectionProvider );
	    	JdbcUtil.setAutoCommitQuietly( m_connection, false );
    	}
    	
    	return m_connection;
    }
	
	final boolean hasConnection()
	{
		if ( m_closed )
			throw new IllegalStateException( "Context has been closed" );
		return m_connection != null;
	}
	
	final void close()
	{
		if ( m_closed )
			throw new IllegalStateException( "Context has been closed" );

		final ConnectionProvider connectionProvider = m_connectionProvider;
		final Connection connection = m_connection;

		m_closed = true;
		m_connectionProvider = null;
		m_connection = null;
		
		if ( connection != null )
		{
			TxnUtil.close( connectionProvider, connection );
		}
	}
}