package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import alb.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceRecord;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;

public class InvoiceGatewayImpl implements InvoiceGateway {

	Conf conf = Conf.getInstance();
	
	@Override
	public void add(InvoiceRecord t) { 
		
		PreparedStatement pst = null;
		Connection c = null;
		
		try {
			c = Jdbc.getCurrentConnection();

			pst = c.prepareStatement(conf.getProperty("TINVOICES_ADD"));
			pst.setString(1, t.id);
			pst.setLong(2, t.number);
			pst.setDate(3, java.sql.Date.valueOf(t.date));
			pst.setDouble(4, t.vat);
			pst.setDouble(5, t.amount);
			pst.setString(6, "NOT_YET_PAID");

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			Jdbc.close(pst);
		}
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(InvoiceRecord t) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<InvoiceRecord> findById(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<InvoiceRecord> result = null;
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TINVOICES_FINDBYID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toInvoiceRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

	@Override
	public List<InvoiceRecord> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<InvoiceRecord> findByNumber(Long number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getNextInvoiceNumber() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TINVOICES_GETNEXTINVOICENUMBER"));
			rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getLong(1) + 1; // +1, next
			} else { // there is none yet
				return 1L;
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} finally {
			Jdbc.close(rs, pst);
		}
	}

//	@Override
//	public List<InvoiceRecord> findInvoicesForVouchersOver500() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<InvoiceRecord> findInvoicesPaidNotUsed() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<InvoiceRecord> findPaid() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
