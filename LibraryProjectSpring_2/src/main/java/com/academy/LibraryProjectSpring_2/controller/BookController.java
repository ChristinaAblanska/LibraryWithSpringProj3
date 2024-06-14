package com.academy.LibraryProjectSpring_2.controller;

import com.academy.LibraryProjectSpring_2.dto.BookDTO;
import com.academy.LibraryProjectSpring_2.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/findByID/{bookID}")
    public BookDTO findBookByID(@PathVariable long bookID) {
        return bookService.findBookByID(bookID);
    }

    @GetMapping("/findByTitle")
    public List<BookDTO> findBookByTitle(@RequestParam("bookTitle") String bookTitle) {
        return bookService.findBookByTitle(bookTitle);
    }

    @GetMapping("/findByAuthor")
    public List<BookDTO> findBookByAuthor(@RequestParam("authorName") String authorName) {
        return bookService.findBookByAuthor(authorName);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createBook(@RequestBody BookDTO bookDTO) {
        bookService.createBook(bookDTO);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateBookRating(@RequestBody BookDTO bookDTO) {
        bookService.updateBookAccess(bookDTO);
    }


}
