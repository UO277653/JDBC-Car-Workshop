package uo.ri.cws.application.persistence.paymentmean.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alb.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentmeanRecord;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;

public class PaymentMeanGatewayImpl implements PaymentMeanGateway{

	Conf conf = Conf.getInstance(); 
	
	@Override
	public void add(PaymentmeanRecord t) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;

		try {
			c = Jdbc.getCurrentConnection(); // we share the connection
			//
			pst = c.prepareStatement(conf.getProperty("TPAYMENTMEANS_ADD"));
			pst.setString(1, t.id);
			pst.setString(2, t.dtype);
			pst.setDouble(3, t.accumulated);
			pst.setLong(4, t.version);
			pst.setString(5, t.client_id);
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
		finally {
			Jdbc.close(rs, pst);
		}
	}

	@Override
	public void remove(String id) {
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			
			pst = c.prepareStatement(conf.getProperty("TPAYMENTMEANS_REMOVE"));
			pst.setString(1, id);
			
			pst.executeUpdate();
			
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			Jdbc.close(rs, pst, c);
		}
	}

	@Override
	public void update(PaymentmeanRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<PaymentmeanRecord> findById(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<PaymentmeanRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TPAYMENTMEANS_FINDBYID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toPaymentmeanRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

	@Override
	public List<PaymentmeanRecord> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PaymentmeanRecord> findByClientId(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		List<PaymentmeanRecord> res = new ArrayList<>();
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TPAYMENTMEANS_FINDBYCLIENTID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			res = RecordAssembler.toPaymentMeanRecordList(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return res;
	}

	@Override
	public List<PaymentmeanRecord> findByClientIdVouchers(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		List<PaymentmeanRecord> res = new ArrayList<>();
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TPAYMENTMEANS_FINDBYCLIENTIDVOUCHERS"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			res = RecordAssembler.toPaymentMeanRecordList(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return res;
	}

}
