package com.pataniqa.coursera.potlatch.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouchingGiftRepository extends
		CrudRepository<TouchingGift, Long> {

}
