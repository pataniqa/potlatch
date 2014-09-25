package com.pataniqa.coursera.potlatch.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pataniqa.coursera.potlatch.server.repository.*;

public class GiftService {
    
    public static final String GIFT_SVC_PATH = "/gift";

    @Autowired
    private GiftRepository gifts;
    
    @RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody Gift insert(@RequestBody Gift gift) {
        return gifts.save(gift);
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable long id, @RequestBody Gift gift) {
        gifts.save(gift);
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.DELETE)
    public void deleteGift(@PathVariable long id) {
        gifts.delete(id);
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}/like/{userID}/{like}", method = RequestMethod.PUT)
    public void setLike(@PathVariable long id, @PathVariable long userID, @PathVariable boolean like) {
        // TODO
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}/flag/{userID}/{like}", method = RequestMethod.PUT)
    public void setFlag(@PathVariable long id, @PathVariable long userID, @PathVariable boolean like) {
        // TODO
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}", method = RequestMethod.GET)
    public ClientGift findOne(@PathVariable Long id) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH, method = RequestMethod.GET)
    public List<ClientGift> findAll() {
        // TODO
        return null;
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + 
            "/queryTitle?title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<ClientGift> queryByTitle(String title,
            int order,
            int direction) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH + 
            "/queryUser?user={userID}&title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<ClientGift> queryByUser(String title,
            long userID,
            int order,
            int direction) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH + "" +
            "/queryTopGiftGivers?title={title}&direction={direction}", method = RequestMethod.GET)
    public List<ClientGift> queryByTopGiftGivers(String title,
            int direction) {
        // TODO
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH + 
            "/queryGiftChain?giftchain={giftchain}&title={title}&resultorder={order}&direction={direction}", method = RequestMethod.GET)
    public List<ClientGift> queryByGiftChain(String title,
            String giftChain,
            int order,
            int direction) {
        // TODO
        return null;
    }

}
