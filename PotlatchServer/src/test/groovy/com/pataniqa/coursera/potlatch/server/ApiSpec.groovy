package com.pataniqa.coursera.potlatch.server

import retrofit.RestAdapter.LogLevel
import retrofit.client.ApacheClient

import com.fasterxml.jackson.databind.ObjectMapper
import com.pataniqa.coursera.potlatch.model.*
import com.pataniqa.coursera.potlatch.store.*
import com.pataniqa.coursera.potlatch.store.remote.*


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

    def numberOfGiftChains = { giftChainSvcUser.findAll().toBlocking().last().size() }
    def insertChain(GiftChain chain) { giftChainSvcUser.insert(chain).toBlocking().last() }
    def updateChain(GiftChain chain) { giftChainSvcUser.update(chain.getId(), chain).toBlocking().last() }
    def deleteChain(GiftChain chain) { giftChainSvcUser.delete(chain.getId()).toBlocking().last() }
    
    def numberOfUsers = { userSvcUser.findAll().toBlocking().last().size() }
    def insertUser(User user) { userSvcUser.insert(user).toBlocking().last() }
    def updateUser(User user) { userSvcUser.update(user.getId(), newUser).toBlocking().last() }
    def deleteUser(User user) { userSvcUser.delete(user.getId()).toBlocking().last() }
    def findAllUser() { userSvcUser.findAll().toBlocking().last() }

    def numberOfGifts = { giftSvcUser.findAll().toBlocking().last().size() }
    def findAllGift() { giftSvcUser.findAll().toBlocking().last() }
    def insertGift(Gift gift) { giftSvcUser.insert(gift).toBlocking().last() }
    def updateGift(Gift gift) { giftSvcUser.update(newGift.getId(), newGift).toBlocking.last() }
    def deleteGift(Gift gift) { giftSvcUser.delete(newGift.getId()).toBlocking().last() }
    def findOneGift(long giftId) { giftSvcUser.findOne(newGift.getId()).toBlocking().last() }
    def setLike(Gift gift, boolean like) { giftSvcUser.setLike(newGift.getId(), true).toBlocking().last() }
    def setFlag(Gift gift, boolean flag) { giftSvcUser.setFlag(newGift.getId(), true).toBlocking().last() }
    
    def "Create, retrieve, update and delete a gift chain"() {
        
        when: "a gift chain is added"
        def numberOfGiftChainsBefore = numberOfGiftChains()
        def giftChain = new GiftChain("some-random-giftchain-" + new Random().nextLong())
        def newGiftChain = insertGiftChain(giftChain)

        then: "the new gift chain should have the same name"
        giftChain.getName() == newGiftChain.getName()
        and: "there should be one more gift chain"
        numberOfGiftChains() == numberOfGiftChainsBefore + 1

        when: "a gift chain is updated"
        numberOfGiftChainsBefore = numberOfGiftChains()
        updateChain(newGiftChain)

        then: "there should be the same number of gift chains"
        numberOfGiftChains() == numberOfGiftChainsBefore

        when: "a gift chain is deleted"
        numberOfGiftChainsBefore = numberOfGiftChains()
        deleteChain(newGiftChain)

        then: "there should be one less gift chain"
        numberOfGiftChains() == numberOfGiftChainsBefore - 1
        
    }

    def "Create, retrieve, update and delete a user"() {
        
        when: "a user is added"
        def numberOfUsersBefore = numberOfUsers()
        def user = new User("some-random-user-" + new Random().nextLong())
        def newUser = insertUser(user)

        then: "the new user should have the same name"
        user.getName() == newUser.getName()
        and: "there should be one more user"
        numberOfUsers() == numberOfUsersBefore + 1

        when: "a user is updated"
        numberOfUsersBefore = numberOfUsers()
        updateUser(newUser.getId(), newUser)

        then: "there should be the same number of users"
        numberOfUsers() == numberOfUsersBefore

        when: "a user is deleted"
        numberOfUsersBefore = numberOfUsers()
        deleteUser(newUser.getId()).toBlocking().last()

        then: "there should be one less user"
        numberOfUsers() == numberOfUsersBefore - 1
        
    }
    
    def "Create, retrieve, update and delete a gift"() {
        
        println(findAllGift())
        
        when: "a gift chain is created"
        def giftChain = insertChain(new GiftChain("some-random-giftchain-" + new Random().nextLong()))
        
        println(numberOfGifts())
            
        and: "a gift is created"
        def numberOfGiftsBefore = numberOfGifts()
        def title = "some-random-gift-" + new Random().nextLong()
        def description = "some-random-description-" + new Random().nextLong()
        def created = new Date()
        def userId = findAllUser().get(0).getId()
        def giftChainId = giftChain.getId()
        
        def gift = new Gift(GetId.UNDEFINED_ID, title, description, null, null, created, userId, giftChainId) 
        def newGift = insertGift(gift)

        then: "the new gift should have the same properties"
        gift.getTitle() == newGift.getTitle()
        gift.getDescription() == newGift.getDescription()
        gift.getGiftChainID() == newGift.getGiftChainID()
        gift.getUserID() == newGift.getUserID()
        and: "there should be one more user"
        numberOfGifts() == numberOfGiftsBefore + 1

        when: "a gift is updated"
        numberOfGiftsBefore = numberOfGifts()
        updateGift(newGift.getId(), newGift)

        then: "there should be the same number of gifts"
        numberOfGifts() == numberOfGiftsBefore

        when: "a gift is deleted"
        numberOfGiftsBefore = numberOfGifts()
        deleteGift(newGift.getId())

        then: "there should be one less gift"
        numberOfGifts() == numberOfGiftsBefore - 1
        
        then: "a giftchain is deleted"
        deleteChain(giftChain.getId())
    }
    
    def "Insert some giftchains, users and gifts"(String title, String description, String chain, String name) {
        
        when: "add a gift"
        def numberOfGiftsBefore = numberOfGifts()
        def giftChain = insertChain(new GiftChain(chain))
        def created = new Date()
        def user = insertUser(new User(name))
        def gift = new Gift(GetId.UNDEFINED_ID, 
            title, 
            description, 
            null, 
            null, 
            created, 
            user.getId(), 
            giftChain.getId())
        def newGift = insertGift(gift)
        
        then: "there should be one more gift"
        numberOfGifts() == numberOfGiftsBefore + 1
        
        when: "we find that gift"
        def result = findOneGift(newGift.getId())
        
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
        setLike(newGift.getId(), true)
        
        then: "it should be liked"
        checkLikes(findOneGift(newGift.getId()), true, 1, 1)
        
        when: "we unlike the gift"
        setLike(newGift.getId(), false)
        
        then: "it should not liked"
        checkLikes(findOneGift(newGift.getId()), false, 0, 0)
        
        when: "we flag the gift"
        setFlag(newGift.getId(), true)
        
        then: "it should be flagged"
        checkFlagged(findOneGift(newGift.getId()), true, true)
        
        when: "we unflag the gift"
        giftSvcUser.setFlag(newGift.getId(), false).toBlocking().last()
        
        then: "it should be unflagged"
        checkFlagged(giftSvcUser.findOne(newGift.getId()).toBlocking().last(), false, false)
        
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
        def query = giftSvcUser.queryByTitle("", ResultOrder.LIKES, ResultOrderDirection.ASCENDING).toBlocking().last()
        
        then: "there should be four results"
        query.size() >= 4
        
        when: "query by title"
        def query2 = giftSvcUser.queryByTitle("car", ResultOrder.LIKES, ResultOrderDirection.ASCENDING).toBlocking().last()
        
        then: "there should be a result"
        query2.size() > 0
                
        then: "the first result should match"
        checkGift(query2.get(0), "A car", "A fast Porsche car", "cars", "fred")
    }  
    
    def "Query by user"() {
        when: "query by user"
        def user = insertUser(new User("fred"))
        def query = giftSvcUser.queryByUser("", user.getId(), ResultOrder.TIME, ResultOrderDirection.ASCENDING).toBlocking().last()
        
        then: "there should be two results"
        query.size() >= 2
        
        and: "the first result should match"
        checkGift(query.get(0), "A car", "A fast Porsche car", "cars", "fred")
        
        when: "query by time descending"
        def query2 = giftSvcUser.queryByUser("", user.getId(), ResultOrder.TIME, ResultOrderDirection.DESCENDING).toBlocking().last()
        
        then: "there should be two results"
        query2.size() >= 2
        
        and: "the first result should match"
        checkGift(query2.get(0), "Model T", "A very old car", "cars", "fred")
    }
    
    def "Query by gift chain"() {
        when: "query by gift chain"
        def chain = insertChain(new GiftChain("cars"))
        def query = giftSvcUser.queryByGiftChain("", chain.getId(), ResultOrder.TIME, ResultOrderDirection.ASCENDING).toBlocking().last()
        
        then: "there should be two results"
        query.size() >= 2
        
        and: "the first result should match"
        checkGift(query.get(0), "A car", "A fast Porsche car", "cars", "fred")
        
        when: "query by time descending"
        def query2 = giftSvcUser.queryByGiftChain("", chain.getId(), ResultOrder.TIME, ResultOrderDirection.DESCENDING).toBlocking().last()
        
        then: "there should be two results"
        query2.size() >= 2
        
        and: "the first result should match"
        checkGift(query2.get(0), "Model T", "A very old car", "cars", "fred")
    }
    
    def "Query by top gift givers"() {
        when: "we like every gift"
        def query = giftSvcUser.queryByTitle("", ResultOrder.LIKES, ResultOrderDirection.ASCENDING).toBlocking().last()
        
        for (gift in query) {
            giftSvcUser.setLike(gift.getId(), true);
        }
        def query2 = giftSvcUser.queryByTopGiftGivers("", ResultOrderDirection.DESCENDING).toBlocking().last()
        
        then: "fred should be the top gift giver"
        def result = query2.get(0)
        result.getUsername() == "fred"
    }
    
    def "image upload"() {
//        ResourceStatus setImageData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile imageData)
//        Response getImageData(@Path(ID_PARAMETER) long id)
    }
    
    def "video upload"() {
    //        ResourceStatus setVideoData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile imageData)
    //        Response getVideoData(@Path(ID_PARAMETER) long id)
        }
}

