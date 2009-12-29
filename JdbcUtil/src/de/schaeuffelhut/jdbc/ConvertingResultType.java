package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.schaeuffelhut.jdbc.IfcResultType;

/**
 * A JdbcUtil {@link IfcResultType} which delegates the data retrieval to an
 * other {@link IfcResultType} and then converts to the actual type.
 * 
 * @author M.Sc. Friedrich Schäuffelhut
 * 
 * @param <Tout>
 * @param <Tin>
 */
public abstract class ConvertingResultType<Tout,Tin> implements IfcResultType<Tout>
{
	private static final long	serialVersionUID	= -4995146891207196356L;

	final IfcResultType<Tin> delegate;
	
	public ConvertingResultType(IfcResultType<Tin> delegate)
	{
		this.delegate = delegate;
	}

	@Override
	public Tout getResult(ResultSet resultSet, int index) throws SQLException
	{
		return convert( delegate.getResult( resultSet, index ) );
	}
	
	protected abstract Tout convert(Tin value);
}