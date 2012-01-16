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
package de.schaeuffelhut.jdbc.experimental;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.schaeuffelhut.jdbc.IfcResultType;

/**
 * @author Friedrich Schäuffelhut
 *
 */
public final class CachedUUIDResultType<T> implements IfcResultType<UUID>
{
	private static final long	serialVersionUID	= 5910873367702192783L;


	public final static class Key<T>
	{
		public final String domain;
		
		public final T value;
		
		private Key(String domain, T value)
		{
			this.domain = domain;
			this.value = value;
		}
		
		@Override
		public final int hashCode()
		{
			int aSeed = HashCodeUtil.SEED;
			aSeed = HashCodeUtil.hash( aSeed, domain );
			aSeed = HashCodeUtil.hash( aSeed, value );			
			return aSeed;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if ( this == obj )
			{
				return true;
			}
			else if ( obj instanceof Key<?> )
			{
				Key<?> that = (Key<?>)obj;
				return (
						(this.domain == null
								? that.domain == null
										: this.domain.equals( that.domain )
						) &&
						( this.value == null
								? that.value == null
										: this.value.equals( that.value )
						)
				);
			}
			else
			{
				return false;
			}
		}
		
		@Override
		public final String toString()
		{
			return String.format( "Key[domain=%s,value=%s]", domain, value );
		}
	}
	
	final static Map<Key<?>, UUID> key2uuid = new HashMap<Key<?>,UUID>();
	final static Map<UUID,Key<?>> uuid2key = new HashMap<UUID,Key<?>>();

	final String domain;
	
	final IfcResultType<T> resultType;
	
    public CachedUUIDResultType(String domain, IfcResultType<T> resultType)
	{
		this.domain = domain;
		this.resultType = resultType;
	}


	public final UUID getResult(ResultSet resultSet, int index) throws SQLException
    {
    	T t = resultType.getResult( resultSet, index );
    	final Key<T> key = new Key<T>( domain, t );
    	UUID uuid = key2uuid.get( key );
    	if ( uuid == null )
    	{
    		uuid = UUID.randomUUID();
    		key2uuid.put( key, uuid );
    		uuid2key.put( uuid, key );
    	}
        return uuid;
    }
	
	public final static <T> CachedUUIDResultType<T> mappedUUID(
			String domain, IfcResultType<T> resultType
	){
		return new CachedUUIDResultType<T>(domain,resultType);
	}


	public final static Key<?> reverse(UUID uuid)
	{
		return uuid2key.get( uuid );
	}


	public Class<UUID> getResultType()
	{
		return UUID.class;
	}
}