package com.pataniqa.coursera.potlatch.server

import retrofit.RequestInterceptor;
import retrofit.RestAdapter
import retrofit.RestAdapter.LogLevel
import retrofit.client.*

import com.fasterxml.jackson.databind.ObjectMapper
import com.pataniqa.coursera.potlatch.model.*
import com.pataniqa.coursera.potlatch.store.*
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrder
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrderDirection

import com.pataniqa.coursera.potlatch.store.remote.*
import com.pataniqa.coursera.potlatch.store.remote.unsafe.*

import retrofit.mime.*
import org.apache.commons.io.*
import spock.lang.Specification
import org.springframework.test.context.web.*
import org.springframework.boot.test.*
import org.springframework.test.context.*

import rx.Observable

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
class ApiSpec extends Specification {
    
    def ENDPOINT = "https://localhost:8443"
    def USERNAME = "sophia"
    def PASSWORD = "three"
    def CLIENT_ID = "mobile"

    def converter = new JacksonConverter(new ObjectMapper())
    
    def client = new OkClient(UnsafeOkHttpClient.getUnsafeOkHttpClient());
    def interceptor = new OAuthHandler(client, USERNAME, PASSWORD, ENDPOINT);
    def svcUser = new RestAdapter.Builder()
            .setClient(client).setEndpoint(ENDPOINT)
            .setLogLevel(LogLevel.FULL).setRequestInterceptor(interceptor).setConverter(converter).build();
    
    def userSvcUser = svcUser.create(RemoteUserApi.class)
    def giftChainSvcUser = svcUser.create(RemoteGiftChainApi.class)
    def giftSvcUser = svcUser.create(RemoteGiftApi.class)

    def numberOfGiftChains = { giftChainSvcUser.findAll().toBlocking().last().size() }
    def insertChain(GiftChain chain) { giftChainSvcUser.insert(chain).toBlocking().last() }
    def updateChain(GiftChain chain) { giftChainSvcUser.update(chain.getId(), chain).toBlocking().last() }
    def deleteChain(GiftChain chain) { giftChainSvcUser.delete(chain.getId()).toBlocking().last() }
    
    def numberOfUsers = { userSvcUser.findAll().toBlocking().last().size() }
    def insertUser(User user) { userSvcUser.insert(user).toBlocking().last() }
    def updateUser(User user) { userSvcUser.update(user.getId(), user).toBlocking().last() }
    def deleteUser(User user) { userSvcUser.delete(user.getId()).toBlocking().last() }
    def findAllUser() { userSvcUser.findAll().toBlocking().last() }

    def numberOfGifts = { giftSvcUser.findAll().toBlocking().last().size() }
    def findAllGift() { giftSvcUser.findAll().toBlocking().last() }
    def insertGift(Gift gift) { giftSvcUser.insert(gift).toBlocking().last() }
    def updateGift(Gift gift) { giftSvcUser.update(gift.getId(), gift).toBlocking().last() }
    def deleteGift(Gift gift) { giftSvcUser.delete(gift.getId()).toBlocking().last() }
    def findOneGift(Gift gift) { giftSvcUser.findOne(gift.getId()).toBlocking().last() }
    def setLike(Gift gift, boolean like) { giftSvcUser.setLike(gift.getId(), like).toBlocking().last() }
    def setFlag(Gift gift, boolean flag) { giftSvcUser.setFlag(gift.getId(), flag).toBlocking().last() }
    
    def "Create, retrieve, update and delete a gift chain"() {
        
        when: "a gift chain is added"
        def numberOfGiftChainsBefore = numberOfGiftChains()
        def giftChain = new GiftChain("some-random-giftchain-" + new Random().nextLong())
        def newGiftChain = insertChain(giftChain)

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
        def user = new User("sophia")
        def newUser = insertUser(user)

        then: "the new user should have the same name"
        user.getName() == newUser.getName()
        and: "there should be one more user"
        numberOfUsers() == numberOfUsersBefore + 1

        when: "a user is updated"
        numberOfUsersBefore = numberOfUsers()
        updateUser(newUser)

        then: "there should be the same number of users"
        numberOfUsers() == numberOfUsersBefore
    }

    def "Create, retrieve, update and delete a gift"() {
        
        when: "a gift chain is created"
        def giftChain = insertChain(new GiftChain("some-random-giftchain-" + new Random().nextLong()))
        
        and: "a gift is created"
        def numberOfGiftsBefore = numberOfGifts()
        def title = "some-random-gift-" + new Random().nextLong()
        def description = "some-random-description-" + new Random().nextLong()
        def created = new Date()
        def userId = findAllUser().get(0).getId()
        def giftChainId = giftChain.getId()
        
        def gift = new Gift(HasId.UNDEFINED_ID, title, description, null, null, created, userId, giftChainId) 
        def newGift = insertGift(gift)

        then: "the new gift should have the same properties"
        gift.getTitle() == newGift.getTitle()
        gift.getDescription() == newGift.getDescription()
        gift.getGiftChainID() == newGift.getGiftChainID()
        gift.getUserID() == newGift.getUserID()
        
        and: "there should be one more gift"
        numberOfGifts() == numberOfGiftsBefore + 1

        when: "a gift is updated"
        numberOfGiftsBefore = numberOfGifts()
        updateGift(newGift)

        then: "there should be the same number of gifts"
        numberOfGifts() == numberOfGiftsBefore

        when: "a gift is deleted"
        numberOfGiftsBefore = numberOfGifts()
        deleteGift(newGift)

        then: "there should be one less gift"
        numberOfGifts() == numberOfGiftsBefore - 1
        
        then: "a giftchain is deleted"
        deleteChain(giftChain)
    }
    
    def "Insert some giftchains, users and gifts"(String title, String description, String chain, String name) {
        
        when: "add a gift"
        def numberOfGiftsBefore = numberOfGifts()
        def giftChain = insertChain(new GiftChain(chain))
        def created = new Date()
        def user = insertUser(new User(name))
        def gift = new Gift(HasId.UNDEFINED_ID, 
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
        def result = findOneGift(newGift)
        
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
        setLike(newGift, true)
        
        then: "it should be liked"
        checkLikes(newGift, true, 1, 1)
        
        when: "we unlike the gift"
        setLike(newGift, false)
        
        then: "it should not liked"
        checkLikes(newGift, false, 0, 0)
        
        when: "we flag the gift"
        setFlag(newGift, true)
        
        then: "it should be flagged"
        checkFlagged(newGift, true, true)
        
        when: "we unflag the gift"
        setFlag(newGift, false)
        
        then: "it should be unflagged"
        checkFlagged(newGift, false, false)
        
        where:
        title | description | chain | name
       "A car" | "A fast Porsche car" | "cars" | "fred"
       "A horse" | "A racing horse at West Derby" | "horses" | "john"
       "Temples" | "Temples in Jhubei" | "Temples" | "jenny"
       "Model T" | "A very old car" | "cars" | "fred"
    }
    
    def checkLikes(Gift gift, boolean like, int likes, int userLikes) {
        def result = findOneGift(gift)
        result.isLike() == like
        result.getLikes() == likes
        result.getUserLikes() == userLikes
    }
    
    def checkFlagged(Gift gift, boolean flag, boolean flagged) {
        def result = findOneGift(gift)
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
        def query = giftSvcUser.queryByTitle("", ResultOrder.LIKES, ResultOrderDirection.ASCENDING, false).toBlocking().last()
        
        then: "there should be four results"
        query.size() >= 4
        
        when: "query by title"
        def query2 = giftSvcUser.queryByTitle("car", ResultOrder.LIKES, ResultOrderDirection.ASCENDING, false).toBlocking().last()
        
        then: "there should be a result"
        query2.size() > 0
                
        then: "the first result should match"
        checkGift(query2.get(0), "A car", "A fast Porsche car", "cars", "fred")
    }  
    
    def "Query by user"() {
        when: "query by user"
        def user = insertUser(new User("fred"))
        def query = giftSvcUser.queryByUser("", user.getId(), ResultOrder.TIME, ResultOrderDirection.ASCENDING, false).toBlocking().last()
        
        then: "there should be two results"
        query.size() >= 2
        
        and: "the first result should match"
        checkGift(query.get(0), "A car", "A fast Porsche car", "cars", "fred")
        
        when: "query by time descending"
        def query2 = giftSvcUser.queryByUser("", user.getId(), ResultOrder.TIME, ResultOrderDirection.DESCENDING, false).toBlocking().last()
        
        then: "there should be two results"
        query2.size() >= 2
        
        and: "the first result should match"
        checkGift(query2.get(0), "Model T", "A very old car", "cars", "fred")
    }
    
    def "Query by gift chain"() {
        when: "query by gift chain"
        def chain = insertChain(new GiftChain("cars"))
        def query = giftSvcUser.queryByGiftChain("", chain.getId(), ResultOrder.TIME, ResultOrderDirection.ASCENDING, false).toBlocking().last()
        
        then: "there should be two results"
        query.size() >= 2
        
        and: "the first result should match"
        checkGift(query.get(0), "A car", "A fast Porsche car", "cars", "fred")
        
        when: "query by time descending"
        def query2 = giftSvcUser.queryByGiftChain("", chain.getId(), ResultOrder.TIME, ResultOrderDirection.DESCENDING, false).toBlocking().last()
        
        then: "there should be two results"
        query2.size() >= 2
        
        and: "the first result should match"
        checkGift(query2.get(0), "Model T", "A very old car", "cars", "fred")
    }
    
    def "Query by top gift givers"() {
        when: "we like every gift"
        def query = giftSvcUser.queryByTitle("", ResultOrder.LIKES, ResultOrderDirection.ASCENDING, false).toBlocking().last()
        
        for (gift in query) {
            setLike(gift, true);
        }
        def query2 = giftSvcUser.queryByTopGiftGivers("", ResultOrderDirection.DESCENDING, false).toBlocking().last()
        
        then: "fred should be the top gift giver"
        def result = query2.get(0)
        result.getUsername() == "fred"
    }
    
    def "image upload"() {
        when: "a gift chain is created"
        def giftChain = insertChain(new GiftChain("some-random-giftchain-" + new Random().nextLong()))
        
        and: "a gift is created"
        def numberOfGiftsBefore = numberOfGifts()
        def title = "some-random-gift-" + new Random().nextLong()
        def description = "some-random-description-" + new Random().nextLong()
        def created = new Date()
        def userId = findAllUser().get(0).getId()
        def giftChainId = giftChain.getId()
        
        def gift = new Gift(HasId.UNDEFINED_ID, title, description, null, null, created, userId, giftChainId)
        def newGift = insertGift(gift)
        
        and: "an image is uploaded"
        def file = new File("src/test/resources/background.jpg")
        def imageData = new TypedFile("image/jpg", file)
        def expected = FileUtils.readFileToByteArray(file)
        
        and: "the image is downloaded"
        def setResponse = giftSvcUser.setImageData(newGift.getId(), imageData).toBlocking().last()
        def getResponse = IOUtils.toByteArray(giftSvcUser.getImageData(newGift.getId()).toBlocking().last().getBody().in())
        
        then: "they should match"
        Arrays.equals(expected, getResponse)
        
        when: "a gift is deleted"
        numberOfGiftsBefore = numberOfGifts()
        deleteGift(newGift)

        then: "there should be one less gift"
        numberOfGifts() == numberOfGiftsBefore - 1
        
        then: "a giftchain is deleted"
        deleteChain(giftChain)
    }
    
    def "video upload"() {
        when: "a gift chain is created"
        def giftChain = insertChain(new GiftChain("some-random-giftchain-" + new Random().nextLong()))
        
        and: "a gift is created"
        def numberOfGiftsBefore = numberOfGifts()
        def title = "some-random-gift-" + new Random().nextLong()
        def description = "some-random-description-" + new Random().nextLong()
        def created = new Date()
        def userId = findAllUser().get(0).getId()
        def giftChainId = giftChain.getId()
        
        def gift = new Gift(HasId.UNDEFINED_ID, title, description, null, null, created, userId, giftChainId)
        def newGift = insertGift(gift)
        
        and: "an image is uploaded"
        def file = new File("src/test/resources/background.jpg")
        def ba = FileUtils.readFileToByteArray(file)
        def tmpFile = new File("/tmp/tmpfile")
        FileUtils.writeByteArrayToFile(tmpFile, ba)
        def imageData = new TypedFile("image/jpg", tmpFile)
        def expected = FileUtils.readFileToByteArray(file)
        
        and: "the image is downloaded"
        def setResponse = giftSvcUser.setVideoData(newGift.getId(), imageData).toBlocking().last()
        def getResponse = IOUtils.toByteArray(giftSvcUser.getVideoData(newGift.getId()).toBlocking().last().getBody().in())
        
        then: "they should match"
        Arrays.equals(expected, getResponse)
        expected.size() == getResponse.size()
        
        when: "a gift is deleted"
        numberOfGiftsBefore = numberOfGifts()
        deleteGift(newGift)

        then: "there should be one less gift"
        numberOfGifts() == numberOfGiftsBefore - 1
        
        then: "a giftchain is deleted"
        deleteChain(giftChain)
        }
}

