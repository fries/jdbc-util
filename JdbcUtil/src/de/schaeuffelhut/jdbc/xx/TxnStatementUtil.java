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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import de.schaeuffelhut.jdbc.IfcResultSetCollectionReader;
import de.schaeuffelhut.jdbc.IfcResultSetScalarReader;
import de.schaeuffelhut.jdbc.IfcResultType;
import de.schaeuffelhut.jdbc.IfcStatementInParameter;
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

	public final static <T> Transactional<Void> selectInto(final Collection<T> results, final String sql, final IfcResultSetCollectionReader<T> resultReader, final Collection<IfcStatementInParameter> parameters)
	{
		return selectInto(results, sql, resultReader, parameters.toArray( new IfcStatementInParameter[parameters.size()] ) );
	}
	
	public final static <T> Transactional<Void> selectInto(final Collection<T> results, final String sql, final IfcResultSetCollectionReader<T> resultReader, final IfcStatementInParameter... parameters)
	{
		return new Transactional<Void>(){
			public Void run(TxnContext context) throws Exception
			{
				StatementUtil.selectInto( results,  context.getConnection(), sql, resultReader, parameters );
				return null;
			}
		};
	}

	
	/*
	 * select single row
	 */

	@Deprecated // use selectInto
	public final static <T> Transactional<T> selectIntoScalar(final String sql, final IfcResultType<T> resultType)
	{
		return selectIntoScalar(sql, resultType, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<T> selectIntoScalar(final String sql, final IfcResultType<T> resultType, final IfcStatementInParameter...parameters)
	{
		return new Transactional<T>() {
			public T run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoScalar(
						context.getConnection(), sql, resultType, parameters );
			}
		};
	}

	@Deprecated // use selectInto
	public final static Transactional<Object[]> selectIntoTuple(final String sql, final IfcResultType<?>... resultTypes)
	{
		return selectIntoTuple(sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static Transactional<Object[]> selectIntoTuple(final String sql, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters)
	{
		return new Transactional<Object[]>() {
			public Object[] run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoTuple(
						context.getConnection(), sql, resultTypes, parameters );
			}
		};
	}

	@Deprecated // use selectInto
	public final static Transactional<Map<String,Object>> selectIntoMap(final String sql, final IfcResultType<?>... resultTypes)
	{
		return selectIntoMap(sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static Transactional<Map<String,Object>> selectIntoMap(final String sql, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter... parameters)
	{
		return new Transactional<Map<String,Object>>() {
			public Map<String,Object> run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoMap(
						context.getConnection(), sql, resultTypes, parameters);
			}
		};
	}

	@Deprecated // use selectInto
	public final static <T> Transactional<T> selectIntoObject(final String sql, final Class<T> type, final IfcResultType<?>... resultTypes)
	{
		return selectIntoObject(sql, type, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<T> selectIntoObject(final String sql, final Class<T> type, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters )
	{
		return new Transactional<T>() {
			public T run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoObject(
						context.getConnection(), sql, type, resultTypes, parameters );
			}
		};
	}

	/*
	 * select multiple rows
	 */

	@Deprecated // use selectInto
	public final static <T> Transactional<ArrayList<T>> selectIntoScalars(final String sql, final IfcResultType<T> resultType)
	{
		return selectIntoScalars(sql, resultType, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<ArrayList<T>> selectIntoScalars(final String sql, final IfcResultType<T> resultType, final IfcStatementInParameter... parameters)
	{
		return new Transactional<ArrayList<T>>() {
			public ArrayList<T> run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoScalars(
						context.getConnection(), sql, resultType, parameters );
			}
		};
	}

	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoScalars(final Collection<T> results, final String sql, final IfcResultType<T> resultType)
	{
		return selectIntoScalars(results, sql, resultType, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoScalars(final Collection<T> results, final String sql, final IfcResultType<T> resultType, final IfcStatementInParameter...parameters)
	{
		return new Transactional<Void>() {
			public Void run(TxnContext context) throws Exception
			{
				StatementUtil.selectIntoScalars(
						results, context.getConnection(), sql, resultType, parameters );
				return null;
			}
		};
	}

	@Deprecated // use selectInto
	public final static Transactional<ArrayList<Object[]>> selectIntoTuples(final String sql, final IfcResultType<?>... resultTypes)
	{
		return selectIntoTuples(sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static Transactional<ArrayList<Object[]>> selectIntoTuples(final String sql, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters)
	{
		return new Transactional<ArrayList<Object[]>>() {
			public ArrayList<Object[]> run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoTuples(
						context.getConnection(), sql, resultTypes, parameters );
			}
		};
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoTuples(final Collection<Object[]> results, final String sql, final IfcResultType<?>... resultTypes)
	{
		return selectIntoTuples(results, sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoTuples(final Collection<Object[]> results, final String sql, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters)
	{
		return new Transactional<Void>() {
			public Void run(TxnContext context) throws Exception
			{
				StatementUtil.selectIntoTuples(
						results, context.getConnection(), sql, resultTypes, parameters );
				return null;
			}
		};
	}

	@Deprecated // use selectInto
	public final static Transactional<ArrayList<Map<String,Object>>> selectIntoMaps(final String sql, final IfcResultType<?>... resultTypes)
	{
		return selectIntoMaps(sql, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static Transactional<ArrayList<Map<String,Object>>> selectIntoMaps(final String sql, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters)
	{
		return new Transactional<ArrayList<Map<String,Object>>>() {
			public ArrayList<Map<String,Object>> run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoMaps(
						context.getConnection(), sql, resultTypes, parameters );
			}
		};
	}

	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoMaps(final Collection<Map<String,Object>> results, final String sql, final IfcResultType<?>... resultTypes)
	{
		return selectIntoMaps(results, sql, resultTypes, (IfcStatementInParameter[])null);
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoMaps(final Collection<Map<String,Object>> results, final String sql, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters)
	{
		return new Transactional<Void>() {
			public Void run(TxnContext context) throws Exception
			{
				StatementUtil.selectIntoMaps(
						results, context.getConnection(), sql, resultTypes, parameters );
				return null;
			}
		};
	}

	@Deprecated // use selectInto
	public final static <T> Transactional<ArrayList<T>> selectIntoObjects(final String sql, final Class<T> type, final IfcResultType<?>... resultTypes)
	{
		return selectIntoObjects(sql, type, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<ArrayList<T>> selectIntoObjects(final String sql, final Class<T> type, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters)
	{
		return new Transactional<ArrayList<T>>() {
			public ArrayList<T> run(TxnContext context) throws Exception
			{
				return StatementUtil.selectIntoObjects(
						context.getConnection(), sql, type, resultTypes, parameters );
			}
		};
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoObjects(final Collection<T> results, final String sql, final Class<T> type, final IfcResultType<?>... resultTypes)
	{
		return selectIntoObjects(results, sql, type, resultTypes, (IfcStatementInParameter[])null );
	}
	
	@Deprecated // use selectInto
	public final static <T> Transactional<Void> selectIntoObjects(final Collection<T> results, final String sql, final Class<T> type, final IfcResultType<?>[] resultTypes, final IfcStatementInParameter...parameters)
	{
		return new Transactional<Void>() {
			public Void run(TxnContext context) throws Exception
			{
				StatementUtil.selectIntoObjects( 
						results, context.getConnection(), sql, type, resultTypes, parameters );
				return null;
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
