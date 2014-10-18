package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pataniqa.coursera.potlatch.server.model.ServerGift;
import com.pataniqa.coursera.potlatch.server.model.ServerGiftChain;
import com.pataniqa.coursera.potlatch.server.model.ServerUser;

/**
 * A repository that stores gifts.
 */
@Repository
public interface GiftRepository extends PagingAndSortingRepository<ServerGift, Long> {

    Collection<ServerGift> findByTitleLike(String title, Sort sort);

    Collection<ServerGift> findByUserAndTitleLike(ServerUser user, String title, Sort sort);

    Collection<ServerGift> findByUser(ServerUser user, Sort sort);
    
    Collection<ServerGift> findByGiftChainAndTitleLike(ServerGiftChain giftChain,
            String title, Sort sort);

    Collection<ServerGift> findByGiftChain(ServerGiftChain giftChain, Sort sort);

}
