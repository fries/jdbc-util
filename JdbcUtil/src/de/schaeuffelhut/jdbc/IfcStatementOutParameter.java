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

import java.sql.CallableStatement;
import java.sql.SQLException;



/**
 * @author Friedrich Schäuffelhut
 *
 */
public interface IfcStatementOutParameter<T> extends IfcStatementParameter
{
/**
	 * Configures one or more statement out parameters at position {code index}. This
	 * method is called after {@code modify()} and before the statement is
	 * executed. This method should
	 * register the values type by using
	 * {@code CallableStatement.registerOutParameter().
	 * 
	 * @param stmt
	 * @param index
	 * @return number of filled in place holders (amount by which {@code index}
	 *         should be advanced)
	 * @throws SQLException
	 */

	/**
	 * Configures statement parameter at position {code index}.
	 * @param stmt
	 * @param index
	 * @return number of filled in place holders
	 * @throws SQLException 
	 */
	public abstract int configure(CallableStatement stmt, int index) throws SQLException;
	
	public abstract T readValue(CallableStatement stmt, int index) throws SQLException;
}
