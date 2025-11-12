/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

/**
 * Marker interface for enum types that are stored in the database as {@code INTEGER}
 * using a custom key.
 *
 * <p>Implementing enums must provide a unique {@code int} key via {@link #getKey()}.
 * This key is used for safe binding to {@link java.sql.PreparedStatement#setInt(int, int)}
 * and avoids the pitfalls of {@link Enum#ordinal()} (which is fragile across
 * refactorings).</p>
 *
 * <p><strong>Used with:</strong> {@code EnumByIntKeyParameterType} — automatically
 * binds the enum's key or {@code NULL} if the value is {@code null}.</p>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * public enum UserStatus implements EnumIntKey {
 *     ACTIVE(1),
 *     INACTIVE(2),
 *     SUSPENDED(3);
 *
 *     private final int key;
 *     UserStatus(int key) { this.key = key; }
 *     public int getKey() { return key; }
 * }
 *
 * // Automatically works with StatementUtil:
 * stmtUtil.execute(
 *     "UPDATE users SET status = ? WHERE id = ?",
 *     StatementParameters.enumIntKey(UserStatus.ACTIVE),
 *     StatementParameters.Long(123L)
 * );
 * }</pre>
 *
 * <p><strong>Why not {@code ordinal()}?</strong> Ordinal is tied to declaration
 * order and breaks if enums are reordered.  Using an explicit key is safe and
 * database-friendly.</p>
 *
 * @see EnumByIntKeyParameterType
 * @see ResultTypes#enumByIntKey(Class)
 * @see StatementParameters#EnumByIntKey(Enum)
 * @author Friedrich Schäuffelhut
 * @since 2018-06-01
 */
public interface EnumIntKey {

    /**
     * Returns the integer key used to represent this enum value in the database.
     *
     * @return the database key (unique within the enum type)
     */
    int getKey();
}