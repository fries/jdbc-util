/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2StatementUtil extends AbstractStatementUtil
{
    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection( JDBC_URL, USER, PASSWORD );
    }

    @Override
    protected <R, E extends Exception> R execute(Transactional<R, E> transactional, String task, String sql)
    {
        try (Connection connection = getConnection())
        {
            return transactional.execute( connection );
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    protected RuntimeException translate(String task, String sql, SQLException e)
    {
        return new RuntimeException( e );
    }
}
