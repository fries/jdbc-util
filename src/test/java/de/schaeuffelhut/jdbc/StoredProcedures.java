/*
 * Copyright (c) 2026 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

/**
 * A utility class to hold static methods that can be aliased as stored procedures in H2 for testing purposes.
 */
public class StoredProcedures {

    /**
     * A simple "stored procedure" that converts a string to uppercase.
     * @param input the string to process.
     * @return the uppercase version of the input string.
     */
    public static String processString(String input) {
        return input.toUpperCase();
    }

    /**
     * A simple "stored procedure" that echoes a UUID.
     * @param input the UUID to echo.
     * @return the same UUID.
     */
    public static java.util.UUID echoUUID(java.util.UUID input) {
        return input;
    }
}
