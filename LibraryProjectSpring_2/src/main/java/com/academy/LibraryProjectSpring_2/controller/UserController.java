package com.academy.LibraryProjectSpring_2.controller;

import com.academy.LibraryProjectSpring_2.dto.RequestUserDTO;
import com.academy.LibraryProjectSpring_2.dto.ResponseUserDTO;
import com.academy.LibraryProjectSpring_2.model.User;
import com.academy.LibraryProjectSpring_2.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseUserDTO getUserByID(@PathVariable @Min(value = 1, message = "No negative values allowed") long id) {
        return userService.getUserByID(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertUser(@RequestBody User user) {
        userService.insertUser(user);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@Valid @RequestBody RequestUserDTO requestUserDTO) {
        userService.updateUser(requestUserDTO);
    }

    @PutMapping("/lock/{id}")
    public void updateUserLOCK(@PathVariable @Min(value = 1, message = "No negative values allowed") long id) {
        userService.updateUserLOCK(id);
    }

    @PutMapping("/unlock/{id}")
    public void updateUserUNLOCK(@PathVariable @Min(value = 1, message = "No negative values allowed") long id) {
        userService.updateUserUNLOCK(id);
    }

    //TODO not working as expected - switches bookID and userID in the db, rating = 0
    @PutMapping("/rateBook")
    public void addUserRatingToBook(@RequestParam("userID") long userID, @RequestParam("bookID") long bookID, @RequestParam("rating") double rating) {
        userService.addUserRatingToBook(userID, bookID, rating);
    }
    // TODO to be completed as a transaction: vacate all tables with this PRIMARY KEY
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable long id) {
//        userService.deleteUser(id);
//    }
}
