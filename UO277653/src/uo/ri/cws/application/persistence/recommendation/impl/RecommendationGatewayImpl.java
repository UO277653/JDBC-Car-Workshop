package uo.ri.cws.application.persistence.recommendation.impl;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import alb.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationRecord;
import uo.ri.cws.application.persistence.util.Conf;
import uo.ri.cws.application.persistence.util.RecordAssembler;

public class RecommendationGatewayImpl implements RecommendationGateway {

	Conf conf = Conf.getInstance(); 
	
	@Override
	public void add(RecommendationRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(RecommendationRecord t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<RecommendationRecord> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RecommendationRecord> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RecommendationRecord> findRecommendedIdBySponsorIdNotUsedForVoucher(String idSponsor) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection c = null;
		List<RecommendationRecord> res = new ArrayList<>();
		
		try {
			c = Jdbc.getCurrentConnection();
			pst = c.prepareStatement(conf.getProperty("TRECOMMENDATIONS_FINDRECOMMENDEDBYSPONSORIDNOTUSEDFORVOUCHER"));
			pst.setString(1, idSponsor);
			rs = pst.executeQuery();
			
			res = RecordAssembler.toRecommendationRecordList(rs);
			
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		} 
		finally {
			Jdbc.close(rs,pst);
		}
		
		return res;
	}

	@Override
	public void updateUseVoucherForClient(String idRecommended) {
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			
			pst = c.prepareStatement(conf.getProperty("TRECOMMENDATIONS_UPDATEUSEVOUCHERFORCLIENT"));
			pst.setString(1,idRecommended);
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			Jdbc.close(rs, pst, c);
		}
	}

}
