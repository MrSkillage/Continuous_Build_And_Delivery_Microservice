package com.tus.catalog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tus.catalog.model.Book;
import com.tus.catalog.repository.BookRepository;
import com.tus.catalog.service.BookService;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired
	BookController bookController;
	
	@MockBean
	BookService bookService;
	
	@MockBean
	BookRepository bookRepo;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Test
	public void addBook_TestSuccess() {
		Book book = buildBook();
		Book savedBook = buildBook();
		when(bookService.createBook(book)).thenReturn(savedBook);	
		ResponseEntity resp = bookController.addBook(book);
		assertEquals(resp.getStatusCode(), HttpStatus.CREATED);
		Book bookAdded = (Book) resp.getBody();
		assertEquals(1L, bookAdded.getId());
		assertTrue(bookAdded.equals(savedBook));
	}
	
	@Test
	public void getBookById_TestSuccess() {
	    Long bookId = 1L;
	    Book book = buildBook();
	    book.setId(bookId);
	    when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));
	    ResponseEntity<Book> response = bookController.getBookById(bookId);
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(book, response.getBody());
	}
	
	@Test
	public void getBookById_TestNotFound() {
	    Long bookId = 1L;
	    when(bookService.getBookById(bookId)).thenReturn(Optional.empty());
	    ResponseEntity<Book> response = bookController.getBookById(bookId);
	    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    assertNull(response.getBody());
	}
	
	@Test
	public void getAllBooks_TestSuccess() {
	    List<Book> books = new ArrayList<>();
	    books.add(buildBook());
	    books.add(buildBook());
	    books.add(buildBook());
	    when(bookService.getAllBooks()).thenReturn(books);
	    ResponseEntity<List<Book>> response = bookController.getAllBooks();
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(books, response.getBody());
	}

	@Test
	public void deleteBook_TestSuccess() {
	    Long bookId = 1L;
	    when(bookService.getBookById(bookId)).thenReturn(Optional.of(buildBook()));
	    ResponseEntity<?> response = bookController.deleteBook(bookId);
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void deleteBook_TestNotFound() {
	    Long bookId = 1L;
	    when(bookService.getBookById(bookId)).thenReturn(Optional.empty());
	    ResponseEntity<?> response = bookController.deleteBook(bookId);
	    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	public Book buildBook() {
		Book book = new Book();
		book.setId(1L);
		book.setAuthor("Frank Herbert");
		book.setTitle("Dune");
		book.setDescription("Epic science fiction novel");
		book.setPrice(24.99);
		return book;
	}
	
	@Test
	public void updateBook_TestSuccess() {
	    Long bookId = 1L;
	    Book originalBook = buildBook();
	    originalBook.setId(bookId);
	    Book updatedBook = buildBook();
	    updatedBook.setId(bookId);
	    updatedBook.setTitle("New Title");
	    when(bookService.getBookById(bookId)).thenReturn(Optional.of(originalBook));
	    when(bookService.updateBook(bookId, updatedBook)).thenReturn(updatedBook);
	    ResponseEntity<?> response = bookController.updateBook(bookId, updatedBook);
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(updatedBook, response.getBody());
	}

	@Test
	public void updateBook_TestNotFound() {
	    Long bookId = 1L;
	    Book updatedBook = buildBook();
	    when(bookService.getBookById(bookId)).thenReturn(Optional.empty());
	    ResponseEntity<?> response = bookController.updateBook(bookId, updatedBook);
	    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	    assertNull(response.getBody());
	}
	
}
