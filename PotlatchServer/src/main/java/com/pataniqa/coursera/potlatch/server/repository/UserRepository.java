package com.pataniqa.coursera.potlatch.server.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pataniqa.coursera.potlatch.server.model.ServerUser;

@Repository
public interface UserRepository extends
		CrudRepository<ServerUser, Long> {

    List<ServerUser> findByName(String name);
    
}
