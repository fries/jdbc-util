///*
// * Copyright (c) 2024 Schäuffelhut Berger GmbH. All rights reserved.
// *
// * This program is proprietary and confidential.
// * Unauthorized use, distribution, or reproduction is prohibited.
// * This program is provided to clients/customers for use
// * according to the terms and conditions of the agreement
// * between Schäuffelhut Berger GmbH and the client/customer.
// */
//
//package de.schaeuffelhut.jdbc;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.datasource.DataSourceUtils;
//import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
//import org.springframework.jdbc.support.SQLExceptionTranslator;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
///**
// * @author Friedrich Schäuffelhut
// * @since 2017-08-25
// */
//public class SpringStatementUtil extends AbstractStatementUtil
//{
//    private final DataSource dataSource;
//    private final SQLExceptionTranslator exceptionTranslator;
//
//    public SpringStatementUtil(DataSource dataSource)
//    {
//        this.dataSource = dataSource;
//        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator( dataSource );
//    }
//
//    protected <R, E extends Exception> R execute(Transactional<R, E> transactional, String task, String sql)
//    {
//        if (!TransactionSynchronizationManager.isActualTransactionActive())
//            throw new IllegalStateException( "Not in transaction" );
//
//        Connection connection = DataSourceUtils.getConnection( dataSource );
//        try
//        {
//            return transactional.execute( connection );
//        }
//        catch (RuntimeException e)
//        {
//            throw e;
//        }
//        catch (Exception e)
//        {
//            if (e instanceof SQLException)
//            {
//                DataAccessException dataAccessException = translate( task, sql, (SQLException) e );
//                if (dataAccessException != null)
//                    throw dataAccessException;
//            }
//            throw new RuntimeException( e );
//        }
//        finally
//        {
//            DataSourceUtils.releaseConnection( connection, dataSource );
//        }
//    }
//
//    protected DataAccessException translate(String task, String sql, SQLException e)
//    {
//        return exceptionTranslator.translate( task, sql, e );
//    }
//
//}
