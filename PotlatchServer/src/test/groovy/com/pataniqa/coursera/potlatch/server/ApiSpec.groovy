package com.pataniqa.coursera.potlatch.server

import retrofit.RestAdapter.LogLevel
import retrofit.client.ApacheClient

import org.codehaus.jackson.map.ObjectMapper
import com.pataniqa.coursera.potlatch.model.GetId
import com.pataniqa.coursera.potlatch.model.Gift
import com.pataniqa.coursera.potlatch.model.GiftChain
import com.pataniqa.coursera.potlatch.model.User
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftChainApi
import com.pataniqa.coursera.potlatch.store.remote.RemoteUserApi
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder

import com.pataniqa.coursera.potlatch.server.JacksonConverter

class ApiSpec extends spock.lang.Specification {
    
    def TEST_URL = "https://localhost:8443"
    def USERNAME1 = "user0"
    def PASSWORD = "pass"
    def CLIENT_ID = "mobile"
    
    def converter = new JacksonConverter(new ObjectMapper())
    
    def svcUser = new SecuredRestBuilder()
        .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
        .setEndpoint(TEST_URL)
        .loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
        .setLogLevel(LogLevel.FULL)
        .username(USERNAME1)
        .password(PASSWORD)
        .clientId(CLIENT_ID)
        .setConverter(converter)
        .build()
        
    def userSvcUser = svcUser.create(RemoteUserApi.class)
    
    def giftChainSvcUser = svcUser.create(RemoteGiftChainApi.class)
    
    def "Create, retrieve, update and delete a gift chain"() {
        
        def numberOfGiftChains = { giftChainSvcUser.findAll().size() }
                
        when: "a gift chain is added"
        def numberOfGiftChainsBefore = numberOfGiftChains()
        def giftChain = new GiftChain("some-random-giftchain-" + new Random().nextLong())
        def newGiftChain = giftChainSvcUser.insert(giftChain)

        then: "the new gift chain should have the same name"
        giftChain.getName() == newGiftChain.getName()
        and: "there should be one more gift chain"
        numberOfGiftChains() == numberOfGiftChainsBefore + 1

        when: "a gift chain is updated"
        numberOfGiftChainsBefore = numberOfGiftChains()
        giftChainSvcUser.update(newGiftChain.getId(), newGiftChain)

        then: "there should be the same number of gift chains"
        numberOfGiftChains() == numberOfGiftChainsBefore

        when: "a gift chain is deleted"
        numberOfGiftChainsBefore = numberOfGiftChains()
        giftChainSvcUser.delete(newGiftChain.getId())

        then: "there should be one less gift chain"
        numberOfGiftChains() == numberOfGiftChainsBefore - 1
        
    }

    def "Create, retrieve, update and delete a user"() {
        
        def numberOfUsers = { userSvcUser.findAll().size() }
            
        when: "a user is added"
        def numberOfUsersBefore = numberOfUsers()
        def user = new User("some-random-user-" + new Random().nextLong())
        def newUser = userSvcUser.insert(user)

        then: "the new user should have the same name"
        user.getName() == newUser.getName()
        and: "there should be one more user"
        numberOfUsers() == numberOfUsersBefore + 1

        when: "a user is updated"
        numberOfUsersBefore = numberOfUsers()
        userSvcUser.update(newUser.getId(), newUser)

        then: "there should be the same number of users"
        numberOfUsers() == numberOfUsersBefore

        when: "a user is deleted"
        numberOfUsersBefore = numberOfUsers()
        userSvcUser.delete(newUser.getId())

        then: "there should be one less user"
        numberOfUsers() == numberOfUsersBefore - 1
        
    }
    
    def "Create, retrieve, update and delete a gift"() {
        
        def giftSvcUser = svcUser.create(RemoteGiftApi.class)
    
        def numberOfGifts = { giftSvcUser.findAll().size() }
        
        println(giftSvcUser.findAll())
        
        when: "a gift chain is created"
        def giftChain = new GiftChain("some-random-giftchain-" + new Random().nextLong())
        def newGiftChain = giftChainSvcUser.insert(giftChain)
        
        println(numberOfGifts())
            
        and: "a gift is created"
        def numberOfGiftsBefore = numberOfGifts()
        def title = "some-random-gift-" + new Random().nextLong()
        def description = "some-random-description-" + new Random().nextLong()
        def videoUri = "http://www.example.com/gift/" + new Random().nextLong() + "/video"
        def imageUri = "http://www.example.com/gift/" + new Random().nextLong() + "/image"
        def created = new Date()
        def userId = userSvcUser.findAll().get(0).getId()
        def giftChainId = newGiftChain.getId()
        
        def gift = new Gift(GetId.UNDEFINED_ID, title, description, videoUri, imageUri, created, userId, giftChainId) 
        def newGift = giftSvcUser.insert(gift)

        then: "the new gift should have the same properties"
        gift.getTitle() == newGift.getTitle()
        gift.getDescription() == newGift.getDescription()
        gift.getGiftChainID() == newGift.getGiftChainID()
        gift.getUserID() == newGift.getUserID()
        and: "there should be one more user"
        numberOfGifts() == numberOfGiftsBefore + 1

        when: "a gift is updated"
        numberOfGiftsBefore = numberOfGifts()
        giftSvcUser.update(newGift.getId(), newGift)

        then: "there should be the same number of gifts"
        numberOfGifts() == numberOfGiftsBefore

        when: "a gift is deleted"
        numberOfGiftsBefore = numberOfGifts()
        giftSvcUser.delete(newGift.getId())

        then: "there should be one less gift"
        numberOfGifts() == numberOfGiftsBefore - 1
        
    }
    
    def "The Gift API should work correctly"() {
        
//        List<GiftResult> findAll()    
//        Gift insert(@Body Gift data)
//        Gift update(long id, @Body Gift data)
//        boolean delete(long id)
//        GiftResult findOne(Long id)
//        boolean setLike(long id, boolean like)
//        boolean setFlag(long id, boolean flag)
//        List<GiftResult> queryByTitle(String title, int order, int direction)
//        List<GiftResult> queryByUser(String title, long userID, int order, int direction)
//        List<GiftResult> queryByTopGiftGivers(String title, int direction)
//        List<GiftResult> queryByGiftChain(String title, String giftChain, int order, int direction)
//        ResourceStatus setImageData(@Path(ID_PARAMETER) long id,
//                @Part(DATA_PARAMETER) TypedFile imageData)
//        Response getImageData(@Path(ID_PARAMETER) long id)
//        ResourceStatus setVideoData(@Path(ID_PARAMETER) long id,
//                @Part(DATA_PARAMETER) TypedFile imageData)
//        Response getVideoData(@Path(ID_PARAMETER) long id)

    }
}

