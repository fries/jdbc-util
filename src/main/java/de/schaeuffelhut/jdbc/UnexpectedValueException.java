/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

/**
 * Exception thrown when an unexpected value is encountered during processing, typically
 * when mapping a database value to an Enum or another specific type where the value
 * does not match any expected cases.
 */
public final class UnexpectedValueException extends RuntimeException
{
    /**
     * Constructs a new {@code UnexpectedValueException} with a detail message that includes
     * the unexpected value.
     *
     * @param value the unexpected value that caused this exception.
     */
    public UnexpectedValueException(Object value)
    {
        super( value == null ? "<null>" : "'" + value + "'" );
    }
}
