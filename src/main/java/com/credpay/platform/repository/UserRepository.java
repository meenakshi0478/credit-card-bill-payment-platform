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

    //publisher
    @Query(value = "select * from user u where u.user_id = ?1 and u.role = ?2", nativeQuery = true)
    User findByPublisherId(String userId, String role);

    @Query(value = "select * from user u where u.id = ?1 and u.role = ?2", nativeQuery = true)
    User findById(Long id, String role);

    @Query(value = "select * from user u where u.role = ?1", nativeQuery = true)
    List<User> findAllByRole(String role);

    @Query(value = "select * from user u where u.id = ?1", nativeQuery = true)
    User findAllById(Long id);

    //user
    User findByEmail(String email);

    User getUserByUserId(String userId);

    User findByUserId(String userId);


    Optional<User> findById(Long Id);

    void deleteByUserId(String userId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query(value= "UPDATE User u SET u.enabled = TRUE WHERE a.email = ?1",nativeQuery = true)
    int enableAppUser(String email);
}

