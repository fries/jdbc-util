package de.schaeuffelhut.jdbc;

import org.joda.time.*;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Friedrich Schäuffelhut
 * @since 2017-05-16
 */
public class ResultTypesTest
{
    private int uniqueIndex = (int) (System.currentTimeMillis() % 128);
    private int uniqueInt = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    private ResultSet rs = Mockito.mock( ResultSet.class );

    // ==================== Boolean ====================

    @Test
    public void Boolean() throws Exception
    {
        ResultTypes.Boolean.getResult( rs, uniqueIndex );
        verify( rs ).getBoolean( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Boolean_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Boolean.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Boolean_true() throws Exception
    {
        when( rs.getBoolean( Matchers.anyInt() ) ).thenReturn( true );
        assertTrue( ResultTypes.Boolean.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Boolean_false() throws Exception
    {
        when( rs.getBoolean( Matchers.anyInt() ) ).thenReturn( false );
        assertFalse( ResultTypes.Boolean.getResult( rs, uniqueIndex ) );
    }


    // ==================== BooleanAsInteger ====================

    @Test
    public void BooleanAsInteger() throws Exception
    {
        ResultTypes.BooleanAsInteger.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void BooleanAsInteger_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.BooleanAsInteger.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void BooleanAsInteger_true() throws Exception
    {
        when( rs.getInt( Matchers.anyInt() ) ).thenReturn( 1 );
        assertTrue( ResultTypes.BooleanAsInteger.getResult( rs, uniqueIndex ) );
    }


    @Test
    public void BooleanAsInteger_false() throws Exception
    {
        when( rs.getInt( Matchers.anyInt() ) ).thenReturn( 0 );
        assertFalse( ResultTypes.BooleanAsInteger.getResult( rs, uniqueIndex ) );
    }


    // ==================== Byte ====================

    @Test
    public void Byte() throws Exception
    {
        ResultTypes.Byte.getResult( rs, uniqueIndex );
        verify( rs ).getByte( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Byte_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Byte.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Byte_values() throws Exception
    {
        for (byte b = 0; b < Byte.MAX_VALUE; b++)
        {
            when( rs.getByte( Matchers.anyInt() ) ).thenReturn( b );
            assertEquals( (Byte) b, ResultTypes.Byte.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Char ====================

    @Test
    public void Char() throws Exception
    {
        ResultTypes.Character.getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void Char_null() throws Exception
    {
        when( rs.getString( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.Character.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Char_values() throws Exception
    {
        for (char b : new char[]{0, Character.MAX_VALUE})
        {
            when( rs.getString( Matchers.anyInt() ) ).thenReturn( Character.toString( b ) );
            assertEquals( (Character) b, ResultTypes.Character.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Short ====================

    @Test
    public void Short() throws Exception
    {
        ResultTypes.Short.getResult( rs, uniqueIndex );
        verify( rs ).getShort( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Short_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Short.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Short_values() throws Exception
    {
        for (Short s : new Short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE})
        {
            when( rs.getShort( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( (Short) s, ResultTypes.Short.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Integer ====================

    @Test
    public void Integer() throws Exception
    {
        ResultTypes.Integer.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Integer_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Integer.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Integer_values() throws Exception
    {
        for (Integer s : new Integer[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE})
        {
            when( rs.getInt( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( (Integer) s, ResultTypes.Integer.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Long ====================

    @Test
    public void Long() throws Exception
    {
        ResultTypes.Long.getResult( rs, uniqueIndex );
        verify( rs ).getLong( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Long_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Long.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Long_values() throws Exception
    {
        for (Long s : new Long[]{Long.MIN_VALUE, 0L, Long.MAX_VALUE})
        {
            when( rs.getLong( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( (Long) s, ResultTypes.Long.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Float ====================

    @Test
    public void Float() throws Exception
    {
        ResultTypes.Float.getResult( rs, uniqueIndex );
        verify( rs ).getFloat( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Float_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Float.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Float_values() throws Exception
    {
        for (Float s : new Float[]{Float.MIN_VALUE, 0.0f, Float.MAX_VALUE})
        {
            when( rs.getFloat( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( (Float) s, ResultTypes.Float.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Double ====================

    @Test
    public void Double() throws Exception
    {
        ResultTypes.Double.getResult( rs, uniqueIndex );
        verify( rs ).getDouble( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Double_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Double.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Double_values() throws Exception
    {
        for (Double s : new Double[]{Double.MIN_VALUE, 0.0d, Double.MAX_VALUE})
        {
            when( rs.getDouble( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( (Double) s, ResultTypes.Double.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== BigDecimal ====================

    @Test
    public void BigDecimal() throws Exception
    {
        ResultTypes.BigDecimal.getResult( rs, uniqueIndex );
        verify( rs ).getBigDecimal( uniqueIndex );
    }

    @Test
    public void BigDecimal_null() throws Exception
    {
        when( rs.getBigDecimal( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.BigDecimal.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void BigDecimal_values() throws Exception
    {
        for (BigDecimal s : new BigDecimal[]{BigDecimal.valueOf( Double.MIN_VALUE ), BigDecimal.ZERO, BigDecimal.valueOf( Double.MAX_VALUE )})
        {
            when( rs.getBigDecimal( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( (BigDecimal) s, ResultTypes.BigDecimal.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== String ====================

    @Test
    public void String() throws Exception
    {
        ResultTypes.String.getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void String_null() throws Exception
    {
        when( rs.getString( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.String.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void String_values() throws Exception
    {
        for (String s : new String[]{"", "NOT EMPTY"})
        {
            when( rs.getString( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( s, ResultTypes.String.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Date ====================

    @Test
    public void Date() throws Exception
    {
        ResultTypes.Date.getResult( rs, uniqueIndex );
        verify( rs ).getDate( uniqueIndex );
    }

    @Test
    public void Date_null() throws Exception
    {
        when( rs.getDate( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.Date.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Date_values() throws Exception
    {
        for (Date s : new Date[]{new Date( System.currentTimeMillis() )})
        {
            when( rs.getDate( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( s, ResultTypes.Date.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Timestamp ====================

    @Test
    public void Timestamp() throws Exception
    {
        ResultTypes.Timestamp.getResult( rs, uniqueIndex );
        verify( rs ).getTimestamp( uniqueIndex );
    }

    @Test
    public void Timestamp_null() throws Exception
    {
        when( rs.getTimestamp( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.Timestamp.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Timestamp_values() throws Exception
    {
        for (Timestamp s : new Timestamp[]{new Timestamp( System.currentTimeMillis() )})
        {
            when( rs.getTimestamp( Matchers.anyInt() ) ).thenReturn( s );
            assertEquals( s, ResultTypes.Timestamp.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Object ====================

    @Test
    public void Object() throws Exception
    {
        ResultTypes.Object.getResult( rs, uniqueIndex );
        verify( rs ).getObject( uniqueIndex );
    }

    @Test
    public void Object_null() throws Exception
    {
        when( rs.getObject( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.Object.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Object_values() throws Exception
    {
        for (Object o : new Object[]{new Object()})
        {
            when( rs.getObject( Matchers.anyInt() ) ).thenReturn( o );
            assertEquals( o, ResultTypes.Object.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Class ====================

    @Test
    public void Class() throws Exception
    {
        ResultTypes.Class.getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void Class_null() throws Exception
    {
        when( rs.getString( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.Class.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Class_values() throws Exception
    {
        for (Class c : new Class[]{Object.class, Integer.class})
        {
            when( rs.getString( Matchers.anyInt() ) ).thenReturn( c.getName() );
            assertEquals( c, ResultTypes.Class.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Serializeable ====================

    @Test
    public void Serializeable() throws Exception
    {
        ResultTypes.Serializeable.getResult( rs, uniqueIndex );
        verify( rs ).getBytes( uniqueIndex );
    }

    @Test
    public void Serializeable_null() throws Exception
    {
        when( rs.getBytes( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.Serializeable.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Serializeable_values() throws Exception
    {
        when( rs.getBytes( Matchers.anyInt() ) ).thenReturn( new byte[]{-84, -19, 0, 5, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0, 5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112, 0, 0, 0, 1} );
        assertEquals( Integer.valueOf( 1 ), ResultTypes.Serializeable.getResult( rs, uniqueIndex ) );
    }

    // ==================== Bytes ====================

    @Test
    public void Bytes() throws Exception
    {
        ResultTypes.Bytes.getResult( rs, uniqueIndex );
        verify( rs ).getBytes( uniqueIndex );
    }

    @Test
    public void Bytes_null() throws Exception
    {
        when( rs.getBytes( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.Bytes.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Bytes_values() throws Exception
    {
        when( rs.getBytes( Matchers.anyInt() ) ).thenReturn( new byte[]{-1, 0, 1} );
        assertArrayEquals( new byte[]{-1, 0, 1}, ResultTypes.Bytes.getResult( rs, uniqueIndex ) );
    }


    // ==================== EnumByName ====================

    enum EnumByNameDummy
    {
        VALUE_A, VALUE_B
    }

    @Test
    public void EnumByName() throws Exception
    {
        ResultTypes.enumByName( EnumByNameDummy.class ).getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void EnumByName_null() throws Exception
    {
        when( rs.getString( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.enumByName( EnumByNameDummy.class ).getResult( rs, uniqueIndex ) );
    }

    @Test
    public void EnumByName_values() throws Exception
    {
        for (EnumByNameDummy e : EnumByNameDummy.values())
        {
            when( rs.getString( uniqueIndex ) ).thenReturn( e.name() );
            assertEquals( e, ResultTypes.enumByName( EnumByNameDummy.class ).getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== EnumByIntKey ====================

    enum EnumByIntKeyDummy implements IfcEnumIntKey
    {
        VALUE_A, VALUE_B;

        @Override
        public int getKey()
        {
            return ordinal();
        }
    }

    @Test
    public void EnumByIntKey() throws Exception
    {
        ResultTypes.enumByIntKey( EnumByIntKeyDummy.class ).getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void EnumByIntKey_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.enumByIntKey( EnumByIntKeyDummy.class ).getResult( rs, uniqueIndex ) );
    }

    @Test
    public void EnumByIntKey_values() throws Exception
    {
        for (EnumByIntKeyDummy e : EnumByIntKeyDummy.values())
        {
            when( rs.getInt( uniqueIndex ) ).thenReturn( e.getKey() );
            assertEquals( e, ResultTypes.enumByIntKey( EnumByIntKeyDummy.class ).getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== EnumByKey ====================

    enum EnumByKeyDummy implements IfcEnumKey
    {
        VALUE_A, VALUE_B;

        @Override
        public String getKey()
        {
            return "key-" + name();
        }
    }

    @Test
    public void EnumByKey() throws Exception
    {
        ResultTypes.enumByKey( EnumByKeyDummy.class, ResultTypes.String ).getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void EnumByKey_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.enumByKey( EnumByKeyDummy.class, ResultTypes.String ).getResult( rs, uniqueIndex ) );
    }

    @Test
    public void EnumByKey_values() throws Exception
    {
        for (EnumByKeyDummy e : EnumByKeyDummy.values())
        {
            when( rs.getString( uniqueIndex ) ).thenReturn( e.getKey() );
            assertEquals( e, ResultTypes.enumByKey( EnumByKeyDummy.class, ResultTypes.String ).getResult( rs, uniqueIndex ) );
        }
    }


    // ==================== DateTime ====================

    @Test
    public void DateTime() throws Exception
    {
        ResultTypes.DateTime.getResult( rs, uniqueIndex );
        verify( rs ).getTimestamp( uniqueIndex );
    }

    @Test
    public void DateTime_null() throws Exception
    {
        when( rs.getTimestamp( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.DateTime.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void DateTime_values() throws Exception
    {
        for (DateTime o : new DateTime[]{new DateTime()})
        {
            when( rs.getTimestamp( Matchers.anyInt() ) ).thenReturn( new Timestamp( o.getMillis() ) );
            assertEquals( o, ResultTypes.DateTime.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== DateMidnight ====================

    @Test
    public void DateMidnight() throws Exception
    {
        ResultTypes.DateMidnight.getResult( rs, uniqueIndex );
        verify( rs ).getTimestamp( uniqueIndex );
    }

    @Test
    public void DateMidnight_null() throws Exception
    {
        when( rs.getTimestamp( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.DateMidnight.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void DateMidnight_values() throws Exception
    {
        for (DateMidnight o : new DateMidnight[]{new DateMidnight()})
        {
            when( rs.getTimestamp( Matchers.anyInt() ) ).thenReturn( new Timestamp( o.getMillis() ) );
            assertEquals( o, ResultTypes.DateMidnight.getResult( rs, uniqueIndex ) );
        }
    }


    // ==================== DateMidnightAsIsoString ====================

    @Test
    public void DateMidnightAsIsoString() throws Exception
    {
        ResultTypes.DateMidnightAsIsoString.getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void DateMidnightAsIsoString_null() throws Exception
    {
        when( rs.getString( uniqueIndex ) ).thenReturn( null );
        assertNull( ResultTypes.DateMidnightAsIsoString.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void DateMidnightAsIsoString_values() throws Exception
    {
        when( rs.getString( Matchers.anyInt() ) ).thenReturn( "2017-01-01" );
        assertEquals( new DateMidnight( "2017-01-01" ), ResultTypes.DateMidnightAsIsoString.getResult( rs, uniqueIndex ) );
    }


    // ==================== Days ====================

    @Test
    public void Days() throws Exception
    {
        ResultTypes.Days.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Days_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Days.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Days_values() throws Exception
    {
        for (Days s : new Days[]{Days.MIN_VALUE, Days.ZERO, Days.MAX_VALUE})
        {
            when( rs.getInt( Matchers.anyInt() ) ).thenReturn( s.getDays() );
            assertEquals( (Days) s, ResultTypes.Days.getResult( rs, uniqueIndex ) );
        }
    }


    // ==================== Hours ====================

    @Test
    public void Hours() throws Exception
    {
        ResultTypes.Hours.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Hours_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Hours.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Hours_values() throws Exception
    {
        for (Hours s : new Hours[]{Hours.MIN_VALUE, Hours.ZERO, Hours.MAX_VALUE})
        {
            when( rs.getInt( Matchers.anyInt() ) ).thenReturn( s.getHours() );
            assertEquals( (Hours) s, ResultTypes.Hours.getResult( rs, uniqueIndex ) );
        }
    }


    // ==================== Minutes ====================

    @Test
    public void Minutes() throws Exception
    {
        ResultTypes.Minutes.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Minutes_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Minutes.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Minutes_values() throws Exception
    {
        for (Minutes s : new Minutes[]{Minutes.MIN_VALUE, Minutes.ZERO, Minutes.MAX_VALUE})
        {
            when( rs.getInt( Matchers.anyInt() ) ).thenReturn( s.getMinutes() );
            assertEquals( (Minutes) s, ResultTypes.Minutes.getResult( rs, uniqueIndex ) );
        }
    }


    // ==================== Seconds ====================

    @Test
    public void Seconds() throws Exception
    {
        ResultTypes.Seconds.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Seconds_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Seconds.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Seconds_values() throws Exception
    {
        for (Seconds s : new Seconds[]{Seconds.MIN_VALUE, Seconds.ZERO, Seconds.MAX_VALUE})
        {
            when( rs.getInt( Matchers.anyInt() ) ).thenReturn( s.getSeconds() );
            assertEquals( (Seconds) s, ResultTypes.Seconds.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== Weeks ====================

    @Test
    public void Weeks() throws Exception
    {
        ResultTypes.Weeks.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Weeks_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Weeks.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Weeks_values() throws Exception
    {
        for (Weeks s : new Weeks[]{Weeks.MIN_VALUE, Weeks.ZERO, Weeks.MAX_VALUE})
        {
            when( rs.getInt( Matchers.anyInt() ) ).thenReturn( s.getWeeks() );
            assertEquals( (Weeks) s, ResultTypes.Weeks.getResult( rs, uniqueIndex ) );
        }
    }


    // ==================== Years ====================

    @Test
    public void Years() throws Exception
    {
        ResultTypes.Years.getResult( rs, uniqueIndex );
        verify( rs ).getInt( uniqueIndex );
        verify( rs ).wasNull();
    }

    @Test
    public void Years_null() throws Exception
    {
        when( rs.wasNull() ).thenReturn( true );
        assertNull( ResultTypes.Years.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void Years_values() throws Exception
    {
        for (Years s : new Years[]{Years.MIN_VALUE, Years.ZERO, Years.MAX_VALUE})
        {
            when( rs.getInt( Matchers.anyInt() ) ).thenReturn( s.getYears() );
            assertEquals( (Years) s, ResultTypes.Years.getResult( rs, uniqueIndex ) );
        }
    }

    // ==================== PeriodIsoEncoded ====================

    @Test
    public void PeriodIsoEncoded() throws Exception
    {
        ResultTypes.PeriodIsoEncoded.getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void PeriodIsoEncoded_null() throws Exception
    {
        when( rs.getString( Matchers.anyInt())).thenReturn( null );
        assertNull( ResultTypes.PeriodIsoEncoded.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void PeriodIsoEncoded_values() throws Exception
    {
            when( rs.getString( Matchers.anyInt() ) ).thenReturn( "P10D" );
            assertEquals( Period.days( 10 ), ResultTypes.PeriodIsoEncoded.getResult( rs, uniqueIndex ) );
    }

    // ==================== DateTimeZone ====================

    @Test
    public void DateTimeZone() throws Exception
    {
        ResultTypes.DateTimeZone.getResult( rs, uniqueIndex );
        verify( rs ).getString( uniqueIndex );
    }

    @Test
    public void DateTimeZone_null() throws Exception
    {
        when( rs.getString( Matchers.anyInt())).thenReturn( null );
        assertNull( ResultTypes.DateTimeZone.getResult( rs, uniqueIndex ) );
    }

    @Test
    public void DateTimeZone_values() throws Exception
    {
            when( rs.getString( Matchers.anyInt() ) ).thenReturn( "UTC" );
            assertEquals( DateTimeZone.UTC, ResultTypes.DateTimeZone.getResult( rs, uniqueIndex ) );
    }
}