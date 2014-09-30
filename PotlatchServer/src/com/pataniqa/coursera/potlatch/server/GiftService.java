package com.pataniqa.coursera.potlatch.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.server.repository.GiftChainRepository;
import com.pataniqa.coursera.potlatch.server.repository.GiftMetadataRepository;
import com.pataniqa.coursera.potlatch.server.repository.GiftRepository;
import com.pataniqa.coursera.potlatch.server.repository.ServerGift;
import com.pataniqa.coursera.potlatch.server.repository.ServerGiftChain;
import com.pataniqa.coursera.potlatch.server.repository.ServerGiftMetadata;
import com.pataniqa.coursera.potlatch.server.repository.ServerGiftMetadataPk;
import com.pataniqa.coursera.potlatch.server.repository.ServerUser;
import com.pataniqa.coursera.potlatch.server.repository.UserRepository;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;

public class GiftService {

    public static final String GIFT_SVC_PATH = "/gift";

    @Autowired
    private GiftRepository gifts;

    @Autowired
    private UserRepository users;

    @Autowired
    private GiftChainRepository giftChains;

    @Autowired
    private GiftMetadataRepository giftMetadata;

    @RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody
    Gift insert(@RequestBody Gift gift) {
        ServerGiftChain giftChain = giftChains.findOne(gift.getGiftID());
        ServerUser user = users.findOne(gift.getUserID());
        return gifts.save(new ServerGift(gift, user, giftChain)).toClient();
    }

    @RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable long id, @RequestBody Gift gift) {
        ServerGiftChain giftChain = giftChains.findOne(gift.getGiftID());
        ServerUser user = users.findOne(gift.getUserID());
        gifts.save(new ServerGift(gift, user, giftChain));
    }

    @RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.DELETE)
    public void deleteGift(@PathVariable long id) {
        gifts.delete(id);
    }

    @RequestMapping(value = GIFT_SVC_PATH + "/{id}/like/{like}", method = RequestMethod.PUT)
    public void setLike(@PathVariable long id, @PathVariable boolean like, Principal p) {
        // TODO use join
        ServerGiftMetadata metadata = giftMetadata.findOne(new ServerGiftMetadataPk(getUser(p),
                gifts.findOne(id)));
        metadata.setUserLike(like);
        giftMetadata.save(metadata);
    }

    @RequestMapping(value = GIFT_SVC_PATH + "/{id}/flag/{like}", method = RequestMethod.PUT)
    public void setFlag(@PathVariable long id, @PathVariable boolean flag, Principal p) {
        // TODO use join
        ServerGiftMetadata metadata = giftMetadata.findOne(new ServerGiftMetadataPk(getUser(p),
                gifts.findOne(id)));
        metadata.setUserFlagged(flag);
        giftMetadata.save(metadata);
    }

    @RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.GET)
    public GiftResult findOne(@PathVariable Long id, Principal p) {
        // TODO use join
        return fromGift(gifts.findOne(id), getUser(p));
    }

    @RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.GET)
    public List<GiftResult> findAll(Principal p) {
        ServerUser user = getUser(p);
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : gifts.findAll()) {
            results.add(fromGift(gift, user));
        }
        return results;
    }

    @RequestMapping(value = GIFT_SVC_PATH
            + "/queryTitle?title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByTitle(String title, int order, int direction, Principal p) {
        ResultOrderDirection resultDirection = ResultOrderDirection.toEnum(direction);
        Collection<ServerGift> query = null;
        if (ResultOrder.toEnum(order) == ResultOrder.LIKES) {
            if (resultDirection == ResultOrderDirection.ASCENDING)
                query = gifts.findByTitleLikeOrderByLikesAsc(title);
            else
                query = gifts.findByTitleLikeOrderByLikesDesc(title);
        } else {
            if (resultDirection == ResultOrderDirection.ASCENDING)
                query = gifts.findByTitleLikeOrderByCreatedAsc(title);
            else
                query = gifts.findByTitleLikeOrderByCreatedDesc(title);
        }
        return toResult(query, p);
    }

    @RequestMapping(value = GIFT_SVC_PATH
            + "/queryUser?user={userID}&title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByUser(String title,
            long userID,
            int order,
            int direction,
            Principal p) {
        ResultOrderDirection resultDirection = ResultOrderDirection.toEnum(direction);
        Collection<ServerGift> query = null;
        if (ResultOrder.toEnum(order) == ResultOrder.LIKES) {
            if (resultDirection == ResultOrderDirection.ASCENDING)
                query = gifts.findByUserIDAndTitleLikeOrderByLikesAsc(userID, title);
            else
                query = gifts.findByUserIDAndTitleLikeOrderByLikesDesc(userID, title);
        } else {
            if (resultDirection == ResultOrderDirection.ASCENDING)
                query = gifts.findByUserIDAndTitleLikeOrderByCreatedAsc(userID, title);
            else
                query = gifts.findByUserIdAndTitleLikeOrderByCreatedDesc(userID, title);
        }
        return toResult(query, p);
    }

    @RequestMapping(value = GIFT_SVC_PATH + ""
            + "/queryTopGiftGivers?title={title}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByTopGiftGivers(String title, int direction, Principal p) {
        ResultOrderDirection resultDirection = ResultOrderDirection.toEnum(direction);
        Collection<ServerGift> query = null;
        if (resultDirection == ResultOrderDirection.ASCENDING)
            query = gifts.findByTitleLikeOrderByUserLikesAsc(title);
        else
            query = gifts.findByTitleLikeOrderByUserLikesDesc(title);
        return toResult(query, p);
    }

    @RequestMapping(value = GIFT_SVC_PATH
            + "/queryGiftChain?giftchain={giftchain}&title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByGiftChain(String title,
            long giftChainID,
            int order,
            int direction,
            Principal p) {
        ResultOrderDirection resultDirection = ResultOrderDirection.toEnum(direction);
        Collection<ServerGift> query = null;
        if (ResultOrder.toEnum(order) == ResultOrder.LIKES) {
            if (resultDirection == ResultOrderDirection.ASCENDING)
                query = gifts.findByGiftChainIDAndTitleLikeOrderByLikesAsc(giftChainID, title);
            else
                query = gifts.findByGiftChainIDIDAndTitleLikeOrderByLikesDesc(giftChainID, title);
        } else {
            if (resultDirection == ResultOrderDirection.ASCENDING)
                query = gifts.findByGiftChainIDAndTitleLikeOrderByCreatedAsc(giftChainID, title);
            else
                query = gifts.findByGiftChainIDAndTitleLikeOrderByCreatedDesc(giftChainID, title);
        }
        return toResult(query, p);
    }

    private ServerUser getUser(Principal p) {
        return users.findByUsername(p.getName()).get(0);
    }

    private GiftResult fromGift(ServerGift gift, ServerUser user) {
        // TODO - do proper join!
        ServerGiftMetadata metadata = giftMetadata.findOne(new ServerGiftMetadataPk(user, gift));
        boolean like = metadata != null ? metadata.isUserLike() : false;
        boolean flag = metadata != null ? metadata.isUserFlagged() : false;
        return new GiftResult(gift.getGiftID(), gift.getTitle(), gift.getDescription(),
                gift.getVideoUri(), gift.getImageUri(), gift.getCreated(), gift.getUserID(), like,
                flag, gift.getLikes(), gift.isFlagged(), gift.getGiftChainID(), gift.getGiftChain()
                        .getGiftChainName(), gift.getUser().getUserLikes(), gift.getUser()
                        .getUsername());
    }

    private List<GiftResult> toResult(Collection<ServerGift> query, Principal p) {
        ServerUser user = getUser(p);
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : query) {
            results.add(fromGift(gift, user));
        }
        return results;
    }

}
