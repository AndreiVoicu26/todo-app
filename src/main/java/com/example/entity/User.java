package com.example.entity;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "TodoUser")
@NamedQuery(name = User.FIND_ALL_USERS, query = "SELECT u FROM User u ORDER BY u.fullName")
@NamedQuery(name = User.FIND_USER_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email")
@NamedQuery(name = User.FIND_USER_BY_PASSWORD, query = "SELECT u FROM User u WHERE u.password = :password")
public class User extends AbstractEntity {
    public static final String FIND_ALL_USERS = "User.findAllUsers";
    public static final String FIND_USER_BY_EMAIL = "User.findUserByEmail";
    public static final String FIND_USER_BY_PASSWORD = "User.findUserByPassword";

    @NotEmpty(message = "Full name must be set")
    //@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Full name must be a valid name")
    private String fullName;

    @NotEmpty(message = "Email must be set")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotEmpty(message = "Password must be set")
    @Size(min = 8, message = "Password must be at least 8 characters")
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one number")
    private String password;

    private String salt;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
