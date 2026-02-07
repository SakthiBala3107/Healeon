package com.pm.authservice.repository;

import com.pm.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserRepository extends JpaRepository<User, UUID> {

//bts a jpa and hibernate do the tsuf and fetch the user witht he email
    Optional<User> findByEmail(String email);
}
