package com.academy.LibraryProjectSpring_2.repository;

import com.academy.LibraryProjectSpring_2.dto.BookDTO;
import com.academy.LibraryProjectSpring_2.dto.LibraryDTO;
import com.academy.LibraryProjectSpring_2.enums.BookAccess;
import com.academy.LibraryProjectSpring_2.model.Library;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class LibraryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public LibraryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Library> getAllLibraries() {
        return jdbcTemplate.query("SELECT libraryID, LibraryName FROM library", BeanPropertyRowMapper.newInstance(Library.class));
    }

    public List<LibraryDTO> getAllUserLibraries() {
        return jdbcTemplate.query("""
                Select l.libraryID , l.LibraryName, ul.UserID, CONCAT(u.FirstName, " ", u.LastName) as Name from library l\s
                join userLibrary ul on l.libraryID = ul.LibraryID\s
                join user u on u.UserID = ul.UserID""", new RowMapper<LibraryDTO>() {
            @Override
            public LibraryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new LibraryDTO(rs.getLong("libraryID"),
                        rs.getLong("UserID"), rs.getString("LibraryName"), rs.getString("Name"));
            }
        });
    }

    public List<BookDTO> getUserLibrarySortedByTitle(long libraryID) {
        return jdbcTemplate.query("""
                select bl.LibraryID, vb.Title, vb.authorName, vb.ISBN, vb.genreName, vb.languageName,
                vb.rating, vb.AccessID, bl.DateRead from bookLibrary bl
                join v_bookDetails vb on vb.bookID = bl.BookID
                where bl.LibraryID = :libraryID
                order by vb.Title""", new MapSqlParameterSource("libraryID", libraryID), new RowMapper<BookDTO>() {
            @Override
            public BookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BookAccess access = BookAccess.getBookAccess(rs.getLong("AccessID"));
                return new BookDTO(rs.getString("Title"), rs.getString("authorName"), rs.getString("ISBN"),
                        rs.getString("genreName"), rs.getString("languageName"), access,
                        rs.getDouble("rating"));
            }
        });
    }

    public List<BookDTO> getUserLibrarySortedByAuthor(long libraryID) {
        return jdbcTemplate.query("""
                select bl.LibraryID, vb.Title, vb.authorName, vb.ISBN, vb.genreName, vb.languageName,
                vb.rating, vb.AccessID, bl.DateRead from bookLibrary bl
                join v_bookDetails vb on vb.bookID = bl.BookID
                where bl.LibraryID = :libraryID
                order by vb.authorName""", new MapSqlParameterSource("libraryID", libraryID), new RowMapper<BookDTO>() {
            @Override
            public BookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BookAccess access = BookAccess.getBookAccess(rs.getLong("AccessID"));
                return new BookDTO(rs.getString("Title"), rs.getString("authorName"), rs.getString("ISBN"),
                        rs.getString("genreName"), rs.getString("languageName"), access,
                        rs.getDouble("rating"));
            }
        });
    }

    public List<BookDTO> getUserLibrarySortedByGenre(long libraryID) {
        return jdbcTemplate.query("""
                select bl.LibraryID, vb.Title, vb.authorName, vb.ISBN, vb.genreName, vb.languageName,
                vb.rating, vb.AccessID, bl.DateRead from bookLibrary bl
                join v_bookDetails vb on vb.bookID = bl.BookID
                where bl.LibraryID = :libraryID
                order by vb.genreName""", new MapSqlParameterSource("libraryID", libraryID), new RowMapper<BookDTO>() {
            @Override
            public BookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BookAccess access = BookAccess.getBookAccess(rs.getLong("AccessID"));
                return new BookDTO(rs.getString("Title"), rs.getString("authorName"), rs.getString("ISBN"),
                        rs.getString("genreName"), rs.getString("languageName"), access,
                        rs.getDouble("rating"));
            }
        });
    }

    public List<BookDTO> getUserLibrarySortedByLastRead(long libraryID) {
        return jdbcTemplate.query("""
                select bl.LibraryID, vb.Title, vb.authorName, vb.ISBN, vb.genreName, vb.languageName,
                vb.rating, vb.AccessID, bl.DateRead from bookLibrary bl
                join v_bookDetails vb on vb.bookID = bl.BookID
                where bl.LibraryID = :libraryID
                order by bl.DateRead desc
                limit 3""", new MapSqlParameterSource("libraryID", libraryID), new RowMapper<BookDTO>() {
            @Override
            public BookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BookAccess access = BookAccess.getBookAccess(rs.getLong("AccessID"));
                return new BookDTO(rs.getString("Title"), rs.getString("authorName"), rs.getString("ISBN"),
                        rs.getString("genreName"), rs.getString("languageName"), access,
                        rs.getDouble("rating"));
            }
        });
    }

    public List<BookDTO> getUserLibrary(long libraryID) {
        return jdbcTemplate.query("""
                select bl.LibraryID, vb.Title, vb.authorName, vb.ISBN, vb.genreName, vb.languageName, 
                vb.rating, vb.AccessID, bl.DateRead from bookLibrary bl
                join v_bookDetails vb on vb.bookID = bl.BookID
                where bl.LibraryID = :libraryID""", new MapSqlParameterSource("libraryID", libraryID), new RowMapper<BookDTO>() {
            @Override
            public BookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BookAccess access = BookAccess.getBookAccess(rs.getLong("AccessID"));
                return new BookDTO(rs.getString("Title"), rs.getString("authorName"), rs.getString("ISBN"),
                        rs.getString("genreName"), rs.getString("languageName"), access,
                        rs.getDouble("rating"));
            }
        });
    }

    public Library getLibraryByID(long libraryID) {
        return jdbcTemplate.queryForObject("SELECT libraryID, LibraryName FROM library where libraryID = :libraryID", new MapSqlParameterSource("libraryID", libraryID), BeanPropertyRowMapper.newInstance(Library.class));
    }

    public LibraryDTO getUserLibraryByID(long userID) {
        return jdbcTemplate.queryForObject("""
                Select l.libraryID , l.LibraryName, ul.UserID, CONCAT(u.FirstName, " ", u.LastName) as Name from library l\s
                join userLibrary ul on l.libraryID = ul.LibraryID
                join user u on u.UserID = ul.UserID
                where ul.UserID = :userID""", new MapSqlParameterSource("userID", userID), new RowMapper<LibraryDTO>() {
            @Override
            public LibraryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new LibraryDTO(rs.getLong("libraryID"),
                        rs.getLong("UserID"), rs.getString("LibraryName"), rs.getString("Name"));
            }
        });
    }

    public Long getLibraryIDByUserID(long userID) {
        return jdbcTemplate.queryForObject("""
                select ul.LibraryID from userLibrary ul
                where ul.UserID = :userID""", new MapSqlParameterSource("userID", userID), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("LibraryID");
            }
        });
    }

    public Integer getLibraryIDCount(long libraryID) {
        return jdbcTemplate.queryForObject("""
                select count(*) as libraryCount from library
                where libraryID = :libraryID""", new MapSqlParameterSource("libraryID", libraryID), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("libraryCount");
            }
        });
    }

    public long createNewLibrary(String libraryName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("""
                INSERT INTO academy.library
                (LibraryName)
                VALUES(:libraryName)""", new MapSqlParameterSource("libraryName", libraryName), keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void insertUserLibrary(long userID, long libraryID) {
        jdbcTemplate.update("""
                INSERT INTO academy.userLibrary
                (LibraryID, UserID)
                VALUES(:libraryID, :userID);""", new MapSqlParameterSource("userID", userID).addValue("libraryID", libraryID));
    }

    public void deleteLibrary(long LibraryID) {
        jdbcTemplate.update("DELETE FROM library WHERE libraryID", new MapSqlParameterSource("LibraryID", LibraryID));
    }

    public void deleteUserLibrary(long userID) {
        jdbcTemplate.update("DELETE FROM userLibrary\n" +
                "WHERE UserID = :userID;", new MapSqlParameterSource("userID", userID));
    }

    public void deleteBookLibrary(long libraryID) {
        jdbcTemplate.update("DELETE FROM bookLibrary\n" +
                "WHERE LibraryID = :libraryID", new MapSqlParameterSource("libraryID", libraryID));
    }

    public void addBookToLibrary(long libraryID, long bookID, boolean readFlag, LocalDateTime date) {
        jdbcTemplate.update("""
                INSERT INTO academy.bookLibrary
                (LibraryID, BookID, ReadFlag, DateRead)
                VALUES(:libraryID, :bookID, :readFlag, :date)""", new MapSqlParameterSource("libraryID", libraryID)
                .addValue("bookID", bookID).addValue("readFlag", readFlag).addValue("date", date));
    }

}
