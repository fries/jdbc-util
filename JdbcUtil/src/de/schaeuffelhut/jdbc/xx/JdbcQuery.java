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

import java.sql.ResultSet;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public abstract class JdbcQuery
{
	// execute selects
	abstract ResultSet execute(Object...objects );
}
