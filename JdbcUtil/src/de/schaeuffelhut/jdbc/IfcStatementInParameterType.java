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
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface IfcStatementInParameterType<T> extends Serializable
{
	/**
	 * @param sql the SQL query to be modified
	 * @param value TODO
	 * @return the modified SQL query
	 */
	public abstract String modify(String sql, T value);

	/**
	 * Configures statement parameter at position {code index} with teh given {code value}.
	 * @param stmt
	 * @param pos
	 * @param value
	 * @return number of filled in place holders
	 * @throws SQLException 
	 */
	public abstract int configure(PreparedStatement stmt, int pos, T value) throws SQLException;
}
