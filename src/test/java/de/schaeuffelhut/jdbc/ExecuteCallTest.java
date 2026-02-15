/*
 * Copyright (c) 2026 the JdbcUtil authors
 *
 * SPDX-License-Identifier: MIT
 */

package de.schaeuffelhut.jdbc;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static de.schaeuffelhut.jdbc.StatementParameters.String;
import static org.assertj.core.api.Assertions.assertThat;

class ExecuteCallTest
{
    @Test
    void executeCallWithInAndOutParameters()
    {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
            "CREATE ALIAS PROCESS_STRING FOR 'de.schaeuffelhut.jdbc.StoredProcedures.processString'"
        );

        try
        {
            Object[] results = statementUtil.executeCall(
                "{? = CALL PROCESS_STRING(?)}",
                StatementOutParameters.String,
                String( "test" )
            );

            assertThat( results ).hasSize( 1 );
            assertThat( results[0] ).isEqualTo( "TEST" );
        }
        finally
        {
            statementUtil.execute("DROP ALIAS PROCESS_STRING");
        }
    }

    @Test
    void executeCallWithUUIDParameter() {
        StatementUtil statementUtil = new H2StatementUtil();
        statementUtil.execute(
            "CREATE ALIAS ECHO_UUID FOR 'de.schaeuffelhut.jdbc.StoredProcedures.echoUUID'"
        );

        try {
            var inUUID = UUID.randomUUID();
            var outParam = StatementOutParameters.UUID;
            Object[] results = statementUtil.executeCall(
                "{? = CALL ECHO_UUID(?)}",
                outParam,
                ExtraStatementParameters.UUID(inUUID)
            );

            assertThat(results).hasSize(1);
            assertThat(results[0]).isEqualTo(inUUID);
        } finally {
            statementUtil.execute("DROP ALIAS ECHO_UUID");
        }
    }
}
