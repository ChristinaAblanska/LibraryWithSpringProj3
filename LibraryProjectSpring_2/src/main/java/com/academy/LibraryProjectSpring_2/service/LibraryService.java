package com.academy.LibraryProjectSpring_2.service;

import com.academy.LibraryProjectSpring_2.dto.BookDTO;
import com.academy.LibraryProjectSpring_2.dto.BookStatistic;
import com.academy.LibraryProjectSpring_2.dto.LibraryDTO;
import com.academy.LibraryProjectSpring_2.excetionHandling.BusinessNotFound;
import com.academy.LibraryProjectSpring_2.model.Library;
import com.academy.LibraryProjectSpring_2.repository.BookRepository;
import com.academy.LibraryProjectSpring_2.repository.LibraryRepository;
import com.academy.LibraryProjectSpring_2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public LibraryService(LibraryRepository libraryRepository, BookRepository bookRepository,
                          UserRepository userRepository, UserService userService) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Library> getAllLibraries() {
        return libraryRepository.getAllLibraries();
    }

    public Library getLibraryById(long libraryID) {
        if (isValidLibraryID(libraryID)) {
            return libraryRepository.getLibraryByID(libraryID);
        } else {
            throw new BusinessNotFound("Library with ID: " + libraryID + " does not exist!");
        }

    }

    public List<LibraryDTO> getAllUserLibraries() {
        return libraryRepository.getAllUserLibraries();
    }

    public LibraryDTO getUserLibraryById(long userID) {
        if (userService.isValidUserID(userID)) {
            return libraryRepository.getUserLibraryByID(userID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public void addBookToLibrary(long userID, String bookName, String authorName, boolean readFlag) {
        long libraryID = libraryRepository.getLibraryIDByUserID(userID);
        long authorID = userRepository.getAuthorID(authorName);
        long bookID = bookRepository.findBookByNameAndAuthor(bookName, authorID);
        libraryRepository.addBookToLibrary(libraryID, bookID, readFlag, LocalDateTime.now());
    }


    public List<BookDTO> getMyLibrary(long userID) {
        if (userService.isValidUserID(userID)) {
            long libraryID = libraryRepository.getLibraryIDByUserID(userID);
            return libraryRepository.getUserLibrary(libraryID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public long getAuthorID(String authorName) {
        long authorID = userRepository.getAuthorID(authorName);
        if (authorID > 0) {
            return authorID;
        } else {
            throw new BusinessNotFound("Author with name: " + authorName + " does not exist!");
        }
    }

    public List<BookDTO> getUserLibrarySortedByTitle(long userID) {
        if (userService.isValidUserID(userID)) {
            long libraryID = libraryRepository.getLibraryIDByUserID(userID);
            return libraryRepository.getUserLibrarySortedByTitle(libraryID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public List<BookDTO> getUserLibrarySortedByAuthor(long userID) {
        if (userService.isValidUserID(userID)) {
            long libraryID = libraryRepository.getLibraryIDByUserID(userID);
            return libraryRepository.getUserLibrarySortedByAuthor(libraryID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public List<BookDTO> getUserLibrarySortedByGenre(long userID) {
        if (userService.isValidUserID(userID)) {
            long libraryID = libraryRepository.getLibraryIDByUserID(userID);
            return libraryRepository.getUserLibrarySortedByGenre(libraryID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public List<BookDTO> getUserLibraryLastRead(long userID) {
        if (userService.isValidUserID(userID)) {
            long libraryID = libraryRepository.getLibraryIDByUserID(userID);
            return libraryRepository.getUserLibrarySortedByLastRead(libraryID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public List<BookStatistic> getAuthorLibrary(long userID) {
        if (userService.isValidUserID(userID)) {
            List<BookDTO> userBooks = getUserLibrarySortedByTitle(userID);
            List<BookStatistic> bookStatistics = new ArrayList<>();

            for (BookDTO bookDTO : userBooks) {
                long authorID = userRepository.getAuthorID(bookDTO.author());
                long bookID = bookRepository.findBookByNameAndAuthor(bookDTO.title(), authorID);
                int appearance = bookRepository.countBookAppearances(bookID);
                BookStatistic bookStatistic = new BookStatistic(bookDTO.title(), bookDTO.ISBN(), bookDTO.genre(), bookDTO.language(), bookDTO.access(), bookDTO.rating(), appearance);
                bookStatistics.add(bookStatistic);
            }

            return bookStatistics;
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public boolean isValidLibraryID(long libraryID) {
        return libraryRepository.getLibraryIDCount(libraryID) == 1;
    }

}
