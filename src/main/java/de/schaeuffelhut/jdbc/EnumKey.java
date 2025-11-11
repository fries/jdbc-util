/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

// TODO: generalize; any class which implements this interface can be persisted, just reading needs mapping
public interface EnumKey<T>
{
    T getKey();
}
