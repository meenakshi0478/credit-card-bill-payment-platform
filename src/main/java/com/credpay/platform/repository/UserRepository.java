package com.credpay.platform.repository;

import com.credpay.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    User getUserByUserId(String userId);

    User findByUserId(String userId);


    Optional<User> findById(Long Id);

    void deleteByUserId(String userId);

    boolean existsByEmail(String email);

    @Query(value= "UPDATE User u SET u.enabled = TRUE WHERE a.email = ?1",nativeQuery = true)
    int enableAppUser(String email);
}

