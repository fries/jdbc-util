/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.util.ArrayList;

public final class QueryStringBuilder
{
    private StringBuilder m_query = new StringBuilder();
    private ArrayList<StatementInParameter> m_parameters = new ArrayList<StatementInParameter>();

    public final void append(String q, StatementInParameter... params)
    {
        m_query.append( q );
        if (params != null)
            for (StatementInParameter p : params)
                m_parameters.add( p );
    }

    public String getQueryString()
    {
        return m_query.toString();
    }

    public StatementInParameter[] getParameters()
    {
        return m_parameters.toArray( StatementInParameter[]::new );
    }
}
