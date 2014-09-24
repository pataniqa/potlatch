package com.pataniqa.coursera.potlatch.server;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pataniqa.coursera.potlatch.server.repository.GiftChain;
import com.pataniqa.coursera.potlatch.server.repository.GiftChainRepository;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Lists;

@Controller
public class GiftChainSvc {

    @Autowired
    private GiftChainRepository giftChains;

    public static final String GIFT_CHAIN_SVC_PATH = "/giftchain";

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH, method = RequestMethod.POST)
    public GiftChain insert(GiftChain data) {
        return giftChains.save(data);
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH + "/{id}", method = RequestMethod.PUT)
    public void update(long id, GiftChain data) {
        giftChains.save(data);
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH + "/{id}", method = RequestMethod.DELETE)
    public void delete(long id) {
        giftChains.delete(id);
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody
    Collection<GiftChain> findAll() {
        return Lists.newArrayList(giftChains.findAll());
    }

}
