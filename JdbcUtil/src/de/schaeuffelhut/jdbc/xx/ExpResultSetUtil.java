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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.schaeuffelhut.jdbc.IfcResultAdaptor;
import de.schaeuffelhut.jdbc.IfcResultFactory;
import de.schaeuffelhut.jdbc.ResultAdaptors;


/**
 * @author Friedrich Schäuffelhut
 *
 */
public class ExpResultSetUtil
{

	
	
	/*
	 * most general, read into multiple result holders
	 */
	public final static class ResultReader<T>
	{
		final IfcResultFactory<T> factory;
		final IfcResultAdaptor<T>[] adaptors;
		
		private ResultReader(IfcResultFactory<T> factory,
				IfcResultAdaptor<T>[] adaptors)
		{
			this.factory = factory;
			this.adaptors = adaptors;
		}
		
		private final int adopt(
				ResultSet rs,
				int columnIndex,
				T t
		) throws SQLException
		{
			return ResultAdaptors.adapt( rs, columnIndex, t, adaptors );
		}
	}
	
	public final static <T> ResultReader<T> createResultReader(
			IfcResultFactory<T> factory,
			IfcResultAdaptor<T>... adaptors
	){
		return new ResultReader<T>( factory, adaptors );
	}
	
	public final static void readMultipleResults(
			Collection<Object> results,
			ResultSet resultSet,
			ResultReader<?>... resultReaders
			
	) throws SQLException
	{
	    while( resultSet.next() )
	    {
	    	int columnIndex = 1;
	    	Object[] result = new Object[resultReaders.length];
	    	for(int i = 0; i < resultReaders.length; i++ )
	    	{
	    		ResultReader<Object> resultReader = (ResultReader<Object>)resultReaders[i];
				Object o = resultReader.factory.newInstance();
	    		resultReader.adopt( resultSet, columnIndex + i, o );
	    		result[i] = o;
	    	}
    		results.add( result );
	    }
	}

	public final static class TreeResult
	{
		final static Object ROOT_KEY = null;

		final Map<Object, ArrayList<Object>> parentChildMapping = new HashMap<Object, ArrayList<Object>>();
		final Map<Object, Object> childParentMapping = new HashMap<Object, Object>();

		
		/**
		 * Returns a List of root nodes.
		 * @return
		 */
		public final List<Object> rootNodes()
		{
			return Collections.unmodifiableList( parentChildMapping.get( ROOT_KEY ) );
		}

		
		/**
		 * Returns the children of a parent, or null, if the given object
		 * is a leaf node.
		 * @param parent
		 * @return
		 */
		public final List<Object> children(Object parent)
		{
			ArrayList<Object> children = parentChildMapping.get(  parent  );
			if ( children == null )
				return null;
			else
				return Collections.unmodifiableList( children );
		}
		
		/**
		 * Returns the parent for a given child, or null,
		 * if the given object is a root node
		 * ( or is not a element of this TreeResult )
		 * @param child
		 * @return
		 */
		public final Object parent(Object child)
		{
			return childParentMapping.get(  child );
		}
		
		
		/**
		 * @param parent null means child is root element
		 * @param child
		 */
		final void put(Object parent, Object child)
		{
			childParentMapping.put( child, parent );
			ArrayList<Object> children = parentChildMapping.get(  parent  );
			if ( children == null )
			{
				children = new ArrayList<Object>();
				parentChildMapping.put( parent, children );
			}
			children.add(  child );
		}
	}
	
	public final static TreeResult readTreeResults(
			ResultSet resultSet,
			ResultReader<?>... resultReaders
			
	) throws SQLException
	{
		TreeResult treeResult = new TreeResult();
		
		Object[] seen = new Object[resultReaders.length];
	    while( resultSet.next() )
	    {
	    	Object parent = null;
	    	int columnIndex = 1;
	    	for(int i = 0; i < resultReaders.length; i++ )
	    	{
	    		ResultReader<Object> resultReader = (ResultReader<Object>)resultReaders[i];
				Object child = resultReader.factory.newInstance();
	    		columnIndex += resultReader.adopt( resultSet, columnIndex, child );
	    		if ( seen[i] == null || !seen[i].equals( child ) )
	    			treeResult.put( parent, child );
	    		parent = child;
	    		seen[i] = child;
	    	}
	    }
	    
	    return treeResult;
	}
	
	
	
	
	
}
