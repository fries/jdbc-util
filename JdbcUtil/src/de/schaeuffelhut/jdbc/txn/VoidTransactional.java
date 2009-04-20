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
package de.schaeuffelhut.jdbc.txn;

/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public interface VoidTransactional extends Transactional<Void>
{
	public Void run(TxnContext context) throws Exception;
}
