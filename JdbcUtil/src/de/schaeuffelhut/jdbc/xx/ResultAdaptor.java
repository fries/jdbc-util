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
import java.sql.SQLException;

import de.schaeuffelhut.jdbc.IfcResultSetAdaptor;
import de.schaeuffelhut.jdbc.IfcResultType;



public final class ResultAdaptor<T,V> implements IfcResultSetAdaptor<T> 
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