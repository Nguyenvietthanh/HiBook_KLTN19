package ute.hibook.service;

import java.util.List;

import ute.hibook.dto.ConvertPromotionDTO;
import ute.hibook.dto.PromotionDTO;


public interface PromotionService {

	public void addPromotion(PromotionDTO promotionDTO);
	public void updatePromotion(PromotionDTO promotionDTO);
	public void deletePromotion(int idPromotion);
	public ConvertPromotionDTO getPromotionById(int idPromotion);
	public List<ConvertPromotionDTO> getAllPromotion();
	public List<PromotionDTO> getAllPromotions();
	public List<PromotionDTO> getAllPromotiondate();
}
