package com.pataniqa.coursera.potlatch.server.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends CrudRepository<ServerGift, Long> {

    Collection<ServerGift> findByTitleLikeOrderByLikesAsc(String title);

    Collection<ServerGift> findByTitleLikeOrderByLikesDesc(String title);

    Collection<ServerGift> findByTitleLikeOrderByCreatedAsc(String title);

    Collection<ServerGift> findByTitleLikeOrderByCreatedDesc(String title);
    
    Collection<ServerGift> findByTitleLikeOrderByUserLikesAsc(String title);

    Collection<ServerGift> findByTitleLikeOrderByUserLikesDesc(String title);

    Collection<ServerGift> findByUserIDAndTitleLikeOrderByLikesAsc(ServerUser user, String title);

    Collection<ServerGift> findByUserIDAndTitleLikeOrderByLikesDesc(ServerUser user, String title);

    Collection<ServerGift> findByUserIDAndTitleLikeOrderByCreatedAsc(ServerUser user, String title);

    Collection<ServerGift> findByUserIdAndTitleLikeOrderByCreatedDesc(ServerUser user, String title);

    Collection<ServerGift> findByGiftChainIDAndTitleLikeOrderByLikesAsc(ServerGiftChain giftChain,
            String title);

    Collection<ServerGift> findByGiftChainIDIDAndTitleLikeOrderByLikesDesc(ServerGiftChain giftChain,
            String title);

    Collection<ServerGift> findByGiftChainIDAndTitleLikeOrderByCreatedAsc(ServerGiftChain giftChain,
            String title);

    Collection<ServerGift> findByGiftChainIDAndTitleLikeOrderByCreatedDesc(ServerGiftChain giftChain,
            String title);

}
