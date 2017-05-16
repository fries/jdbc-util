package de.schaeuffelhut.jdbc;

public abstract class AbstractRowProcessor<V, T> implements IfcRowProcessor<V, T>
{
	private static final long serialVersionUID = 4757351893331940602L;

	V result;

	public AbstractRowProcessor()
	{
		this( null );
	}
	
	public AbstractRowProcessor(V result)
	{
		this.result = result;
	}

	public final V getResult() {
		return result;
	}

	public abstract void process(T t) throws Exception;

}
