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





/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface IfcStatementParameter
{
	/**
	 * Modify the given SQL query string. This method is called before
	 * configure(). 
	 *  
	 * @param sql the SQL query to be modified
	 * @return the modified SQL query
	 */
	public abstract String modify(String sql);
}
