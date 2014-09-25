package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GiftRepository extends PagingAndSortingRepository<ServerGift, Long>{
	
	public Collection<ServerGift> findByTitleLike(String title, Pageable pageable);

}
