/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Friedrich Schäuffelhut
 *
 */
public interface StatementInParameterType<T>
{
    /**
     * @param sql the SQL query to be modified
     * @param value TODO
     * @return the modified SQL query
     */
    String modify(String sql, T value);

    /**
     * Configures statement parameter at position {code index} with teh given {code value}.
     * @param stmt
     * @param pos
     * @param value
     * @return number of filled in placeholders
     * @throws SQLException
     */
    int configure(PreparedStatement stmt, int pos, T value) throws SQLException;
}
