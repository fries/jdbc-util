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
package de.schaeuffelhut.jdbc.xx;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.schaeuffelhut.jdbc.IfcResultAdaptor;
import de.schaeuffelhut.jdbc.IfcResultType;



public final class ResultAdaptor<T,V> implements IfcResultAdaptor<T> 
{
	final IfcResultType<V> resultType;
	final IfcSetter<T,V> setter;
	
	public ResultAdaptor(IfcResultType<V> resultType, IfcSetter<T, V> setter)
	{
		this.resultType = resultType;
		this.setter = setter;
	}
	
	public final int adapt(T t, ResultSet rs, int i) throws SQLException
	{
		setter.setValue( t, resultType.getResult( rs, i ) );
		return 1;
	}
}