/*
 * Copyright (c) 2026 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

/**
 * A stateful pointer to a column index in a {@link java.sql.ResultSet}.
 *
 * <p>Used by {@link ResultType#getResult(java.sql.ResultSet, ColumnIndex)} to support
 * extracting multiple columns while automatically advancing the index.</p>
 *
 * @since 2025-02-15
 */
public interface ColumnIndex {
    /**
     * Creates a new {@link ColumnIndex} starting at the specified index.
     *
     * @param startIndex the 1-based start index
     * @return a new column index pointer
     */
    static ColumnIndex create(int startIndex) {
        return new SimpleColumnIndex(startIndex);
    }
    /**
     * Returns the current 1-based index and increments it.
     *
     * @return the current index
     */
    int next();

    /**
     * Returns the current 1-based index without incrementing it.
     *
     * @return the current index
     */
    int peek();

    /**
     * Creates a copy of this column index.
     *
     * @return a copy of this column index
     */
    ColumnIndex copy();
}


/**
 * A simple implementation of {@link ColumnIndex}.
 */
class SimpleColumnIndex implements ColumnIndex {
    private int index;

    public SimpleColumnIndex(int startIndex) {
        this.index = startIndex;
    }

    @Override
    public int next() {
        return index++;
    }

    @Override
    public int peek() {
        return index;
    }

    @Override
    public ColumnIndex copy() {
        return new SimpleColumnIndex( index );
    }
}
