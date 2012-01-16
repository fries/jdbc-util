package de.schaeuffelhut.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class StatementOutParameters
{
	private StatementOutParameters() {}
	
	public final static IfcStatementOutParameter<Integer> Integer = new IntegerOutParameter();
	
}

final class IntegerOutParameter implements IfcStatementOutParameter<Integer>
{
	private static final long serialVersionUID = 1349896276470400623L;
	
	public int configure(CallableStatement stmt, int index) throws SQLException {
		stmt.registerOutParameter(index, Types.INTEGER );
		return 1;
	}
	
	public Integer readValue(CallableStatement stmt, int index) throws SQLException {
		int value = stmt.getInt( index );
		return stmt.wasNull() ? null : value;
	}
	
	public String modify(String sql) {
		return sql;
	}
}
