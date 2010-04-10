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
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @author Friedrich Schäuffelhut
 *
 */
final class BoundValue<T> implements IfcStatementInParameter
{
	private static final long	serialVersionUID	= 1324455535658426764L;

	public final static Logger logger = Logger.getLogger( StatementParameters.class );
	
	BoundValue(IfcStatementInParameterType<T> parameter, T value)
	{
		this.parameter = parameter;
		this.value = value;
	}
	
	final IfcStatementInParameterType<T> parameter;
	final T value;
	
	/* (non-Javadoc)
	 * @see de.schaeuffelhut.jdbc.StatementParameter#modify(java.lang.String)
	 */
	public final String modify(String sql)
	{
		return parameter.modify( sql, value );
	}
	
	/* (non-Javadoc)
	 * @see de.schaeuffelhut.jdbc.StatementParameter#configure(java.sql.PreparedStatement, int)
	 */
	public final int configure(PreparedStatement stmt, int index) throws SQLException
	{
		if ( logger.isTraceEnabled() )
			if ( value == null )
				logger.trace( String.format("setting param %d to null", index ) );
			else
				logger.trace( String.format(
						"setting param %d (%s) = %s",
						index, value.getClass().getSimpleName(), value ) );
			
		return parameter.configure( stmt, index, value );
	}
}
