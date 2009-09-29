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





/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface IfcStatementParameter extends Serializable
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
