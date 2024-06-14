package com.academy.LibraryProjectSpring_2.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {
    private long bookID;
    private String title;
    private String ISBN;
    private long authorID;
    private long genreID;
    private long languageID;
    private double rating;
    private long accessID;
}
