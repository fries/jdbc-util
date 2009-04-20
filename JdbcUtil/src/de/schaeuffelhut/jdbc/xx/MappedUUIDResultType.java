/**
 * (C) Copyright 2007 M.Sc. Friedrich Schäuffelhut
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * $Revison$
 * $Author$
 * $Date$
 */
package de.schaeuffelhut.jdbc.xx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.schaeuffelhut.jdbc.IfcResultType;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public final class MappedUUIDResultType<T> implements IfcResultType<UUID>
{
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
	
    public MappedUUIDResultType(String domain, IfcResultType<T> resultType)
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
	
	public final static <T> MappedUUIDResultType<T> mappedUUID(
			String domain, IfcResultType<T> resultType
	){
		return new MappedUUIDResultType<T>(domain,resultType);
	}


	public final static Key<?> reverse(UUID uuid)
	{
		return uuid2key.get( uuid );
	}
}