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
package de.schaeuffelhut.jdbc.txn;

import java.sql.Connection;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface ConnectionProvider
{
	static final String PROP_CONN_PROVIDER = "jdbcutil.connection.provider";
	public static final String PROP_CONN_PROVIDER_PREFIX = "jdbcutil.connection.provider.";

	public abstract Connection open() throws Exception;
	public abstract void close(Connection connection) throws Exception;
}
