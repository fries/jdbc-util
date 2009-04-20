package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class ResultSetUtil
{
	private ResultSetUtil(){}
	
	/*
	 * read single row, single value
	 */
	
    public final static <T> T readScalar(
    		ResultSet resultSet, IfcResultType<T> resultType
    ) throws SQLException
    {
		final T result;
		if ( resultSet.next() )
        {
			result = resultType.getResult( resultSet, 1 );
        	if ( resultSet.next() )
        	{
        		throw new RuntimeException( "ResultSet returned more than one row" );
        	}
        }
        else
        {
            result = null;
        }
        return result;
    }


	/*
	 * read single row, multiple values
	 */

    public final static Object[] readTuple(
    		ResultSet resultSet,
    		IfcResultType<?>... resultTypes
    ) throws SQLException
    {
		return readResult(
				resultSet,
				new ResultFactories.ArrayResultFactory( resultTypes.length ),
				ResultAdaptors.createArrayResultAdaptors(
						resultTypes
				)
		);
    }

    public final static Map<String, Object> readMap(
    		ResultSet resultSet,
    		IfcResultType<?>... resultTypes
    ) throws SQLException
    {
		return readResult(
				resultSet,
				ResultFactories.HashMapResultFactory,
				ResultAdaptors.createMapResultAdaptors(
						resultSet,
						resultTypes
				)
		);
    }

    public final static <T> T readObject(
    		ResultSet resultSet,
    		Class<T> type,
    		IfcResultType<?>... resultTypes
    ) throws SQLException
    {
		return readResult(
				resultSet,
				new ResultFactories.ReflectionResultFactory<T>( type ),
				ResultAdaptors.createFieldResultAdaptors(
						resultSet,
						type,
						resultTypes
				)
		);
    }

	@SuppressWarnings("unchecked")
    public final static <T> T readObject(
    		ResultSet resultSet,
    		final T result,
    		IfcResultType<?>... resultTypes
    ) throws SQLException
    {
		return readResult(
				resultSet,
				new IfcResultFactory<T>() {
					public T newInstance() {return result;}
				},
				ResultAdaptors.createFieldResultAdaptors(
						resultSet,
						(Class<T>)result.getClass(),
						resultTypes
				)
		);
    }

	
	public final static <T> T readResult(
			ResultSet resultSet,
			IfcResultFactory<T> resultHolderFactory,
			IfcResultSetAdaptor<T>[] adaptors
	) throws SQLException
	{
		final T result;
		if ( resultSet.next() )
        {
			result = resultHolderFactory.newInstance();
			ResultAdaptors.adapt( resultSet, result, adaptors );
        	if ( resultSet.next() )
        	{
        		throw new RuntimeException(
        				"ResultSet returned more than one row" );
        	}
        }
        else
        {
            result = null;
        }
        return result;
	}


	
	/*
	 * read multiple rows, single value
	 */

	public final static <T> ArrayList<T> readScalars(
			ResultSet resultSet,
			IfcResultType<T> resultType
	) throws SQLException
    {
        ArrayList<T> results = new ArrayList<T>();
		readScalars( results, resultSet, resultType );
		return results;
    }

	public final static <T> void readScalars(
			Collection<T> results,
			ResultSet resultSet,
			IfcResultType<T> resultType
	) throws SQLException
	{
		while( resultSet.next() )
        	results.add( resultType.getResult(resultSet, 1) );
	}

	
	/*
	 * read multiple rows, multiple value
	 */

    public final static ArrayList<Object[]> readTuples(
    		ResultSet resultSet, IfcResultType<?>... resultTypes
    ) throws SQLException
    {
    	return readResults(
        		resultSet,
        		new ResultFactories.ArrayResultFactory( resultTypes.length ),
        		ResultAdaptors.createArrayResultAdaptors( resultTypes )
        );
    }

	public final static void readTuples(
			Collection<Object[]> results,
			ResultSet resultSet,
			IfcResultType<?>... resultTypes
	) throws SQLException
	{
		ResultSetUtil.readResults(
        		results,
        		resultSet,
        		new ResultFactories.ArrayResultFactory( resultTypes.length ),
        		ResultAdaptors.createArrayResultAdaptors( resultTypes )
        );
	}

    public final static ArrayList<Map<String, Object>> readMaps(
    		ResultSet resultSet, IfcResultType<?>... resultTypes
    ) throws SQLException
    {
		return readResults(
        		resultSet,
        		ResultFactories.HashMapResultFactory,
        		ResultAdaptors.createMapResultAdaptors(
        				resultSet,
						resultTypes
        		)
        );
    }
	
	public final static void readMaps(
			Collection<Map<String,Object>> results,
			ResultSet resultSet,
			IfcResultType<?>... resultTypes
	) throws SQLException
	{
		ResultSetUtil.readResults(
        		results,
        		resultSet,
        		ResultFactories.HashMapResultFactory,
        		ResultAdaptors.createMapResultAdaptors(
        				resultSet,
						resultTypes
        		)
        );
	}

	public final static <T> ArrayList<T> readObjects(
			ResultSet resultSet, Class<T> type, IfcResultType<?>... resultTypes
	) throws SQLException
    {
        return readResults(
        		resultSet,
        		ResultFactories.ReflectionResultFactory.create( type ),
        		ResultAdaptors.createFieldResultAdaptors( resultSet, type, resultTypes )
        );
    }

	public final static <T> void readObjects(
			Collection<T> results,
			ResultSet resultSet,
			Class<T> type,
			IfcResultType<?>... resultTypes
	) throws SQLException
	{
        ResultSetUtil.readResults(
        		results,
        		resultSet,
        		ResultFactories.ReflectionResultFactory.create( type ),
        		ResultAdaptors.createFieldResultAdaptors( resultSet, type, resultTypes )
        );
	}

	@SuppressWarnings("unchecked")
	public final static <T extends Runnable> void forResultsInvoke(
			final T resultHandler,
			ResultSet resultSet,
			IfcResultType<?>... resultTypes
	) throws SQLException
	{
		forResultsInvoke(
				resultHandler,
				resultSet,
				ResultAdaptors.createFieldResultAdaptors(
						resultSet,
						(Class<T>)resultHandler.getClass(),
						resultTypes
				)
		);
	}
	
	
	/*
	 * most general, read into single result holder
	 */
		
	public final static <T> ArrayList<T> readResults(
			ResultSet resultSet,
			IfcResultFactory<T> factory,
			IfcResultSetAdaptor<T>... adaptors
	) throws SQLException
	{
		ArrayList<T> results = new ArrayList<T>();
		ResultSetUtil.readResults(results, resultSet, factory, adaptors);
		return results;
	}

	public final static <T> void readResults(
			Collection<T> results,
			ResultSet resultSet,
			IfcResultFactory<T> factory,
			IfcResultSetAdaptor<T>... adaptors
	) throws SQLException
	{
	    while( resultSet.next() )
	    {
	    	T t = factory.newInstance();
	    	ResultAdaptors.adapt( resultSet, t, adaptors );
			results.add( t );
	    }
	}
	
	public final static <T extends Runnable> void forResultsInvoke(
			final T resultHandler,
			ResultSet resultSet, 
			IfcResultSetAdaptor<T>... adaptors
	) throws SQLException
	{
	    while( resultSet.next() )
	    {
			ResultAdaptors.adapt( resultSet, resultHandler, adaptors );
			resultHandler.run();
	    }
	}
}
