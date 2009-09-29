package de.schaeuffelhut.jdbc;

// TODO: generalize; any class which implements this interface can be persisted, just reading needs mapping
public interface IfcEnumKey<T>
{
	public abstract T getKey();
}
