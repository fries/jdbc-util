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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface IfcStatementInParameterType<T> extends Serializable
{
	/**
	 * @param sql the SQL query to be modified
	 * @param value TODO
	 * @return the modified SQL query
	 */
	public abstract String modify(String sql, T value);

	/**
	 * Configures statement parameter at position {code index} with teh given {code value}.
	 * @param stmt
	 * @param pos
	 * @param value
	 * @return number of filled in place holders
	 * @throws SQLException 
	 */
	public abstract int configure(PreparedStatement stmt, int pos, T value) throws SQLException;
}
