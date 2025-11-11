/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 *
 * @author Friedrich Schäuffelhut
 *
 */
public interface StatementInParameter extends StatementParameter
{
    /**
     * Configures one or more statement in parameters beginning at position
     * {code index}. This method is called after {@code modify()} and before the
     * statement is executed. This method should configure the statement
     * paremeter with an appropriate value (usually by using
     * Statement.setObject() or friends)
     *
     * @param stmt
     * @param index
     * @return number of filled in place holders (amount by which {@code index}
     *         should be advanced)
     * @throws SQLException
     */
    int configure(PreparedStatement stmt, int index) throws SQLException;
}
