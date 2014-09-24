package com.pataniqa.coursera.potlatch.server;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pataniqa.coursera.potlatch.model.GiftMetadata;
import com.pataniqa.coursera.potlatch.server.repository.*;

import org.springframework.web.bind.annotation.*;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;

import com.google.common.collect.Lists;

@Controller
public class GiftChainSvc {
    
    public static final String GIFT_CHAIN_SVC_PATH = "/giftchain";
    public static final String GIFT_SVC_PATH = "/gift";

    @Autowired
    private GiftChainRepository giftChains;

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody GiftChain insert(@RequestBody GiftChain data) {
        return giftChains.save(data);
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH + "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable long id, @RequestBody GiftChain data) {
        giftChains.save(data);
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH + "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        giftChains.delete(id);
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody
    Collection<GiftChain> findAll() {
        return Lists.newArrayList(giftChains.findAll());
    }
    
    @RequestMapping(value = GIFT_SVC_PATH + "/{id}/meta?user={userID}", method = RequestMethod.GET)
    public @ResponseBody
    GiftMetadata findOne(@PathVariable long id, @PathVariable long userID) {
        // TODO this won't work, we need the user ID too
        return null;
    }

    @RequestMapping(value = GIFT_SVC_PATH + "/{id}/meta", method = RequestMethod.PUT)
    @PUT(GIFT_SVC_PATH + "/{id}/meta")
    GiftMetadata update(@Body GiftMetadata data) {
        // TODO this won't work we need the user ID too
        return null;
    }

}
