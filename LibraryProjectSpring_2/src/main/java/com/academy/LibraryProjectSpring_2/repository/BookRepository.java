package com.academy.LibraryProjectSpring_2.repository;

import com.academy.LibraryProjectSpring_2.model.Book;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Repository
public class BookRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAllBooks() {
        return jdbcTemplate.query("""
                SELECT BookID, Title,
                ISBN, AuthorID, GenreID,
                rating, AccessID, LanguageID
                FROM book""", BeanPropertyRowMapper.newInstance(Book.class));
    }

    public Book findBookById(long bookID) {
        return jdbcTemplate.queryForObject("""
                SELECT BookID, Title,
                ISBN, AuthorID, GenreID,
                rating, AccessID, LanguageID
                FROM book where BookID = :bookID""", new MapSqlParameterSource("bookID", bookID), BeanPropertyRowMapper.newInstance(Book.class));
    }

    public Long findBookByNameAndAuthor(String bookName, long authorID) {
        return jdbcTemplate.queryForObject("""
                        SELECT BookID
                        FROM book where 
                        Title = :bookName and authorID = :authorID""", new MapSqlParameterSource("bookName", bookName).addValue("authorID", authorID),
                new RowMapper<Long>() {
                    @Override
                    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getLong("BookID");
                    }
                });
    }

    public List<Book> findBookByTitle(String bookTitle) {
        return jdbcTemplate.query("""
                SELECT BookID, Title,
                ISBN, AuthorID, GenreID,
                rating, AccessID, LanguageID
                FROM book where Title like :bookTitle""", new MapSqlParameterSource("bookTitle", bookTitle), BeanPropertyRowMapper.newInstance(Book.class));
    }

    public List<Book> findBookByAuthor(long authorID) {
        return jdbcTemplate.query("""
                SELECT BookID, Title,
                ISBN, AuthorID, GenreID,
                rating, AccessID, LanguageID
                FROM book where authorID = :authorID""", new MapSqlParameterSource("authorID", authorID), BeanPropertyRowMapper.newInstance(Book.class));
    }

    public String getGenreByID(long genreID) {
        return jdbcTemplate.queryForObject("""
                SELECT genreName
                FROM genre
                WHERE genreID = :genreID""", new MapSqlParameterSource("genreID", genreID), new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("genreName");
            }
        });
    }

    public Long getGenreID(String genreName) {
        return jdbcTemplate.queryForObject("""
                SELECT genreID
                FROM genre
                WHERE genreName = :genreName""", new MapSqlParameterSource("genreName", genreName), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("genreID");
            }
        });
    }

    public String getLanguageById(long languageID) {
        return jdbcTemplate.queryForObject("""
                 SELECT languageName
                 FROM language
                 WHERE languageID = :languageID""", new MapSqlParameterSource("languageID", languageID), new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("languageName");
            }
        });
    }

    public Long getLanguageID(String languageName) {
        return jdbcTemplate.queryForObject("""
                SELECT languageID
                FROM language
                WHERE languageName = :languageName""", new MapSqlParameterSource("languageName", languageName), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("languageID");
            }
        });
    }

    public Double getBookRating(long bookID) {
        return jdbcTemplate.queryForObject("""
                select bookRating from v_book_rating vbr
                where bookID = :bookID""", new MapSqlParameterSource("bookID", bookID), new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getDouble("bookRating");
            }
        });
    }

    public Integer getBookIDCount(long bookID) {
        return jdbcTemplate.queryForObject("""
                select count(bookID) as countBooks from book
                where bookID = :bookID""", new MapSqlParameterSource("bookID", bookID), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("countBooks");
            }
        });
    }

    public Integer countBookAppearances(long bookID) {
        return jdbcTemplate.queryForObject("""
                select appearances from v_bookAppearances
                where BookID = :bookID""", new MapSqlParameterSource("bookID", bookID), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("appearances");
            }
        });
    }

    public void createBook(String title, String ISBN, long authorID, long genreID, double rating, long accessID, long languageID) {
        jdbcTemplate.update("""
                        INSERT INTO book
                        (Title, ISBN, AuthorID, GenreID, rating, AccessID, LanguageID)
                        VALUES(:title, :ISBN, :authorID, :genreID, :rating, :accessID, :languageID)""",
                new MapSqlParameterSource("title", title).addValue("ISBN", ISBN).addValue("authorID", authorID)
                        .addValue("genreID", genreID).addValue("rating", rating).addValue("accessID", accessID).addValue("languageID", languageID));
    }

    public void updateBookRating(long bookID, double rating) {
        jdbcTemplate.update("""
                        UPDATE book
                        SET rating = :rating
                        WHERE BookID = :bookID""",
                new MapSqlParameterSource("bookID", bookID).addValue("rating", rating));
    }

    public void updateBookAccess(long bookID, long accessID) {
        jdbcTemplate.update("""
                        UPDATE book
                        SET AccessID = :accessID
                        WHERE BookID = :bookID""",
                new MapSqlParameterSource("bookID", bookID).addValue("accessID", accessID));
    }
}
