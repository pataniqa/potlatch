package com.pataniqa.coursera.potlatch.server;

import static com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi.*;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.server.model.ServerGift;
import com.pataniqa.coursera.potlatch.server.model.ServerGiftChain;
import com.pataniqa.coursera.potlatch.server.model.ServerGiftMetadata;
import com.pataniqa.coursera.potlatch.server.model.ServerGiftMetadataPk;
import com.pataniqa.coursera.potlatch.server.model.ServerUser;
import com.pataniqa.coursera.potlatch.server.repository.GiftChainRepository;
import com.pataniqa.coursera.potlatch.server.repository.GiftMetadataRepository;
import com.pataniqa.coursera.potlatch.server.repository.GiftRepository;
import com.pataniqa.coursera.potlatch.server.repository.UserRepository;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.remote.ResourceStatus;
import com.pataniqa.coursera.potlatch.store.remote.ResourceStatus.ResourceState;

@Controller
public class GiftService {

    @Autowired private GiftRepository gifts;

    @Autowired private UserRepository users;

    @Autowired private GiftChainRepository giftChains;

    @Autowired private GiftMetadataRepository giftMetadata;

    // TODO - should be injected
    private FileManager<ServerGift> imageManager = new FileManager<ServerGift>("image", "jpg");

    // TODO - should be injected
    private FileManager<ServerGift> videoManager = new FileManager<ServerGift>("video", "mpg");

    @RequestMapping(value = GIFT_PATH, method = RequestMethod.POST)
    public @ResponseBody
    GiftResult insert(@RequestBody Gift gift) {
        ServerGiftChain giftChain = getGiftChain(gift);
        ServerUser user = users.findOne(gift.getUserID());
        return fromGift(gifts.save(new ServerGift(gift, user, giftChain)), user);
    }

    @RequestMapping(value = GIFT_PATH, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> findAll(Principal p) {
        ServerUser user = getUser(p);
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : gifts.findAll()) {
            results.add(fromGift(gift, user));
        }
        return results;
    }

    @RequestMapping(value = GIFT_ID_PATH, method = RequestMethod.PUT)
    public @ResponseBody
    GiftResult update(@PathVariable(ID) long id, @RequestBody Gift gift) {
        ServerGiftChain giftChain = getGiftChain(gift);
        ServerUser user = users.findOne(gift.getUserID());
        return fromGift(gifts.save(gifts.findOne(id).update(gift, user, giftChain)), user);
    }

    @RequestMapping(value = GIFT_ID_PATH, method = RequestMethod.DELETE)
    public @ResponseBody
    boolean deleteGift(@PathVariable(ID) long id) {
        gifts.delete(id);
        return true;
    }

    @RequestMapping(value = GIFT_ID_PATH, method = RequestMethod.GET)
    public @ResponseBody
    GiftResult findOne(@PathVariable(ID) Long id, Principal p) {

        // TODO use join

        return fromGift(gifts.findOne(id), getUser(p));
    }

    @RequestMapping(value = GIFT_LIKE_PATH, method = RequestMethod.PUT)
    public @ResponseBody
    boolean setLike(@PathVariable(ID) long id, @PathVariable(LIKE) boolean like, Principal p) {

        // TODO use join

        ServerUser user = getUser(p);
        ServerGift gift = gifts.findOne(id);
        ServerUser creator = gift.getUser();
        ServerGiftMetadata metadata = getMetadata(user, gift);
        if (like != metadata.isLiked()) {
            if (like) {
                gift.incrementLikes();
                creator.incrementLikes();
            } else {
                gift.decrementLikes();
                creator.decrementLikes();
            }
            metadata.setLiked(like);
            giftMetadata.save(metadata);
            users.save(creator);
            gifts.save(gift);
        }
        return true;
    }

    @RequestMapping(value = GIFT_FLAG_PATH, method = RequestMethod.PUT)
    public @ResponseBody
    boolean setFlag(@PathVariable(ID) long id, @PathVariable(FLAG) boolean flag, Principal p) {

        // TODO use join

        ServerUser user = getUser(p);
        ServerGift gift = gifts.findOne(id);
        ServerGiftMetadata metadata = getMetadata(user, gift);
        if (flag != metadata.isFlagged()) {
            if (flag)
                gift.incrementFlagged();
            else
                gift.decrementFlagged();
            metadata.setFlagged(flag);
            giftMetadata.save(metadata);
            gifts.save(gift);
        }
        return true;
    }

    @RequestMapping(value = QUERY_BY_TITLE, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByTitle(@RequestParam(TITLE) String title,
            @RequestParam(ORDER) int order,
            @RequestParam(DIRECTION) int direction,
            Principal principal) {
        Sort sort = getSort(direction, order);
        if (title.isEmpty()) 
            return toResult(Lists.newArrayList(gifts.findAll(sort)), principal);
        return toResult(gifts.findByTitleLike(likeTitle(title), sort), principal);
    }

    @RequestMapping(value = QUERY_BY_USER, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByUser(@RequestParam(TITLE) String title,
            @RequestParam(USER) long userID,
            @RequestParam(ORDER) int order,
            @RequestParam(DIRECTION) int direction,
            Principal principal) {
        ServerUser user = users.findOne(userID);
        Sort sort = getSort(direction, order);
        if (title.isEmpty()) 
            return toResult(Lists.newArrayList(gifts.findByUser(user, sort)), principal);
        return toResult(gifts.findByUserAndTitleLike(user, likeTitle(title), sort), principal);
    }
    
    private String likeTitle(String title) {
        return "%" + title  + "%";
    }

    @RequestMapping(value = QUERY_BY_GIFT_CHAIN, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByGiftChain(@RequestParam(TITLE) String title,
            @RequestParam(GIFT_CHAIN) long giftChainID,
            @RequestParam(ORDER) int order,
            @RequestParam(DIRECTION) int direction,
            Principal principal) {
        ServerGiftChain giftChain = giftChains.findOne(giftChainID);
        Sort sort = getSort(direction, order);
        if (title.isEmpty()) 
            return toResult(Lists.newArrayList(gifts.findByGiftChain(giftChain, sort)), principal);
        return toResult(gifts.findByGiftChainAndTitleLike(giftChain, likeTitle(title), sort), principal);
    }

    @RequestMapping(value = QUERY_BY_TOP_GIFT_GIVERS, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByTopGiftGivers(@RequestParam(TITLE) String title, @RequestParam(DIRECTION) int direction, Principal p) {
        
        // TODO this is going to be horribly expensive
        
        Sort.Direction d = getDirection(direction);
        Sort userSort = new Sort(d, ServerUser.USER_LIKES);
        List<GiftResult> results = new ArrayList<GiftResult>();
        ServerUser user = getUser(p);
        
        // Get the top gift givers
        
        Iterable<ServerUser> topUsers = users.findAll(userSort);
        String likeTitle = likeTitle(title);
        
        for (ServerUser topUser : topUsers) {
            
            // Get the most popular gift of each top gift giver
            
            Sort giftSort = new Sort(d, ServerGift.LIKES);
            ServerGift sg = null;
            if (title.isEmpty()) 
                sg = head(gifts.findByUser(topUser, giftSort));
            else
                sg = head(gifts.findByUserAndTitleLike(topUser, likeTitle, giftSort));
            results.add(fromGift(sg, user));
        }
        return results;
    }
    
    @RequestMapping(value = GIFT_VIDEO_PATH, method = RequestMethod.POST)
    public @ResponseBody
    ResourceStatus setVideoData(@PathVariable(ID) long id,
            @RequestParam(DATA) MultipartFile videoData) throws IOException {
        setData(id, videoData, videoManager);
        return new ResourceStatus(ResourceState.READY);
    }

    @RequestMapping(value = GIFT_VIDEO_PATH, method = RequestMethod.GET)
    public void getVideoData(@PathVariable(ID) long id, HttpServletResponse response)
            throws IOException {
        getData(id, response, videoManager);
    }

    @RequestMapping(value = GIFT_IMAGE_PATH, method = RequestMethod.POST)
    public @ResponseBody
    ResourceStatus setImageData(@PathVariable(ID) long id,
            @RequestParam(DATA) MultipartFile imageData) throws IOException {
        setData(id, imageData, imageManager);
        return new ResourceStatus(ResourceState.READY);
    }

    @RequestMapping(value = GIFT_IMAGE_PATH, method = RequestMethod.GET)
    public void getImageData(@PathVariable(ID) long id, HttpServletResponse response)
            throws IOException {
        getData(id, response, imageManager);
    }

    private Path setData(long id, MultipartFile data, FileManager<ServerGift> manager)
            throws IOException {
        if (gifts.exists(id)) {
            ServerGift gift = gifts.findOne(id);
            return manager.saveData(gift, data.getInputStream());
        } else
            throw new ResourceNotFoundException();
    }

    private void getData(long id, HttpServletResponse response, FileManager<ServerGift> manager)
            throws IOException {
        if (gifts.exists(id)) {
            ServerGift gift = gifts.findOne(id);
            if (manager.hasData(gift)) {
                manager.getData(gift, response.getOutputStream());
            } else
                throw new ResourceNotFoundException();
        } else
            throw new ResourceNotFoundException();

    }
    
    private Sort.Direction getDirection(int direction) {
        ResultOrderDirection resultDirection = ResultOrderDirection.toEnum(direction);
        return resultDirection == ResultOrderDirection.ASCENDING ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }

    private Sort getSort(int order, int direction) {
        String col = ResultOrder.toEnum(order) == ResultOrder.LIKES ? ServerGift.LIKES
                : ServerGift.CREATED;
        return new Sort(getDirection(direction), col);
    }

    private ServerUser getUser(Principal p) {
        return users.findByName(p.getName()).get(0);
    }

    private GiftResult fromGift(ServerGift gift, ServerUser user) {
        // TODO - do proper join!
        ServerGiftMetadata metadata = getMetadata(user, gift);
        boolean like = metadata != null ? metadata.isLiked() : false;
        boolean flag = metadata != null ? metadata.isFlagged() : false;
        ServerUser creator = gift.getUser();
        return new GiftResult(gift.getId(),
                gift.getTitle(),
                gift.getDescription(),
                "",
                "",
                gift.getCreated(),
                creator.getId(),
                like,
                flag,
                gift.getLikes(),
                gift.isFlagged(),
                gift.getGiftChain().getId(),
                gift.getGiftChain().getName(),
                creator.getLikes(),
                creator.getName());
    }

    private List<GiftResult> toResult(Collection<ServerGift> query, Principal p) {
        ServerUser user = getUser(p);
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : query) {
            results.add(fromGift(gift, user));
        }
        return results;
    }

    private ServerGiftMetadata getMetadata(ServerUser user, ServerGift gift) {
        ServerGiftMetadataPk pk = new ServerGiftMetadataPk(user, gift);
        return giftMetadata.exists(pk) ? giftMetadata.findOne(pk) : new ServerGiftMetadata(pk);
    }

    private ServerGiftChain getGiftChain(Gift gift) {
        return giftChains.findOne(gift.getGiftChainID());
    }
    
    private <T> T head(Collection<T> collection) {
        return collection.size() > 0 ? collection.iterator().next() : null;
    }

}
