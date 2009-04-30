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
			public void readResults(Collection<T> results, ResultSet resultSet) throws Exception
			{
				ResultSetUtil.readObjects( results, resultSet, type, resultTypes );
			}
		};
	}
}
