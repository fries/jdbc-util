/*
 * Copyright (c) 2009-2025 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */
package de.schaeuffelhut.jdbc;

/**
 * Marker interface for enum types that are stored in the database using a custom key
 * of type {@code T}.
 *
 * <p>Implementing enums must provide a unique key via {@link #getKey()}.  This key
 * is used to safely map database values back to enum instances via
 * {@link EnumByKeyResultType}.</p>
 *
 * <p><strong>Used with:</strong> {@code EnumByKeyResultType} — automatically converts
 * a database key (e.g., {@code String}, {@code Integer}) to the correct enum value,
 * or returns {@code null} if the database value is {@code NULL}.</p>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * public enum UserRole implements EnumKey<String> {
 *     ADMIN("admin"),
 *     USER("user"),
 *     GUEST("guest");
 *
 *     private final String key;
 *     UserRole(String key) { this.key = key; }
 *     public String getKey() { return key; }
 * }
 *
 * // Automatically works with ResultSetMappers:
 * ResultType<UserRole> ROLE = ResultType.enumByKey(UserRole.class, ResultType.STRING);
 *
 * ResultSetMapper<User> mapper = ResultSetMappers.object(
 *     User::new,
 *     ResultType.LONG,      // id
 *     ROLE                  // role (from DB string)
 * );
 * }</pre>
 *
 * <p><strong>Why not {@code ordinal()} or {@code name()}?</strong> These are fragile.
 * Using an explicit key decouples the enum from declaration order and database schema.</p>
 *
 * @param <T> the type of the database key
 *
 * @see EnumByKeyResultType
 * @see ResultTypes#enumByKey(Class, ResultType)
 * @author Friedrich Schäuffelhut
 * @since 2018-06-01
 */
public interface EnumKey<T> {

    /**
     * Returns the key used to represent this enum value in the database.
     *
     * @return the database key (unique within the enum type)
     */
    T getKey();
}
