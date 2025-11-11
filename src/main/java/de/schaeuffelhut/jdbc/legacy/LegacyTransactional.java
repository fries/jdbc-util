/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc.legacy;

import java.sql.Connection;

@Deprecated
public interface LegacyTransactional<T>
{
    T run(Connection connection) throws Exception;
}
