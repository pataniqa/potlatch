package com.pataniqa.coursera.potlatch.server.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends
		CrudRepository<ServerUser, Long> {

    List<ServerUser> findByUsername(@Param("username") String name);
    
}
