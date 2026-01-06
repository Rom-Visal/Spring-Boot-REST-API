package com.example.rolebase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.rolebase.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM User u " + "LEFT JOIN FETCH u.roles ur " +
            "LEFT JOIN FETCH ur.role " + "WHERE LOWER(u.username) = LOWER(:username)")
    Optional<User> findByUsernameWithRolesIgnoreCase(@Param("username") String username);

    Optional<User> findByEmail(String email);

    boolean existsById(@NonNull Integer id);
}