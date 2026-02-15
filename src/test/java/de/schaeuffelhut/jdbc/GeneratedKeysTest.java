/*
 * Copyright (c) 2026 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class GeneratedKeysTest {

    @Test
    void executeWithGeneratedKeys() {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
            """
            CREATE TABLE employees_gk (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255)
            );
            """
        );

        try {
            Integer generatedId = statementUtil.execute(
                GeneratedKeys.REPORT,
                StatementProperties.GENERATED_KEY(ResultTypes.Integer),
                "INSERT INTO employees_gk (name) VALUES (?)",
                StatementParameters.String("Mallory")
            );

            assertThat(generatedId).isEqualTo(1);

            // Verify the data was actually inserted
            String name = statementUtil.selectInto(
                "SELECT name FROM employees_gk WHERE id = ?",
                ResultSetReaders.readOne(),
                ResultSetMappers.scalar(ResultTypes.String),
                StatementParameters.Integer(generatedId)
            );

            assertThat(name).isEqualTo("Mallory");

        } finally {
            statementUtil.execute("DROP TABLE employees_gk");
        }
    }

    @Test
    void executeBatchWithGeneratedKeys() {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
            """
            CREATE TABLE employees_gk_batch (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255)
            );
            """
        );

        try {
            var names = java.util.List.of("Peter", "Paul", "Mary");
            var params = names.stream()
                .map(name -> new StatementInParameter[]{ StatementParameters.String(name) })
                .toList();

            java.util.List<Integer> generatedIds = statementUtil.executeBatch(
                GeneratedKeys.REPORT,
                StatementProperties.GENERATED_KEYS(ResultTypes.Integer),
                "INSERT INTO employees_gk_batch (name) VALUES (?)",
                params
            );

            assertThat(generatedIds).containsExactly(1, 2, 3);

        } finally {
            statementUtil.execute("DROP TABLE employees_gk_batch");
        }
    }
}
