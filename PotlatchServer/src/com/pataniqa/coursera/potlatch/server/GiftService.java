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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi;
import com.pataniqa.coursera.potlatch.store.remote.ResourceStatus;

public class GiftService {

    @Autowired
    private GiftRepository gifts;

    @Autowired
    private UserRepository users;

    @Autowired
    private GiftChainRepository giftChains;

    @Autowired
    private GiftMetadataRepository giftMetadata;

    private FileManager imageManager = new FileManager("image", "jpg");

    private FileManager videoManager = new FileManager("video", "mpg");

    @RequestMapping(value = RemoteGiftApi.GIFT_PATH, method = RequestMethod.POST)
    public @ResponseBody
    Gift insert(@RequestBody Gift gift) {
        ServerGiftChain giftChain = giftChains.findOne(gift.getGiftID());
        ServerUser user = users.findOne(gift.getUserID());
        return gifts.save(new ServerGift(gift, user, giftChain)).toClient();
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_PATH, method = RequestMethod.GET)
    public List<GiftResult> findAll(Principal p) {
        ServerUser user = getUser(p);
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : gifts.findAll()) {
            results.add(fromGift(gift, user));
        }
        return results;
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_ID_PATH, method = RequestMethod.PUT)
    public void update(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id, @RequestBody Gift gift) {
        ServerGiftChain giftChain = giftChains.findOne(gift.getGiftID());
        ServerUser user = users.findOne(gift.getUserID());
        gifts.save(new ServerGift(gift, user, giftChain));
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_ID_PATH, method = RequestMethod.DELETE)
    public void deleteGift(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id) {
        gifts.delete(id);
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_ID_PATH, method = RequestMethod.GET)
    public GiftResult findOne(@PathVariable(RemoteGiftApi.ID_PARAMETER) Long id, Principal p) {
        // TODO use join
        return fromGift(gifts.findOne(id), getUser(p));
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_LIKE_PATH, method = RequestMethod.PUT)
    public void setLike(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id,
            @PathVariable boolean like,
            Principal p) {

        // TODO use join

        ServerGiftMetadata metadata = giftMetadata.findOne(new ServerGiftMetadataPk(getUser(p),
                gifts.findOne(id)));

        // TODO need to update total likes for user

        // TODO need to update total likes for gift

        metadata.setUserLike(like);
        giftMetadata.save(metadata);
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_FLAG_PATH, method = RequestMethod.PUT)
    public void setFlag(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id,
            @PathVariable boolean flag,
            Principal p) {

        // TODO use join

        ServerGiftMetadata metadata = giftMetadata.findOne(new ServerGiftMetadataPk(getUser(p),
                gifts.findOne(id)));

        // TODO need to update total flags for user

        // TODO need to update total flags for gift

        metadata.setUserFlagged(flag);
        giftMetadata.save(metadata);
    }

    private Sort.Direction getDirection(int direction) {
        ResultOrderDirection resultDirection = ResultOrderDirection.toEnum(direction);
        return resultDirection == ResultOrderDirection.ASCENDING ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }

    @RequestMapping(value = RemoteGiftApi.QUERY_BY_TITLE, method = RequestMethod.GET)
    public List<GiftResult> queryByTitle(String title, int order, int direction, Principal p) {
        String col = null;
        if (ResultOrder.toEnum(order) == ResultOrder.LIKES) {
            col = ServerGift.LIKES;
        } else if (ResultOrder.toEnum(order) == ResultOrder.TOP_GIFT_GIVERS) {

            // TODO fix me this won't work no such column

            col = "top_gift_givers";
        } else {
            col = ServerGift.CREATED;
        }
        return toResult(gifts.findByTitleLike(title, new Sort(getDirection(direction), col)), p);
    }

    @RequestMapping(value = RemoteGiftApi.QUERY_BY_USER, method = RequestMethod.GET)
    public List<GiftResult> queryByUser(String title,
            long userID,
            int order,
            int direction,
            Principal p) {
        ServerUser user = users.findOne(userID);
        String col = ResultOrder.toEnum(order) == ResultOrder.LIKES ? ServerGift.LIKES
                : ServerGift.CREATED;
        return toResult(gifts.findByUserAndTitleLike(user, title, new Sort(getDirection(direction),
                col)), p);
    }

    @RequestMapping(value = RemoteGiftApi.QUERY_BY_GIFT_CHAIN, method = RequestMethod.GET)
    public List<GiftResult> queryByGiftChain(String title,
            long giftChainID,
            int order,
            int direction,
            Principal p) {
        ServerGiftChain giftChain = giftChains.findOne(giftChainID);
        String col = ResultOrder.toEnum(order) == ResultOrder.LIKES ? ServerGift.LIKES
                : ServerGift.CREATED;
        return toResult(gifts.findByGiftChainAndTitleLike(giftChain,
                title,
                new Sort(getDirection(direction), col)), p);
    }

    private ServerUser getUser(Principal p) {
        return users.findByUsername(p.getName()).get(0);
    }

    private GiftResult fromGift(ServerGift gift, ServerUser user) {
        // TODO - do proper join!
        ServerGiftMetadata metadata = giftMetadata.findOne(new ServerGiftMetadataPk(user, gift));
        boolean like = metadata != null ? metadata.isUserLike() : false;
        boolean flag = metadata != null ? metadata.isUserFlagged() : false;
        return new GiftResult(gift.getGiftID(),
                gift.getTitle(),
                gift.getDescription(),
                gift.getVideoUri(),
                gift.getImageUri(),
                gift.getCreated(),
                gift.getUserID(),
                like,
                flag,
                gift.getLikes(),
                gift.isFlagged(),
                gift.getGiftChainID(),
                gift.getGiftChain().getGiftChainName(),
                gift.getUser().getUserLikes(),
                gift.getUser().getUsername());
    }

    private List<GiftResult> toResult(Collection<ServerGift> query, Principal p) {
        ServerUser user = getUser(p);
        List<GiftResult> results = new ArrayList<GiftResult>();
        for (ServerGift gift : query) {
            results.add(fromGift(gift, user));
        }
        return results;
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_VIDEO_PATH, method = RequestMethod.POST)
    public @ResponseBody
    ResourceStatus setVideoData(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id,
            @RequestParam(RemoteGiftApi.DATA_PARAMETER) MultipartFile videoData) throws IOException {
        return setData(id, videoData, videoManager);
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_VIDEO_PATH, method = RequestMethod.GET)
    public void getVideoData(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id,
            HttpServletResponse response) throws IOException {
        getData(id, response, videoManager);
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_IMAGE_PATH, method = RequestMethod.POST)
    public @ResponseBody
    ResourceStatus setImageData(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id,
            @RequestParam(RemoteGiftApi.DATA_PARAMETER) MultipartFile imageData) throws IOException {
        return setData(id, imageData, imageManager);
    }

    @RequestMapping(value = RemoteGiftApi.GIFT_IMAGE_PATH, method = RequestMethod.GET)
    public void getImageData(@PathVariable(RemoteGiftApi.ID_PARAMETER) long id,
            HttpServletResponse response) throws IOException {
        getData(id, response, imageManager);
    }

    private ResourceStatus setData(long id, MultipartFile data, FileManager manager)
            throws IOException {
        if (gifts.exists(id)) {
            ServerGift gift = gifts.findOne(id);
            manager.saveData(gift, data.getInputStream());
            return new ResourceStatus(ResourceStatus.ResourceState.READY);
        } else
            throw new ResourceNotFoundException();
    }

    private void getData(long id, HttpServletResponse response, FileManager manager)
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

}
