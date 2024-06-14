package com.academy.LibraryProjectSpring_2.controller;

import com.academy.LibraryProjectSpring_2.dto.BookDTO;
import com.academy.LibraryProjectSpring_2.dto.BookStatistic;
import com.academy.LibraryProjectSpring_2.dto.LibraryDTO;
import com.academy.LibraryProjectSpring_2.model.Library;
import com.academy.LibraryProjectSpring_2.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/libraries")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<Library> getAllLibraries() {
        return libraryService.getAllLibraries();
    }

    @GetMapping("/{id}")
    public Library getLibraryByID(@PathVariable long id) {
        return libraryService.getLibraryById(id);
    }

    @GetMapping("/users")
    public List<LibraryDTO> getAllUserLibraries() {
        return libraryService.getAllUserLibraries();
    }

    @GetMapping("/users/{id}")
    public LibraryDTO getUserLibraryByID(@PathVariable long id) {
        return libraryService.getUserLibraryById(id);
    }

    @GetMapping("/myLibrary")
    public List<BookDTO> getMyLibrary(@RequestParam("userID") long userID) {
        return libraryService.getMyLibrary(userID);
    }

    @GetMapping("/myLibrary/sortedByTitle")
    public List<BookDTO> getUserLibrarySortedByTitle(@RequestParam("userID") long userID) {
        return libraryService.getUserLibrarySortedByTitle(userID);
    }

    @GetMapping("/myLibrary/sortedByAuthor")
    public List<BookDTO> getUserLibrarySortedByAuthor(@RequestParam("userID") long userID) {
        return libraryService.getUserLibrarySortedByAuthor(userID);
    }

    @GetMapping("/myLibrary/sortedByGenre")
    public List<BookDTO> getUserLibrarySortedByGenre(@RequestParam("userID") long userID) {
        return libraryService.getUserLibrarySortedByGenre(userID);
    }

    @GetMapping("/lastRead")
    public List<BookDTO> getUserLibrarySortedByLastRead(@RequestParam("userID") long userID) {
        return libraryService.getUserLibraryLastRead(userID);
    }

    @GetMapping("/authorStatistics")
    public List<BookStatistic> getAuthorLibraryStatistics(@RequestParam("userID") long userID) {
        return libraryService.getAuthorLibrary(userID);
    }

    @PutMapping("/addBookToLibrary")
    public void addBookToLibrary(@RequestParam("userID") long userID, @RequestParam("bookTitle") String bookTitle,
                                 @RequestParam("authorName") String authorName, @RequestParam("readFlag") boolean readFlag) {
        libraryService.addBookToLibrary(userID, bookTitle, authorName, readFlag);
    }


}
