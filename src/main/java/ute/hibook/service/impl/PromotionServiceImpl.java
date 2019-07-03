package ute.hibook.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ute.hibook.dao.imp.PromotionDaoImpl;
import ute.hibook.dto.BookDTO;
import ute.hibook.dto.ConvertPromotionDTO;
import ute.hibook.dto.PromotionDTO;
import ute.hibook.entity.Book;
import ute.hibook.entity.Promotion;
import ute.hibook.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	PromotionDaoImpl promotionDao;

	public void addPromotion(PromotionDTO promotionDTO) {

		Promotion promotion = new Promotion();
		promotion.setContentPromotion(promotionDTO.getContentPromotion());
		promotion.setPicPromotion(promotionDTO.getPicPromotion());
		promotion.setSaleOff(promotionDTO.getSaleOff());
		promotion.setTimeEnd(promotionDTO.getTimeEnd());
		promotion.setTimeStart(promotionDTO.getTimeStart());
		promotion.setTitlePromotion(promotionDTO.getTitlePromotion());

		promotionDao.addPromotion(promotion);
		System.out.println("add Promotion successful!");
	}

	public void updatePromotion(PromotionDTO promotionDTO) {
		Promotion promotion = promotionDao.getPromotionById(promotionDTO.getIdPromotion());
		if (promotion != null) {
			promotion.setContentPromotion(promotionDTO.getContentPromotion());
			promotion.setPicPromotion(promotionDTO.getPicPromotion());
			promotion.setSaleOff(promotionDTO.getSaleOff());
			promotion.setTimeEnd(promotionDTO.getTimeEnd());
			promotion.setTimeStart(promotionDTO.getTimeStart());
			promotion.setTitlePromotion(promotionDTO.getTitlePromotion());

			promotionDao.updatePromotion(promotion);
			System.out.println("update Promotion successful!");
		}
	}

	public void deletePromotion(int idPromotion) {
		Promotion promotion = promotionDao.getPromotionById(idPromotion);
		if (promotion != null) {
			promotionDao.deletePromotion(idPromotion);
			System.out.println("delete Promotion successful!");
		}
	}

	public ConvertPromotionDTO getPromotionById(int idPromotion) {
		Promotion promotion = promotionDao.getPromotionById(idPromotion);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		ConvertPromotionDTO promotionDTO = new ConvertPromotionDTO();
		promotionDTO.setIdPromotion(promotion.getIdPromotion());
		promotionDTO.setContentPromotion(promotion.getContentPromotion());
		promotionDTO.setPicPromotion(promotion.getPicPromotion());
		promotionDTO.setSaleOff(promotion.getSaleOff());
		String dateStart;
		String dateEnd;
		Date convertedCurrentDateStart;
		Date convertedCurrentDateEnd;
		try {
			convertedCurrentDateStart = sdf.parse(promotion.getTimeStart().toString());
			convertedCurrentDateEnd = sdf.parse(promotion.getTimeEnd().toString());
			String date1 = sdf.format(convertedCurrentDateStart);
			String date2 = sdf.format(convertedCurrentDateEnd);
			dateStart = date1;
			dateEnd = date2;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			dateStart = "";
			dateEnd = "";

			e.printStackTrace();
		}
		promotionDTO.setTimeEnd(dateEnd);
		promotionDTO.setTimeStart(dateStart);
		promotionDTO.setTitlePromotion(promotion.getTitlePromotion());
		List<BookDTO> bookDTOs = new ArrayList<BookDTO>();
		for (Book book : promotion.getBooks()) {
			BookDTO bookDto = new BookDTO();
			bookDto.setIdBook(book.getIdBook());
			bookDto.setNameBook(book.getNameBook());
			bookDTOs.add(bookDto);
		}
		promotionDTO.setBooks(bookDTOs);
		// System.out.println(promotion.getBooks().get(0).getNameBook());

		return promotionDTO;
	}

	public List<ConvertPromotionDTO> getAllPromotion() {
		List<Promotion> lstPromotion = promotionDao.getAllPromotion();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<ConvertPromotionDTO> lstPromotionDTO = new ArrayList<ConvertPromotionDTO>();
		for (Promotion promotion : lstPromotion) {

			ConvertPromotionDTO promotionDTO = new ConvertPromotionDTO();
			promotionDTO.setIdPromotion(promotion.getIdPromotion());
			promotionDTO.setContentPromotion(promotion.getContentPromotion());
			promotionDTO.setPicPromotion(promotion.getPicPromotion());
			promotionDTO.setSaleOff(promotion.getSaleOff());
			String dateStart;
			String dateEnd;
			Date convertedCurrentDateStart;
			Date convertedCurrentDateEnd;
			try {
				convertedCurrentDateStart = sdf.parse(promotion.getTimeStart().toString());
				convertedCurrentDateEnd = sdf.parse(promotion.getTimeEnd().toString());
				String date1 = sdf.format(convertedCurrentDateStart);
				String date2 = sdf.format(convertedCurrentDateEnd);
				dateStart = date1;
				dateEnd = date2;

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				dateStart = "";
				dateEnd = "";

				e.printStackTrace();
			}
			promotionDTO.setTimeEnd(dateEnd);
			promotionDTO.setTimeStart(dateStart);
			promotionDTO.setTitlePromotion(promotion.getTitlePromotion());
			List<BookDTO> bookDTOs = new ArrayList<BookDTO>();
			for (Book book : promotion.getBooks()) {
				BookDTO bookDto = new BookDTO();
				bookDto.setIdBook(book.getIdBook());
				bookDto.setNameBook(book.getNameBook());
				bookDTOs.add(bookDto);
				promotionDTO.setBooks(bookDTOs);
			}

			lstPromotionDTO.add(promotionDTO);
		}
		return lstPromotionDTO;
	}

	public List<PromotionDTO> getAllPromotiondate() {
		List<Promotion> lstPromotionDate = promotionDao.getAllPromotionDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<PromotionDTO> lstPromotionDTOdate = new ArrayList<PromotionDTO>();
		for (Promotion promotion : lstPromotionDate) {

			PromotionDTO promotionDTO = new PromotionDTO();
			promotionDTO.setIdPromotion(promotion.getIdPromotion());
			promotionDTO.setContentPromotion(promotion.getContentPromotion());
			promotionDTO.setPicPromotion(promotion.getPicPromotion());
			promotionDTO.setSaleOff(promotion.getSaleOff());
			String a;
			Date convertedCurrentDate;
			try {
				convertedCurrentDate = sdf.parse(promotion.getTimeEnd().toString());
				String date = sdf.format(convertedCurrentDate);
				a = date;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				a = "0";
				e.printStackTrace();
			}
			System.out.println("nguyenvietthanh1197" + a);

			promotionDTO.setTimeEnd(promotion.getTimeEnd());
			promotionDTO.setTimeStart(promotion.getTimeStart());
			promotionDTO.setTitlePromotion(promotion.getTitlePromotion());

			List<BookDTO> bookDTOs = new ArrayList<BookDTO>();
			for (Book book : promotion.getBooks()) {
				BookDTO bookDto = new BookDTO();
				bookDto.setIdBook(book.getIdBook());
				bookDto.setNameBook(book.getNameBook());
				bookDTOs.add(bookDto);
			}
			promotionDTO.setBooks(bookDTOs);

			lstPromotionDTOdate.add(promotionDTO);
		}
		return lstPromotionDTOdate;
	}

	public List<PromotionDTO> getAllPromotions() {
		List<Promotion> lstPromotion = promotionDao.getAllPromotion();

		

		List<PromotionDTO> lstPromotionDTO = new ArrayList<PromotionDTO>();
		for (Promotion promotion : lstPromotion) {

			PromotionDTO promotionDTO = new PromotionDTO();
			promotionDTO.setIdPromotion(promotion.getIdPromotion());
			promotionDTO.setContentPromotion(promotion.getContentPromotion());
			promotionDTO.setPicPromotion(promotion.getPicPromotion());
			promotionDTO.setSaleOff(promotion.getSaleOff());
			promotionDTO.setTimeEnd(promotion.getTimeEnd());
			promotionDTO.setTimeStart(promotion.getTimeStart());
			promotionDTO.setTitlePromotion(promotion.getTitlePromotion());
			List<BookDTO> bookDTOs = new ArrayList<BookDTO>();
			for (Book book : promotion.getBooks()) {
				BookDTO bookDto = new BookDTO();
				bookDto.setIdBook(book.getIdBook());
				bookDto.setNameBook(book.getNameBook());
				bookDTOs.add(bookDto);
				promotionDTO.setBooks(bookDTOs);
			}

			lstPromotionDTO.add(promotionDTO);
		}
		return lstPromotionDTO;

	}

}
