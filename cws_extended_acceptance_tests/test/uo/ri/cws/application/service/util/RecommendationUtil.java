package uo.ri.cws.application.service.util;

import java.util.UUID;

import uo.ri.cws.application.service.util.sql.AddRecommendationSqlUnitOfWork;

public class RecommendationUtil {

	private RecommendationDto dto = createDefaultDto();

	public class RecommendationDto {
		public String id;
		public Long version;
		
		public String sponsor_id;
		public String recommended_id;
		public boolean usedForVoucher;
	}
	
	private RecommendationDto createDefaultDto() {
		RecommendationDto adto = new RecommendationDto ();
		adto.id = UUID.randomUUID().toString();
		adto.version = 1L;
		adto.usedForVoucher = false;
		adto.recommended_id = UUID.randomUUID().toString();
		adto.sponsor_id = UUID.randomUUID().toString();
		return adto;
		
	}

	public RecommendationUtil withSponsor(String id) {
		this.dto.sponsor_id = id;
		return this;
	}


	public RecommendationUtil withClient(String id) {
		this.dto.recommended_id = id;
		return this;
	}


	public void register() {
		AddRecommendationSqlUnitOfWork work = new AddRecommendationSqlUnitOfWork(this.dto);
		work.execute();
	
		dto.id = work.get().id; 
				
	}
	
	public RecommendationDto get() {
		return this.dto;
	}

	public RecommendationUtil withUse(String string) {
		dto.usedForVoucher = Boolean.valueOf(string);
		return this;
	}
}
