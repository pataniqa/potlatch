package com.pataniqa.coursera.potlatch.server;

import static com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi.*;

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
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrder;
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrderDirection;

@Controller
public class GiftService {
    
    @Autowired private GiftRepository gifts;

    @Autowired private UserRepository users;

    @Autowired private GiftChainRepository giftChains;

    @Autowired private GiftMetadataRepository giftMetadata;

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
        return fromGift(gifts.findOne(id), getUser(p));
    }

    @RequestMapping(value = GIFT_LIKE_PATH, method = RequestMethod.PUT)
    public @ResponseBody
    boolean setLike(@PathVariable(ID) long id, @PathVariable(LIKE) boolean like, Principal p) {
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
            @RequestParam(ORDER) ResultOrder order,
            @RequestParam(DIRECTION) ResultOrderDirection direction,
            @RequestParam(HIDE) boolean hide,
            Principal principal) {
        Sort sort = getSort(order, direction);
        if (title.isEmpty())
            return toResult(Lists.newArrayList(gifts.findAll(sort)), principal, hide);
        return toResult(gifts.findByTitleLike(likeTitle(title), sort), principal, hide);
    }

    @RequestMapping(value = QUERY_BY_USER, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByUser(@RequestParam(TITLE) String title,
            @RequestParam(USER) long userID,
            @RequestParam(ORDER) ResultOrder order,
            @RequestParam(DIRECTION) ResultOrderDirection direction,
            @RequestParam(HIDE) boolean hide,
            Principal principal) {
        ServerUser user = users.findOne(userID);
        Sort sort = getSort(order, direction);
        if (title.isEmpty())
            return toResult(gifts.findByUser(user, sort), principal, hide);
        return toResult(gifts.findByUserAndTitleLike(user, likeTitle(title), sort), principal, hide);
    }

    private static String likeTitle(String title) {
        return "%" + title + "%";
    }

    @RequestMapping(value = QUERY_BY_GIFT_CHAIN, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByGiftChain(@RequestParam(TITLE) String title,
            @RequestParam(GIFT_CHAIN) long giftChainID,
            @RequestParam(ORDER) ResultOrder order,
            @RequestParam(DIRECTION) ResultOrderDirection direction,
            @RequestParam(HIDE) boolean hide,
            Principal principal) {
        ServerGiftChain giftChain = giftChains.findOne(giftChainID);
        Sort sort = getSort(order, direction);
        if (title.isEmpty())
            return toResult(gifts.findByGiftChain(giftChain, sort), principal, hide);
        return toResult(gifts.findByGiftChainAndTitleLike(giftChain, likeTitle(title), sort),
                principal,
                hide);
    }

    @RequestMapping(value = QUERY_BY_TOP_GIFT_GIVERS, method = RequestMethod.GET)
    public @ResponseBody
    List<GiftResult> queryByTopGiftGivers(@RequestParam(TITLE) String title,
            @RequestParam(DIRECTION) ResultOrderDirection direction,
            @RequestParam(HIDE) boolean hide,
            Principal p) {
        Sort.Direction d = getDirection(direction);
        Sort userSort = new Sort(d, "likes");
        List<GiftResult> results = new ArrayList<GiftResult>();
        ServerUser user = getUser(p);

        // Get the top gift givers

        Iterable<ServerUser> topUsers = users.findAll(userSort);
        String likeTitle = likeTitle(title);

        for (ServerUser topUser : topUsers) {

            // Get the most popular gift of each top gift giver

            Sort giftSort = new Sort(d, "likes");
            ServerGift sg = null;
            if (title.isEmpty())
                sg = head(gifts.findByUser(topUser, giftSort));
            else
                sg = head(gifts.findByUserAndTitleLike(topUser, likeTitle, giftSort));
            GiftResult result = fromGift(sg, user);
            if (result != null && (!result.isFlagged() || !hide))
                results.add(fromGift(sg, user));
        }
        return results;
    }

    @RequestMapping(value = GIFT_VIDEO_PATH, method = RequestMethod.POST)
    public @ResponseBody
    boolean setVideoData(@PathVariable(ID) long id,
            @RequestParam(DATA) MultipartFile videoData) throws IOException {
        setData("video", "mp4", id, videoData);
        return true;
    }

    @RequestMapping(value = GIFT_VIDEO_PATH, method = RequestMethod.GET)
    public void getVideoData(@PathVariable(ID) long id, HttpServletResponse response)
            throws IOException {
        getData("video", "mp4", id, response);
    }

    @RequestMapping(value = GIFT_IMAGE_PATH, method = RequestMethod.POST)
    public @ResponseBody
    boolean setImageData(@PathVariable(ID) long id,
            @RequestParam(DATA) MultipartFile imageData) throws IOException {
        setData("image", "png", id, imageData);
        return true;
    }

    @RequestMapping(value = GIFT_IMAGE_PATH, method = RequestMethod.GET)
    public void getImageData(@PathVariable(ID) long id, HttpServletResponse response)
            throws IOException {
        getData("image", "png", id, response);
    }

    private void setData(String dir, String extension, long id, MultipartFile data)
            throws IOException {
        if (gifts.exists(id)) {
            ServerFileManager.saveData(dir, extension, id, data.getInputStream());
            if (dir.equals("video"))
                gifts.findOne(id).setVideoUri("/gift/" + id + "/video");
            else 
                gifts.findOne(id).setImageUri("/gift/" + id + "/image");
        } else
            throw new ResourceNotFoundException();
    }

    private void getData(String dir, String extension, long id, HttpServletResponse response)
            throws IOException {
        if (gifts.exists(id)) {
            if (ServerFileManager.hasData(dir, extension, id)) {
                ServerFileManager.getData(dir, extension, id, response.getOutputStream());
            } else
                throw new ResourceNotFoundException();
        } else
            throw new ResourceNotFoundException();

    }

    private static Sort.Direction getDirection(ResultOrderDirection direction) {
        return direction == ResultOrderDirection.ASCENDING ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }

    private static Sort getSort(ResultOrder order, ResultOrderDirection direction) {
        String col = order == ResultOrder.LIKES ? "likes" : "created";
        return new Sort(getDirection(direction), col);
    }

    private ServerUser getUser(Principal p) {
        return users.findByName(p.getName()).get(0);
    }

    private GiftResult fromGift(ServerGift gift, ServerUser user) {
        // TODO - do proper join!
        if (gift == null)
            return null;
        ServerGiftMetadata metadata = getMetadata(user, gift);
        boolean like = metadata != null ? metadata.isLiked() : false;
        boolean flag = metadata != null ? metadata.isFlagged() : false;
        ServerUser creator = gift.getUser();
        return new GiftResult(gift.getId(),
                gift.getTitle(),
                gift.getDescription(),
                gift.getImageUri(),
                gift.getVideoUri(),
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

    private List<GiftResult> toResult(Collection<ServerGift> query, Principal p, boolean hide) {
        ServerUser user = getUser(p);
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : query) {
            GiftResult result = fromGift(gift, user);
            if (!gift.isFlagged() || !hide)
                results.add(result);
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

    private static <T> T head(Collection<T> collection) {
        return collection.size() > 0 ? collection.iterator().next() : null;
    }

}
