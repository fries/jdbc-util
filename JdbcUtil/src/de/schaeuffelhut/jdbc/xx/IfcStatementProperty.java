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
package de.schaeuffelhut.jdbc.xx;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.schaeuffelhut.jdbc.IfcStatementParameter;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface IfcStatementProperty<T> extends IfcStatementParameter
{
	public T get(PreparedStatement stmt) throws SQLException;
}
