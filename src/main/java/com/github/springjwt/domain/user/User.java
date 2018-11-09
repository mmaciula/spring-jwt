package com.github.springjwt.domain.user;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;
    @Column(name = "username", length = 50, unique = true)
    @Size(min = 3, max = 50)
    @NotNull
    private String username;
    @Column(name = "email", length = 100)
    @Size(min = 4, max = 100)
    @NotNull
    private String email;
    @Column(name = "password", length = 100)
    @Size(min = 4, max = 100)
    @NotNull
    private String password;
    @Column(name = "enabled")
    @NotNull
    private Boolean enabled;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastpasswordresetdate")
    @NotNull
    private Date lastPasswordResetDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
}
