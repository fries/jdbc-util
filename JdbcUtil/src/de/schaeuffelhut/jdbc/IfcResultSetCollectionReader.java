/**
 * 
 */
package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.util.Collection;

/**
 * Reads values from a {@link ResultSet} into a {@link Collection}.
 *  
 * @author fries
 *
 * @param <T>
 */
public interface IfcResultSetCollectionReader<T>
{
	/**
	 * Reads values from the given {@link ResultSet} into the given {@link Collection}.
	 * 
	 * @param results
	 * @param resultSet
	 * @throws Exception 
	 */
	public abstract void readResults( Collection<T> results, ResultSet resultSet  ) throws Exception;
}