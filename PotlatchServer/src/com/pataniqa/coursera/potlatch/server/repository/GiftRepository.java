package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends CrudRepository<ServerGift, Long> {

    Collection<ServerGift> findByTitleLike(String title, Sort sort);

    Collection<ServerGift> findByUserAndTitleLike(ServerUser user, String title, Sort sort);

    Collection<ServerGift> findByGiftChainAndTitleLike(ServerGiftChain giftChain,
            String title, Sort sort);

}
