package uo.ri.cws.application.persistence.creditcard.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.ZoneId;

import alb.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.creditcard.CreditCardRecord;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;

public class CreditCardGatewayImpl implements CreditCardGateway{

	Conf conf = Conf.getInstance();
	
	@Override
	public void add(CreditCardRecord card) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;

		try {
			c = Jdbc.getCurrentConnection(); // we share the connection
			//
			pst = c.prepareStatement(conf.getProperty("TCREDITCARDS_ADD"));
			pst.setString(1, card.id);
			pst.setString(2, card.number);
			pst.setString(3, card.type);
			pst.setObject(4, Date.from(card.validthru.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			
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
			
			pst = c.prepareStatement(conf.getProperty("TCREDITCARDS_REMOVE"));
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
	public void update(CreditCardRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<CreditCardRecord> findById(String id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<CreditCardRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TCREDITCARDS_FINDBYID"));
			pst.setString(1, id);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toCreditCardRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

	@Override
	public List<CreditCardRecord> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<CreditCardRecord> findByNumber(String number) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		Optional<CreditCardRecord> result = null;
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TCREDITCARDS_FINDBYNUMBER"));
			pst.setString(1, number);
			rs = pst.executeQuery();
			
			result = RecordAssembler.toCreditCardRecord(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return result;
	}

}
