/**
 * (C) Copyright 2007 M.Sc. Friedrich Schäuffelhut
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * $Revison$
 * $Author$
 * $Date$
 */
package de.schaeuffelhut.jdbc.xx;

import java.util.Arrays;
import java.util.Collection;

import de.schaeuffelhut.jdbc.txn.Transactional;
import de.schaeuffelhut.jdbc.txn.TxnContext;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class CombinedTransactional implements Transactional<Object[]>
{
	final Collection<Transactional<?>> transactionals;
	
	public CombinedTransactional(Transactional<?>... transactionals)
	{
		this( Arrays.asList( transactionals ) );
	}

	public CombinedTransactional(Collection<Transactional<?>> transactionals)
	{
		this.transactionals = transactionals;
	}
	
	public final Object[] run(TxnContext context) throws Exception
	{
		Object[] results = new Object[ transactionals.size() ];
		int i = 0;
		for(Transactional<?> t : transactionals )
			results[i++] = t.run( context );
		return results;
	}
}
