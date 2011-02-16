package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatementProperties
{
	public final static IfcStatementProperty<?>[] list(IfcStatementProperty<?>... ifcStatementProperties )
	{
		return ifcStatementProperties;
	}
	
	public final static <T> IfcStatementProperty<T> GENERATED_KEY(IfcResultType<T> resultType)
	{
		return new GeneratedKeyStatementProperty<T>( resultType );
	}

	public final static <T> IfcStatementProperty<ArrayList<T>> GENERATED_KEYS(IfcResultType<T> resultType)
	{
		return new GeneratedKeysStatementProperty<T>( resultType );
	}
}

final class GeneratedKeyStatementProperty<T> implements IfcStatementProperty<T>
{
	private static final long serialVersionUID = -2615185114014138675L;

	private final IfcResultType<T> resultType;

	GeneratedKeyStatementProperty(IfcResultType<T> resultType)
	{
		this.resultType = resultType;
	}

	public final T get(PreparedStatement stmt) throws SQLException {

		ResultSet generatedKeys = stmt.getGeneratedKeys();
		try
		{
			return ResultSetReaders.readScalar(resultType).readResult( generatedKeys );
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public final String modify(String sql) {
		return sql;
	}
}

final class GeneratedKeysStatementProperty<T> implements IfcStatementProperty<ArrayList<T>>
{
	private static final long serialVersionUID = -2615185114014138675L;

	private final IfcResultType<T> resultType;

	GeneratedKeysStatementProperty(IfcResultType<T> resultType)
	{
		this.resultType = resultType;
	}

	public final ArrayList<T> get(PreparedStatement stmt) throws SQLException 
	{
		ResultSet generatedKeys = stmt.getGeneratedKeys();
		try
		{
			ArrayList<T> results = new ArrayList<T>();
			ResultSetReaders.readScalars(resultType).readResults( results, generatedKeys );
			return results;
		} catch (SQLException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public final String modify(String sql) {
		return sql;
	}
}
