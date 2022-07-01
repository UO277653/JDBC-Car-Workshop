package uo.ri.cws.application.persistence.voucher.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import alb.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.persistence.voucher.VoucherRecord;

public class VoucherGatewayImpl implements VoucherGateway{

	Conf conf = Conf.getInstance();
	
	@Override
	public void add(VoucherRecord t) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;

		try {
			c = Jdbc.getCurrentConnection(); // we share the connection
			//
			pst = c.prepareStatement(conf.getProperty("TVOUCHERS_ADD"));
			pst.setString(1, t.id);
			pst.setDouble(2, t.available);
			pst.setString(3, t.code);
			pst.setString(4, t.description);
			
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
			
			pst = c.prepareStatement(conf.getProperty("TVOUCHERS_REMOVE"));
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
	public void update(VoucherRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<VoucherRecord> findById(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<VoucherRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TVOUCHERS_FINDBYID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toVoucherRecordOptionalGateway(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

	@Override
	public List<VoucherRecord> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<VoucherRecord> findByCode(String code) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<VoucherRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TVOUCHERS_FINDBYCODE"));
			pst.setString(1, code);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toVoucherRecordOptional(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

}
