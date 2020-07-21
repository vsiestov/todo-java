package com.vsiestov.users.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    /*@OneToMany(targetEntity = Authorities.class)
    private List<Authorities> authorities;*/
}
