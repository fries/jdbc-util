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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class ResultFactories
{
	public final static <T> IfcResultFactory<T> singleton(final T t)
	{
		return new IfcResultFactory<T>() {
			public T newInstance()
			{
				return t;
			}
		};
	}
	
	/**
	 * @author M.Sc. Friedrich Schäuffelhut
	 *
	 */
	public static final class ArrayResultFactory implements
			IfcResultFactory<Object[]>
	{
		/**
		 * 
		 */
		private final int length;
	
		/**
		 * @param length
		 */
		public ArrayResultFactory(int length)
		{
			this.length = length;
		}
	
		public Object[] newInstance() {
			return new Object[length];
		}
	}

	/**
	 * @author M.Sc. Friedrich Schäuffelhut
	 *
	 */
	private static final class HashMapResultFactory
	implements IfcResultFactory<Map<String, Object>>
	{
		private HashMapResultFactory()
		{
		}
	
		public final Map<String, Object> newInstance()
		{
			return new HashMap<String, Object>();
		}
	}

	public static final HashMapResultFactory HashMapResultFactory =
		new HashMapResultFactory();

	/**
	 * @author M.Sc. Friedrich Schäuffelhut
	 *
	 */
	public final static class ReflectionResultFactory<T>
	implements IfcResultFactory<T>
	{
		private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
		private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
		private final Constructor<T> constructor;
		
		public ReflectionResultFactory(Class<T> type)
		{
			try
			{
				constructor = type.getDeclaredConstructor(
						EMPTY_CLASS_ARRAY
				);
				constructor.setAccessible( true );
			}
			catch (SecurityException e)
			{
				throw new RuntimeException( e );
			}
			catch (NoSuchMethodException e)
			{
				throw new RuntimeException( e );
			}
		}
		
		public final static <T> ReflectionResultFactory<T> create(
				Class<T> type
		) {
			return new ReflectionResultFactory<T>( type );
		}
		
		public final T newInstance()
		{
				try
				{
					return constructor.newInstance( EMPTY_OBJECT_ARRAY );
				}
				catch (InstantiationException e)
				{
					throw new RuntimeException( e );
				}
				catch (IllegalAccessException e)
				{
					throw new RuntimeException( e );
				}
				catch (InvocationTargetException e)
				{
					throw new RuntimeException( e );
				}
		}
	}


	private ResultFactories() {
		
	}
}
