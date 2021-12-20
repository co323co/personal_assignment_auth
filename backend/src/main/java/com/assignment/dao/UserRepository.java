package com.assignment.dao;

import com.assignment.dto.user.selectprofile.SelectProfileOutput;
import com.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    @Query("select new com.assignment.dto.user.selectprofile.SelectProfileOutput(u.id, u.email, u.nickname, u.grade, u.createdAt, u.updatedAt)"
            + " from User u where u.id = ?1")
    SelectProfileOutput findUserProfile(int userId);

}