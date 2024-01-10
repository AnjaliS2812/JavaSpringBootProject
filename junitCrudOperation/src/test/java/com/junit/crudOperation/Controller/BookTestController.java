package com.junit.crudOperation.Controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.junit.crudOperation.BookController;
import com.junit.crudOperation.Entity.Book;
import com.junit.crudOperation.repo.BookRepository;


@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class BookTestController {
	
	private MockMvc mockMvc;
	
	ObjectMapper objectMapper=new ObjectMapper();
	ObjectWriter objectWriter=objectMapper.writer();
	
	@Mock
	private BookRepository bookRepository;
	
	@InjectMocks
	private BookController bookController;
	
	Book Record1=new Book(1L,"Atomic Habits","How to build better habits",8);
	Book Record2=new Book(2L,"Rich Dad Poor Dad","How to became rich",9);
	Book Record3=new Book(3L,"The power of Habits","Benifits of power of habbit",7);
	Book Record4=new Book(4L,"BhagwadGeeta","Reality of life ",10);

	@BeforeEach
	public void setUp() {
	MockitoAnnotations.initMocks(this);	
	this.mockMvc=MockMvcBuilders.standaloneSetup(bookController).build();
	}
	
	
	@Test
	public void getAllRecords() throws Exception
	{
		List<Book> records=new ArrayList<>(Arrays.asList(Record1,Record2,Record3,Record4));
		Mockito.when(bookRepository.findAll()).thenReturn(records);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/book")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(4)))
				.andExpect(jsonPath("$[2].name", is("The power of Habits")))
				.andExpect(jsonPath("$[1].name", is("Rich Dad Poor Dad")));
				
	}
	
	@Test
	public void getBookbyId() throws Exception{
		Mockito.when(bookRepository.findById(Record1.getBookId())).thenReturn(Optional.of(Record1));
		mockMvc.perform(MockMvcRequestBuilders
				.get("/book/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Atomic Habits")));
	}
	
	@Test
	public void createRecord() throws Exception{
	Book record=Book.builder()
			.BookId(5L)
			.name("Ramayan")
			.summary("Story of shri Ram")
			.rating(10)
			.build();
	Mockito.when(bookRepository.save(record)).thenReturn(record);
	String content=objectMapper.writeValueAsString(record);
	
	MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders.post("/book")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(content);
			
	
	mockMvc.perform(mockRequest)
	.andExpect(status().isOk())
	.andExpect(jsonPath("$", notNullValue()))
	.andExpect(jsonPath("$.name",is("Ramayan")));
	
	}

	@Test
	public void updateBookRecord() throws Exception{
	Book updateRecord=Book.builder()
			.BookId(1L)
			.name("Update the book name")
			.summary("updated summary")
			.rating(8)
			.build();
			
	Mockito.when(bookRepository.findById(Record1.getBookId())).thenReturn(Optional.of(Record1));
	Mockito.when(bookRepository.save(updateRecord)).thenReturn(updateRecord);
	String updatedContent=objectMapper.writeValueAsString(updateRecord);
	MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders.put("/book")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(updatedContent);
	
	 mockMvc.perform(mockRequest)
	.andExpect(status().isOk())
	.andExpect(jsonPath("$", notNullValue()))
	.andExpect(jsonPath("$.name",is("Update the book name")));
	
	}
	
	@Test
	public void deleteBookById() throws Exception{
	Mockito.when(bookRepository.findById(Record2.getBookId())).thenReturn(Optional.of(Record2));
	mockMvc.perform(MockMvcRequestBuilders
			.delete("/book/2",Record2.getBookId())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
	
			
	}
}
