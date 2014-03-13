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
package de.schaeuffelhut.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

import de.schaeuffelhut.jdbc.txn.Transactional;
import de.schaeuffelhut.jdbc.txn.TxnContext;


/**
 * @author Friedrich Schäuffelhut
 *
 */
public final class TxnStatementUtil
{
	private TxnStatementUtil(){}

	public final static <T> Transactional<T> selectInto(final String sql, final IfcResultSetScalarReader<T> resultReader, final Collection<IfcStatementInParameter> parameters)
	{
		return selectInto(sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T> Transactional<T> selectInto(final String sql, final IfcResultSetScalarReader<T> resultReader, final IfcStatementInParameter... parameters)
	{
		return new Transactional<T>(){
			public T run(TxnContext context) throws Exception
			{
				return StatementUtil.selectInto( context.getConnection(), sql, resultReader, parameters );
			}
		};
	}

	public final static <T> Transactional<ArrayList<T>> selectInto(final String sql, final IfcResultSetCollectionReader<T> resultReader, final Collection<IfcStatementInParameter> parameters)
	{
		return selectInto(sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T> Transactional<ArrayList<T>> selectInto(final String sql, final IfcResultSetCollectionReader<T> resultReader, final IfcStatementInParameter... parameters)
	{
		return new Transactional<ArrayList<T>>(){
			public ArrayList<T> run(TxnContext context) throws Exception
			{
				return StatementUtil.selectInto( context.getConnection(), sql, resultReader, parameters);
			}
		};
	}

	public final static <T extends Collection<E>,E> Transactional<T> selectInto(final T results, final String sql, final IfcResultSetCollectionReader<E> resultReader, final Collection<IfcStatementInParameter> parameters)
	{
		return selectInto(results, sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T extends Collection<E>,E> Transactional<T> selectInto(final T results, final String sql, final IfcResultSetCollectionReader<E> resultReader, final IfcStatementInParameter... parameters)
	{
		return new Transactional<T>(){
			public T run(TxnContext context) throws Exception
			{
				return StatementUtil.selectInto( results,  context.getConnection(), sql, resultReader, parameters );
			}
		};
	}

	public final static <V,T> Transactional<V> process( final String sql, final IfcResultSetProcessor<V,T> resultSetProcessor, final IfcStatementInParameter... parameters )
	{
		return new Transactional<V>(){
			public V run(TxnContext context) throws Exception
			{
				return StatementUtil.process( context.getConnection(), sql, resultSetProcessor, parameters );
			}
		};
	}

	/*
	 * insert / updates
	 */

	public final static Transactional<Integer> execute(final String sql, final IfcStatementInParameter...parameters)
	{
		return new Transactional<Integer>() {
			public Integer run(TxnContext context) throws Exception
			{
				StatementUtil.execute( 
						context.getConnection(), sql, parameters );
				return null;
			}
		};
	}

	public final static Transactional<Integer> execute(final String sql, final IfcStatementInParameter[]...parameters)
	{
		return new Transactional<Integer>() {
			public Integer run(TxnContext context) throws Exception
			{
				StatementUtil.execute( 
						context.getConnection(), sql, parameters );
				return null;
			}
		};
	}
}
