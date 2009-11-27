/**
 * Copyright 2009 Friedrich Schäuffelhut
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package de.schaeuffelhut.jdbc;

import java.io.Serializable;
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
public interface IfcResultSetScalarReader<T> extends Serializable
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