package com.academy.LibraryProjectSpring_2.dto;

import com.academy.LibraryProjectSpring_2.enums.BookAccess;

public record BookStatistic(String title, String ISBN, String genre, String language, BookAccess access, double rating, int appearances) {
}
