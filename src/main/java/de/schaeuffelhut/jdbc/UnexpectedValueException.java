/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

public final class UnexpectedValueException extends RuntimeException
{
    public UnexpectedValueException(Object value)
    {
        super( value == null ? "<null>" : "'" + value + "'" );
    }
}
