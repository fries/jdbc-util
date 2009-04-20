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
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
final class BoundValue<T> implements IfcStatementInParameter
{
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
