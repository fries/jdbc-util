package de.schaeuffelhut.jdbc;

import org.joda.time.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.*;

/**
 * @author Friedrich Schäuffelhut
 * @since 2017-05-16
 */
public class StatementParametersTest
{
    private int uniqueIndex = (int) (System.currentTimeMillis() % 128);
    private PreparedStatement stmt = Mockito.mock( PreparedStatement.class );

    // ==================== Boolean ====================

    @Test
    public void Boolean_null() throws Exception
    {
        StatementParameters.Boolean.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.BOOLEAN );
    }

    @Test
    public void Boolean_false() throws Exception
    {
        StatementParameters.Boolean.configure( stmt, uniqueIndex, false );
        Mockito.verify( stmt ).setBoolean( uniqueIndex, false );
    }

    @Test
    public void Boolean_true() throws Exception
    {
        StatementParameters.Boolean.configure( stmt, uniqueIndex, true );
        Mockito.verify( stmt ).setBoolean( uniqueIndex, true );
    }

    // ==================== BooleanAsInteger ====================

    @Test
    public void BooleanAsInteger_null() throws Exception
    {
        StatementParameters.BooleanAsInteger.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void BooleanAsInteger_false() throws Exception
    {
        StatementParameters.BooleanAsInteger.configure( stmt, uniqueIndex, false );
        Mockito.verify( stmt ).setInt( uniqueIndex, 0 );
    }

    @Test
    public void BooleanAsInteger_true() throws Exception
    {
        StatementParameters.BooleanAsInteger.configure( stmt, uniqueIndex, true );
        Mockito.verify( stmt ).setInt( uniqueIndex, 1 );
    }

    // ==================== Byte ====================

    @Test
    public void Byte_null() throws Exception
    {
        StatementParameters.Byte.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.TINYINT );
    }

    @Test
    public void Byte_values() throws Exception
    {
        for (byte b = 0; b < Byte.MAX_VALUE; b++)
        {
            StatementParameters.Byte.configure( stmt, uniqueIndex, b );
            Mockito.verify( stmt ).setByte( uniqueIndex, b );
        }
    }

    // ==================== Character ====================

    @Test
    public void Character_null() throws Exception
    {
        StatementParameters.Character.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setString( uniqueIndex, null );
    }

    @Test
    public void Character_values() throws Exception
    {
        for (char c : new char[]{0, 100, Character.MAX_VALUE})
        {
            StatementParameters.Character.configure( stmt, uniqueIndex, c );
            Mockito.verify( stmt ).setString( uniqueIndex, Character.toString( c ) );
        }
    }

    // ==================== Short ====================

    @Test
    public void Short_null() throws Exception
    {
        StatementParameters.Short.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.SMALLINT );
    }

    @Test
    public void Short_values() throws Exception
    {
        for (short s : new short[]{Short.MIN_VALUE, -1, 0, 1, 100, Short.MAX_VALUE})
        {
            StatementParameters.Short.configure( stmt, uniqueIndex, s );
            Mockito.verify( stmt ).setShort( uniqueIndex, s );
        }
    }

    // ==================== Long ====================

    @Test
    public void Long_null() throws Exception
    {
        StatementParameters.Long.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.BIGINT );
    }

    @Test
    public void Long_values() throws Exception
    {
        for (Long l : new Long[]{Long.MIN_VALUE, -1L, 0L, 1L, 100L, Long.MAX_VALUE})
        {
            StatementParameters.Long.configure( stmt, uniqueIndex, l );
            Mockito.verify( stmt ).setLong( uniqueIndex, l );
        }
    }


    // ==================== Float ====================

    @Test
    public void Float_null() throws Exception
    {
        StatementParameters.Float.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.FLOAT );
    }

    @Test
    public void Float_values() throws Exception
    {
        for (Float f : new Float[]{Float.MIN_VALUE, -1.0f, 0.0f, 1.0f, 100.f, Float.MAX_VALUE})
        {
            StatementParameters.Float.configure( stmt, uniqueIndex, f );
            Mockito.verify( stmt ).setFloat( uniqueIndex, f );
        }
    }


    // ==================== Double ====================

    @Test
    public void Double_null() throws Exception
    {
        StatementParameters.Double.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.DOUBLE );
    }

    @Test
    public void Double_values() throws Exception
    {
        for (Double d : new Double[]{Double.MIN_VALUE, -1.0d, 0.0d, 1.0d, 100.d, Double.MAX_VALUE})
        {
            StatementParameters.Double.configure( stmt, uniqueIndex, d );
            Mockito.verify( stmt ).setDouble( uniqueIndex, d );
        }
    }


    // ==================== BigDecimal ====================

    @Test
    public void BigDecimal_null() throws Exception
    {
        StatementParameters.BigDecimal.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setBigDecimal( uniqueIndex, null );
    }

    @Test
    public void BigDecimal_values() throws Exception
    {
        for (BigDecimal d : new BigDecimal[]{BigDecimal.valueOf( Double.MIN_VALUE ), BigDecimal.ZERO, BigDecimal.valueOf( Double.MAX_VALUE )})
        {
            StatementParameters.BigDecimal.configure( stmt, uniqueIndex, d );
            Mockito.verify( stmt ).setBigDecimal( uniqueIndex, d );
        }
    }

    // ==================== String ====================

    @Test
    public void String_null() throws Exception
    {
        StatementParameters.String.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setString( uniqueIndex, null );
    }

    @Test
    public void String_values() throws Exception
    {
        for (String d : new String[]{"", "not empty"})
        {
            StatementParameters.String.configure( stmt, uniqueIndex, d );
            Mockito.verify( stmt ).setString( uniqueIndex, d );
        }
    }

    // ==================== Date ====================

    @Test
    public void Date_null() throws Exception
    {
        StatementParameters.Date.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setDate( uniqueIndex, null );
    }

    @Test
    public void Date_values() throws Exception
    {
        Date d = new Date( System.currentTimeMillis() );
        StatementParameters.Date.configure( stmt, uniqueIndex, d );
        Mockito.verify( stmt ).setDate( uniqueIndex, d );
    }

    // ==================== Timestamp ====================

    @Test
    public void Timestamp_null() throws Exception
    {
        StatementParameters.Timestamp.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setTimestamp( uniqueIndex, null );
    }

    @Test
    public void Timestamp_values() throws Exception
    {
        Timestamp d = new Timestamp( System.currentTimeMillis() );
        StatementParameters.Timestamp.configure( stmt, uniqueIndex, d );
        Mockito.verify( stmt ).setTimestamp( uniqueIndex, d );
    }

    // ==================== Object ====================

    @Test
    public void Object_null() throws Exception
    {
        StatementParameters.Object.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setObject( uniqueIndex, null );
    }

    @Test
    public void Object_values() throws Exception
    {
        Object d = new Object();
        StatementParameters.Object.configure( stmt, uniqueIndex, d );
        Mockito.verify( stmt ).setObject( uniqueIndex, d );
    }

    // ==================== Serializeable ====================

    @Test
    public void Serializeable_null() throws Exception
    {
        StatementParameters.Serializeable.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setBytes( uniqueIndex, null );
    }

    @Test
    public void Serializeable_values() throws Exception
    {
        Object o = Integer.valueOf( 1 );
        StatementParameters.Serializeable.configure( stmt, uniqueIndex, o );
        Mockito.verify( stmt ).setBytes( uniqueIndex, new byte[]{-84, -19, 0, 5, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0, 5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112, 0, 0, 0, 1} );
    }


    // ==================== Bytes ====================

    @Test
    public void Bytes_null() throws Exception
    {
        StatementParameters.Bytes.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setBytes( uniqueIndex, null );
    }

    @Test
    public void Bytes_values() throws Exception
    {
        byte[] d = new byte[]{1, 2};
        StatementParameters.Bytes.configure( stmt, uniqueIndex, d );
        Mockito.verify( stmt ).setBytes( uniqueIndex, d );
    }


    // ==================== EnumByName ====================

    @Test
    public void EnumByName_null() throws Exception
    {
        StatementParameters.EnumByName.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setString( uniqueIndex, null );
    }

    static enum EnumByNameDummy
    {
        VALUE_A, VALUE_B
    }

    @Test
    public void EnumByName_values() throws Exception
    {
        for (EnumByNameDummy e : EnumByNameDummy.values())
        {
            StatementParameters.EnumByName.configure( stmt, uniqueIndex, e );
            Mockito.verify( stmt ).setString( uniqueIndex, e.name() );
        }
    }


    // ==================== EnumByIntKey ====================

    @Test
    public void EnumByIntKey_null() throws Exception
    {
        StatementParameters.EnumByIntKey.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    static enum EnumByIntKeyDummy implements IfcEnumIntKey
    {
        VALUE_A, VALUE_B;

        @Override
        public int getKey()
        {
            return ordinal();
        }
    }

    @Test
    public void EnumByIntKey_values() throws Exception
    {
        for (EnumByIntKeyDummy e : EnumByIntKeyDummy.values())
        {
            StatementParameters.EnumByIntKey.configure( stmt, uniqueIndex, e );
            Mockito.verify( stmt ).setInt( uniqueIndex, e.getKey() );
        }
    }


    // ==================== Class ====================

    @Test
    public void Class_null() throws Exception
    {
        StatementParameters.Class.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setString( uniqueIndex, null );
    }

    @Test
    public void Class_values() throws Exception
    {
        StatementParameters.Class.configure( stmt, uniqueIndex, Object.class );
        Mockito.verify( stmt ).setString( uniqueIndex, "java.lang.Object" );
    }


    // ==================== DateTime ====================

    @Test
    public void DateTime_null() throws Exception
    {
        StatementParameters.DateTime.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setTimestamp( uniqueIndex, null );
    }

    @Test
    public void DateTime_values() throws Exception
    {
        long time = System.currentTimeMillis();
        StatementParameters.DateTime.configure( stmt, uniqueIndex, new DateTime( time ) );
        Mockito.verify( stmt ).setTimestamp( uniqueIndex, new Timestamp( time ) );
    }

    // ==================== DateMidnight ====================

    @Test
    public void DateMidnight_null() throws Exception
    {
        StatementParameters.DateMidnight.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setTimestamp( uniqueIndex, null );
    }

    @Test
    public void DateMidnight_values() throws Exception
    {
        long time = System.currentTimeMillis();
        StatementParameters.DateMidnight.configure( stmt, uniqueIndex, new DateMidnight( time ) );
        Mockito.verify( stmt ).setTimestamp( uniqueIndex, new Timestamp( new DateMidnight( time ).getMillis() ) );
    }


    // ==================== DateMidnightAsIsoString ====================

    @Test
    public void DateMidnightAsIsoString_null() throws Exception
    {
        StatementParameters.DateMidnightAsIsoString.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setString( uniqueIndex, null );
    }

    @Test
    public void DateMidnightAsIsoString_values() throws Exception
    {
        StatementParameters.DateMidnightAsIsoString.configure( stmt, uniqueIndex, new DateMidnight( "2017-01-01" ) );
        Mockito.verify( stmt ).setString( uniqueIndex, "2017-01-01" );
    }


    // ==================== Days ====================

    @Test
    public void Days_null() throws Exception
    {
        StatementParameters.Days.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void Days_values() throws Exception
    {
        StatementParameters.Days.configure( stmt, uniqueIndex, Days.days( 10 ) );
        Mockito.verify( stmt ).setInt( uniqueIndex, 10 );
    }


    // ==================== Hours ====================

    @Test
    public void Hours_null() throws Exception
    {
        StatementParameters.Hours.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void Hours_values() throws Exception
    {
        StatementParameters.Hours.configure( stmt, uniqueIndex, Hours.hours( 10 ) );
        Mockito.verify( stmt ).setInt( uniqueIndex, 10 );
    }


    // ==================== Minutes ====================

    @Test
    public void Minutes_null() throws Exception
    {
        StatementParameters.Minutes.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void Minutes_values() throws Exception
    {
        StatementParameters.Minutes.configure( stmt, uniqueIndex, Minutes.minutes( 10 ) );
        Mockito.verify( stmt ).setInt( uniqueIndex, 10 );
    }


    // ==================== Months ====================

    @Test
    public void Months_null() throws Exception
    {
        StatementParameters.Months.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void Months_values() throws Exception
    {
        StatementParameters.Months.configure( stmt, uniqueIndex, Months.months( 10 ) );
        Mockito.verify( stmt ).setInt( uniqueIndex, 10 );
    }


    // ==================== Seconds ====================

    @Test
    public void Seconds_null() throws Exception
    {
        StatementParameters.Seconds.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void Seconds_values() throws Exception
    {
        StatementParameters.Seconds.configure( stmt, uniqueIndex, Seconds.seconds( 10 ) );
        Mockito.verify( stmt ).setInt( uniqueIndex, 10 );
    }


    // ==================== Weeks ====================

    @Test
    public void Weeks_null() throws Exception
    {
        StatementParameters.Weeks.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void Weeks_values() throws Exception
    {
        StatementParameters.Weeks.configure( stmt, uniqueIndex, Weeks.weeks( 10 ) );
        Mockito.verify( stmt ).setInt( uniqueIndex, 10 );
    }


    // ==================== Years ====================

    @Test
    public void Years_null() throws Exception
    {
        StatementParameters.Years.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.INTEGER );
    }

    @Test
    public void Years_values() throws Exception
    {
        StatementParameters.Years.configure( stmt, uniqueIndex, Years.years( 10 ) );
        Mockito.verify( stmt ).setInt( uniqueIndex, 10 );
    }


    // ==================== PeriodIsoEncoded ====================

    @Test
    public void PeriodIsoEncoded_null() throws Exception
    {
        StatementParameters.PeriodIsoEncoded.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setString( uniqueIndex, null );
    }

    @Test
    public void PeriodIsoEncoded_values() throws Exception
    {
        StatementParameters.PeriodIsoEncoded.configure( stmt, uniqueIndex, Period.days( 10 ) );
        Mockito.verify( stmt ).setString( uniqueIndex, "P10D" );
    }


    // ==================== DurationAsLong ====================

    @Test
    public void DuractionAsLong_null() throws Exception
    {
        StatementParameters.DurationAsLong.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setNull( uniqueIndex, Types.BIGINT );
    }

    @Test
    public void DuractionAsLong_values() throws Exception
    {
        long duration = System.currentTimeMillis();
        StatementParameters.DurationAsLong.configure( stmt, uniqueIndex, new Duration( duration ) );
        Mockito.verify( stmt ).setLong( uniqueIndex, duration );
    }


    // ==================== DateTimeZone ====================

    @Test
    public void DateTimeZone_null() throws Exception
    {
        StatementParameters.DateTimeZone.configure( stmt, uniqueIndex, null );
        Mockito.verify( stmt ).setString( uniqueIndex, null );
    }

    @Test
    public void DateTimeZone_values() throws Exception
    {
        StatementParameters.DateTimeZone.configure( stmt, uniqueIndex, DateTimeZone.UTC );
        Mockito.verify( stmt ).setString( uniqueIndex, "UTC" );
    }

    // ==================== QueryTimeout ====================

    @Test
    public void QueryTimeout() throws Exception
    {
        int count = StatementParameters.QueryTimeout( 10 ).configure( stmt, uniqueIndex );

        Assert.assertEquals( 0, count );
        Mockito.verify( stmt ).setQueryTimeout( 10 );
    }

}