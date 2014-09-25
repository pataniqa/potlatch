package com.pataniqa.coursera.potlatch.server;

import java.security.Principal;
import java.util.ArrayList;
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
import com.pataniqa.coursera.potlatch.server.repository.GiftRepository;
import com.pataniqa.coursera.potlatch.server.repository.ServerGift;
import com.pataniqa.coursera.potlatch.server.repository.ServerGiftChain;
import com.pataniqa.coursera.potlatch.server.repository.ServerUser;
import com.pataniqa.coursera.potlatch.server.repository.UserRepository;

public class GiftService {
    
    public static final String GIFT_SVC_PATH = "/gift";

    @Autowired
    private GiftRepository gifts;
    
    @Autowired
    private UserRepository users;
    
    @Autowired
    private GiftChainRepository giftChains;
    
    @RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody Gift insert(@RequestBody Gift gift) {
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
        // TODO
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}/flag/{like}", method = RequestMethod.PUT)
    public void setFlag(@PathVariable long id, @PathVariable boolean like, Principal p) {
        // TODO
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.GET)
    public GiftResult findOne(@PathVariable Long id) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.GET)
    public List<GiftResult> findAll(Principal p) {
        ServerUser user = users.findByUsername(p.getName()).get(0);
        
        
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : gifts.findAll()) {
            GiftResult result = new GiftResult(gift.getGiftID(),
                    gift.getTitle(),
                    gift.getDescription(),
                    gift.getVideoUri(),
                    gift.getImageUri(),
                    gift.getCreated(),
                    gift.getUserID(),
                    boolean like,
                    boolean flag,
                    long likes,
                    boolean flagged,
                    long giftChainID,
                    String giftChainName,
                    long userLikes,
                    String username);
        }
        // TODO
        return null;
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + 
            "/queryTitle?title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByTitle(String title,
            int order,
            int direction) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH + 
            "/queryUser?user={userID}&title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByUser(String title,
            long userID,
            int order,
            int direction) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH + "" +
            "/queryTopGiftGivers?title={title}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByTopGiftGivers(String title,
            int direction) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH + 
            "/queryGiftChain?giftchain={giftchain}&title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<GiftResult> queryByGiftChain(String title,
            String giftChain,
            int order,
            int direction) {
        // TODO
        return null;
    }

}
