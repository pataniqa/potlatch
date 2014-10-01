package com.pataniqa.coursera.potlatch.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pataniqa.coursera.potlatch.server.model.ServerGiftChain;

@Repository
public interface GiftChainRepository extends CrudRepository<ServerGiftChain, Long> {

}
