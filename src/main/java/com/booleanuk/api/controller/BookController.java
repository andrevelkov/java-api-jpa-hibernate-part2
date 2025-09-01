package com.booleanuk.api.controller;

import com.booleanuk.api.model.Book;
import com.booleanuk.api.model.Book;
import com.booleanuk.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repo;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            Book savedBook = repo.save(book);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create Book.");
        }
        return ResponseEntity.ok().body("Book created");
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnBook(@PathVariable int id) {
        Optional<Book> book = repo.findById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No books with that id were found.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnBook(@PathVariable int id, @RequestBody Book body) {
        if (body == null ||
                body.getGenre() == null || body.getGenre().trim().isEmpty() ||
                body.getTitle() == null || body.getTitle().trim().isEmpty() ||
                body.getAuthor() == null ||
                body.getPublisher() == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields must be provided and non-empty.");
        }

        Optional<Book> optionalBook = repo.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(body.getTitle());
            book.setGenre(body.getGenre());
            book.setAuthor(body.getAuthor());
            book.setPublisher(body.getPublisher());

            repo.save(book);

            return ResponseEntity.status(HttpStatus.CREATED).body("Book updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No books with that id were found.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No books with that id were found.");
        }

        repo.deleteById(id);
        return ResponseEntity.ok().body("Book deleted.");
    }

}
