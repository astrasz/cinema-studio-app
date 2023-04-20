package com.cinemastudio.cinemastudioapp.repository;

import com.cinemastudio.cinemastudioapp.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("Select t from Token t where t.apiUser.id = :id and (t.expired=FALSE or t.invalidated=FALSE)")
    List<Token> findAllValidTokensByUserId(@Param("id") String id);

    Optional<Token> findByToken(String token);
}
