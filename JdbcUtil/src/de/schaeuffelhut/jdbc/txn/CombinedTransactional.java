package de.schaeuffelhut.jdbc.txn;


public final class CombinedTransactional implements Transactional<Object[]>
{
	final Transactional<?>[] transactionals;
	
	public CombinedTransactional(Transactional<?>... transactionals)
	{
		this.transactionals = transactionals;
	}

	public final Object[] run(TxnContext context) throws Exception
	{
		Object[] results = new Object[transactionals.length];
		for(int i = 0; i < transactionals.length; i++)
			results[i] = transactionals[i].run( context );
		return results;
	}
}
