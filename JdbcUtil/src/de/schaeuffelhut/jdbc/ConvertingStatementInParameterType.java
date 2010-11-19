package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public abstract class ConvertingStatementInParameterType<Tout,Tin> implements IfcStatementInParameterType<Tin>
{
	private static final long serialVersionUID = -8671681485814918932L;

	final IfcStatementInParameterType<Tout> delegate;

	public ConvertingStatementInParameterType(IfcStatementInParameterType<Tout> delegate)
	{
		this.delegate = delegate;
	}
	
	public final String modify(String sql, Tin value)
	{
		return delegate.modify( sql, convert( value ) );
	}

	public final int configure(PreparedStatement stmt, int pos, Tin value) throws SQLException
	{
		return delegate.configure(stmt, pos, convert( value ) );
	}

	protected abstract Tout convert(Tin value);
}
