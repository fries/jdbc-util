package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.util.Map;

public class ResultSetProcessors
{
		private ResultSetProcessors() {}
		
		public final static <V,T> IfcResultSetProcessor<V,T> processScalars(
				final IfcRowProcessor<V,T> processor,
				final IfcResultType<T> resultType
		){
			return new IfcResultSetProcessor<V,T>(){
				private static final long serialVersionUID = 8164477356292078695L;

				public V readResults(ResultSet resultSet) throws Exception
				{
					return ResultSetUtil.processScalars( processor, resultSet, resultType );
				}
			};
		}

		public final static <V> IfcResultSetProcessor<V,Object[]> processTuples(
				final IfcRowProcessor<V,Object[]> processor,
				final IfcResultType<?>... resultTypes
		){
			return new IfcResultSetProcessor<V,Object[]>(){
				private static final long serialVersionUID = 6669314927102655946L;

				public V readResults(ResultSet resultSet) throws Exception
				{
					return ResultSetUtil.processTuples( processor, resultSet, resultTypes );
				}
			};
		}

		public final static <V> IfcResultSetProcessor<V,Map<String, Object>> processMaps(
				final IfcRowProcessor<V,Map<String, Object>> processor,
				final IfcResultType<?>... resultTypes
		){
			return new IfcResultSetProcessor<V,Map<String,Object>>(){
				private static final long serialVersionUID = 6240590161413164274L;

				public V readResults(ResultSet resultSet) throws Exception
				{
					return ResultSetUtil.processMaps( processor, resultSet, resultTypes );
				}
			};
		}
		
		public final static <V,T> IfcResultSetProcessor<V,T> processObjects(
				final IfcRowProcessor<V,T> processor,
				final Class<T> type,
				final IfcResultType<?>... resultTypes
		){
			return new IfcResultSetProcessor<V,T>(){
				private static final long serialVersionUID = -4231333909870577652L;

				public V readResults(ResultSet resultSet) throws Exception
				{
					return ResultSetUtil.processObjects( processor, resultSet, type, resultTypes );
				}
			};
		}
}
