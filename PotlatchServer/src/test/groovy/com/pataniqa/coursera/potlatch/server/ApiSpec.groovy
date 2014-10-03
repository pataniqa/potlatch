package com.pataniqa.coursera.potlatch.server

import retrofit.RestAdapter.LogLevel
import retrofit.client.ApacheClient

import org.codehaus.jackson.map.ObjectMapper
import com.pataniqa.coursera.potlatch.model.*
import com.pataniqa.coursera.potlatch.store.*
import com.pataniqa.coursera.potlatch.store.remote.*

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

    def giftSvcUser = svcUser.create(RemoteGiftApi.class)

    def numberOfGiftChains = { giftChainSvcUser.findAll().size() }

    def numberOfUsers = { userSvcUser.findAll().size() }

    def numberOfGifts = { giftSvcUser.findAll().size() }
    
    def "Create, retrieve, update and delete a gift chain"() {
        
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
        
        println(giftSvcUser.findAll())
        
        when: "a gift chain is created"
        def giftChain = giftChainSvcUser.insert(new GiftChain("some-random-giftchain-" + new Random().nextLong()))
        
        println(numberOfGifts())
            
        and: "a gift is created"
        def numberOfGiftsBefore = numberOfGifts()
        def title = "some-random-gift-" + new Random().nextLong()
        def description = "some-random-description-" + new Random().nextLong()
        def created = new Date()
        def userId = userSvcUser.findAll().get(0).getId()
        def giftChainId = giftChain.getId()
        
        def gift = new Gift(GetId.UNDEFINED_ID, title, description, null, null, created, userId, giftChainId) 
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
        
        then: "a giftchain is deleted"
        giftChainSvcUser.delete(giftChain.getId())
    }
    
    def "Insert some giftchains, users and gifts"(String title, String description, String chain, String name) {
        
        when: "add a gift"
        def numberOfGiftsBefore = numberOfGifts()
        def giftChain = giftChainSvcUser.insert(new GiftChain(chain))
        def created = new Date()
        def user = userSvcUser.insert(new User(name))
        def gift = new Gift(GetId.UNDEFINED_ID, 
            title, 
            description, 
            null, 
            null, 
            created, 
            user.getId(), 
            giftChain.getId())
        def newGift = giftSvcUser.insert(gift)
        
        then: "there should be one more gift"
        numberOfGifts() == numberOfGiftsBefore + 1
        
        when: "we find that gift"
        def result = giftSvcUser.findOne(newGift.getId())
        
        then: "it should match"
        result.getTitle() == title
        result.getDescription() == description
        result.getUsername() == name
        result.getGiftChainName() == chain
        result.isLike() == false
        result.isFlag() == false
        result.isFlagged() == false
        result.getLikes() == 0
        result.getUserLikes() == 0
        
        when: "we like the gift"
        giftSvcUser.setLike(newGift.getId(), true)
        
        then: "it should be liked"
        checkLikes(giftSvcUser.findOne(newGift.getId()), true, 1, 1)
        
        when: "we unlike the gift"
        giftSvcUser.setLike(newGift.getId(), false)
        
        then: "it should not liked"
        checkLikes(giftSvcUser.findOne(newGift.getId()), false, 0, 0)
        
        when: "we flag the gift"
        giftSvcUser.setFlag(newGift.getId(), true)
        
        then: "it should be flagged"
        checkFlagged(giftSvcUser.findOne(newGift.getId()), true, true)
        
        when: "we unflag the gift"
        giftSvcUser.setFlag(newGift.getId(), false)
        
        then: "it should be unflagged"
        checkFlagged(giftSvcUser.findOne(newGift.getId()), false, false)
        
        where:
        title | description | chain | name
       "A car" | "A fast Porsche car" | "cars" | "fred"
       "A horse" | "A racing horse at West Derby" | "horses" | "john"
       "Temples" | "Temples in Jhubei" | "Temples" | "jenny"
       "Model T" | "A very old car" | "cars" | "fred"
    }
    
    def checkLikes(GiftResult result, boolean like, int likes, int userLikes) {
        result.isLike() == like
        result.getLikes() == likes
        result.getUserLikes() == userLikes
    }
    
    def checkFlagged(GiftResult result, boolean flag, boolean flagged) {
        result.isFlag() == flag
        result.isFlagged() == flagged
    }
    
    def checkGift(GiftResult result, String title, String description, String chain, String name) {
        result.getTitle() == title
        result.getDescription() ==  title
        result.getGiftChainName() == description
        result.getUsername() == name
    }
    
    def "Query by title"() {
        when: "query by title"
        def query = giftSvcUser.queryByTitle("", ResultOrder.LIKES.ordinal(), ResultOrderDirection.ASCENDING.ordinal())
        
        then: "there should be four results"
        query.size() >= 4
        
        when: "query by title"
        def query2 = giftSvcUser.queryByTitle("car", ResultOrder.LIKES.ordinal(), ResultOrderDirection.ASCENDING.ordinal())
        
        then: "there should be a result"
        query2.size() > 0
        
        when: "we get the first result"
        def result = query2.get(0)
        
        then: "it should match"
        checkGift(result, "A car", "A fast Porsche car", "cars", "fred")
    }  
    
    def "Query by user"() {
        when: "query by user"
        def user = userSvcUser.insert(new User("fred"))
        def query = giftSvcUser.queryByUser("", user.getId(), ResultOrder.TIME.ordinal(), ResultOrderDirection.ASCENDING.ordinal())
        
        then: "there should be two results"
        query.size() >= 2
        
        and: "the first result should match"
        checkGift(query.get(0), "A car", "A fast Porsche car", "cars", "fred")
        
        when: "query by time descending"
        def query2 = giftSvcUser.queryByUser("", user.getId(), ResultOrder.TIME.ordinal(), ResultOrderDirection.DESCENDING.ordinal())
        
        then: "there should be two results"
        query2.size() >= 2
        
        and: "the first result should match"
        checkGift(query2.get(0), "Model T", "A very old car", "cars", "fred")
    }
    
    def "Query by gift chain"() {
        when: "query by gift chain"
        def chain = giftChainSvcUser.insert(new GiftChain("cars"))
        def query = giftSvcUser.queryByGiftChain("", chain.getId(), ResultOrder.TIME.ordinal(), ResultOrderDirection.ASCENDING.ordinal())
        
        then: "there should be two results"
        query.size() >= 2
        
        and: "the first result should match"
        checkGift(query.get(0), "A car", "A fast Porsche car", "cars", "fred")
        
        when: "query by time descending"
        def query2 = giftSvcUser.queryByGiftChain("", chain.getId(), ResultOrder.TIME.ordinal(), ResultOrderDirection.DESCENDING.ordinal())
        
        then: "there should be two results"
        query2.size() >= 2
        
        and: "the first result should match"
        checkGift(query2.get(0), "Model T", "A very old car", "cars", "fred")
    }
    
    def "more tests"() {
//        List<GiftResult> queryByTopGiftGivers(String title, int direction)
//        ResourceStatus setImageData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile imageData)
//        Response getImageData(@Path(ID_PARAMETER) long id)
//        ResourceStatus setVideoData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile imageData)
//        Response getVideoData(@Path(ID_PARAMETER) long id)

    }
}

