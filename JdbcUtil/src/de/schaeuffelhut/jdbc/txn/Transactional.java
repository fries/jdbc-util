package de.schaeuffelhut.jdbc.txn;

public interface Transactional<T>
{
    public abstract T run(TxnContext context) throws Exception;
}
