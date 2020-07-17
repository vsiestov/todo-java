package com.vsiestov.users.repository;

import com.vsiestov.users.domain.User;
import com.vsiestov.users.domain.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(UserEmail email);
}
