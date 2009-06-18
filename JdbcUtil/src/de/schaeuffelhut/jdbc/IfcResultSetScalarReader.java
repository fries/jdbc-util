/**
 * 
 */
package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;

/**
 * Reads a single value from a {@link ResultSet}.
 * This may be a single primitive value that maps onto one column or
 * one complex object, that maps onto multiple columns. 
 *  
 * @author fries
 *
 * @param <T>
 */
public interface IfcResultSetScalarReader<T>
{
	/**
	 * Reads one value from the given {@link ResultSet}. Might return null or
	 * throw an expection if the {@link ResultSet} is empty. Might throw a
	 * Exception if the {@link ResultSet} contains more than one row.
	 * 
	 * @param results
	 * @param resultSet
	 * @throws Exception
	 */
	public abstract T readResult( ResultSet resultSet  ) throws Exception;
}