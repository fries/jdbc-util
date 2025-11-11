/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

/**
 * @author Friedrich Schäuffelhut
 *
 */
public interface StatementParameter
{
    /**
     * Modify the given SQL query string. This method is called before
     * configure().
     *
     * @param sql the SQL query to be modified
     * @return the modified SQL query
     */
    String modify(String sql);
}
