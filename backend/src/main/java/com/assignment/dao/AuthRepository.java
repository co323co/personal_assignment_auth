package com.assignment.dao;

import com.assignment.dto.user.selectprofile.SelectProfileOutput;
import com.assignment.entity.Auth;
import com.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Integer> {
    Optional<Auth> findByUserId(int userId);
}