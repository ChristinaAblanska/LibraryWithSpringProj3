package com.academy.LibraryProjectSpring_2.service;

import com.academy.LibraryProjectSpring_2.dto.RequestUserDTO;
import com.academy.LibraryProjectSpring_2.enums.Role;
import com.academy.LibraryProjectSpring_2.excetionHandling.BusinessNotFound;
import com.academy.LibraryProjectSpring_2.model.User;
import com.academy.LibraryProjectSpring_2.dto.ResponseUserDTO;
import com.academy.LibraryProjectSpring_2.repository.BookRepository;
import com.academy.LibraryProjectSpring_2.repository.LibraryRepository;
import com.academy.LibraryProjectSpring_2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    public UserService(UserRepository userRepository, LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    public List<ResponseUserDTO> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        List<ResponseUserDTO> responseUserDTOS = new ArrayList<>();
        for (User user : users) {
            ResponseUserDTO responseUserDTO = new ResponseUserDTO(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPhoneNumber(), user.getRole(), user.isLocked());
            responseUserDTOS.add(responseUserDTO);
        }
        return responseUserDTOS;
    }

    public ResponseUserDTO getUserByID(long userID) {
        if (isValidUserID(userID)) {
            User user = userRepository.getUserByID(userID);
            return new ResponseUserDTO(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPhoneNumber(), user.getRole(), user.isLocked());
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public void insertUser(User user) {
        long userID = userRepository.insertUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.isLocked());
        int role = Role.getUserRoleID(user.getRole());
        userRepository.insertUserRole(userID, role);
        userRepository.insertUserCredentials(userID, user.getUserName(), UserRepository.encryptPassword(user.getPassword()));

        if (role == 1 || role == 2) {
            long libraryID = libraryRepository.createNewLibrary(user.getFirstName() + user.getLastName());
            libraryRepository.insertUserLibrary(userID, libraryID);
        }

        if (role == 2) {
            userRepository.insertUserAuthor(userID);
        }
    }

    public void updateUser(RequestUserDTO requestUserDTO) {
        if (isValidUserID(requestUserDTO.id())) {
            int role = Role.getUserRoleID(requestUserDTO.role());
            userRepository.updateUser(requestUserDTO.id(), requestUserDTO.firstName(), requestUserDTO.lastName(), requestUserDTO.email(), requestUserDTO.phoneNumber(), requestUserDTO.isLocked());
            userRepository.updateUserRole(requestUserDTO.id(), role);
        } else {
            throw new BusinessNotFound("User with ID: " + requestUserDTO.id() + " does not exist!");
        }
    }

    public void updateUserLOCK(long userID) {
        if (isValidUserID(userID)) {
            userRepository.updateUserLOCK(userID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public void updateUserUNLOCK(long userID) {
        if (isValidUserID(userID)) {
            userRepository.updateUserUNLOCK(userID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

    public void addUserRatingToBook(long userID, long bookID, double rating) {
        if (isValidUserID(userID) && bookRepository.getBookIDCount(bookID) == 1) {
            if (userRepository.getUserRatingCount(userID, bookID) == 0) {
                userRepository.addUserRating(userID, bookID, rating);
                double newRating = bookRepository.getBookRating(bookID);
                bookRepository.updateBookRating(bookID, newRating);
            } else {
                throw new IllegalArgumentException("Rating already exists!");
            }
        } else {
            throw new BusinessNotFound("Something went wrong! User and/or Book don't exist!");
        }
    }
    
    public boolean isValidUserID(long userID) {
        return userRepository.getUserIDCount(userID) == 1;
    }

    public boolean isValidAuthorID(long userID) {
        return userRepository.getAuthorIDCount(userID) == 1;
    }

    public void deleteUser(long userID) {
        if (isValidUserID(userID)) {
            long libraryID = libraryRepository.getLibraryIDByUserID(userID);

            libraryRepository.deleteBookLibrary(libraryID);
            libraryRepository.deleteUserLibrary(userID);
            libraryRepository.getLibraryByID(libraryID);

            userRepository.deleteAuthor(userID);

            userRepository.deleteUserRole(userID);
            userRepository.deleteCredentials(userID);
            userRepository.deleteUser(userID);
        } else {
            throw new BusinessNotFound("User with ID: " + userID + " does not exist!");
        }
    }

}
