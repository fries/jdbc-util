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

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

public final class ResultSetReaders
{
	private ResultSetReaders()
	{
	}

	/*
	 * select into scalar
	 */

	public final static <T> IfcResultSetScalarReader<T> readScalar(
			final IfcResultType<T> resultType
	){
		return new IfcResultSetScalarReader<T>(){
			private static final long	serialVersionUID	= 8911654214633193260L;

			public T readResult(ResultSet resultSet) throws Exception
			{
				return ResultSetUtil.readScalar( resultSet, resultType );
			}
		};
	}

	public final static IfcResultSetScalarReader<Object[]> readTuple(
			final IfcResultType<?>... resultTypes
	){
		return new IfcResultSetScalarReader<Object[]>(){
			private static final long	serialVersionUID	= -2886595304052908844L;

			public Object[] readResult(ResultSet resultSet) throws Exception
			{
				return ResultSetUtil.readTuple( resultSet, resultTypes );
			}
		};
	}

	public final static IfcResultSetScalarReader<Map<String,Object>> readMap(
			final IfcResultType<?>... resultTypes
	){
		return new IfcResultSetScalarReader<Map<String,Object>>(){
			private static final long	serialVersionUID	= 8545376014979236299L;

			public Map<String,Object> readResult(ResultSet resultSet) throws Exception
			{
				return ResultSetUtil.readMap( resultSet, resultTypes );
			}
		};
	}

	public final static <T> IfcResultSetScalarReader<T> readObject(
			final Class<T> type, final IfcResultType<?>... resultTypes
	){
		return new IfcResultSetScalarReader<T>(){
			private static final long	serialVersionUID	= -8859588960602175157L;

			public T readResult(ResultSet resultSet) throws Exception
			{
				return ResultSetUtil.readObject( resultSet, type, resultTypes );
			}
		};
	}

	/*
	 * select into collection
	 */
	
	public final static <T> IfcResultSetCollectionReader<T> readScalars(
			final IfcResultType<T> resultType
	){
		return new IfcResultSetCollectionReader<T>(){
			private static final long	serialVersionUID	= 5008801957885037291L;

			public void readResults(Collection<T> results, ResultSet resultSet) throws Exception
			{
				ResultSetUtil.readScalars( results, resultSet, resultType );
			}
		};
	}

	public final static IfcResultSetCollectionReader<Object[]> readTuples(
			final IfcResultType<?>... resultTypes
	){
		return new IfcResultSetCollectionReader<Object[]>(){
			private static final long	serialVersionUID	= -1018835897002057163L;

			public void readResults(Collection<Object[]> results, ResultSet resultSet) throws Exception
			{
				ResultSetUtil.readTuples( results, resultSet, resultTypes );
			}
		};
	}

	public final static IfcResultSetCollectionReader<Map<String, Object>> readMaps(
			final IfcResultType<?>... resultTypes
	){
		return new IfcResultSetCollectionReader<Map<String,Object>>(){
			private static final long	serialVersionUID	= -4552881760536118694L;

			public void readResults(Collection<Map<String, Object>> results, ResultSet resultSet) throws Exception
			{
				ResultSetUtil.readMaps( results, resultSet, resultTypes );
			}
		};
	}
	
	public final static <T> IfcResultSetCollectionReader<T> readObjects(
			final Class<T> type, final IfcResultType<?>... resultTypes
	){
		return new IfcResultSetCollectionReader<T>(){
			private static final long	serialVersionUID	= -6157564706582485580L;

			public void readResults(Collection<T> results, ResultSet resultSet) throws Exception
			{
				ResultSetUtil.readObjects( results, resultSet, type, resultTypes );
			}
		};
	}
}
