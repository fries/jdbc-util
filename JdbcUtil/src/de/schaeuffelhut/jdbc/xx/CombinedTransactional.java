/**
 * Copyright 2009 Friedrich Schäuffelhut
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package de.schaeuffelhut.jdbc.xx;

import java.util.Arrays;
import java.util.Collection;

import de.schaeuffelhut.jdbc.txn.Transactional;
import de.schaeuffelhut.jdbc.txn.TxnContext;

/**
 * @author Friedrich Schäuffelhut
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
