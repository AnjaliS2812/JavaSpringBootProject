package com.junit.crudOperation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.junit.crudOperation.Entity.Book;
import com.junit.crudOperation.repo.BookRepository;

import jakarta.validation.Valid;

@RestController
public class BookController {

	@Autowired
	private BookRepository bookRepository;
	
	@GetMapping("/book")
	public List<Book>getAllBookRecord()
	{
		return bookRepository.findAll();
	}
	
	@GetMapping("/book/{bookId}")
	public Book getBokById(@PathVariable (value="bookId") Long bookId)
	{
		return bookRepository.findById(bookId).get();
	}
	
	@PostMapping("/book")
	public Book createBookRecord(@RequestBody
			 @Valid Book book)
	{
		return bookRepository.save(book);
	}
	
	@PutMapping("/book")
	public Book updateBookRecord(@RequestBody
			@Valid Book book) throws Exception {
	
		if(book==null|| book.getBookId()==null)
		{
			throw new Exception("Book Id must not be null");
		}
		Optional<Book> optionalBook=bookRepository.findById(book.getBookId());
		
		if(!optionalBook.isPresent())
		{
			throw new Exception("Book with Id"+book.getBookId()+"does not exist");
		}
		Book existingbook=optionalBook.get();
		existingbook.setName(book.getName());
		existingbook.setSummary(book.getSummary());
		existingbook.setRating(book.getRating());
		return bookRepository.save(existingbook);
		
	}
	@DeleteMapping("/book/{id}")
	public void deleteBookById(@PathVariable (value="id")Long BookId)throws Exception
	{
		if(!bookRepository.findById(BookId).isPresent())
		{
			throw new Exception("BookId" +BookId+"not present");
		}
		bookRepository.deleteById(BookId);
		}
	}
	

