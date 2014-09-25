package com.pataniqa.coursera.potlatch.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.server.repository.GiftChainRepository;
import com.pataniqa.coursera.potlatch.server.repository.ServerGiftChain;

@Controller
public class GiftChainService {

    public static final String GIFT_CHAIN_SVC_PATH = "/giftchain";

    @Autowired
    private GiftChainRepository giftChains;

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody
    GiftChain insert(@RequestBody GiftChain data) {
        return giftChains.save(new ServerGiftChain(data)).toClient();
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH + "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable long id, @RequestBody GiftChain data) {
        giftChains.save(new ServerGiftChain(data));
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH + "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        giftChains.delete(id);
    }

    @RequestMapping(value = GIFT_CHAIN_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody
    Collection<GiftChain> findAll() {
        List<GiftChain> result = new ArrayList<GiftChain>();
        for (ServerGiftChain giftChain : giftChains.findAll()) {
            result.add(giftChain.toClient());
        }
        return result;
    }

}
