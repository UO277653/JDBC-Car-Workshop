package uo.ri.cws.application.persistence.recommendation;

import java.util.List;

import uo.ri.cws.application.persistence.Gateway;

public interface RecommendationGateway extends Gateway<RecommendationRecord> {

	List<RecommendationRecord> findRecommendedIdBySponsorIdNotUsedForVoucher(String idSponsor);
	
	void updateUseVoucherForClient(String idClient);
}
