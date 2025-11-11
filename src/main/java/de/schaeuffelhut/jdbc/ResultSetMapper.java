/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<T>
{
    default void initialize(ResultSet resultSet) throws SQLException
    {
        // noop
    }

    T map(ResultSet resultSet) throws SQLException;
}
