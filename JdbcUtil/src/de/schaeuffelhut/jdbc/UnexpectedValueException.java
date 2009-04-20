package de.schaeuffelhut.jdbc;

public final class UnexpectedValueException extends RuntimeException
{
    private static final long serialVersionUID = -1873135633760648850L;

    public UnexpectedValueException(Object value)
    {
        super( value == null ? "<null>" : "'" + value.toString() + "'" );
    }
}
