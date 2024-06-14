package com.academy.LibraryProjectSpring_2.service;

import com.academy.LibraryProjectSpring_2.dto.BookDTO;
import com.academy.LibraryProjectSpring_2.enums.BookAccess;
import com.academy.LibraryProjectSpring_2.excetionHandling.BusinessNotFound;
import com.academy.LibraryProjectSpring_2.model.Book;
import com.academy.LibraryProjectSpring_2.model.User;
import com.academy.LibraryProjectSpring_2.repository.BookRepository;
import com.academy.LibraryProjectSpring_2.repository.LibraryRepository;
import com.academy.LibraryProjectSpring_2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, LibraryRepository libraryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.userRepository = userRepository;
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.getAllBooks();
        List<BookDTO> bookDTOS = new ArrayList<>();

        for (Book book : books) {
            User author = userRepository.getAuthorByID(book.getAuthorID());
            String authorName = author.getFirstName() + " " + author.getLastName();
            String genre = bookRepository.getGenreByID(book.getGenreID());
            String language = bookRepository.getLanguageById(book.getLanguageID());
            BookAccess access = BookAccess.getBookAccess(book.getAccessID());
            BookDTO bookDTO = new BookDTO(book.getTitle(), authorName, book.getISBN(), genre, language, access, book.getRating());
            bookDTOS.add(bookDTO);
        }

        return bookDTOS;
    }

    public BookDTO findBookByID(long bookID) {
        if (isValidBookID(bookID)) {
            Book book = bookRepository.findBookById(bookID);
            User author = userRepository.getAuthorByID(book.getAuthorID());
            String authorName = author.getFirstName() + " " + author.getLastName();
            String genre = bookRepository.getGenreByID(book.getGenreID());
            String language = bookRepository.getLanguageById(book.getLanguageID());
            BookAccess access = BookAccess.getBookAccess(book.getAccessID());

            return new BookDTO(book.getTitle(), authorName, book.getISBN(), genre, language, access, book.getRating());
        } else {
            throw new BusinessNotFound("BookID: " + bookID + " does not exist!");
        }
    }

    public List<BookDTO> findBookByTitle(String bookTitle) {
        List<Book> books = bookRepository.findBookByTitle(bookTitle);
        if (books.isEmpty()) {
            throw new BusinessNotFound("Book with name: " + bookTitle + " does not exist!");
        } else {
            List<BookDTO> bookDTOS = new ArrayList<>();

            for (Book book : books) {
                User author = userRepository.getAuthorByID(book.getAuthorID());
                String authorName = author.getFirstName() + " " + author.getLastName();
                String genre = bookRepository.getGenreByID(book.getGenreID());
                String language = bookRepository.getLanguageById(book.getLanguageID());
                BookAccess access = BookAccess.getBookAccess(book.getAccessID());
                BookDTO bookDTO = new BookDTO(book.getTitle(), authorName, book.getISBN(), genre, language, access, book.getRating());
                bookDTOS.add(bookDTO);
            }
            return bookDTOS;
        }
    }

    public List<BookDTO> findBookByAuthor(String authorName) {
        List<Book> books = bookRepository.findBookByAuthor(userRepository.getAuthorID(authorName));
        if (books.isEmpty()) {
            throw new BusinessNotFound("Author with name: " + authorName + "does not exist!");
        } else {
            List<BookDTO> bookDTOS = new ArrayList<>();

            for (Book book : books) {
                String genre = bookRepository.getGenreByID(book.getGenreID());
                String language = bookRepository.getLanguageById(book.getLanguageID());
                BookAccess access = BookAccess.getBookAccess(book.getAccessID());
                BookDTO bookDTO = new BookDTO(book.getTitle(), authorName, book.getISBN(), genre, language, access, book.getRating());
                bookDTOS.add(bookDTO);
            }

            return bookDTOS;
        }
    }

    public void createBook(BookDTO bookDTO) {
        long genreID = bookRepository.getGenreID(bookDTO.genre());
        long languageID = bookRepository.getLanguageID(bookDTO.language());
        long authorID = userRepository.getAuthorID(bookDTO.author());
        long accessID = BookAccess.getBookAccessID(bookDTO.access());
        bookRepository.createBook(bookDTO.title(), bookDTO.ISBN(), authorID, genreID, bookDTO.rating(), accessID, languageID);
    }

    public void updateBookRating(BookDTO bookDTO) {
        long authorID = userRepository.getAuthorID(bookDTO.author());
        long bookID = bookRepository.findBookByNameAndAuthor(bookDTO.title(), authorID);
        bookRepository.updateBookRating(bookID, bookDTO.rating());
    }

    public void updateBookAccess(BookDTO bookDTO) {
        long authorID = userRepository.getAuthorID(bookDTO.author());
        long accessID = BookAccess.getBookAccessID(bookDTO.access());
        long bookID = bookRepository.findBookByNameAndAuthor(bookDTO.title(), authorID);
        if (userRepository.getAuthorIDCount(authorID) == 1 && isValidBookID(bookID)) {
            bookRepository.updateBookAccess(bookID, accessID);
        } else {
            throw new BusinessNotFound("Author or book does not exist!");
        }
    }

    public boolean isValidBookID(long bookID) {
        return bookRepository.getBookIDCount(bookID) == 1;
    }
}
