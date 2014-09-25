package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pataniqa.coursera.potlatch.model.server.Gift;

@Repository
public interface GiftRepository extends PagingAndSortingRepository<Gift, Long>{
	
	public Collection<Gift> findByTitleLike(String title, Pageable pageable);

}
