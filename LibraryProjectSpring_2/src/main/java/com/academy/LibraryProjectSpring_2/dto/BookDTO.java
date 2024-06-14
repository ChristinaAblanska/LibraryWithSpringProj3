package com.academy.LibraryProjectSpring_2.dto;

import com.academy.LibraryProjectSpring_2.enums.BookAccess;

public record BookDTO (String title, String author, String ISBN, String genre, String language, BookAccess access, double rating) {
}
