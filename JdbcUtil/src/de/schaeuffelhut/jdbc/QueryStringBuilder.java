package de.schaeuffelhut.jdbc;

import java.util.ArrayList;

public final class QueryStringBuilder
{
	private StringBuilder m_query = new StringBuilder();
	private ArrayList<IfcStatementInParameter> m_parameters = new ArrayList<IfcStatementInParameter>();
	
	public final void append(String q, IfcStatementInParameter... params)
	{
		m_query.append( q );
		if ( params != null )
			for( IfcStatementInParameter p : params )
				m_parameters.add( p );
	}
	
	public final String getQueryString()
	{
		return m_query.toString();
	}
	
	public final IfcStatementInParameter[] getParameters()
	{
		return m_parameters.toArray( new IfcStatementInParameter[m_parameters.size()] );
	}
}
