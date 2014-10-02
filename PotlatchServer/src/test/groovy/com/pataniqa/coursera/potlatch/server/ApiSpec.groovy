package com.pataniqa.coursera.potlatch.server

import retrofit.RestAdapter.LogLevel
import retrofit.client.ApacheClient

import com.pataniqa.coursera.potlatch.model.GiftChain
import com.pataniqa.coursera.potlatch.model.User
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftChainApi
import com.pataniqa.coursera.potlatch.store.remote.RemoteUserApi
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder

class ApiSpec extends spock.lang.Specification {
    
    def TEST_URL = "https://localhost:8443"
    def USERNAME1 = "user0"
    def PASSWORD = "pass"
    def CLIENT_ID = "mobile"
    
    def svcUser = new SecuredRestBuilder()
        .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
        .setEndpoint(TEST_URL)
        .loginUrl(TEST_URL + RemoteGiftApi.TOKEN_PATH)
        .setLogLevel(LogLevel.FULL)
        .username(USERNAME1)
        .password(PASSWORD)
        .clientId(CLIENT_ID)
        .build()
    
    def "Create, retrieve, update and delete a gift chain"() {
        
        def giftChainSvcUser = svcUser.create(RemoteGiftChainApi.class)
        
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
        
        def userSvcUser = svcUser.create(RemoteUserApi.class)
    
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
}

