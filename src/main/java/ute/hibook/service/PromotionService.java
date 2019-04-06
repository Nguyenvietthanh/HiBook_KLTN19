package ute.hibook.service;

import java.util.List;

import ute.hibook.dto.PromotionDTO;


public interface PromotionService {

	public void addPromotion(PromotionDTO promotionDTO);
	public void updatePromotion(PromotionDTO promotionDTO);
	public void deletePromotion(int idPromotion);
	public PromotionDTO getPromotionById(int idPromotion);
	public List<PromotionDTO> getAllPromotion();
}