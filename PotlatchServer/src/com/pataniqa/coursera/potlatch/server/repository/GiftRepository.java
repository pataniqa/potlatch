package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends CrudRepository<ServerGift, Long> {

    Collection<ServerGift> findByTitleLike(String title, Sort sort);

    Collection<ServerGift> findByUserAndTitleLikeOrderByLikesAsc(ServerUser user, String title);

    Collection<ServerGift> findByUserAndTitleLikeOrderByLikesDesc(ServerUser user, String title);

    Collection<ServerGift> findByUserAndTitleLikeOrderByCreatedAsc(ServerUser user, String title);

    Collection<ServerGift> findByUserAndTitleLikeOrderByCreatedDesc(ServerUser user, String title);

    Collection<ServerGift> findByGiftChainAndTitleLikeOrderByLikesAsc(ServerGiftChain giftChain,
            String title);

    Collection<ServerGift> findByGiftChainAndTitleLikeOrderByLikesDesc(ServerGiftChain giftChain,
            String title);

    Collection<ServerGift> findByGiftChainAndTitleLikeOrderByCreatedAsc(ServerGiftChain giftChain,
            String title);

    Collection<ServerGift> findByGiftChainAndTitleLikeOrderByCreatedDesc(ServerGiftChain giftChain,
            String title);

}
