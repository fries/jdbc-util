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

import java.sql.CallableStatement;
import java.sql.SQLException;



/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface IfcStatementOutParameter<T> extends IfcStatementParameter
{
/**
	 * Configures one or more statement out parameters at position {code index}. This
	 * method is called after {@code modify()} and before the statement is
	 * executed. This method should
	 * register the values type by using
	 * {@code CallableStatement.registerOutParameter().
	 * 
	 * @param stmt
	 * @param index
	 * @return number of filled in place holders (amount by which {@code index}
	 *         should be advanced)
	 * @throws SQLException
	 */

	/**
	 * Configures statement parameter at position {code index}.
	 * @param stmt
	 * @param index
	 * @return number of filled in place holders
	 * @throws SQLException 
	 */
	public abstract int configure(CallableStatement stmt, int index) throws SQLException;
	
	public abstract T readValue(CallableStatement stmt, int index) throws SQLException;
}
