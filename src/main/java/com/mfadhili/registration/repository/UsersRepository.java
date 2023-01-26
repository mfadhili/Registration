package com.mfadhili.registration.repository;

import com.mfadhili.registration.dto.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("select u from Users u where u.id = ?1")
    Users findById2(Long aLong);

    Users findByName(String name);


}