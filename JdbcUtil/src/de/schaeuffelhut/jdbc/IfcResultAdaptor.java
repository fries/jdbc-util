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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Transfers a single value from the given {@link ResultSet} into the given
 * {@link Object} of type <code>T</code>.
 * 
 * @author M.Sc. Friedrich Schäuffelhut
 * 
 * @param <T> type of object receiving the result set value. 
 */
public interface IfcResultAdaptor<T>
{
	public abstract int adapt(T t, ResultSet resultSet, int index) throws SQLException;
}