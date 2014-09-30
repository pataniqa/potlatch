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

    Collection<ServerGift> findByUserIDAndTitleLikeOrderByLikesAsc(long userID, String title);

    Collection<ServerGift> findByUserIDAndTitleLikeOrderByLikesDesc(long userID, String title);

    Collection<ServerGift> findByUserIDAndTitleLikeOrderByCreatedAsc(long userID, String title);

    Collection<ServerGift> findByUserIdAndTitleLikeOrderByCreatedDesc(long userID, String title);

    Collection<ServerGift> findByTitleLikeOrderByUserLikesAsc(String title);

    Collection<ServerGift> findByTitleLikeOrderByUserLikesDesc(String title);

    Collection<ServerGift> findByGiftChainIDAndTitleLikeOrderByLikesAsc(long giftChainID,
            String title);

    Collection<ServerGift> findByGiftChainIDIDAndTitleLikeOrderByLikesDesc(long giftChainID,
            String title);

    Collection<ServerGift> findByGiftChainIDAndTitleLikeOrderByCreatedAsc(long giftChainID,
            String title);

    Collection<ServerGift> findByGiftChainIDAndTitleLikeOrderByCreatedDesc(long giftChainID,
            String title);

}
