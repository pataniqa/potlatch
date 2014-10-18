package com.pataniqa.coursera.potlatch.server.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pataniqa.coursera.potlatch.server.model.ServerUser;

/**
 * A repository that stores users.
 */
@Repository
public interface UserRepository extends
		PagingAndSortingRepository<ServerUser, Long> {

    List<ServerUser> findByName(String name);
    
}
