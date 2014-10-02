package com.pataniqa.coursera.potlatch.server;

import java.io.IOException;
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
import static com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi.*;
import com.pataniqa.coursera.potlatch.store.remote.ResourceStatus;

import org.apache.log4j.Logger;

@Controller
public class GiftService {

    static Logger log = Logger.getLogger(GiftService.class.getName());

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
        System.out.println("Got gift: " + gift);
        ServerGiftChain giftChain = getGiftChain(gift);
        System.out.println("Got giftChain: " + giftChain);
        ServerUser user = users.findOne(gift.getUserID());
        System.out.println("Got user: " + user);
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
    boolean setLike(@PathVariable(ID) long id, @PathVariable boolean like, Principal p) {

        // TODO use join

        ServerUser user = getUser(p);
        ServerGift gift = gifts.findOne(id);
        ServerGiftMetadata metadata = getMetadata(user, gift);
        if (like != metadata.isLiked()) {
            if (like) {
                gift.incrementLikes();
                user.incrementLikes();
            } else {
                gift.decrementLikes();
                user.decrementLikes();
            }
            metadata.setLiked(like);
            giftMetadata.save(metadata);
            users.save(user);
            gifts.save(gift);
        }
        return true;
    }

    @RequestMapping(value = GIFT_FLAG_PATH, method = RequestMethod.PUT)
    public @ResponseBody
    boolean setFlag(@PathVariable(ID) long id, @PathVariable boolean flag, Principal p) {

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
        return toResult(gifts.findByTitleLike(title, sort), principal);
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
        return toResult(gifts.findByUserAndTitleLike(user, title, sort), principal);
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
        return toResult(gifts.findByGiftChainAndTitleLike(giftChain, title, sort), principal);
    }

    @RequestMapping(value = QUERY_BY_TOP_GIFT_GIVERS, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByTopGiftGivers(@RequestParam(TITLE) String title, @RequestParam(DIRECTION) int direction, Principal p) {

        // TODO need to get a list of users ranked by the number of likes
        // then turn that into a list of gifts

        return null;
    }

    @RequestMapping(value = GIFT_VIDEO_PATH, method = RequestMethod.POST)
    public @ResponseBody
    ResourceStatus setVideoData(@PathVariable(ID) long id,
            @RequestParam(DATA) MultipartFile videoData) throws IOException {

        // TODO need to set video field in ServerGift

        return setData(id, videoData, videoManager);
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

        // TODO need to set image field in ServerGift

        return setData(id, imageData, imageManager);
    }

    @RequestMapping(value = GIFT_IMAGE_PATH, method = RequestMethod.GET)
    public void getImageData(@PathVariable(ID) long id, HttpServletResponse response)
            throws IOException {
        getData(id, response, imageManager);
    }

    private ResourceStatus setData(long id, MultipartFile data, FileManager<ServerGift> manager)
            throws IOException {
        if (gifts.exists(id)) {
            ServerGift gift = gifts.findOne(id);
            manager.saveData(gift, data.getInputStream());
            return new ResourceStatus(ResourceStatus.ResourceState.READY);
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

    private Sort getSort(int order, int direction) {
        String col = ResultOrder.toEnum(order) == ResultOrder.LIKES ? ServerGift.LIKES
                : ServerGift.CREATED;
        ResultOrderDirection resultDirection = ResultOrderDirection.toEnum(direction);
        Sort.Direction d = resultDirection == ResultOrderDirection.ASCENDING ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return new Sort(d, col);
    }

    private ServerUser getUser(Principal p) {
        return users.findByName(p.getName()).get(0);
    }

    private GiftResult fromGift(ServerGift gift, ServerUser user) {
        // TODO - do proper join!
        ServerGiftMetadata metadata = getMetadata(user, gift);
        boolean like = metadata != null ? metadata.isLiked() : false;
        boolean flag = metadata != null ? metadata.isFlagged() : false;
        return new GiftResult(gift.getId(),
                gift.getTitle(),
                gift.getDescription(),
                gift.getVideoUri(),
                gift.getImageUri(),
                gift.getCreated(),
                gift.getUser().getId(),
                like,
                flag,
                gift.getLikes(),
                gift.isFlagged(),
                gift.getGiftChain().getId(),
                gift.getGiftChain().getName(),
                gift.getUser().getLikes(),
                gift.getUser().getName());
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

}
