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



/**
 * 
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface IfcStatementInParameter extends IfcStatementParameter
{
	/**
	 * Configures one or more statement in parameters beginning at position
	 * {code index}. This method is called after {@code modify()} and before the
	 * statement is executed. This method should configure the statement
	 * paremeter with an appropriate value (usually by using
	 * Statement.setObject() or friends)
	 * 
	 * @param stmt
	 * @param index
	 * @return number of filled in place holders (amount by which {@code index}
	 *         should be advanced)
	 * @throws SQLException
	 */
	public abstract int configure(PreparedStatement stmt, int index) throws SQLException;
}
