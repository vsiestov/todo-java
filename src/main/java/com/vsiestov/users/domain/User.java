package com.vsiestov.users.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "T_USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name="first_name"))
    })
    private UserName firstName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name="last_name"))
    })
    private UserName lastName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name="email"))
    })
    private UserEmail email;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name="password"))
    })
    private UserPassword password;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserName getFirstName() {
        return firstName;
    }

    public void setFirstName(UserName firstName) {
        this.firstName = firstName;
    }

    public UserName getLastName() {
        return lastName;
    }

    public void setLastName(UserName lastName) {
        this.lastName = lastName;
    }

    public UserEmail getEmail() {
        return email;
    }

    public void setEmail(UserEmail email) {
        this.email = email;
    }

    public UserPassword getPassword() {
        return password;
    }

    public void setPassword(UserPassword password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
            Objects.equals(firstName, user.firstName) &&
            Objects.equals(lastName, user.lastName) &&
            Objects.equals(email, user.email) &&
            Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", firstName=" + firstName +
            ", lastName=" + lastName +
            ", email=" + email +
            ", password=" + password +
            '}';
    }
}
