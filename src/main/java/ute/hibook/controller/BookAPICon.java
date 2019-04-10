package ute.hibook.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ute.hibook.dto.BookDTO;
import ute.hibook.service.impl.BookServiceImpl;
@RestController
@RequestMapping(value = "/api/v1")
public class BookAPICon {
	
	@Autowired
	BookServiceImpl bookServiceImpl;
	
	@GetMapping(value="/books")
	public ResponseEntity<List<BookDTO>> getallBook(){
		ArrayList<BookDTO> arrayListBook=(ArrayList<BookDTO>) bookServiceImpl.getAllBook();
		
		if(arrayListBook.isEmpty()) {
			return new ResponseEntity<List<BookDTO>>(HttpStatus.NOT_FOUND);
		}
		 return new ResponseEntity<List<BookDTO>>(arrayListBook,HttpStatus.OK);
	}
	
	@GetMapping(value="/books/{idBook}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable int idBook){
		BookDTO book=bookServiceImpl.getBookById(idBook);
		
		if(null == book) {
			return new ResponseEntity<BookDTO>(HttpStatus.NOT_FOUND);
		}
		 return new ResponseEntity<BookDTO>(book,HttpStatus.OK);
	}

}