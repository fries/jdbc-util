/*
 * $Header: /home/cvs/pme/PME-Client/src/main/java/com/eonis/pme/client/kernel/utils/jdbc/Result.java,v 1.1 2008/04/30 09:06:31 F7152 Exp $
 * 
 * $Revision: 1.1 $ <br>
 * $Name:  $
 * $Date: 2008/04/30 09:06:31 $<br>
 * $Author: F7152 $<br>
 *
 * History: <br>
 * $Log: Result.java,v $
 * Revision 1.1  2008/04/30 09:06:31  F7152
 * Initial Checkin
 *
 * Revision 1.1  2008/04/14 10:36:45  R15802
 * dbcUtil moved
 *
 */
package de.schaeuffelhut.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

//TODO: rename, eg IfcResultReader 
public interface IfcResultType<T>
{
    public abstract T getResult(ResultSet resultSet, int index) throws SQLException;
}