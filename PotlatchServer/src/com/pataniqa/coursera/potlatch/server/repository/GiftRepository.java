package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends CrudRepository<Gift, Long>{
	
	public Collection<Gift> findByTitleLike(String title);

}
