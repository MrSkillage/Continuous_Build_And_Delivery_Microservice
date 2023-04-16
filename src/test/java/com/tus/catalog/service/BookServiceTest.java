package com.tus.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.tus.catalog.model.Book;
import com.tus.catalog.repository.BookRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class BookServiceTest {

	@Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;
    
    @Test
    public void getAllBooks_TestSuccess() {
        List<Book> books = new ArrayList<>();
        books.add(buildBook());
        Book book2 = buildBook();
        book2.setId(2L);
        books.add(book2);
        when(bookRepository.findAll()).thenReturn(books);
        List<Book> result = bookService.getAllBooks();
        assertEquals(books, result);
    }
    
    @Test
    public void getBookById_TestSuccess() {
        Book book = buildBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> result = bookService.getBookById(1L);
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }
    
    @Test
    public void createBook_TestSuccess() {
        Book book = buildBook();
        Book savedBook = buildBook();
        when(bookRepository.save(book)).thenReturn(savedBook);
        Book result = bookService.createBook(book);
        assertEquals(savedBook, result);
    }
    
    @Test
    public void UpdateBook_TestSuccess() {
        // Arrange
        Long bookId = 1L;
        Book existingBook = buildBook();
        Book updatedBook = buildBook();
        updatedBook.setTitle("Dune: Updated Edition");
        updatedBook.setPrice(25.99);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        // Act
        Book result = bookService.updateBook(bookId, updatedBook);

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(updatedBook);
        assertEquals(bookId, result.getId());
        assertEquals("Dune: Updated Edition", result.getTitle());
        assertEquals(25.99, result.getPrice());
    }
    
    @Test
    public void UpdateBook_TestFailure() {
        // Arrange
        Long bookId = 1L;
        Book updatedBook = buildBook();
        updatedBook.setTitle("Dune: Updated Edition");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act
        Book result = bookService.updateBook(bookId, updatedBook);

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(0)).save(updatedBook);
        assertNull(result);
    }
    
    @Test
    public void DeleteBookById_TestSuccess() {
        // Arrange
        Long bookId = 1L;
        Book existingBook = buildBook();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookRepository).deleteById(bookId);

        // Act
        boolean result = bookService.deleteBookById(bookId);

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
        assertTrue(result);
    }
    
    @Test
    public void DeleteBookById_TestFailure() {
        // Arrange
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act
        boolean result = bookService.deleteBookById(bookId);

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(0)).deleteById(bookId);
        assertFalse(result);
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
	
}
