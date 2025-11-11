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
 */
public interface StatementProperty<T> extends StatementParameter
{
    T get(PreparedStatement stmt) throws SQLException;
}
