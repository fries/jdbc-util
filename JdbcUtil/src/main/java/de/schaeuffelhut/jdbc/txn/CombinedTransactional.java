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
package de.schaeuffelhut.jdbc.txn;

import java.util.Collection;


public final class CombinedTransactional implements Transactional<Object[]>
{
	final Transactional<?>[] transactionals;
	
	public CombinedTransactional(Collection<?> transactionals)
	{
		this( transactionals.toArray( new Transactional<?>[transactionals.size()] ) );
	}
	
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
