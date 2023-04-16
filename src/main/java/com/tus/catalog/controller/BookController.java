package com.tus.catalog.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.tus.catalog.errors.ErrorValidation;
import com.tus.catalog.model.Book;
import com.tus.catalog.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /*
    public void newMethod() {
    	System.out.println("A new method to Test the Webhook!");
    }
    */
    
    @GetMapping("")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(value = "id") Long bookId) {
        Optional<Book> book = bookService.getBookById(bookId);
        if(book.isPresent()) {
            return ResponseEntity.ok().body(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {    	
        Book savedBook = bookService.createBook(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedBook.getId())
                        .toUri();
        return ResponseEntity.created(location).body(savedBook);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        Optional<Book> book = bookService.getBookById(id);
        if (!book.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable(value = "id") Long bookId, 
    		@Valid @RequestBody Book bookDetails) {
    	Optional<Book> book = bookService.getBookById(bookId);
    	if (!book.isPresent()) {
    		return ResponseEntity.notFound().build();
    	}
    	Book updatedBook = bookService.updateBook(bookId, bookDetails);
    	return ResponseEntity.ok(updatedBook);
    }   
    
}
