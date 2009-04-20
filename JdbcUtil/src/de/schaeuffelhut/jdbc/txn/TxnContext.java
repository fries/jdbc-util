package de.schaeuffelhut.jdbc.txn;

import java.sql.Connection;

public final class TxnContext
{
    public final Connection connection;

    TxnContext(final Connection connection)
    {
        this.connection = connection;
    }
}