/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;


/**
 * @author Friedrich Schäuffelhut
 */
public interface StatementOutParameter<T> extends StatementParameter
{
    /**
     * Configures one or more statement out parameters at position {code index}. This
     * method is called after {@code modify()} and before the statement is
     * executed. This method should
     * register the values type by using
     * {@code CallableStatement.registerOutParameter().
     *
     * @param stmt
     * @param index
     * @return number of filled in place holders (amount by which {@code index}
     * should be advanced)
     * @throws SQLException
     */
    int configure(CallableStatement stmt, int index) throws SQLException;

    T readValue(CallableStatement stmt, int index) throws SQLException;
}
