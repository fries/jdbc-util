/*
 * Copyright (c) 2026 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

class MultiColumnResultTypeTest {

    static class Address {
        final String street;
        final String city;

        Address(String street, String city) {
            this.street = street;
            this.city = city;
        }
    }

    static class AddressResultType implements ResultType<Address> {
        @Override
        public Address getResult(ResultSet resultSet, ColumnIndex index) throws SQLException {
            String street = resultSet.getString(index.next());
            String city = resultSet.getString(index.next());
            if (street == null && city == null) return null;
            return new Address(street, city);
        }

        @Override
        public Class<Address> getResultType() {
            return Address.class;
        }
    }

    @Test
    void multiColumnMapping() {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
                """
                CREATE TABLE person_addresses (
                    id INT PRIMARY KEY,
                    street VARCHAR(255),
                    city VARCHAR(255),
                    name VARCHAR(255)
                );
                INSERT INTO person_addresses (id, street, city, name) VALUES
                    (1, 'Main St', 'Springfield', 'Homer');
                """
        );

        try {
            record Person(Address address, String name) {}

            Person person = statementUtil.selectInto(
                    "SELECT street, city, name FROM person_addresses WHERE id = 1",
                    ResultSetReaders.readOne(),
                    ResultSetMappers.object(Person::new, new AddressResultType(), ResultTypes.String)
            );

            assertThat(person.address.street).isEqualTo("Main St");
            assertThat(person.address.city).isEqualTo("Springfield");
            assertThat(person.name).isEqualTo("Homer");
        } finally {
            statementUtil.execute("DROP TABLE person_addresses");
        }
    }

    @Test
    void multiColumnMappingWithIfNull() {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
                """
                CREATE TABLE person_addresses_null (
                    id INT PRIMARY KEY,
                    street VARCHAR(255),
                    city VARCHAR(255),
                    name VARCHAR(255)
                );
                INSERT INTO person_addresses_null (id, street, city, name) VALUES
                    (1, NULL, NULL, 'Homer');
                """
        );

        try {
            record Person(Address address, String name) {}

            Address defaultAddress = new Address("Default St", "Default City");
            ResultType<Address> addressTypeWithDefault = ResultTypes.ifNull(new AddressResultType(), defaultAddress);

            Person person = statementUtil.selectInto(
                    "SELECT street, city, name FROM person_addresses_null WHERE id = 1",
                    ResultSetReaders.readOne(),
                    ResultSetMappers.object(Person::new, addressTypeWithDefault, ResultTypes.String)
            );

            assertThat(person.address).isSameAs(defaultAddress);
            assertThat(person.name).isEqualTo("Homer");
        } finally {
            statementUtil.execute("DROP TABLE person_addresses_null");
        }
    }

    @Test
    void customResultTypeOnlyImplementingNewMethod() {
        // Verify that a ResultType implementing only the new method still works
        ResultType<String> stringType = new ResultType<String>() {
            @Override
            public String getResult(ResultSet resultSet, ColumnIndex index) throws SQLException {
                return resultSet.getString(index.next());
            }

            @Override
            public Class<String> getResultType() {
                return String.class;
            }
        };

        StatementUtil statementUtil = new H2StatementUtil();
        String result = statementUtil.selectInto(
                "SELECT 'test' FROM (VALUES(1))",
                ResultSetReaders.readOne(),
                ResultSetMappers.scalar(stringType)
        );

        assertThat(result).isEqualTo("test");
    }
}
