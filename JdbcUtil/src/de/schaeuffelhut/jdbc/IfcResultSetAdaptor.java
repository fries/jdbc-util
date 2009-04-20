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
 * @author M.Sc. Friedrich Schäuffelhut
 *
 * @param <T>
 * @param <V>
 */
public interface IfcResultSetAdaptor<T>
{
	public abstract int adapt(T t, ResultSet resultSet, int index) throws SQLException;
}