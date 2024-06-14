package com.academy.LibraryProjectSpring_2.repository;

import com.academy.LibraryProjectSpring_2.enums.Role;
import com.academy.LibraryProjectSpring_2.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("""
                select u.UserID,u.FirstName,u.LastName,u.email,u.phoneNumber,u.isLocked, r.userRole as UserRole from user u
                join userRole ur on u.UserID = ur.UserID\s
                join role r on r.userRoleID = ur.UserRoleID
                ORDER by u.UserID""", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setLocked(rs.getBoolean("isLocked"));
                Role role = Role.getUserRole(rs.getString("UserRole"));
                user.setRole(role);
                return user;
            }
        });
    }

    public User getUserByID(long id) {
        return jdbcTemplate.queryForObject("""
                select u.UserID,u.FirstName,u.LastName,u.email,u.phoneNumber,u.isLocked, r.userRole as UserRole from user u
                join userRole ur on u.UserID = ur.UserID
                join role r on r.userRoleID = ur.UserRoleID
                where u.UserID = :id""", new MapSqlParameterSource("id", id), new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setLocked(rs.getBoolean("isLocked"));
                Role role = Role.getUserRole(rs.getString("UserRole"));
                user.setRole(role);
                return user;
            }
        });
    }

    public User getAuthorByID(long authorID) {
        return getUserByID(getUserIDByAuthorID(authorID));
    }

    public Long getAuthorID(String authorName) {
        return jdbcTemplate.queryForObject("""
                SELECT authorID
                FROM v_authors
                WHERE authorName like :authorName""", new MapSqlParameterSource("authorName", authorName), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("authorID");
            }
        });
    }

    private long getUserIDByAuthorID(long authorID) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject("""
                SELECT UserID
                FROM author
                WHERE authorID = :authorID""", new MapSqlParameterSource("authorID", authorID), new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("UserID"));
                return user;
            }
        })).getId();
    }

    public Integer getUserIDCount(long userID) {
        return jdbcTemplate.queryForObject("""
                select count(userID) as countUsers from user
                where userID = :userID""", new MapSqlParameterSource("userID", userID), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("countUsers");
            }
        });
    }

    public Integer getAuthorIDCount(long authorID) {
        return jdbcTemplate.queryForObject("""
                select count(*) as countAuthors from author
                where authorID = :authorID""", new MapSqlParameterSource("authorID", authorID), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("countAuthors");
            }
        });
    }

    public Integer getUserRatingCount(long userID, long bookID) {
        return jdbcTemplate.queryForObject("""
                select count(*) as countRatings from UserRating ur
                where userID = :userID and ur.bookID = :bookID""", new MapSqlParameterSource("userID", userID).addValue("bookID", bookID), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("countRatings");
            }
        });
    }

    public long insertUser(String firstName, String lastName, String email, String phoneNumber, Boolean isLocked) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("INSERT INTO user (FirstName, LastName, email, phoneNumber, isLocked) VALUES(:firstName, :lastName, :email, :phoneNumber, :isLocked)",
                new MapSqlParameterSource("firstName", firstName).addValue("lastName", lastName).addValue("email", email).addValue("phoneNumber", phoneNumber).addValue("isLocked", isLocked), keyHolder);

        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return id;
    }

    public void insertUserRole(long id, int role) {
        jdbcTemplate.update("""
                INSERT INTO userRole
                (UserID, UserRoleID)
                VALUES(:id, :role)""", new MapSqlParameterSource("id", id).addValue("role", role));
    }

    public void insertUserCredentials(long id, String userName, String encryptedPass) {
        jdbcTemplate.update("""
                INSERT INTO academy.credentials
                (userID, username, password)
                VALUES(:id, :userName, :encryptedPass)""", new MapSqlParameterSource("id", id)
                .addValue("userName", userName).addValue("encryptedPass", encryptedPass));
    }

    public void insertUserAuthor(long userID) {
        jdbcTemplate.update("""
                INSERT INTO author
                (UserID)
                VALUES(:userID)""", new MapSqlParameterSource("userID", userID));
    }

    public void addUserRating(long userID, long bookID, double rating) {
        jdbcTemplate.update("""
                INSERT INTO UserRating
                (bookID, userID, rating)
                VALUES(:bookID, :userID, :rating);""", new MapSqlParameterSource("bookID", bookID).addValue("userID", userID).addValue("rating", rating));
    }

    public void updateUser(long id, String firstName, String lastName, String email, String phoneNumber, Boolean isLocked) {
        jdbcTemplate.update("UPDATE user SET FirstName=:firstName, LastName=:lastName, email=:email, phoneNumber=:phoneNumber, isLocked=:isLocked WHERE UserID=:id",
                new MapSqlParameterSource("id", id).addValue("firstName", firstName).addValue("lastName", lastName).addValue("email", email).addValue("phoneNumber", phoneNumber).addValue("isLocked", isLocked));
    }

    public void updateUserRole(long id, int role) {
        jdbcTemplate.update("""
                UPDATE academy.userRole
                SET UserRoleID = :role
                WHERE UserID = :id""", new MapSqlParameterSource("id", id).addValue("role", role));
    }

    public void updateUserLOCK(long id) {
        jdbcTemplate.update("""
                UPDATE user 
                SET isLocked = 1
                WHERE UserID = :id""", new MapSqlParameterSource("id", id));
    }

    public void updateUserUNLOCK(long id) {
        jdbcTemplate.update("""
                UPDATE user 
                SET isLocked = 0
                WHERE UserID = :id""", new MapSqlParameterSource("id", id));
    }

    public void deleteUser(long id) {
        jdbcTemplate.update("DELETE FROM user WHERE UserID=:id", new MapSqlParameterSource("id", id));
    }

    public void deleteUserRole(long userID) {
        jdbcTemplate.update("DELETE FROM userRole WHERE UserID = :userID", new MapSqlParameterSource("userID", userID));
    }

    public void deleteAuthor(long userID) {
        jdbcTemplate.update("DELETE FROM author WHERE UserID = :userID", new MapSqlParameterSource("userID", userID));
    }

    public void deleteCredentials(long userID) {
        jdbcTemplate.update("DELETE FROM credentials WHERE UserID = :userID", new MapSqlParameterSource("userID", userID));
    }

    public static String encryptPassword(String password) {
        String encryptedpassword = null;
        try
        {
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(password.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        /* Display the unencrypted and encrypted passwords. */
//        System.out.println("Plain-text password: " + password);
//        System.out.println("Encrypted password using MD5: " + encryptedpassword);

        return encryptedpassword;
    }
}

