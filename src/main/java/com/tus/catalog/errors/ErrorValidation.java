package com.tus.catalog.errors;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.tus.catalog.model.Book;

@Component
public class ErrorValidation {

	private static final List<String> BLACKLISTED_AUTHORS = Arrays.asList("Stephenie Meyer", "E.L. James");

	public static void validateBook(Book book) {
		if (book.getPrice() < 50.00) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be at least 50.00.");
		}
		if (BLACKLISTED_AUTHORS.contains(book.getAuthor())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author is not allowed.");
		}
		if (book.getTitle().length() < 30) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must be at least 30 characters long.");
		}
	}

}
