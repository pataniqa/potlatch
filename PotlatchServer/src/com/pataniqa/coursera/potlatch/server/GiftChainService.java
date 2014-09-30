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
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi;
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftChainApi;

@Controller
public class GiftChainService {

    @Autowired
    private GiftChainRepository giftChains;

    @RequestMapping(value = RemoteGiftChainApi.GIFT_CHAIN_PATH, method = RequestMethod.POST)
    public @ResponseBody
    GiftChain insert(@RequestBody GiftChain data) {
        return giftChains.save(new ServerGiftChain(data)).toClient();
    }

    @RequestMapping(value = RemoteGiftChainApi.GIFT_CHAIN_PATH, method = RequestMethod.GET)
    public @ResponseBody
    Collection<GiftChain> findAll() {
        List<GiftChain> result = new ArrayList<GiftChain>();
        for (ServerGiftChain giftChain : giftChains.findAll()) {
            result.add(giftChain.toClient());
        }
        return result;
    }

    @RequestMapping(value = RemoteGiftChainApi.GIFT_CHAIN_ID_PATH, method = RequestMethod.PUT)
    public void update(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id, @RequestBody GiftChain data) {
        giftChains.save(new ServerGiftChain(data));
    }

    @RequestMapping(value = RemoteGiftChainApi.GIFT_CHAIN_ID_PATH, method = RequestMethod.DELETE)
    public void delete(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id) {
        giftChains.delete(id);
    }

}
