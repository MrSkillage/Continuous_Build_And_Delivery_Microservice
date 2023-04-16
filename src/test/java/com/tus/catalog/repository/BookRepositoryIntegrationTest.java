package com.tus.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.tus.catalog.model.Book;

@SpringJUnitConfig
@DataJpaTest
public class BookRepositoryIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void testSaveAndFindById() {
		// Arrange
		Book book = new Book();
		book.setAuthor("Frank Herbert");
		book.setTitle("Dune");
		book.setDescription("Epic science fiction novel");
		book.setPrice(24.99);

		// Act
		Book savedBook = entityManager.persistAndFlush(book);
		Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

		// Assert
		assertThat(foundBook).isPresent();
		assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
		assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
	}

	@Test
	public void testDeleteById() {
		// Arrange
		Book book = new Book();
		book.setAuthor("Frank Herbert");
		book.setTitle("Dune");
		book.setDescription("Epic science fiction novel");
		book.setPrice(24.99);
		Book savedBook = entityManager.persistAndFlush(book);

		// Act
		bookRepository.deleteById(savedBook.getId());
		Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

		// Assert
		assertThat(foundBook).isNotPresent();
	}

	@Test
	public void testFindAllBooks() {
		// Act
		List<Book> books = bookRepository.findAll();

		// Assert
		assertEquals(6, books.size(), "Expected 4 books, but found: " + books.size());
	}

	@Test
	public void testFindBookByTitle() {
		// Arrange
		String title = "Dune";

		// Act
		Optional<Book> book = bookRepository.findByTitle(title);

		// Assert
		assertThat(book).isPresent();
		assertThat(book.get().getTitle()).isEqualTo(title);
		assertThat(book.get().getAuthor()).isEqualTo("Frank Herbert");
	}

	@Test
	public void testDeleteBooksByAuthor() {
		// Arrange
		String author = "Aldous Huxley";

		// Act
		bookRepository.deleteByAuthor(author);
		List<Book> books = bookRepository.findAll();

		// Assert
		assertEquals(5, books.size());
	}

	@Test
	public void testDeleteBooksByTitle() {
		// Arrange
		String title = "1984";

		// Act
		bookRepository.deleteByTitle(title);
		List<Book> books = bookRepository.findAll();

		// Assert
		assertEquals(5, books.size());
	}

}
